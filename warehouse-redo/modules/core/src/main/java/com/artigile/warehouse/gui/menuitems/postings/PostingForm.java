/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.postings;

import com.artigile.warehouse.bl.contractors.ContractorService;
import com.artigile.warehouse.bl.deliveryNote.DeliveryNoteFilter;
import com.artigile.warehouse.bl.finance.CurrencyExchangeService;
import com.artigile.warehouse.bl.warehouse.StoragePlaceFilter;
import com.artigile.warehouse.bl.warehouse.WarehouseFilter;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteState;
import com.artigile.warehouse.domain.purchase.PurchaseState;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.init.InitUtils;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.browser.BrowseResult;
import com.artigile.warehouse.gui.menuitems.contractors.ContractorsList;
import com.artigile.warehouse.gui.menuitems.deliveryNote.DeliveryNoteList;
import com.artigile.warehouse.gui.menuitems.purchase.PurchaseList;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.AccountTO;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteTOForReport;
import com.artigile.warehouse.utils.dto.postings.PostingTOForReport;
import com.artigile.warehouse.utils.dto.postings.PostingType;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.CurrencyTransformer;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 03.02.2009
 */
public class PostingForm implements PropertiesForm {
    private JPanel contentPanel;
    private JTextField fieldState;
    private JTextField fieldNumber;
    private JButton generatePostingNumber;
    private JTextField fieldCreatedUser;
    private JXDatePicker fieldCreateDate;
    private JTextField fieldContractor;
    private JButton browseContractor;
    private JComboBox fieldDefaultCurrency;
    private JComboBox fieldCurrency;
    private JTextField fieldContractorBalance;
    private JTextField fieldTotalPrice;
    private JTextArea fieldNotice;
    private JComboBox fieldWarehouse;
    private JComboBox fieldDefaultStoragePlace;
    private JTextField fieldPurchase;
    private JButton browsePurchase;
    private JPanel purchasePanel;
    private JComboBox fieldPostingType;
    private JTextField fieldDeliveryNote;
    private JButton browseDeliveryNote;
    private JPanel deliveryNotePanel;

    private PostingTOForReport posting;
    boolean canEdit;

    /**
     * Temporary data (used additionally to data, stored in UI controls).
     */
    ContractorTO selectedContractor;
    WarehouseTOForReport selectedWarehouse;
    PurchaseTOForReport selectedPurchase;
    DeliveryNoteTOForReport selectedDeliveryNote;

