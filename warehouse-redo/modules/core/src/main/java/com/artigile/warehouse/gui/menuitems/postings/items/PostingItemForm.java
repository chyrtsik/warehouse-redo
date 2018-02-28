/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.postings.items;

import com.artigile.warehouse.bl.warehouse.StoragePlaceFilter;
import com.artigile.warehouse.bl.warehouse.WarehouseFilter;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.init.InitUtils;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.menuitems.marketProposals.MarketProposalsForm;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 06.02.2009
 */
public class PostingItemForm implements PropertiesForm {

    private JPanel contentPanel;

    private JTextField fieldName;

    private JLabel fieldOriginalCurrencyLabel;

    private JTextField fieldOriginalPrice;

    private JComboBox fieldOriginalCurrency;

    private JLabel fieldCurrencyLabel;

    private JTextField fieldPrice;

    private JTextField fieldCount;

    private JLabel fieldMeasure;

    private JComboBox fieldWarehouse;

    private JComboBox fieldStoragePlace;

    private JTextArea fieldNotice;

    private JTextField fieldTotalPrice;

    private JLabel fieldTotalPriceCurrency;

    private JTextField fieldAppendCount;

    private JLabel appendCountLabel;

    private JTextField fieldSalePrice;

    private JLabel fieldSaleCurrencyLabel;

    private JButton btnContractorsProposals;

    private JXDatePicker shelfLifeDate;

    private JLabel shelfLifeDateLabel;

    private WarehouseTOForReport selectedWarehouse;

    /**
     * Is true, posting item can be edited.
     */
    private boolean canEdit;

    /**
     * Is true, than append count field is shown (simplifies append of new count to posting item).
     */
    private boolean appendCount;

    /**
     * Posting item being edited.
     */
    private PostingItemTO postingItem;

    private Boolean trackShelfLife;

