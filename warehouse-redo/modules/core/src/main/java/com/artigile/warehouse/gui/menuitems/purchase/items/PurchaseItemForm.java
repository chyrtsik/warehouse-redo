/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.purchase.items;

import com.artigile.warehouse.bl.finance.CurrencyExchangeService;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.utils.UIComponentUtils;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.needs.WareNeedItemTO;
import com.artigile.warehouse.utils.dto.purchase.PurchaseItemTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 02.03.2009
 */
public class PurchaseItemForm implements PropertiesForm {
    private JPanel contentPanel;
    private JTextField fieldName;
    private JTextField fieldMisc;
    private JTextField fieldCount;
    private JTextField fieldPrice;
    private JTextField fieldTotalPrice;
    private JTextArea fieldNotice;
    private JLabel fieldCountMeas;
    private JLabel fieldPriceCurrency;
    private JLabel fieldTotalPriceCurrency;
    private JTextField fieldMaxPrice;
    private JLabel fieldMaxPriceCurrency;

    private PurchaseItemTO purchaseItem;
    private boolean canEdit;

    public PurchaseItemForm(PurchaseItemTO purchaseItem, boolean canEdit) {
        this.purchaseItem = purchaseItem;
        this.canEdit = canEdit;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(fieldName, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(fieldMisc, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(fieldCount, ModelFieldsLengths.MAX_TEXT_LENGTH_LONG_VAR);
        DataFiltering.setTextLengthLimit(fieldPrice, ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION);
        DataFiltering.setTextLengthLimit(fieldNotice, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        initFields();
        initListeners();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("purchase.item.properties.title");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public boolean canSaveData() {
        return canEdit;
    }

    private BigDecimal getMaxPrice() {
        WareNeedItemTO wareNeedItem = purchaseItem.getWareNeedItem();
        if (wareNeedItem == null || wareNeedItem.getMaxPrice() == null) {
            //Max price not set. This is allowed.
            return null;
        }

        long wareNeedItemCurrencyId = wareNeedItem.getCurrency().getId();
        long purchaseItemCurrencyId = purchaseItem.getPurchase().getCurrency().getId();
        if (wareNeedItemCurrencyId == purchaseItemCurrencyId) {
            return wareNeedItem.getMaxPrice();
        } else {
            //Ware need price should be converted into purchase currency.
            CurrencyExchangeService exchange = SpringServiceContext.getInstance().getCurencyExchangeService();
            return exchange.convert(purchaseItemCurrencyId, wareNeedItemCurrencyId, wareNeedItem.getMaxPrice());
        }
    }

    private String getMaxPriceCurrency() {
        return purchaseItem.getPurchase().getCurrency().getSign();
    }

    @Override
    public void loadData() {
        fieldName.setText(purchaseItem.getItemName());
        fieldMisc.setText(purchaseItem.getItemMisc());
        fieldCount.setText(purchaseItem.getCount() == null ? null : String.valueOf(purchaseItem.getCount()));
        fieldCountMeas.setText(purchaseItem.isText() || purchaseItem.getWareNeedItem().isText() ? null : purchaseItem.getWareNeedItem().getDetailBatch().getCountMeas().getSign());
        fieldPrice.setText(StringUtils.formatNumber(purchaseItem.getPrice()));
        fieldPriceCurrency.setText(purchaseItem.getPurchase().getCurrency().getSign());
        fieldMaxPrice.setText(StringUtils.formatNumber(getMaxPrice()));
        fieldMaxPriceCurrency.setText(getMaxPriceCurrency());
        fieldTotalPrice.setText(StringUtils.formatNumber(purchaseItem.getTotalPrice()));
        fieldTotalPriceCurrency.setText(purchaseItem.getPurchase().getCurrency().getSign());
        fieldNotice.setText(purchaseItem.getNotice());
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(fieldName);
        DataValidation.checkNotEmpty(fieldCount);
        DataValidation.checkIsNumberLong(fieldCount.getText(), fieldCount);
        DataValidation.checkNotEmpty(fieldPrice);
        DataValidation.checkIsNumber(fieldPrice.getText(), fieldPrice);
    }

    @Override
    public void saveData() {
        if (purchaseItem.isText()) purchaseItem.setName(fieldName.getText());
        if (purchaseItem.isText()) purchaseItem.setMisc(fieldMisc.getText());
        purchaseItem.setCount(Long.valueOf(fieldCount.getText()));
        purchaseItem.setPrice(StringUtils.parseStringToBigDecimal(fieldPrice.getText()));
        purchaseItem.setNotice(fieldNotice.getText());
    }

    //======================== User input processing and helpers ========================================

    private void initFields() {
        fieldName.setEditable(purchaseItem.isText());
        fieldName.setFocusable(purchaseItem.isText());
        fieldMisc.setEditable(purchaseItem.isText());
        fieldMisc.setFocusable(purchaseItem.isText());

        UIComponentUtils.makeSelectingAllTextOnFocus(fieldCount);
        UIComponentUtils.makeSelectingAllTextOnFocus(fieldPrice);
    }

    private void initListeners() {
        fieldPrice.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                refreshTotalPrice();
            }
        });

        fieldCount.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                refreshTotalPrice();
            }
        });
    }

    private void refreshTotalPrice() {
        if (StringUtils.isNumber(fieldPrice.getText()) && StringUtils.isNumberLong(fieldCount.getText())) {
            BigDecimal price = StringUtils.parseStringToBigDecimal(fieldPrice.getText());
            BigDecimal count = StringUtils.parseStringToBigDecimal(fieldCount.getText());
            fieldTotalPrice.setText(StringUtils.formatNumber(price.multiply(count)));
        } else {
            fieldTotalPrice.setText(null);
        }
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
        contentPanel.setLayout(new GridLayoutManager(6, 4, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("purchase.item.properties.name"));
        contentPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldName = new JTextField();
        contentPanel.add(fieldName, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("purchase.item.properties.misc"));
        contentPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldMisc = new JTextField();
        contentPanel.add(fieldMisc, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("purchase.item.properties.count"));
        contentPanel.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldCount = new JTextField();
        panel1.add(fieldCount, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        fieldCountMeas = new JLabel();
        fieldCountMeas.setText("<meas>");
        panel1.add(fieldCountMeas, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel2, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldTotalPrice = new JTextField();
        fieldTotalPrice.setEditable(false);
        fieldTotalPrice.setText("");
        panel2.add(fieldTotalPrice, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        fieldTotalPriceCurrency = new JLabel();
        fieldTotalPriceCurrency.setText("<currency>");
        panel2.add(fieldTotalPriceCurrency, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("purchase.item.properties.totalPrice"));
        contentPanel.add(label4, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("purchase.item.properties.notice"));
        contentPanel.add(label5, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPanel.add(scrollPane1, new GridConstraints(5, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 50), null, null, 0, false));
        fieldNotice = new JTextArea();
        scrollPane1.setViewportView(fieldNotice);
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("purchase.item.properties.price"));
        contentPanel.add(label6, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel3, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldPrice = new JTextField();
        panel3.add(fieldPrice, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        fieldPriceCurrency = new JLabel();
        fieldPriceCurrency.setText("<currency>");
        panel3.add(fieldPriceCurrency, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("purchase.item.properties.max.price"));
        contentPanel.add(label7, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel4, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldMaxPriceCurrency = new JLabel();
        fieldMaxPriceCurrency.setText("<currency>");
        panel4.add(fieldMaxPriceCurrency, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldMaxPrice = new JTextField();
        fieldMaxPrice.setEditable(false);
        panel4.add(fieldMaxPrice, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
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