    public PostingForm(PostingTOForReport posting, boolean canEdit) {
        this.posting = posting;
        this.canEdit = canEdit;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(fieldNumber, ModelFieldsLengths.MAX_TEXT_LENGTH_LONG_VAR);
        DataFiltering.setTextLengthLimit(fieldNotice, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        InitUtils.initCurrenciesCombo(fieldCurrency, null);
        InitUtils.initCurrenciesCombo(fieldDefaultCurrency, null);
        InitUtils.initWarehousesCombo(fieldWarehouse, WarehouseFilter.createOnlyAvailableForPostingsFilter(), null);
        InitUtils.initComboFromEnumeration(fieldPostingType, PostingType.values(), null);
        fieldCreateDate.setFormats(StringUtils.getDateFormat());
        initListeners();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("posting.properties.title");
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
        fieldState.setText(posting.getState().getName());
        fieldNumber.setText(posting.getNumber().toString());
        fieldCreatedUser.setText(posting.getCreatedUser().getDisplayName());
        fieldCreateDate.setDate(posting.getCreateDate());
        fieldNotice.setText(posting.getNotice());
        fieldTotalPrice.setText(posting.getTotalPrice() == null ? null : StringUtils.formatNumber(posting.getTotalPrice()));

        DataExchange.selectComboItem(fieldWarehouse, posting.isNew() ? WareHouse.getUserSession().getUserWarehouse() : posting.getWarehouse());
        DataExchange.selectComboItem(fieldDefaultStoragePlace, posting.getDefaultStoragePlace());

        DataExchange.selectComboItem(fieldPostingType, posting.getPostingType());
        selectedPurchase = posting.getPurchase();
        selectedDeliveryNote = posting.getDeliveryNote();
        selectedContractor = posting.getContractor();
        refreshPostingTypeDependentControls();

        DataExchange.selectComboItem(fieldDefaultCurrency, posting.getDefaultCurrency());
        if (posting.isNew()) {
            DataExchange.selectComboItem(fieldCurrency, getDefaultCurrency());
        } else {
            DataExchange.selectComboItem(fieldCurrency, posting.getCurrency());
        }
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(fieldNumber);
        DataValidation.checkIsNumberLong(fieldNumber.getText(), fieldNumber);
        DataValidation.checkValueMinLong(Long.valueOf(fieldNumber.getText()), fieldNumber, 1);
        if (!isUniquePostingNumber(Long.valueOf(fieldNumber.getText()), posting.getId())) {
            DataValidation.failRes(fieldNumber, "posting.properties.number.already.exists");
        }

        PostingType selectedPostingType = (PostingType) DataExchange.getComboSelection(fieldPostingType);
        if (selectedPostingType.equals(PostingType.FROM_PURCHASE)) {
            DataValidation.checkCondition(selectedPurchase != null, fieldPurchase, "posting.properties.purchaseNotSet");
        } else if (selectedPostingType.equals(PostingType.FROM_DELIVERY_NOTE)) {
            DataValidation.checkCondition(selectedDeliveryNote != null, fieldDeliveryNote, "posting.properties.deliveryNoteNotSet");
        }

        DataValidation.checkNotNull(fieldCreateDate.getDate(), fieldCreateDate);
        DataValidation.checkSelected(fieldDefaultCurrency);
        DataValidation.checkSelected(fieldCurrency);
        DataValidation.checkSelected(fieldWarehouse);
        DataValidation.checkSelected(fieldDefaultStoragePlace);
    }

    @Override
    public void saveData() {
        posting.setNumber(Long.valueOf(fieldNumber.getText()));
        posting.setCreateDate(fieldCreateDate.getDate());
        posting.setNotice(fieldNotice.getText());
        posting.setTotalPrice(StringUtils.parseStringToBigDecimal(fieldTotalPrice.getText().isEmpty() ? "0" : fieldTotalPrice.getText()));
        posting.setCurrency((CurrencyTO) DataExchange.getComboSelection(fieldCurrency));
        posting.setDefaultCurrency((CurrencyTO) DataExchange.getComboSelection(fieldDefaultCurrency));
        posting.setWarehouse((WarehouseTOForReport) DataExchange.getComboSelection(fieldWarehouse));
        posting.setDefaultStoragePlace((StoragePlaceTOForReport) DataExchange.getComboSelection(fieldDefaultStoragePlace));
        posting.setPurchase(selectedPurchase);
        posting.setDeliveryNote(selectedDeliveryNote);
        posting.setContractor(selectedContractor);
    }

    //==================================== Helpers ===========================================

    private CurrencyTO getDefaultCurrency() {
        return CurrencyTransformer.transformCurrency(SpringServiceContext.getInstance().getCurrencyService().getDefaultCurrency());
    }

    private void initListeners() {
        generatePostingNumber.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (posting.isNew()) {
                    fieldNumber.setText(String.valueOf(getNextAvailablePostingNumber()));
                }
            }
        });

        fieldCurrency.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshForNewDefaultCurrency();
            }
        });

        fieldPostingType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshPostingTypeDependentControls();
            }
        });

        browsePurchase.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBrowsePurchase();
            }
        });

        browseDeliveryNote.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBrowseDeliveryNote();
            }
        });

        browseContractor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBrowseContractor();
            }
        });

        fieldWarehouse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSelectWarehouse();
            }
        });
    }

    private void refreshPostingTypeDependentControls() {
        //Refreshing posting type dependent controls
        fieldPostingType.setEnabled(posting.canEditPostingType());
        PostingType selectedPostingType = (PostingType) DataExchange.getComboSelection(fieldPostingType);

        if (selectedPostingType.equals(PostingType.SIMPLE)) {
            //Simple posting (user chooses contractor from list).
            selectedPurchase = null;
            selectedDeliveryNote = null;
            selectedWarehouse = null;
        } else if (selectedPostingType.equals(PostingType.FROM_PURCHASE)) {
            //Posting from purchase.
            selectedContractor = null;
            selectedDeliveryNote = null;
            selectedWarehouse = null;
        } else if (selectedPostingType.equals(PostingType.FROM_DELIVERY_NOTE)) {
            //Posting from delivery note.
            selectedContractor = null;
            selectedPurchase = null;
            selectedWarehouse = null;
        } else {
            throw new RuntimeException("PostingForm.refreshPostingTypeDependentControls: Posting type not supported.");
        }

        purchasePanel.setVisible(selectedPostingType.equals(PostingType.FROM_PURCHASE));
        browsePurchase.setEnabled(posting.canEditPostingSource());
        refreshPurchase();

        deliveryNotePanel.setVisible(selectedPostingType.equals(PostingType.FROM_DELIVERY_NOTE));
        browseDeliveryNote.setEnabled(posting.canEditPostingSource());
        refreshDeliveryNote();

        browseContractor.setEnabled(selectedPostingType.equals(PostingType.SIMPLE));
        refreshContractor();

        fieldWarehouse.setEnabled(selectedDeliveryNote == null);
        fieldDefaultCurrency.setEnabled(selectedPurchase == null && selectedDeliveryNote == null);
    }

    private void onSelectWarehouse() {
        //When selecting warehouse, default storage places must be from this warehouse.
        WarehouseTOForReport newWarehouse = (WarehouseTOForReport) DataExchange.getComboSelection(fieldWarehouse);
        if (newWarehouse != selectedWarehouse) {
            InitUtils.initStoragePlacesCombo(fieldDefaultStoragePlace, StoragePlaceFilter.createAvailableForPostingsFilter(newWarehouse.getId()), null);
            selectedWarehouse = newWarehouse;
        }
    }

    private void refreshForNewDefaultCurrency() {
        refreshContractorBalance();
        refreshTotalPrice();
    }

    private void refreshTotalPrice() {
        CurrencyTO currency = (CurrencyTO) DataExchange.getComboSelection(fieldCurrency);
        if (currency != null && posting.getTotalPrice() != null && posting.getCurrency() != null) {
            //Display total price, converted the currency currency.
            CurrencyExchangeService exchangeService = SpringServiceContext.getInstance().getCurencyExchangeService();
            BigDecimal totalPrice = exchangeService.convert(currency.getId(), posting.getCurrency().getId(), posting.getTotalPrice());
            fieldTotalPrice.setText(StringUtils.formatNumber(totalPrice));
        } else {
            fieldTotalPrice.setText(null);
        }
    }

    private void refreshContractorBalance() {
        CurrencyTO currency = (CurrencyTO) DataExchange.getComboSelection(fieldDefaultCurrency);
        if (currency != null && selectedContractor != null) {
            ContractorService contractorService = SpringServiceContext.getInstance().getContractorService();
            AccountTO account = contractorService.getAccountByContractorAndCurrency(selectedContractor.getId(), currency.getId());
            if (account != null) {
                fieldContractorBalance.setText(StringUtils.formatNumber(account.getCurrentBalance()));
            }
        } else {
            fieldContractorBalance.setText(null);
        }
    }

    private void onBrowsePurchase() {
        //Show browser for choosing purchase.
        BrowseResult result = Dialogs.runBrowser(new PurchaseList(new PurchaseState[]{PurchaseState.SHIPPED}));
        if (result.isOk()) {
            selectedPurchase = (PurchaseTOForReport) result.getSelectedItems().get(0);
            refreshPostingTypeDependentControls();
        }
    }

    private void onBrowseDeliveryNote() {
        //Show browser for choosing delivery note.
        DeliveryNoteFilter filter = new DeliveryNoteFilter();
        filter.setStates(new DeliveryNoteState[]{DeliveryNoteState.SHIPPED});
        BrowseResult result = Dialogs.runBrowser(new DeliveryNoteList(filter));
        if (result.isOk()) {
            selectedDeliveryNote = (DeliveryNoteTOForReport) result.getSelectedItems().get(0);
            refreshPostingTypeDependentControls();
        }
    }

    private void onBrowseContractor() {
        //Show browser for choosing update.
        BrowseResult result = Dialogs.runBrowser(new ContractorsList());
        if (result.isOk()) {
            selectedContractor = (ContractorTO) result.getSelectedItems().get(0);
            selectedPurchase = null;
            refreshContractor();
            refreshPurchase();
        }
    }

    private void refreshPurchase() {
        if (selectedPurchase != null) {
            //Init fields from the puchase.
            fieldPurchase.setText(String.valueOf(selectedPurchase.getNumber()));
            selectedContractor = selectedPurchase.getContractor();
            DataExchange.selectComboItem(fieldDefaultCurrency, selectedPurchase.getCurrency());
            DataExchange.selectComboItem(fieldCurrency, selectedContractor.getDefaultCurrency());
        } else {
            fieldPurchase.setText(null);
        }
    }

    private void refreshDeliveryNote() {
        if (selectedDeliveryNote != null) {
            //Init fields from the delivery note.
            fieldDeliveryNote.setText(String.valueOf(selectedDeliveryNote.getNumber()));
            selectedContractor = null;
            DataExchange.selectComboItem(fieldWarehouse, selectedDeliveryNote.getDestinationWarehouse());
            DataExchange.selectComboItem(fieldDefaultCurrency, selectedDeliveryNote.getCurrency());
            DataExchange.selectComboItem(fieldCurrency, selectedDeliveryNote.getCurrency());
        } else {
            fieldDeliveryNote.setText(null);
        }
    }

    private void refreshContractor() {
        if (selectedContractor != null) {
            fieldContractor.setText(selectedContractor.getName());
            DataExchange.selectComboItem(fieldDefaultCurrency, selectedContractor.getDefaultCurrency());
        } else {
            fieldContractor.setText(null);
        }
        refreshContractorBalance();
    }

    private long getNextAvailablePostingNumber() {
        return SpringServiceContext.getInstance().getPostingsService().getNextAvailablePostingNumber();
    }

    private boolean isUniquePostingNumber(Long number, long postingId) {
        return SpringServiceContext.getInstance().getPostingsService().isUniquePostingNumber(number, postingId);
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
        contentPanel.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.state"));
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.number"));
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.createdUser"));
        panel1.add(label3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldState = new JTextField();
        fieldState.setEditable(false);
        panel1.add(fieldState, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldNumber = new JTextField();
        panel2.add(fieldNumber, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        generatePostingNumber = new JButton();
        generatePostingNumber.setText("=");
        panel2.add(generatePostingNumber, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(48, -1), null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.createDate"));
        panel1.add(label4, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCreateDate = new JXDatePicker();
        panel1.add(fieldCreateDate, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldCreatedUser = new JTextField();
        fieldCreatedUser.setEditable(false);
        panel1.add(fieldCreatedUser, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.contractor"));
        panel3.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        browseContractor = new JButton();
        browseContractor.setText("...");
        panel4.add(browseContractor, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldContractor = new JTextField();
        fieldContractor.setEditable(false);
        panel4.add(fieldContractor, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel5, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldDefaultCurrency = new JComboBox();
        panel5.add(fieldDefaultCurrency, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.defaultCurrency"));
        panel5.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.currency"));
        panel5.add(label7, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCurrency = new JComboBox();
        panel5.add(fieldCurrency, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.contractorBalance"));
        panel5.add(label8, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldContractorBalance = new JTextField();
        fieldContractorBalance.setEditable(false);
        panel5.add(fieldContractorBalance, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldTotalPrice = new JTextField();
        fieldTotalPrice.setEditable(false);
        fieldTotalPrice.setText("");
        panel5.add(fieldTotalPrice, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label9 = new JLabel();
        this.$$$loadLabelText$$$(label9, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.totalPrice"));
        panel5.add(label9, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        this.$$$loadLabelText$$$(label10, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.notice"));
        panel6.add(label10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel6.add(scrollPane1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 50), null, null, 0, false));
        fieldNotice = new JTextArea();
        scrollPane1.setViewportView(fieldNotice);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel7, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        this.$$$loadLabelText$$$(label11, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.warehouse"));
        panel7.add(label11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldWarehouse = new JComboBox();
        panel7.add(fieldWarehouse, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        this.$$$loadLabelText$$$(label12, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.defaultStoragePlace"));
        panel7.add(label12, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldDefaultStoragePlace = new JComboBox();
        panel7.add(fieldDefaultStoragePlace, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel8, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel8.add(panel9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        this.$$$loadLabelText$$$(label13, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.postingType"));
        panel9.add(label13, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldPostingType = new JComboBox();
        fieldPostingType.setEditable(false);
        panel9.add(fieldPostingType, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel8.add(panel10, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        purchasePanel = new JPanel();
        purchasePanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel10.add(purchasePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        this.$$$loadLabelText$$$(label14, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.purchase"));
        purchasePanel.add(label14, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        purchasePanel.add(panel11, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldPurchase = new JTextField();
        fieldPurchase.setEditable(false);
        panel11.add(fieldPurchase, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        browsePurchase = new JButton();
        browsePurchase.setText("...");
        panel11.add(browsePurchase, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(48, -1), null, null, 0, false));
        deliveryNotePanel = new JPanel();
        deliveryNotePanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel10.add(deliveryNotePanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        this.$$$loadLabelText$$$(label15, ResourceBundle.getBundle("i18n/warehouse").getString("posting.properties.deliveryNote"));
        deliveryNotePanel.add(label15, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        deliveryNotePanel.add(panel12, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldDeliveryNote = new JTextField();
        fieldDeliveryNote.setEditable(false);
        panel12.add(fieldDeliveryNote, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        browseDeliveryNote = new JButton();
        browseDeliveryNote.setText("...");
        panel12.add(browseDeliveryNote, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(48, -1), null, null, 0, false));
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
    public JComponent $$$getRootComponent$$$() {
        return contentPanel;
    }
}
