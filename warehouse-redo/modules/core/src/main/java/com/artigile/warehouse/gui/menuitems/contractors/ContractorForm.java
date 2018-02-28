/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.contractors;

import com.artigile.warehouse.bl.contractors.AccountAction;
import com.artigile.warehouse.bl.contractors.PutMoneyToAccountAction;
import com.artigile.warehouse.bl.contractors.TransferAccountCurrencyAction;
import com.artigile.warehouse.bl.contractors.WithdrawMoneyFromAccountAction;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.init.ComboBoxFillOptions;
import com.artigile.warehouse.gui.core.properties.data.init.InitUtils;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.TableReport;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.core.report.view.PredefinedCommand;
import com.artigile.warehouse.gui.menuitems.contractors.accounts.AccountsList;
import com.artigile.warehouse.gui.menuitems.contractors.accounts.PutOrWithdrawForm;
import com.artigile.warehouse.gui.menuitems.contractors.accounts.TransferAccountForm;
import com.artigile.warehouse.gui.menuitems.contractors.contacts.ContactsList;
import com.artigile.warehouse.gui.menuitems.contractors.shipping.ShippingList;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.dto.*;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author IoaN, Dec 10, 2008
 */

public class ContractorForm implements PropertiesForm {

    private JPanel contentPanel;

    private JTextField name;

    private JTextField legalAddress;

    private JComboBox defaultLoadPlace;

    private JTextField url;

    private JTextField bankAccount;

    private JTextField bankCode;

    private JTextField okpo;

    private JTextField unp;

    private JTextField bankShortData;

    private JTextField bankFullData;

    private JComboBox currency;

    private JTextArea notice;

    private JComboBox country;

    private JPanel balancePanel;

    private JTabbedPane tabs;

    private JPanel contactsListPanel;

    private JPanel balanceListPanel;

    private JTextField bankAddress;

    private JRadioButton fieldRating4;

    private JRadioButton fieldRating2;

    private JRadioButton fieldRating3;

    private JRadioButton fieldRating5;

    private JRadioButton fieldRatingUnknown;

    private JRadioButton fieldRating1;

    private JPanel shippingListPanel;

    private JTextField discount;

    private JTextField email;

    private JTextField fullname;

    private JTextField postalAddress;

    private JTextField phone;

    private AccountsList accountsList;

    private ContactsList contactsList;

    private ShippingList shippingList;

    private boolean canEdit;

    /**
     * Data of contractor being edited.
     */
    private ContractorTO contractorTO;

    /**
     * Boundaries of the discount (in percent)
     */
    private static final int MIN_DISCOUNT = 0;

    private static final int MAX_DISCOUNT = 100;

    /**
     * Actions, that user has performed with contractor's balance accounts.
     */
    private List<AccountAction> accountActions = new ArrayList<AccountAction>();

