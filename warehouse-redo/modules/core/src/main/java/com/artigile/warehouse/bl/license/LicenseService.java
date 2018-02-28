/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.license;

import com.artigile.warehouse.bl.admin.UserService;
import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.common.exceptions.CannotPerformOperationException;
import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.dao.LicenseDAO;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.admin.license.License;
import com.artigile.warehouse.domain.admin.license.LicenseType;
import com.artigile.warehouse.domain.admin.license.ParsedLicense;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringHexUtils;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.license.LicenseTO;
import com.artigile.warehouse.utils.encryption.EncryptionUtils;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.license.LicenseParsingUtils;
import com.artigile.warehouse.utils.license.LicensePeriod;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.artigile.warehouse.utils.reflect.ClassFromBytesClassLoader;
import com.artigile.warehouse.utils.transofmers.LicenseTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Service for working with application license.
 *
 * @author Aliaksandr.Chyrtsik, 17.07.11
 */
@Transactional
public class LicenseService {

    private LicenseDAO licenseDAO;

    public void setLicenseDAO(LicenseDAO licenseDAO) {
        this.licenseDAO = licenseDAO;
    }

    public LicenseService() {
    }

    //========================= License transformation for global notifications ===============================

    public LicenseService(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getLicenseToLicenseTORule());
    }

    private EntityTransformRule getLicenseToLicenseTORule() {
        //Rule for transformation from License entity to LicenseTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(License.class);
        rule.setTargetClass(LicenseTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return LicenseTransformer.transform(parseLicense((License) entity));
            }
        });
        return rule;
    }

    //====================== License service implementation ==============================
    /**
     * Used to check if application has any valid licence to be used by the end user.
     * @return result of check (has license, not license, expited and so on).
     */
    public LicenseCheckResult checkLicense() {
        //1. Check if any license is present.
        if (!isApplicationLicensePresent()){
            return createLicenseCheckResult(false, 0, I18nSupport.message("license.check.result.no.license.found"));
        }

        //2. Check license expiration date.
        Date currentDate = Calendar.getInstance().getTime();
        Date licenseExpirationDate = getLicenseExpirationDate();
        if (licenseExpirationDate != null && licenseExpirationDate.compareTo(currentDate) < 0){
            String expiredMessage = I18nSupport.message("license.check.result.expired", StringUtils.formatDateTime(licenseExpirationDate));
            return createLicenseCheckResult(false, 0, expiredMessage);
        }

        //3. Check if this host is allowed to run application.
        if (!isClientHardwareAllowedByLicense()){
            return createLicenseCheckResult(false, 0, I18nSupport.message("license.check.result.hardware.not.allowed"));
        }

        //License is OK. Just notify about days left till license expiration.
        Integer daysLeft = (licenseExpirationDate == null) ? null : (int) ((licenseExpirationDate.getTime() - currentDate.getTime()) / (1000*60*60*24));
        return createLicenseCheckResult(true, daysLeft, I18nSupport.message("license.check.result.is.valid"));
    }

    /**
     * Checks of given permission is allowed to given user. License plan is included into decision making.
     * @param user user whose permission are being checked.
     * @param permission permission to be checked.
     * @return true is permission is enabled for user.
     */
    public boolean checkPermission(User user, PermissionType permission) {
        if (isLicenseRelatedPermission(permission)) {
            //Permissions to view and edit license list are always allowed.
            return true;
        }
        else if (checkLicense().isValid() && isPermissionEnabledByLicense(permission)){
            //Permissions is enabled by license.
            return checkLicense().isValid() && isPermissionEnabledByLicense(permission);
        }
        return false;
    }

    private boolean isLicenseRelatedPermission(PermissionType permission) {
        return permission.equals(PermissionType.VIEW_LICENSES) ||
               permission.equals(PermissionType.EDIT_LICENSES) ||
               permission.equals(PermissionType.VIEW_HARDWARE_ID);
    }

    /**
     * Checks is the user specified can edit licences list.
     * @param userId use to be checked.
     * @return true is user can edit license list.
     */
    public boolean canUserEditLicenses(long userId) {
        UserService userService = SpringServiceContext.getInstance().getUserService();
        return userService.checkPermission(userId, PermissionType.VIEW_LICENSES) &&
               userService.checkPermission(userId, PermissionType.EDIT_LICENSES);
    }

    /**
     * Loads list of licenses applied to this application.
     * @return list of licenses actually applied.
     */
    public List getAllLicensesForReport() {
        List<License> licenses = parseLicenses(licenseDAO.getAll());
        return LicenseTransformer.transformList(licenses);
    }

    /**
     * Deletes license with given id from database.
     * @param licenseId identifier of license to be deleted.
     */
    @Transactional(rollbackFor = BusinessException.class)
    public void deleteLicense(long licenseId) {
        License license = licenseDAO.get(licenseId);
        if (license != null){
            licenseDAO.remove(license);
        }
        resetLicenseCachedData();
    }

    /**
     * Adds new license into database.
     * @param licenseData data of o license.
     * @return license DTO with license information.
     * @throws BusinessException if cannot add license (for ex. license data has invalid format).
     */
    @Transactional(rollbackFor = BusinessException.class)
    public LicenseTO addLicense(String licenseData) throws BusinessException {
        ParsedLicense parsedLicense = parseLicense(licenseData);
        if (parsedLicense == null){
            throw new CannotPerformOperationException(I18nSupport.message("license.error.invalidLicenseData"));
        }

        User currentUser = SpringServiceContext.getInstance().getUserService().getUserById(WareHouse.getUserSession().getUser().getId()); //TODO: Eliminate requiest to GUI tier.

        License license = new License();
        license.setLicenseData(encryptLicenseData(licenseData));  //Encrypt license to prevent copying it's data from database.
        license.setAppliedByUser(currentUser);
        licenseDAO.save(license);
        licenseDAO.refresh(license);

        resetLicenseCachedData();

        license.setParsedLicenseData(parsedLicense);
        return LicenseTransformer.transform(license);
    }

    /**
     * Calculates hardware identifier for the current configuration of user workstation.
     *
     * @return hardwareId current hardware identifier. Null if hardware id generation fails.
     */
    public String getCurrentHardwareId() {
        try {
            Method generateMethod = getHardwareIdGenerator().getMethod("generateClientHardwareId");
            return (String)generateMethod.invoke(null);
        } catch (Exception e) {
            LoggingFacade.logWarning("Error generating hardware identifier. Generator data may be corrupted.");
            return null;
        }
    }

    //============================== License checking helpers ==========================================================
    private LicenseCheckResult createLicenseCheckResult(boolean isValid, Integer daysToExpire, String description) {
        /**
         * This implementation is especially hidden into the method to make license
         * check more difficult to be broken.
         */
        class LicenseCheckResultImpl implements LicenseCheckResult{
            /**
             * If true then licence is valid and user may use application.
             */
            private boolean valid;

            /**
             * If set represents number of days before the licence will expire.
             */
            private Integer daysToExpire;

            /**
             * Text explaining license check result.
             */
            private String description;

            public LicenseCheckResultImpl(boolean valid, Integer daysToExpire, String description) {
                this.valid = valid;
                this.daysToExpire = daysToExpire;
                this.description = description;
            }

            public boolean isValid() {
                return valid;
            }

            public Integer getDaysToExpire() {
                return daysToExpire;
            }

            public String getDescription() {
                return description;
            }
        }
        return new LicenseCheckResultImpl(isValid, daysToExpire, description);
    }

    //============================ License data aggregation helpers ======================================

    private boolean licenseCachedDataLoaded;

    private boolean applicationLicensePresent;

    private boolean allPermissionsEnabled;

    private Date licenseExpirationDate;

    private Set<String> allowedHardwareIds = new HashSet<String>();

    private Set<PermissionType> enabledPermissions = new HashSet<PermissionType>();

    /**
     * @return if true then license list has at least one application usage license (valid or invalid).
     */
    private boolean isApplicationLicensePresent() {
        ensureInitializeLicenseCachedData();
        return applicationLicensePresent;
    }

    /**
     * @return calculated license expiration date. If user have not one license then this date is the maximum date of all licenses.
     */
    private Date getLicenseExpirationDate() {
        ensureInitializeLicenseCachedData();
        return licenseExpirationDate;
    }

    /**
     * @return true if current client computer in incluses into allowed hardware list.
     */
    private boolean isClientHardwareAllowedByLicense() {
        ensureInitializeLicenseCachedData();
        return allowedHardwareIds.size() == 0 || allowedHardwareIds.contains(getCurrentHardwareId());
    }

    /**
     * Checks is permissions specified is enabled by any license (enabled meas allowed to be used).
     * @param permission permissions to be checked.
     * @return true if permissions is enabled.
     */
    private boolean isPermissionEnabledByLicense(PermissionType permission) {
        ensureInitializeLicenseCachedData();
        return allPermissionsEnabled || enabledPermissions.contains(permission);
    }

    private void resetLicenseCachedData() {
        licenseCachedDataLoaded = false;
        applicationLicensePresent = false;
        allPermissionsEnabled = false;
        licenseExpirationDate = null;
        allowedHardwareIds.clear();
        enabledPermissions.clear();
    }

    private void ensureInitializeLicenseCachedData() {
        if (licenseCachedDataLoaded){
            //All data are loaded earlier.
            return;
        }

        //Load aggregate license data (process all licenses to calculate aggregate values).
        List<LicensePeriod> licensePeriods = new ArrayList<LicensePeriod>();
        List<License> licenses = parseLicenses(licenseDAO.getAll());
        for (License license : licenses){
            ParsedLicense parsedLicense = license.getParsedLicenseData();
            if (parsedLicense == null){
                //Skip invalid licenses.
                continue;
            }
            if (LicenseType.APPLICATION_USAGE_LICENSE.equals(parsedLicense.getLicenseType())){
                //At least one application usage license is present.
                applicationLicensePresent = true;
            }

            //Parse license expiration date.
            licensePeriods.add(new LicensePeriod(parsedLicense.getValidFromDate(), parsedLicense.getValidTillDate()));

            if (parsedLicense.isExpired()){
                //Skip further processing of expired licenses.
                continue;
            }

            if (!parsedLicense.isActive()){
                //Skip processing of not yet active licenses.
                continue;
            }

            //Parse hardware ids list.
            List<String> hardwareIds = parsedLicense.getHardwareIds();
            if (hardwareIds != null){
                allowedHardwareIds.addAll(hardwareIds);
            }

            //Parse permissions list.
            List<PermissionType> permissions = parsedLicense.getIncludedPermissions();
            if (permissions != null){
                enabledPermissions.addAll(permissions);
            }

            //Parse "enable all permissions" flag.
            Boolean enableAllPermissions = parsedLicense.getAllPermissionsEnabled();
            if (enableAllPermissions != null && enableAllPermissions){
                allPermissionsEnabled = true;
            }
        }

        //Make aggregate expiration date point to the latest expiration date in valid licenses.
        licenseExpirationDate = LicenseParsingUtils.calculateExpirationDate(licensePeriods);

        licenseCachedDataLoaded = true;
    }

    //============================== License parsing helpers ==========================================
    private List<License> parseLicenses(List<License> licenses) {
        for (License license : licenses){
            parseLicense(license);
        }
        return licenses;
    }

    private License parseLicense(License license) {
        ParsedLicense parsedLicense = parseLicense(decryptLicenseData(license.getLicenseData()));
        license.setParsedLicenseData(parsedLicense);
        return license;
    }

    private String encryptLicenseData(String licenseData) {
        return StringHexUtils.bytesToHexString(EncryptionUtils.getInstance().encryptBytes(StringHexUtils.hexStringToBytes(licenseData)));
    }

    private String decryptLicenseData(String encryptedLicenseData){
        try{
            return StringHexUtils.bytesToHexString(EncryptionUtils.getInstance().decryptBytes(StringHexUtils.hexStringToBytes(encryptedLicenseData)));
        }
        catch(Exception e){
            LoggingFacade.logWarning(e);
            return null;
        }
    }

    //========================== Hardware id generation helpers =================================
    private Class hardwareIdGenerator;

    /**
     * Initializes hardware id generator class.
     * @return class to be used for hardware id generation.
     */
    private Class getHardwareIdGenerator() {
        if (hardwareIdGenerator == null) {
            final String hardwareIdGeneratorEncryptedData = getHardwareIdGeneratorEncryptedData();
            byte[] hardwareIdGeneratorDecryptedData = EncryptionUtils.getInstance().decryptBytes(StringHexUtils.hexStringToBytes(hardwareIdGeneratorEncryptedData));
            final String className = "com.artigile.warehouse.license.processing.HardwareIdGenerator";

            ClassLoader classLoader = new ClassFromBytesClassLoader(className, hardwareIdGeneratorDecryptedData);
            try {
                hardwareIdGenerator = Class.forName(className, true, classLoader);
            } catch (ClassNotFoundException e) {
                //DO NOT provide extra details to prevent hardware id generation algorithm disclosure.
                LoggingFacade.logWarning("Cannot load class for hardware id generation");
                throw new RuntimeException("Cannot load class for hardware id generation");
            }
        }
        return hardwareIdGenerator;
    }

    //========================== License checking helpers =======================================
    private Class licenseParser;

    /**
     * Parses license data.
     * @param licenseData license data to be parsed.
     * @return license data or null if data is corrupted.
     */
    @SuppressWarnings("unchecked")
    private ParsedLicense parseLicense(String licenseData) {
        if (licenseData == null){
            return null;
        }

        try {
            Method parseMethod = getLicenseParser().getMethod("parseLicense", String.class);
            Object parsedLicenseObj = parseMethod.invoke(null, licenseData);
            if (parsedLicenseObj == null){
                return null;
            }
            return new ParsedLicense((Properties)parsedLicenseObj);
        } catch (Exception e) {
            LoggingFacade.logWarning("Error parsing license data. License data may be corrupted.");
            return null;
        }
    }

    /**
     * Initializes license parser class.
     * @return class to be used for license parsing.
     */
    private Class getLicenseParser() {
        if (licenseParser == null) {
            String licenseParserEncryptedData = getLicenseParserEncryptedData();
            byte[] licenseParserDecryptedData = EncryptionUtils.getInstance().decryptBytes(StringHexUtils.hexStringToBytes(licenseParserEncryptedData));
            final String className = "com.artigile.warehouse.license.processing.LicenseParser";
            ClassLoader classLoader = new ClassFromBytesClassLoader(className, licenseParserDecryptedData);
            try {
                licenseParser = Class.forName(className, true, classLoader);
            } catch (ClassNotFoundException e) {
                //DO NOT provide extra details to prevent license parsing algorithm disclosure.
                LoggingFacade.logWarning("Cannot load class for license parsing");
                throw new RuntimeException("Cannot load class for license parsing");
            }
        }
        return licenseParser;
    }

    //================================ Getters for license parsing classes ======================================

    private String getHardwareIdGeneratorEncryptedData() {
        return "6e11abec905ca28d57b59217fe422965bac0e6669a2289977ba16cb8d9a058c54b6b7f6345eca14ff6dece3e25d2d1bc338479d14b7e4a76971bb0f3709b78bf7fea2d0290e334894699077c18ffb0bd955b9627b68ca335329fbf3c6eb7816050079024965d35a5e1a9082b03404c10684ad061c2a226c6c13cd898bd4f603ca157f8b4bcb206a78ad1c47ae99e1d93570820779c27fc6284edb06da7d3325369105c2d9421ba8c1a0e846b1df84e250718eba5fdd5c3c034c2cdaa825c43b02f6c80c5b778ae1db47e401baced2a2c8109a2015e4b28af4353d7120dd3e76b9445b8dc2377d56b2256b1362b6acc3372c0607cca89ca6375904d2df020254eb9556c52fae4adc2813b1a02e9650725ed4019157d73c3a8e30e074febb44bca5b6e0aea0948d1657ec7fa01e63ad450f06ca902c361fe4ebe8cb2e75505b1a8fca4aeb2512b1493f39681e7b4ff73d5909cf08ba4a53cb61dd96809dcc7e7dbaaece5a70ff3daf025514dc58a4f7ef6c17a589a161479e9a344269e30d6e2ab0c868399f663378e6c0b33f3c5881d337eb6434ff229015de8f437c80cc9ff7dcda4d493030cf1a729d3aed00c5bd1511263c638dca49c6fea9e3da1b956ae223f25c476e0f61671c322b1699bdb3568ef2ac4dd13d1fb92ec74a31181f916c3cbc9568c9aebbf2e19e1f4753d9ef2fd1f8e7778ee2b84f2debe34c1a49e3640f67f5308be7bbf6f788a538fe0d754365d85c043b9c9a8e16790d45a75b8e127e2b841adb828d1ce2ff1b2c27fca105ddd3f60d344e59f743545723a887ff88699c37822af58a355bee8d4994755f6fd2dd8f1462293a415f84769401f1615f89279d85c35035c7409aba8f9c38d43380c948e705dac24fac095054d69a1735c8038aadf86ddef30b946b1c6f831d9241d02071fbe91a78721eacaa65388124ab6669a75368021619558ddc1cf02c6f8043994963e593bec99b29017ee84e88a20226839bcda5e955fe24bbce4c0bf6d589c6b425df44c31fe0f9414be37a7213022f34c2cc724e6763996cf58f35b1614ab8a82a5a7d5b6ff871555782cc400c16c7010ce567a545e7ef2040ad4c30b17137c3c084ba4605163e20f99ea1cabb74dbb34a66e1ca2bb0529c634a1dde0f5b39a7e98a9bf6dba5a3c31d0e8b01df29e1ee9cc62ee0e908c67c8bf7e50c5a99d5a7f87a7f4b159df0ac877b35bf1b12a2ce3de35a67161d3664432519786979d04e461b4d306236022f5ac781e0ac625c22d7a1e87962f1456951d5bc41dab68264724327d7823352d7e3ad878bbd85074c7494fa485bc4e5ae450f0d16561332601cab1641343c2e0e1b2a9e73d5d5f348bd31f873af35e7e66841c61daf201e7c64275b2823fe5bf0e3415e5f063a7ace31a48f043889058572fbcb343468b96f36db5d217f901fd03e477cce55e2d1feae434381318b01fe7bbdd0f62b9e151bd85cd24b48e9c0f4d9930acf5a87b1a44d503f636866dd0006529c5c15f30a91aa32e3dbe558df23670ed5c153adfae2075595ed77b658719db3d65c9f5fa771c1356a95dc839b9b538902f29756f16191fd36ae543d5511744bd92e0eb5533ec98a3c307ac93011d97495d4ee1285e126102dc9904b7ef0e1dab53f93c9cd8ce7ce21ef424075af4590511f8b2326b7092890e1b7e4fc59d5c3942cd5e07a8a8d543b61edc216c91add7f94de735b7522a0af6d3d90db5c8ffee74c76f2edd5c2de7ce08408b9bbfb193dfdd4243f348b4d9a541ac791f4516198092100aff017f2c3af97d2aff1d70bd25f7d347d6a6a9ec0afb9dab6ed27d88fa083a952084d136674ce9a4eedeb7307889faf407587c3aa41573fa520be10995fff20b9c901922c9ee630b4a70bc262ea1c7499a1f0316cec1c88d8d1e8122a33ab2adf915606adf46baf9ce54ef87d37f98154ae0eddab0e4bc2444d88688133e77f10c7130eed78450a21ae86e62a0abd087d2e153d2e85601d71e36039718c67bbbd45ebaadd58c94ed10b30bb3db3182ee2b1a534877e28322d512d3dee3453717cf6422e6db98c0f58fb826e617017e5db6d8546078650dedbf079dcda91454e62b20b25563d4c5d99bcc5799f8f575658d3e0848a712d7a870d7fe48f9a4597c82c43c76c8c8f0942c2863f09393b370ed2adea1b369a468159d27dd462fe439ba8349fcefd6d02b96bfebbecb81813e071bb7553a406e7c20d1779436178aa52a30dd04858089518976ebcb39b1c5f2486d2601e9c0f50f82d2e1b8cd1727cd5e642f44ab5fd5262b3cbad6872d6eaac85c18415c3832dbc89a54f5eb735378dda1e553bd8bb7c6e7ca6fac09f84243f348b4d9a541ac791f4516198092bb5a9903e5054112436cd3c1887a507ea767a5f8d493659b215596bd32a8d0c068877d299371c8b657baa9778fd6f37862db03295841605faeb6ce7e8bc3fd22b0bbcb54911efefc55e9a20ca4c29e7feeda18f1d247faed851496cd8f61a26230bf78f7b9704b9a8b21fc19a809ad492c5748f5b307fe37399a52a3cdf4029fe32ebabeae7d3e6e98bff83cf24b0c7f835b77f7c0e279708cf092da14e6b5b0a3d5e7e4c54a8e24ae9975b0c4acd697d327da451b500fe0064b3803609c7cdef01fe7449d913918cc6f16ebe7f0d522bfd51f34b776b6f67855e21d8d5be955c14c090beed6ec0a475d5833b10dcdc05c5039f13e4ae2716c0a89d8c0b93315244eb6838cb0e2dd8cd9f4f56b5492231517431346799872998b1e89f153b4869b8d60ad97df98ce35a14e20ec20e98eb161e6b2cbabb2ba2b46a1a57ac3b09a4dac01acd23ee5510988895925e4bfd5fe28b4e6a766e3b831b23863d3879b408aa94968637cc27773ef15e7bfede939f1c2a122055d2378037c8ff18f6762880cc2a38d69641ff8f4e5d77b2bd9e49b05ce9b60d862a451d89cb6a2d86686d2b110f67721f8c815c2134763b58524b183ddffeb6720d0f42c1d3dc0f267ad4ac9e4ae96f71f2fe3109cb41b137cb5f62952ac584b286f95aa7b6d3054a16330eeb7fc1a8e046ee8cfef9aebae96ea8849014ad9fd541ca34d4e2e111b9c4e83b0e26f55e86ab06f88470421bebee89fb00875811ba8a75c18c98110e2988331e3c68a42c596249ad7ea1ea57a04227230ccc6be0bda4d96e50a6e37f2899053bbce5e85d09dd61089dde5bd4b42a91d44e4d08ab4986f74d47640942aac78dfc679c74c2cf9021762b65656fa8b87b38aed67745de0b7a1c8e0eb1bc0c38130baeedef033e17cb6e8a9728e8189fe82874d7b97c8aa53a5afb9091ec6e350fd8acbc28672a878662a91b181bfb6fbe7332e35c882502c449ed85482ab8941a9fdfc938ef8e77542db3eff8727492b24765269a6772ab2a4f33acdfe8cac616a0c6865ddc0d550d0e300423476562b4f9aa487e723ad65adfd75671948f2ac7c20b8aafacf3907c097b2b3c2a757fdd7d99cda3a1d0896f17c2340538272458ed39ce66e33da2a92c0d71fb6c2c02d8b2cae959058ff6656d56cdee58f37d0321d926b096cd3d5824a2340d0fbbfe5d50a382159016728143b28684bd6416e2aedac983915f59afdc848b8e761f479b44589eaa71ba1ef85589e9c10d9ed54afdb2c5612fbb43b55e2d09c73877aff5f117ca3457e68b8b43e7766a6bc602b03b93b3a44a115af9e2ea8a178da2d08c9e2f682588f7b25d4cbb65efdc75d6b26c7627c68a0b706d62788872e506d7c633e3014d1c0b5c7d566d4abc011adbefda1962ff4e82a1e2592cd102cab59ec02df69b80862cfdb156514d9f29a6da114c3d6d1ed6b1d3cd51e9a827bfc8ad464a9bb433b5684a128d5fd0ec50632e666f90dc5b5032293718ec2894adda4743f896637f9c8f073dc84bcfc3e67ebd66eeda9a940c05b2545445998c1a1f1924e5fec42155c101a39f9566561f6ab0134e849f685fa9c763430fe179c78a135cdf29c6a026b352bafe1c24890e5e9b38f3684968b416949d76f87d0e0bdf977b9f843a2c56ac5a1eb08cc9c4feb307b78b3ab40bf107439001a1f12bcbf7a183df06b2e3eca1bf8376c9f612bcf6ddad7b4fc47f50abe4ecffb2c472a79a2db40581cf7a2463c07aa16a38a41482ed97efe9dc5878d36d09d78880c182bee45412b0945f910db851754e6c38f7d0dec153d3ac0f31bbb5a9043b443678f30c9441a3d35a376ebab55f735c307b8e46c85f5121a220f5138b80dd43fdb6a1564fc878843f0f0813a569dc2ca236598770102e2a132fecd1799bdfde6b6aa2c8a22fa751bf090ff95460c463d3c99e9002972131aebba7ad0a42383f5b13a4738bee5537f615beeb87c3b7a9a06d0793f69a48770eca663555ea679e0bc74d27ecf06e070fc6a819b4f9a64c41954abc324c904961d3aad32b1ca41d40678c6746a33c60edd278511fb59156a30eecd69ed580769be07fe80be73bdfe3df93f9f40412e6a875c8e67e6533c0b6225634c2118158afa2d4fcab97022209e2bb716a564e398113c4f558cace02b22cd9fe3dfdd40e8a5f1724501089f22f31feb65ebd2fcd2dd7506054bebc7aea5854ce0a94ee8662631d26a20f0c9dc5b65c0779a263bf1ecf75a3e69fdba8a0cf3396c01995a54061f3000b588a9d16c3eea2a233b6a03106b3ce7717bae04f49b3411218c8885964ab63e06b74e4f77d8be971ca3e152198cc97f390c822e2309fcb8a823004ed62afd12f4579bfad18791270bc13925e9ce595732d302cb3d957a2594cbc1c885ae5360d40d54a01aae88093aa5843c0b9325ac4a866f9ce0b43982b5419cf32a048c90985a605f9c290a9a0012d2d7579fa94627836ac04dd71fc1c1c69a6bd990900a8d0ffea0e4a37e5b0a8ec7787ba4bee2a0ce0aecad3426a7cd848ddda18bf3c92dc02e6038156062dc2204299fa24fec995e8ced082d5cdb6ad28d1899ac2ba8ac6adc2497d713a6eba528337f04e8759ae1b1a5a9af89bd517fe67810ed2ca664950466b30201e6e82d0cc69f4207c5e2886de3f21d73139fe3782255203c1defbf65ee71e71d318ce328edc054f586774a424d6859bfbea5cd9999433013de655559230049ea16809b7fc21d6b869d06a65dea129d2867ec1af83f73f2fdc154dbc1773c344b315ea70f5f140e8afe9c4123c42d9667aa04b0718bbc038aea95858940d1b122a1c809b4934dcad4551ab8dc64f677d83c5f2c130e198ff993cf8fa9d690938cf4e1927a7c75b9c0fdc5a223fa274dc0408fe0c65c88e841047ffd05fbd9a8bfcf37016bab69d3e39842cab85b89314acc0fc6ed9f9b4ddb917081f6214849a509f8c8bc34d1b5a825cbba96ce745982b421b83f31b988378420147e62dbbfa85cc696c461f0e1bdffee11049617bdcd14e95d811fba008ef330cf13713d9a4db0e7f88cf13544b3cbe88caf3bcd23f485880b195737b2b779da9cad6e4771fe523b64217a645ce3d4f10f731940e659900448a6ee350617a7b5913278064a9a94cfb213d9108f568650f1b761d560daf8a513c0071a9a7bc9d00502fba75c6d2efbbdd829da2146752f470ef81260030253b903fefa7f628e8b3f70760c8005da72f8bcb256054e48584187e031048624bf6c7be0621a41de49441ee5463653679ec9f7eda07c9f7b615dd9b06cad4d79702f2021733567b43ef624cc5f5e29a742ed3a5c514fa95f261af8a12c30f29b0b4a3e65367382671b66a751bd056023aa1da5bb00e211ff377098d8b3eefe26194e78f4e822efd11ef0ef471170fdab87b46b0109c8ac9fc8b00450f1194bf34e7c22fc39083073ab36a967a76e5f3e0d2ceb20b1346768f469a891e53b6adcb468645ece642cf662e05fc3c0ba0aa1e106dec7f725a35670245950373e2d73336f3bd57a3b1339f648680f582bbb5f0d53a265ee20b7d9e472471b804f3e30c42d6e697866e682d4839b55eb4c9eaa1f60bf761e665c4af8a240f96346ed9304f06a95af9086474d19064bbac2920e181efc6ba55be82838108cc40a12bbfeaac559a279c71ad760e278274e0d6808ab9ad4ab4e46c7a1f6e5e487794d91076ea2099a74283091c6a7179fd829a1ed94676ee23735b93b9a4e64842803f8fcc0da2ff3403ed1a7ea78107813b518ee8b62ee593d35efb19357d541ec34fd2fac26fd0e3c3a02bd7cfe46f1864048cfd27163d1db68b809b7dd1455fbb9763c9622a7081a444ce959ada515ac99b37090b022a7c2d4b7b73c89f31feb0d74a5a6912d0218b4971616e7fe8a7f5e67a8d2670e3974e27cb352dd9f775162d6956e14b54949d8dddab8afbc972319c9354f89bc7a428aaf8dbaf94041729f137c26326546a6fe7362b0397ea2db211ebfd07ea7e7f7aadb8414772131425476b8a9da044b08a23fefd3afb3fe7fb17b9aad65e7d9f340f5c5326204e7cf515e05690dd96ed2beac4d4a42b2744901a4b50029d525ba7df020a9ad8cd5a0ee6c686ecf1f16d59c4c5ff05e21c0e4ce31260190733cd663fdb8d0d6ffbac6012bdc6c7fc58aecf6a7de67aa87a1c8d57b76431ce3e2544b78d12ecea37de4cadf0881bc620ae61b3f1bd64074bc4e93b4eace21a1203a24f4fa3d1e34876a10ff82ac9b7f2f11cbba4fdfb960173d84f6181d7297698c5751db3f86b327e69872e90fd453c8c3986a9089cbf7c382be2ea4940231799ba8d2a34af6535eb4c052a072aa60607927e42e8cbba12b5a530f7ca22c6cf615b53ed3012151473852253418905268eecedac83cf7547ba8fc9a88ca65aeada9f788bbd92103d6ec4c4fa836f4f8a1002daf0aec1dc15954dc123cd0a4311a5c4fe012375fdb0686d58dce7845d5ca90036fffe7552418ae0a277679694cee023ae1150d9f938c787bc2a2bec76cf773a2c5e15d9e247fc3ecf28de6d1309ce41ec90d1d695e817033936a7049f1f12b0039a8fc56d0aed26cc6edda5b5d8d85f050c2e7c66fc697086617785cd02f3695afbf32b4e31124bd99e7718ad209036472ebd6045a6d51f2f61f9e91bb861705693173a51be5a6723470d2e1f228f2d76dbeff3fb4b9fad2a04a440af9fffa249e458a73e4fec6b4669be67a15b8af3e2587e8eebab4d7ec04e045e65d2499e8edd3ac8eb6d0652f791949f04f287f8619aab05e084054410553009ee37dc2976d0a1d87b744b4bf7c1514d9c791bc1b5bf04b68b98313991109b572da58ad566ccf7718a545ea3e0eaa069f627dc63a0324509db8fb622586b898c43cb1b8f98624cb6f0a091467561afd74e0cabe00623925b77adcd277b9807818d638a20b397ebf1714a83123ae8b47964532e58a05ef7b662bcf772e33261bf3098fa2352896f45baee9fbf58f22d5758b856a829a734671d303a72ceb657d686059a75e63bff6f9ad210d0f1d71ac7e230c3ffba56c56fe534b8dd9a7b48745744e242fefe350b179f20441fe13f13187f6ac71ddc714ee409a12567ba50210293f1779e96118537e5d17526d37c2c1fe46422f7c9f471a1ea5c7736f2eb47cd9a7317bd583f02bb9d2cc8d7293c56c9a83e19b9db5bf4316c4c4200286baf6b27ca29ff5337c0214aae8cc6e6700f6fdac9247bc75d6b24f73dbec4fcac977f3279f857e2735febb393c6013d9e4f6d27d55c455ef6fc6bd26b90ffcbc862c9686bd1abcfab7940589ceb79d94ac34d1808dedc1b71ac46336755496f146fed018d252b0b4f3f0b87d5357072a57b5f70ce008a16724551268b3561585cab94bb3391f5029b3cc0f281308694243cb94eef77aa8a29e61050f7397481ce5d03aa6ba05a27972a6e53bc13b061e1b082b80e850d6b12cceed1741e91eb9e1bbec5a1d60b2af34d805e3b4e31aeab38c2df9cf6661611b920b69325b38f579a867d0415d08ff675ca052bc5f0d46dfc2de9a3c56ea95971acaaa4975e382384a84457cda2a4a33e10685c22429b13fe6a01a2f27665ce8a390b684f1adb23292ffd654430d0d14de6afbcbd65856d7447d79fc912d6594aea9b4b2192f89a020ed692baacfd97e3c3111d8a169dc9ad7fc7874deefc8157d62bc069b15726d5d7a9d207bc936c877225524ae267966ed4747f18533634b40ecc2ba158c41fcbce4c40bd7ad41d8d4930e8619ebeb11d783ac401eff39784691de65d95633d8c7d513f008b8635116b8ab0a1a5a9aef1ee1185814587613a28fe00bad96d119ec6383548a0b52aa5ee2eda5c726e9615585b74648f6fb67909156210eb44224946e3ccb60e5ac120f30076a8f7070b2c483fef689b146b0d3964bedb9d02ae92f3e51e4ab523ccb82a36cdb977e83930ef6c2fd1c86d9eefda1f215b43db1512a00b8d6d31e00879b2a440f37e4138c29ab69c0b9a6201ce62754da4c2dfc76ebd0853fd4aad893039bceee00b2cf75b8f0194de1d983d42c09af516e3d4f224370c615b2662b2432b770d537d1b52bd88086798694d99f830d2bc6d85ef83062c165aac103dfef25d9536c29b5bc43";
    }

    private String getLicenseParserEncryptedData() {
        return "37dd0f5200ca6ece8788afe0a8d3d98a056fa22cc561be7e0ed6abca3bd537db9e33d4fd5b773bd5c5b6d5103c6f8c89c5cd68e1a235fa34fc01d109e7261b8ac224b7a8205a0090f34afe349a52ef22ab97adfab462f1d898a5903c9c8c5033b1f3ac3ae36b1c0f3c9c12c598af2f60c832d2430bc718cb7474a2c3b5a15068c3da60b643af690425e43dbf6376aa2fa02153ce29eb59958645e7cdbb7551b41eb8a95013a2d4929a39c84f6addf0735d3131bfe0636f63024117e2370045e7be0eab733f74bf164071fc4b07cdc7038155a6d7c98b9ec71dbd9dce8b843ee708250b054a746eb9eb3398d5590c82d5bd7f5427b242484bd7cd9404e8affd0742d47bc559a2825ab84d546ce997f7f548fcec45c084553e5d17add2c354e5098e1e253702ca5325a9e5c67b3fc0ff488fd5c888a3343065f222bac88f01b7c1d4617f1a35729ac6d9507a793aee32db0d4c54a1fa3be14b629791b299b5aa92e8314b5b6358bcdd0f9dd216fc4604bb3dfe51f86dbc384b90aeb3cbfd82de4080d410e22570aad5acc556265997b43164d785da9fa7ae2d3fe74b0285aaad7edf128b7aaed1bec05fad33811ef6f42b6c2748325584a94530b35435ad0207dee4f3dfc41c15500852e78dc2beb9284d06bad44acc3c256f654e029b9941cb1c9916125000a371131afcffec3188e723f1ff1fe7782443ef797114069fb439ecc2b454220361e4ac06afa15e7e556e9f4243f348b4d9a541ac791f45161980926767586237ac5df2ee3ad8c8fa97c67ef73e25e92945dd1a9f3abfbe51e629c1ec5b85b70411ccd97d588b50efeb735f24c726d27517fd1933f4021d63129dea9d23c056a72a9004ae8ce07258e424330bdcdfd6fb0949009f05469e63de504c8b6ed8cf32df734acb17b293242313c92aa0c990af1219f19f497846a7634d2a081fe7abd61ebe86c3febc4b9d71832dcd532313c5f8f38ad2b726e10f14751586f6c7a649ff7f56c59b828132b6f636a5391a368d63504c2bf00705649706a8498d191e42488dd7ee0b6258e5c3b498271d1ebe06cf13d5c52e4b389fbf56f1a7ad7232cf43ba391e489e12272c687b877ff6aee6e6aaf60a386ad6311950bd8f33278763fb180bbf551cdb18c83a6044e5e023c419e9a296a9fca97f9121c05b1fe6e3bda2496f0cd879997f1ea7f5ba633f5453e192e239fc1854a498ba815d8f08bf57dfdeab4204c8b3aabcd45f85106ead5fe6cd30492628565f68f1e8c3b4aaef403a91980ade95fb5e420c1c524ef31bc675efd12b27bf286bfd648ce7f24349d711132d3099bedaef66f3257b9318a3519e91a13a20e718765b44ec469a940d7b7769b53023062dbc999ea6b8a15e69ff07f541e17d1120c2f7da5389b9caf0007346f0648d103e84e71cb98189dfe19cfed9d5afa04a1fdfd1015f0e76bf9c7591c1045aa0c2721fc31e642a71acfdd0d2ebea98670ae96614f6cc16d4f17c5332d7e96f8b2cb49201f5190ae6b262912f123947140526a895391555137a80040c34d0f20fb867c71f54b40322fe385280a65f7f8b3333a1219e6839f96ca53b2f5c26245bdf277e0f328237a193f5033c89aea0c9240370b478c50d47deb1220c251867d5cb1bee580efe50e15db227f3ec08a2ef36230ad8cf43fc92ee69ac422e6628742828d82a0a0334d5db1242ebdc1fcf20f42505e534c0afb04abb89536a207f019aa50233fa6b0fbe15932cafe1ee7ebe4a2b29fa0af9b95b6cd84ba80f955e5860de7ccf36339e5fe2b3c5e47d718af42ed50d3b56f34a131270c7272e7bf541af39826869799ba3e7d9076908ee251d12ee72ac107ad9554b67061029afeb8df9c701c2186615d7c71462df037afbc49f72d1d6cb68d0c4d1cb8ab00a937ad3151d9ddac4169c986c68b841e58375e6b15a4cd14ed86d22a1b0426f125bcddee7120c2655fe8401c4082a26c39da504ec8f113b2b0059f67063cad1257f252a92765d9b1d95268610e284bf3fcadf131a0a007aad5578813a64563c959be03b35df311a04d6929e869ff5cbbca019fa7c15a6424ddc1337cb07d1b2507ca448f719fc5086782b1212c17d509363053f3c18efa8d7c1dbded7c2543ecdd5a8a4da4a4f9880755e43d1fb2d148bf0d5e8c8c24e8d7110848d0c6267dd1a9b059acd371372a976f99f59c295409f79cf0f66df19b54ea5044e2bc15cfefdc9d8bdac136065c4210737d5f75238fcbc0fcfb2376d99b16ef2f824d062dae22040aafe54faf36258b1551d55aa09ba4ab91689ae958299dd169b26185bf61cebcd52b85bfdb0384c4769c47713e902bd4108dd4d1ba4f01638ee7597d42a7671b0cfd1e96a7a1d20aa0a22307589cf4f046a185daab2982a966d9409a1a3c00a59175109f5235a2023a4b228e6a8a651a6b911801971e21c9b4bb12709a052b0e6482bd25c933d0c82a2bcbd1814ac5770a4fcbb6986e40e45a7ddb7639db991043c253d6a84528aa8ec86d53003819a9f309a0c0fb96ca0930af919b0faefcb8add8262dc664599dea4f689abecb29857537167ee49f87092b1e6dd91d12c6c589d3d1130c4189bcc84c73fb0c8d7e2a23fe607bdee4ff6400b96cde724ec7e19c9c929d651551d2e8ecae2f668e86d644072c999cb490fcdfa404c388960939c09d28f9e45282a9924b040cfb53f58c26dc8b150576ae7208fbc2e7dd3708e52aa6031f49acb42adbeb28738445babc84d23612c7ee6049f4bf6b0069b09c8a4aacd58c44a9573c9d6eaebdb9f59dea485dd60bc34738b6e31ae3cecb1175c9ca1933caad542d0af3ab525fe79bf8d67922c1e522fec3454a24d4d84d6369eacb1228041fa15913cd09d063220e098144896ca456ac37a47fb96a259a426d4d03b2c359e686552e07217342af76b8e9f1ed11650680184aa17987bf1506b10838796032880e27ce15789b70e3fa8a2e47a301c79e570b4e88db57ca2fb80748a967736fdc1069ad056f4ba31dccfc9d002cdf951c60e73b56bce3f647bd1e326bbf3b1f2434b13c18247e5c1124a84fcd6351d819040bfc679f2282a0918d94567df2058450b93b6ef8387a64e09c118b0ca0f092bec640503e89649ec927a5e4855cc6f5de44483b98b8f55996a57fa88fc44132de986fff587d3c17b5423a9f46826d6533c39f4ca25e588a9aaf637d904105b8c1bbec2756eb7068156f95f4abb53746d96a3c146264c825978152337a469d341e15db263b7365d5569ce935ea94b714f6c7596d8005e55f7f4323b225ebb6aae04eae2645b4dac5838f83cd34518d90e3f31404aedab15a433ae1f58e7c2b30b2d4149600bc9ef3c891f59c3ecef17acbf0a17800dc634a3434262b528bd9bb1d8b0186349d0ee98c81172cacc762f50234913913caa4ce1ca4ab1e9bb8f2c326b90c1d4b13a0a88844da3a93013e9506a8278c60cc120586771441708a4e323e88be71e51c9ac3b8b87b4ceb355d69a18cf202034fc2ec5aa15c6969b7e30f57168136ccaa3dffa8b8541410e639326c08e16fafeee1ce28de3380f79b7875e72c85da5c0c446d77c20e7df7ac84207cb91d52d0368fc1f5989ef7ed7b18c1ba4a9f5d26cf55e4bb5a2e5a2a379c75b553d4c6800a75b55b3575f2c98447a72bff8570f178b37e1ff3621a57de1242170291899212ba9a167fb833fd859a96438852668c523e3b7a1d8d5d010d08731e076c4d9d4ee6011843ae07fa9653dbbf542e8314b5b6358bcdd0f9dd216fc4604bb3dfe51f86dbc384b90aeb3cbfd82de403f8bacdeb5643c8d07ef5b163e089d6fe49a7f658f28b0218b87e4a41e249e5253ec82136b203f9d082ab617ed67084e218717f94590a787ddec21124d43ef77c48821829e51fa8fa6cd92362acb5866a410fa8863f1f8e9721d85905be251fc32fca65d67abdc5f439a01313b49da1dee55f7e847713e2b2016722881b63a436dafad038998333d7713e1c5eee0872fb46a14d2135e79e469aec56bbfe25cddb2f6ec4a9beb81ce9d2820de50ed0c84879c95dcd9c832a672d6ce630df6503b464dc7c9af6f69d08aecebd433a620d946fa7f4efdd84a06c7222d4c364f7de3987f887a2d5ef1d96af69a3c94b5457101f99996780fae33469dd30b19d69916eebcfd78e37e4d266d375e047cabdf3013e21d714b0137ee0f776a1f9ec7a2b966e707dc3a868bf95d9d21de14207124aba4a896ef46c3d3eb7f4679a03e0af827226f0b2f365e1ea491932ff80e3674d2a0a2de14002ddc4618a4b01b4ed8a8f67ea780a965dfaf8e1d33d8d03ed2ece903f7957b10904fa2ed9fd7ae2f711a0c47e33553ffaa76036bb7a2b3a78a73074ec80f2e6f48e10e1a1dd8ea431688df128b7aaed1bec05fad33811ef6f42b6dc30179325a0b24840cb84ac49e2c4a9cb3716fe905bcb4c89b6b505737826da7a659d2a0d57ea7d405553156286ff0f85e803c58ecbeac59a1dacd39280dd920500b8b73546e9cc707f9f5f053b05fa027a6d866d163972ddad421b1d0398c36c8b7c4b033757a52524db9b9aef5f1e99a09355fba0a4a50289e0b64af6a240e065ba3a81529e78eab28b8cff4c18dc7091ffff0de9157a0167e20c26df49bf853e6dd291e024e4842c38a7b6e1544dd3435d4f1ac7b64944dd240cacae825ad33e2cfc85d46e9f745b2d5a26fb604d238a26a19e7fed07222684979962b540b26800d0cf0b3e6cd9baa5b3542909661d5b3f73b41daf601708954db3a913d75608ee12032fb292b245036430e57be23fb8b7495a7899fc16a895b962f3d48405aec4a2d5b6f0e117871abf87908c3ff538a81d21825fa9386f12a74e2e44521b2e53e650f2ba7c2684afc1905609cb1ba3cc6f5cf245287f16d6114d0455fbd2daba2208e9af8bc44b08e0fd41dfd351aeff860ad29ad4362dddb3588fc3bbc02e3bd0d17522165759c27c1b0789fe0b7b0f620d1870da4ef03a70a4aee35e6f2cbd2026716edd7d521e4b983016844c30df4f27b5ba5b6c363d5bed215a7183897c0af22e49c2a80af00d774da3132aa5c0624fe0d9c51b6dff4d0fdddd522619a848e64bf23aea6bf371960353eae40a1f2a0f2ce073979f893c1da5b24c1e800222eb5cecd764eb5809d2ea25345abfd853cb66293b3dfda72aaa2190567b8dc8b9fc84ad103d3cb0c179f43967872389f7ab12ca900b1cbe3281c18b56fe87f9ac0cf77a84268f2fe81d507757b2304681b273de93d3d2f91ac3d5e7d49524ecb272b077905dd98423bbff94181dd325fbb80cc82eaaa219c6a9aab327aaaae89dc4e1fe94e41b9d81eb93d24b83c6006f0d7ccb7bf4cafabf78fce8781c534f304b5bb5ed998331bc3ee0b059b04265d3c2a9bb95bc600b7f925be148bd28ff1c009a3c3d9c7f33701911ff444233d3b1490ef09a4fa074c3df497fc7ea48957fe3bf9938870db091c6f90ea66e4aa7975c82802e8ceeeda944b1d7970c5f691cf5ff32088635f6cf1f622ed8a6719b490cf1da61ecd22c85cf03cf3cb3894d88d4ae7e9c6e04cd1de8fa6019ff0ba4661416c8ca19619b0d2f7a52b24cb1c70655186edf426cae9bc7ce51a441d5dcfa06c4861a865649168420338a191c9047389e2fdd7aef1a82e2f1d14e507c2d42a2aa7a247ef36685efbe14b0b4916497b75316afdc9945061395b4aa1759e776cc5573614f9f0db53edad0090373c74329564f94d45e2206317e437eb5077f99bc4ce7e90777297014981d71141760581eefdadbf139d3fb65f9595641688f204edb2770e84b3615eab5d07301f36fd202ca8162b0a62564208615b3f54c02391951e162919a1d1cba0e232d13e203d3f5cf2d2d5af6269ae56bc4e45135f4c95263be4dd6ef361defd7309a0e2594acc21226918c66f3797ca3c89ffe74538026c41650d26d7154ffb68e9b6065f89ca1f91aa23c30ef76d78ca2b0fb9db5ece4c6af41626ec5302ac09015d27035bc2501908540353518f0e95b5164f8319d234066829fe6df0537d06beb284f0d008c842983f0a9f0e6974776589a4f26f87f0aa963dead5c732d4b334798ad299301597fba28cbcd4040d28e286b4d7257f211e03dfa01a0f2cf35c71fa52f909b59d4c6c1a06511a88dcf998b828fa5f6cc12c85c729d278d2fdf7503e5d50b7aea6897b1bd093a090ae0fb45b3ac8826431ace6fea7ac1c17b1eb2d2ba5381acec35e54f649b50a5a931d81698c91a8a5be1a214822fc3427e26124cc58b18e09836c4af13e249b1f66547c48d8a22ba73b7342d4981e3414d26bffa27c442707c812ae3a506a351d6ba85660fe1e1a4acc69a0b6f1e24817790d8d99f28270ea5f9656f3a61d65ac453cb0704f8778feb0422cfeadddf8e22c93038760c011c5ffc90384ded5f8242b22b5bc31a906051a51ce73b77ff438eae76226ccfd64ade0d18a3bae57f893917a603e87eb56e2940b0ebae09365b0f7271eb00861d23f8e87a419405bf2dbd9f3eb586abf0ed57fb52d0124a7efa48a8b8141ea97b0f07171c9f454974e23eda857973d121da1250d7c734f8554861b927689c11ac84480ac7b61c761741260be0f34edd2867fcca10463d022b20d314db8921f2e4c955353454c2f1e04340e3f596befb1857e0a344c9e7781246efbc326d0b75aed93e98cce7b2bb50c90a24a38999ad7ba0d39173df4b16ab0d012f298772ef071c516b0bfb836c9ea800d698a44f2dd4e37b3256c4c0e43323307775f44f2dd4e37b3256c4c0e43323307775f44f2dd4e37b3256c4c0e43323307775f44f2dd4e37b3256c4c0e43323307775f44f2dd4e37b3256c4c0e43323307775f44f2dd4e37b3256c4c0e43323307775f44f2dd4e37b3256c4c0e43323307775f44f2dd4e37b3256c4c0e43323307775f44f2dd4e37b3256c4c0e43323307775fabada3a8119783f6123d381970d0fd3d27d54308f7596e5b0370ba3f72788ec013a58e38c7a17713a863de614cc8decc59f37eacafd566d059b48631cabed8709fdc4d6055074b798f77844c93f6a4400d91cc84354e0888f2769221a6c5c008563533baaebf5b58b48f9ca57ec91a9fc6ddb4afb43bc68f7c8218e1e606e95223f69c24446311382e07f200103f65964f571aef9e6f1dd4f54e0173d88a386ae3156aafb11f32fd5d090cb150bb89ffbbaf34e1bb300511929caa299d91335ae9bf759b9cb999ce3699a76fa756710b136ef3f6d282e8d43238da6e5ce35721c25dfd96d3d02977627a1991555e7c256bb078c5210902cb7660819b577c5e47d034e1e9847f98d45b4ee872772946a165587cc232176e76e26e757d1e25362941d7ac15bc3954936156c435b6df7a16";
    }
}