    public PostingItemForm(PostingItemTO postingItem, boolean canEdit, boolean appendCount) {
        this.postingItem = postingItem;
        this.canEdit = canEdit;

        PermissionCommandAvailability permissionAvailability = new PermissionCommandAvailability(PermissionType.EDIT_DETAIL_BATCH_SALE_PRICE);
        if (!permissionAvailability.isAvailable(null)) {
            fieldSalePrice.setEditable(false);
        }

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(fieldOriginalPrice, ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION);
        DataFiltering.setTextLengthLimit(fieldPrice, ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION);
        DataFiltering.setTextLengthLimit(fieldSalePrice, ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION);
        DataFiltering.setTextLengthLimit(fieldTotalPrice, ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION + 4);
        DataFiltering.setTextLengthLimit(fieldNotice, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(fieldCount, ModelFieldsLengths.MAX_TEXT_LENGTH_LONG_VAR);
        DataFiltering.setTextLengthLimit(fieldAppendCount, ModelFieldsLengths.MAX_TEXT_LENGTH_LONG_VAR);

        //Only enable append count facility if count is already set.
        this.appendCount = appendCount && (postingItem.getCount() != null);

        InitUtils.initCurrenciesCombo(fieldOriginalCurrency, null);
        InitUtils.initWarehousesCombo(fieldWarehouse, WarehouseFilter.createOnlyAvailableForPostingsFilter(), null);

        initListeners();
        initButtonListeners();

        fieldName.setFocusable(false);
        fieldPrice.setFocusable(false);
        fieldTotalPrice.setFocusable(false);

        if (this.appendCount) {
            fieldCount.setEditable(false);
            fieldCount.setFocusable(false);
            appendCountLabel.setVisible(true);
            fieldAppendCount.setVisible(true);
        } else {
            appendCountLabel.setVisible(false);
            fieldAppendCount.setVisible(false);
        }

        shelfLifeDate.setFormats(StringUtils.getDateFormat());
        if (!isTrackShelfLife()) {
            shelfLifeDateLabel.setVisible(false);
            shelfLifeDate.setVisible(false);
        }
    }


    @Override
    public String getTitle() {
        return I18nSupport.message("posting.item.properties.title");
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
        CurrencyTO postingCurrency = postingItem.getPosting().getCurrency();
        CurrencyTO detailBatchCurrency = postingItem.getDetailBatch().getCurrency();

        fieldName.setText(postingItem.getName());
        fieldOriginalPrice.setText(StringUtils.formatNumber(postingItem.getOriginalPrice()));
        DataExchange.selectComboItem(fieldOriginalCurrency, postingItem.isNew() ? postingItem.getPosting().getDefaultCurrency() : postingItem.getOriginalCurrency());
        fieldSaleCurrencyLabel.setText(detailBatchCurrency.getSign());

        String salePrice = StringUtils.formatNumber(postingItem.getSalePrice());
        String detailBatchSalePrice = StringUtils.formatNumber(postingItem.getDetailBatch().getSellPrice());
        fieldSalePrice.setText(salePrice.isEmpty() ? detailBatchSalePrice : salePrice);

        fieldCurrencyLabel.setText(postingCurrency == null ? null : postingCurrency.getSign());
        fieldPrice.setText(StringUtils.formatNumber(postingItem.getPrice()));
        fieldMeasure.setText(postingItem.getDetailBatch().getCountMeas().getSign());
        fieldCount.setText(postingItem.getCount() == null ? "" : String.valueOf(postingItem.getCount()));
        fieldTotalPrice.setText(StringUtils.formatMoneyAmount(postingItem.getTotalPrice()));
        fieldTotalPriceCurrency.setText(postingCurrency == null ? null : postingCurrency.getSign());
        DataExchange.selectComboItem(fieldWarehouse, postingItem.isNew()
                ? postingItem.getPosting().getWarehouse()
                : postingItem.getWarehouse());
        DataExchange.selectComboItem(fieldStoragePlace, postingItem.isNew()
                ? postingItem.getPosting().getDefaultStoragePlace()
                : postingItem.getStoragePlace());
        fieldNotice.setText(postingItem.getNotice());
        if (isTrackShelfLife()) {
            shelfLifeDate.setDate(postingItem.getShelfLifeDate());
        }
    }

    private Boolean isTrackShelfLife() {
        if (trackShelfLife == null) {
            trackShelfLife = SpringServiceContext.getInstance().getWarehouseBatchService().isTrackShelfLife();
        }
        return trackShelfLife;
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkIsNumberOrIsEmpty(fieldOriginalPrice.getText(), fieldOriginalPrice);
        DataValidation.checkSelected(fieldOriginalCurrency);
        DataValidation.checkIsNumberOrIsEmpty(fieldSalePrice.getText(), fieldSalePrice);
        DataValidation.checkNotEmpty(fieldCount);
        DataValidation.checkIsNumber(fieldCount.getText(), fieldCount);
        DataValidation.checkPositiveValue(Long.valueOf(fieldCount.getText()), fieldCount);

        if (appendCount) {
            DataValidation.checkNotEmpty(fieldAppendCount);
            DataValidation.checkIsNumber(fieldAppendCount.getText(), fieldAppendCount);
            DataValidation.checkPositiveValue(Long.valueOf(fieldAppendCount.getText()), fieldAppendCount);
        }

        DataValidation.checkSelected(fieldWarehouse);
        DataValidation.checkSelected(fieldStoragePlace);
    }

    @Override
    public void saveData() {
        String userSalePriceStr = fieldSalePrice.getText();
        BigDecimal initialSalePrice = postingItem.getDetailBatch().getSellPrice();
        boolean changeSalePrice = false;
        if (!userSalePriceStr.equals(StringUtils.formatNumber(initialSalePrice))) {
            changeSalePrice = MessageDialogs.showConfirm(contentPanel,
                    I18nSupport.message("posting.items.list.salePrice"),
                    I18nSupport.message("posting.item.confirm.change.salePrice"));
        }
        if (changeSalePrice) {
            postingItem.setSalePrice(StringUtils.parseStringToBigDecimal(userSalePriceStr, true));
        } else {
            postingItem.setSalePrice(initialSalePrice);
        }

        postingItem.setOriginalPrice(StringUtils.parseStringToBigDecimal(fieldOriginalPrice.getText(), true));
        postingItem.setOriginalCurrency((CurrencyTO) DataExchange.getComboSelection(fieldOriginalCurrency));
        postingItem.setPrice(StringUtils.parseStringToBigDecimal(fieldPrice.getText(), true));
        postingItem.setCount(getTotalItemCount().longValue());
        postingItem.setStoragePlace(SpringServiceContext.getInstance().getStoragePlaceService().get(((StoragePlaceTOForReport) DataExchange.getComboSelection(fieldStoragePlace)).getId()));
        postingItem.setNotice(fieldNotice.getText());
        if (isTrackShelfLife()) {
            postingItem.setShelfLifeDate(shelfLifeDate.getDate());
        }
    }

    //==================================== Helpers and user input processing ====================================

    private void refreshPrice() {
        //Refres prices (price of one item amd summary price)
        if (StringUtils.isNumber(fieldOriginalPrice.getText()) && fieldOriginalCurrency.getSelectedItem() != null) {
            BigDecimal originalPrice = StringUtils.parseStringToBigDecimal(fieldOriginalPrice.getText());
            long toCurrencyId = postingItem.getPosting().getCurrency().getId();
            long fromCurrencyId = ((CurrencyTO) DataExchange.getComboSelection(fieldOriginalCurrency)).getId();
            BigDecimal price = SpringServiceContext.getInstance().getExchangeService().convert(toCurrencyId, fromCurrencyId, originalPrice);

            fieldPrice.setText(StringUtils.formatNumber(price));

            BigDecimal count = getTotalItemCount();
            if (count != null && price != null) {
                fieldTotalPrice.setText(StringUtils.formatMoneyAmount(price.multiply(count)));
            } else {
                fieldTotalPrice.setText(null);
            }
        } else {
            fieldPrice.setText(null);
        }
    }

    public BigDecimal getTotalItemCount() {
        //Calculates new posting item count using currently entered values and form options.
        if (appendCount) {
            if (StringUtils.isNumberLong(fieldCount.getText()) && StringUtils.isNumberLong(fieldAppendCount.getText())) {
                BigDecimal count = StringUtils.parseStringToBigDecimal(fieldCount.getText());
                BigDecimal countToAppend = StringUtils.parseStringToBigDecimal(fieldAppendCount.getText());
                return count.add(countToAppend);
            }
        } else {
            if (StringUtils.isNumberLong(fieldCount.getText())) {
                return StringUtils.parseStringToBigDecimal(fieldCount.getText());
            }
        }

        return null;
    }

    private void onSelectWarehouse() {
        //When selecting warehouse, default storage places must be from this warehouse.
        WarehouseTOForReport newWarehouse = (WarehouseTOForReport) DataExchange.getComboSelection(fieldWarehouse);
        if (newWarehouse != selectedWarehouse) {
            InitUtils.initStoragePlacesCombo(fieldStoragePlace, StoragePlaceFilter.createAvailableForPostingsFilter(newWarehouse.getId()), null);
            selectedWarehouse = newWarehouse;
        }
    }

    private void initListeners() {
        fieldOriginalCurrency.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                refreshPrice();
                CurrencyTO newOrigignalCurrency = (CurrencyTO) DataExchange.getComboSelection(fieldOriginalCurrency);
                fieldOriginalCurrencyLabel.setText(newOrigignalCurrency == null ? null : newOrigignalCurrency.getSign());
            }
        });

        DocumentListener documentListener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                refreshPrice();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                refreshPrice();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                refreshPrice();
            }
        };
        fieldOriginalPrice.getDocument().addDocumentListener(documentListener);
        fieldCount.getDocument().addDocumentListener(documentListener);

        // TODO: This listener is really needed??? List with warehouses always DISABLED. (06.02.2012)
        fieldWarehouse.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onSelectWarehouse();
            }
        });

        fieldAppendCount.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                refreshPrice();
            }
        });
    }

    private void onMarketProposals() {
        List<Long> detailBatchIds = new ArrayList<Long>();
        detailBatchIds.add(postingItem.getDetailBatch().getId());
        List<Long> contractorIds = new ArrayList<Long>();
        List<Long> currencyIds = new ArrayList<Long>();
        List<Long> measureUnitIds = new ArrayList<Long>();
        Dialogs.runProperties(new MarketProposalsForm(detailBatchIds, contractorIds, currencyIds, measureUnitIds));
    }

    private void initButtonListeners() {
        btnContractorsProposals.setEnabled(true);
        btnContractorsProposals.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                onMarketProposals();
            }
        });
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
        contentPanel.setLayout(new GridLayoutManager(8, 5, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.properties.name"));
        contentPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.properties.originalPrice"));
        label2.setToolTipText("");
        contentPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.properties.count"));
        contentPanel.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.properties.totalPrice"));
        contentPanel.add(label4, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(7, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.properties.warehouse"));
        panel1.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldWarehouse = new JComboBox();
        fieldWarehouse.setEnabled(false);
        panel1.add(fieldWarehouse, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.properties.storagePlace"));
        panel1.add(label6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.properties.notice"));
        panel1.add(label7, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldStoragePlace = new JComboBox();
        panel1.add(fieldStoragePlace, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 50), null, null, 0, false));
        fieldNotice = new JTextArea();
        scrollPane1.setViewportView(fieldNotice);
        fieldOriginalCurrencyLabel = new JLabel();
        fieldOriginalCurrencyLabel.setText("<Currency>");
        contentPanel.add(fieldOriginalCurrencyLabel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        fieldOriginalPrice = new JTextField();
        fieldOriginalPrice.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.properties.originalPriceToolTip"));
        contentPanel.add(fieldOriginalPrice, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldOriginalCurrency = new JComboBox();
        contentPanel.add(fieldOriginalCurrency, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(75, -1), null, null, 0, false));
        fieldName = new JTextField();
        fieldName.setEditable(false);
        fieldName.setEnabled(true);
        contentPanel.add(fieldName, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldMeasure = new JLabel();
        fieldMeasure.setText("<Meas>");
        contentPanel.add(fieldMeasure, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        fieldTotalPriceCurrency = new JLabel();
        fieldTotalPriceCurrency.setText("<Currency>");
        contentPanel.add(fieldTotalPriceCurrency, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        fieldTotalPrice = new JTextField();
        fieldTotalPrice.setEditable(false);
        contentPanel.add(fieldTotalPrice, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel2, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldCount = new JTextField();
        panel2.add(fieldCount, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        appendCountLabel = new JLabel();
        appendCountLabel.setText("+");
        panel2.add(appendCountLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldAppendCount = new JTextField();
        panel2.add(fieldAppendCount, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.properties.price"));
        contentPanel.add(label8, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        this.$$$loadLabelText$$$(label9, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.properties.salePrice"));
        contentPanel.add(label9, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCurrencyLabel = new JLabel();
        fieldCurrencyLabel.setText("<Currency>");
        contentPanel.add(fieldCurrencyLabel, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        fieldSaleCurrencyLabel = new JLabel();
        fieldSaleCurrencyLabel.setText("<Currency>");
        contentPanel.add(fieldSaleCurrencyLabel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), null, null, 0, false));
        fieldPrice = new JTextField();
        fieldPrice.setEditable(false);
        contentPanel.add(fieldPrice, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldSalePrice = new JTextField();
        contentPanel.add(fieldSalePrice, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnContractorsProposals = new JButton();
        this.$$$loadButtonText$$$(btnContractorsProposals, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.properties.button.prices"));
        contentPanel.add(btnContractorsProposals, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        shelfLifeDate = new JXDatePicker();
        contentPanel.add(shelfLifeDate, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        shelfLifeDateLabel = new JLabel();
        this.$$$loadLabelText$$$(shelfLifeDateLabel, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.properties.shelf.life.date"));
        contentPanel.add(shelfLifeDateLabel, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
