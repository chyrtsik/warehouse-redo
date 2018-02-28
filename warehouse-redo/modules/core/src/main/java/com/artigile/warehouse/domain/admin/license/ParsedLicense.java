/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.admin.license;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.utils.license.LicenseParsingUtils;

import java.util.*;

/**
 * Facade for accessing license internal data.
 *
 * @author Aliaksandr.Chyrtsik, 04.08.11
 */
public class ParsedLicense {
    /**
     * All fields of a license.
     */
    private Properties licenseFields;

    public ParsedLicense(Properties licenseFields){
        this.licenseFields = licenseFields;
    }

    /**
     * Checks if this license may be used (is active at this point of time).
     * @return
     */
    public boolean isActive() {
        Date currentDate = Calendar.getInstance().getTime();
        Date validFromDate = getValidFromDate();
        return validFromDate == null || validFromDate.compareTo(currentDate) <= 0;
    }

    /**
     * Checks if this license is expired.
     * @return
     */
    public boolean isExpired() {
        Date currentDate = Calendar.getInstance().getTime();
        Date validTillDate = getValidTillDate();
        return validTillDate != null && validTillDate.compareTo(currentDate) < 0;
    }

    public LicenseType getLicenseType() {
        return LicenseType.valueOf(getLicenseField(LicenseField.LICENSE_TYPE));
    }

    public Date getValidFromDate() {
        return LicenseParsingUtils.parseDateTime(getLicenseField(LicenseField.LICENSE_VALID_FROM_DATE));
    }

    public Date getValidTillDate() {
        return LicenseParsingUtils.parseDateTime(getLicenseField(LicenseField.LICENSE_VALID_TILL_DATE));
    }

    public String getIssuedFor() {
        return getLicenseField(LicenseField.LICENSE_OWNER_CLIENT_NAME);
    }

    public Boolean getAllPermissionsEnabled() {
        return LicenseParsingUtils.parseBoolean(getLicenseField(LicenseField.LICENSE_ALL_PERMISSIONS_FLAG));
    }

    public String getDescription() {
        return getLicenseField(LicenseField.LICENSE_DESCRIPTION);
    }

    public List<PermissionType> getIncludedPermissions(){
        String permissionsStr = getLicenseField(LicenseField.LICENSE_INCLUDED_PERMISSIONS);
        List<String> permissionStrings = LicenseParsingUtils.parseValuesList(permissionsStr);
        List<PermissionType> permissions = new ArrayList<PermissionType>();
        for (String permissionString : permissionStrings){
            permissions.add(PermissionType.valueOf(permissionString));
        }
        return permissions;
    }

    public List<String> getHardwareIds() {
        return LicenseParsingUtils.parseValuesList(getLicenseField(LicenseField.LICENSE_HARDWARE_IDS));
    }

    //====================================== Helpers ======================================================
    private String getLicenseField(LicenseField licenseField) {
        return licenseFields.getProperty(licenseField.name());
    }
}