    public ContractorForm(ContractorTO contractorTO, boolean editRight) {
        this.contractorTO = contractorTO;
        this.canEdit = editRight;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(name, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(fullname, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(legalAddress, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(postalAddress, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(phone, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(url, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(email, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(bankAccount, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(bankCode, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(okpo, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(unp, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(bankShortData, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(bankFullData, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(notice, ModelFieldsLengths.DEFAULT_BIG_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(bankAddress, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        InitUtils.initCurrenciesCombo(currency, null);
        InitUtils.initLoadPlacesCombo(defaultLoadPlace, new ComboBoxFillOptions().setAddNotSelectedItem(true));
        InitUtils.initCountriesCombo(country, new ComboBoxFillOptions().setAddNotSelectedItem(true));
        AutoCompleteDecorator.decorate(country);

        if (contractorTO.isNew()) {
            //Balance operations are available only for existing update.
            tabs.remove(balancePanel);
        }
    }

    //============================ PropertiesForm implementation =========================

    @Override
    public String getTitle() {
        return I18nSupport.message("contractor.property.window");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public boolean canSaveData() {
        return canEdit;
    }

    @Override
    public void loadData() {
        name.setText(contractorTO.getName());
        fullname.setText(contractorTO.getFullName());
        legalAddress.setText(contractorTO.getLegalAddress());
        postalAddress.setText(contractorTO.getPostalAddress());
        phone.setText(contractorTO.getPhone());
        url.setText(contractorTO.getWebSiteURL());
        email.setText(contractorTO.getEmail());
        bankAccount.setText(contractorTO.getBankAccount());
        okpo.setText(contractorTO.getOkpo());
        unp.setText(contractorTO.getUnp());
        bankCode.setText(contractorTO.getBankCode());
        bankAddress.setText(contractorTO.getBankAddress());
        bankShortData.setText(contractorTO.getBankShortData());
        bankFullData.setText(contractorTO.getBankFullData());
        discount.setText(String.valueOf(contractorTO.getDiscount()));

        if (contractorTO.getRating() == null)
            fieldRatingUnknown.setSelected(true);
        else if (contractorTO.getRating().equals(ContractorRatingList.RATING_1.getContractorRating()))
            fieldRating1.setSelected(true);
        else if (contractorTO.getRating().equals(ContractorRatingList.RATING_2.getContractorRating()))
            fieldRating2.setSelected(true);
        else if (contractorTO.getRating().equals(ContractorRatingList.RATING_3.getContractorRating()))
            fieldRating3.setSelected(true);
        else if (contractorTO.getRating().equals(ContractorRatingList.RATING_4.getContractorRating()))
            fieldRating4.setSelected(true);
        else if (contractorTO.getRating().equals(ContractorRatingList.RATING_5.getContractorRating()))
            fieldRating5.setSelected(true);


        notice.setText(contractorTO.getNotice());
        DataExchange.selectComboItem(currency, contractorTO.getDefaultCurrency());
        DataExchange.selectComboItem(defaultLoadPlace, contractorTO.getDefaultShippingAddress());
        DataExchange.selectComboItem(country, contractorTO.getCountry());
        getContacts(contractorTO.getId());
        getShippings(contractorTO.getId());

        if (new PermissionCommandAvailability(PermissionType.VIEW_CONTRACTOR_BALANCE).isAvailable(null)) {
            //User has permission to vew contractor's balance.
            initBalanceTab();
        } else {
            tabs.remove(balancePanel);
        }
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(name);
        DataValidation.checkSelected(currency);

        // Validate discount
        String discountStr = discount.getText();
        DataValidation.checkIsNumberInteger(discountStr, discount);
        DataValidation.checkValueRangeLong(Integer.valueOf(discountStr), discount, MIN_DISCOUNT, MAX_DISCOUNT);
    }

    @Override
    public void saveData() {
        contractorTO.setName(name.getText());
        contractorTO.setFullName(fullname.getText());
        contractorTO.setLegalAddress(legalAddress.getText());
        contractorTO.setPostalAddress(postalAddress.getText());
        contractorTO.setPhone(phone.getText());
        contractorTO.setDefaultShippingAddress((LoadPlaceTO) DataExchange.getComboSelection(defaultLoadPlace));
        contractorTO.setWebSiteURL(url.getText());
        contractorTO.setEmail(email.getText());
        contractorTO.setBankAccount(bankAccount.getText());
        contractorTO.setBankCode(bankCode.getText());
        contractorTO.setBankAddress(bankAddress.getText());
        contractorTO.setOkpo(okpo.getText());
        contractorTO.setUnp(unp.getText());
        contractorTO.setBankShortData(bankShortData.getText());
        contractorTO.setBankFullData(bankFullData.getText());
        contractorTO.setDiscount(Integer.valueOf(discount.getText()));

        if (fieldRating1.isSelected()) contractorTO.setRating(ContractorRatingList.RATING_1.getContractorRating());
        else if (fieldRating2.isSelected()) contractorTO.setRating(ContractorRatingList.RATING_2.getContractorRating());
        else if (fieldRating3.isSelected()) contractorTO.setRating(ContractorRatingList.RATING_3.getContractorRating());
        else if (fieldRating4.isSelected()) contractorTO.setRating(ContractorRatingList.RATING_4.getContractorRating());
        else if (fieldRating5.isSelected()) contractorTO.setRating(ContractorRatingList.RATING_5.getContractorRating());
        else if (fieldRatingUnknown.isSelected())
            contractorTO.setRating(ContractorRatingList.RATING_UNKNOWN.getContractorRating());

        contractorTO.setNotice(notice.getText());
        contractorTO.setCountry((String) DataExchange.getComboSelection(country));
        contractorTO.setDefaultCurrency((CurrencyTO) DataExchange.getComboSelection(currency));
        contractorTO.setDefaultShippingAddress((LoadPlaceTO) DataExchange.getComboSelection(defaultLoadPlace));
    }

    //========================== Form result getters =============================

    public List<ContactTO> getUpdatedContacts() {
        return contactsList.getUpdatedContacts();
    }

    public List<ShippingTO> getUpdatedShippings() {
        return shippingList.getUpdatedShippings();
    }

    public List<AccountAction> getAccountActions() {
        return accountActions;
    }

    //========================= Helpers ==========================================

    private void onTransferAccount() {
        TransferAccountForm transferAccountForm = new TransferAccountForm(accountsList.getReportData(), accountsList.getSelectedItem());
        if (Dialogs.runProperties(transferAccountForm)) {
            //1. Updating accounts balance in table.
            AccountTO fromAccount = transferAccountForm.getFromAccount();
            BigDecimal changeOfFromAccount = transferAccountForm.getChangeOfFromBalance();
            accountsList.fireItemChanged(fromAccount);

            AccountTO toAccount = transferAccountForm.getToAccount();
            BigDecimal changeOfToAccount = transferAccountForm.getChangeOfToBalance();
            accountsList.fireItemChanged(toAccount);

            //2. Saving new account transfer operation.
            BigDecimal usedExchangeRate = transferAccountForm.getUsedExchangeRate();
            storeAccountAction(new TransferAccountCurrencyAction(contractorTO.getId(), fromAccount.getCurrency(),
                    changeOfFromAccount, toAccount.getCurrency(), changeOfToAccount, usedExchangeRate, toAccount.getNotice()));
        }
    }

    private void onWithdraw() {
        AccountTO account = accountsList.getSelectedItem();
        if (account != null) {
            BigDecimal oldBalance = account.getCurrentBalance();
            PutOrWithdrawForm changeBalanceForm = new PutOrWithdrawForm(false, account);
            if (Dialogs.runProperties(changeBalanceForm)) {
                BigDecimal changeOfBalance = account.getCurrentBalance().subtract(oldBalance);
                storeAccountAction(new WithdrawMoneyFromAccountAction(contractorTO.getId(), account.getCurrency().getId(), changeOfBalance, account.getNotice()));
                accountsList.fireItemChanged(account);
            }
        }
    }

    private void onPutMoney() {
        AccountTO account = accountsList.getSelectedItem();
        if (account != null) {
            BigDecimal oldBalance = account.getCurrentBalance();
            PutOrWithdrawForm changeBalanceForm = new PutOrWithdrawForm(true, account);
            if (Dialogs.runProperties(changeBalanceForm)) {
                BigDecimal changeOfBalance = account.getCurrentBalance().subtract(oldBalance);
                storeAccountAction(new PutMoneyToAccountAction(contractorTO.getId(), account.getCurrency().getId(), changeOfBalance, account.getNotice()));
                accountsList.fireItemChanged(account);
            }
        }
    }

    private void storeAccountAction(AccountAction action) {
        //This method may be used to implement checks before adding actions into queue of actions.
        accountActions.add(action);
    }

    private void getContacts(long id) {
        contactsList = new ContactsList(id, canEdit);
        contactsList.getReportInfo().getOptions().disablePredefinedCommand(PredefinedCommand.REFRESH);
        TableReport contactsReport = new TableReport(contactsList);
        contactsListPanel.add(contactsReport.getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
    }

    private void getShippings(long id) {
        shippingList = new ShippingList(id, canEdit);
        shippingList.getReportInfo().getOptions().disablePredefinedCommand(PredefinedCommand.REFRESH);
        TableReport shippingsReport = new TableReport(shippingList);
        shippingListPanel.add(shippingsReport.getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
    }

    private void initBalanceTab() {
        accountsList = new AccountsList(contractorTO.getId(), getAccountsEditingStrategy());
        TableReport accountsReport = new TableReport(accountsList);
        balanceListPanel.add(accountsReport.getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
    }

    private ReportEditingStrategy getAccountsEditingStrategy() {
        //Constructing editing strategy for accounts list.
        return new ReportEditingStrategy() {

            @Override
            public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
                //No commands are supported.
            }

            @Override
            public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
                commands.add(new CustomCommand(new ResourceCommandNaming("contractors.properties.button.append.account"), getEditAvailability()) {

                    @Override
                    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
                        onPutMoney();
                        return true;
                    }
                });
                commands.add(new CustomCommand(new ResourceCommandNaming("contractors.properties.buttons.withdraw"), getEditAvailability()) {

                    @Override
                    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
                        onWithdraw();
                        return true;
                    }
                });
                commands.add(new CustomCommand(new ResourceCommandNaming("contractors.properties.button.translate"), getEditAvailability()) {

                    @Override
                    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
                        onTransferAccount();
                        return true;
                    }
                });
            }

            private AvailabilityStrategy getEditAvailability() {
                return new PermissionCommandAvailability(PermissionType.EDIT_CONTRACTOR_BALANCE);
            }
        };
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:d:noGrow,top:5dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        tabs = new JTabbedPane();
        CellConstraints cc = new CellConstraints();
        contentPanel.add(tabs, cc.xywh(1, 1, 3, 31, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(11, 2, new Insets(5, 5, 5, 5), -1, -1));
        tabs.addTab(ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.tab.main"), panel1);
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.name"));
        panel1.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        name = new JTextField();
        panel1.add(name, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.noticeEdit"));
        panel1.add(label2, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        notice = new JTextArea();
        notice.setLineWrap(false);
        notice.setRows(0);
        scrollPane1.setViewportView(notice);
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.country"));
        panel1.add(label3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        country = new JComboBox();
        country.setEditable(false);
        panel1.add(country, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.legal.address"));
        panel1.add(label4, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        legalAddress = new JTextField();
        panel1.add(legalAddress, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.urlEdit"));
        panel1.add(label5, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        url = new JTextField();
        url.setText("");
        panel1.add(url, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.currencyEdit"));
        panel1.add(label6, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currency = new JComboBox();
        panel1.add(currency, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.email"));
        panel1.add(label7, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        email = new JTextField();
        panel1.add(email, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.fullname"));
        panel1.add(label8, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fullname = new JTextField();
        fullname.setText("");
        panel1.add(fullname, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label9 = new JLabel();
        this.$$$loadLabelText$$$(label9, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.postal.address"));
        panel1.add(label9, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        postalAddress = new JTextField();
        panel1.add(postalAddress, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label10 = new JLabel();
        this.$$$loadLabelText$$$(label10, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.phone"));
        panel1.add(label10, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        phone = new JTextField();
        panel1.add(phone, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label11 = new JLabel();
        this.$$$loadLabelText$$$(label11, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.unpEdit"));
        panel1.add(label11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        unp = new JTextField();
        panel1.add(unp, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 2, new Insets(5, 5, 5, 5), -1, -1));
        tabs.addTab(ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.tab.contacts.and.shipping"), panel2);
        final JLabel label12 = new JLabel();
        this.$$$loadLabelText$$$(label12, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.def.ship.addressEdit"));
        panel2.add(label12, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        defaultLoadPlace = new JComboBox();
        panel2.add(defaultLoadPlace, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contactsListPanel = new JPanel();
        contactsListPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(contactsListPanel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(400, 150), null, null, 0, false));
        contactsListPanel.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.contacts")));
        shippingListPanel = new JPanel();
        shippingListPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(shippingListPanel, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(400, 150), null, null, 0, false));
        shippingListPanel.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.shippings")));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(7, 2, new Insets(5, 5, 5, 5), -1, -1));
        tabs.addTab(ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.tab.bank.details"), panel3);
        final JLabel label13 = new JLabel();
        this.$$$loadLabelText$$$(label13, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.bank.accountEdit"));
        panel3.add(label13, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bankAccount = new JTextField();
        panel3.add(bankAccount, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label14 = new JLabel();
        this.$$$loadLabelText$$$(label14, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.mfoEdit"));
        panel3.add(label14, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bankCode = new JTextField();
        panel3.add(bankCode, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label15 = new JLabel();
        this.$$$loadLabelText$$$(label15, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.okpoEdit"));
        panel3.add(label15, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        okpo = new JTextField();
        panel3.add(okpo, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label16 = new JLabel();
        this.$$$loadLabelText$$$(label16, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.bank.short.dataEdit"));
        panel3.add(label16, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bankShortData = new JTextField();
        panel3.add(bankShortData, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label17 = new JLabel();
        this.$$$loadLabelText$$$(label17, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.bank.full.dataEdit"));
        panel3.add(label17, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bankFullData = new JTextField();
        panel3.add(bankFullData, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label18 = new JLabel();
        this.$$$loadLabelText$$$(label18, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.bank.addressEdit"));
        panel3.add(label18, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bankAddress = new JTextField();
        panel3.add(bankAddress, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        balancePanel = new JPanel();
        balancePanel.setLayout(new GridLayoutManager(2, 1, new Insets(5, 5, 5, 5), -1, -1));
        tabs.addTab(ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.tab.balance"), balancePanel);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        balancePanel.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.current.balance")));
        balanceListPanel = new JPanel();
        balanceListPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(balanceListPanel, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(400, 150), null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        balancePanel.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(3, 2, new Insets(5, 5, 5, 5), -1, -1));
        tabs.addTab(ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.tab.other"), panel5);
        final JLabel label19 = new JLabel();
        this.$$$loadLabelText$$$(label19, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.rating"));
        panel5.add(label19, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel5.add(spacer3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldRating1 = new JRadioButton();
        fieldRating1.setText("1");
        panel6.add(fieldRating1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldRating4 = new JRadioButton();
        fieldRating4.setText("4");
        panel6.add(fieldRating4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldRating2 = new JRadioButton();
        fieldRating2.setText("2");
        panel6.add(fieldRating2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldRating3 = new JRadioButton();
        fieldRating3.setText("3");
        panel6.add(fieldRating3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldRating5 = new JRadioButton();
        fieldRating5.setText("5");
        panel6.add(fieldRating5, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldRatingUnknown = new JRadioButton();
        this.$$$loadButtonText$$$(fieldRatingUnknown, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.radioButtonText.unknownRating"));
        panel6.add(fieldRatingUnknown, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label20 = new JLabel();
        this.$$$loadLabelText$$$(label20, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.discount"));
        panel5.add(label20, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        discount = new JTextField();
        panel5.add(discount, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        label13.setLabelFor(bankAccount);
        label14.setLabelFor(bankCode);
        label15.setLabelFor(okpo);
        label16.setLabelFor(bankShortData);
        label17.setLabelFor(bankFullData);
        label18.setLabelFor(bankAddress);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPanel;
    }
}
