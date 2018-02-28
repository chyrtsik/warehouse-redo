/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.orders.items;

import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.orders.OrderItemTO;
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
 * @author Shyrik, 10.01.2009
 */
public class OrderTextItemForm implements PropertiesForm {
    private JTextField itemName;
    private JTextField itemPrice;
    private JTextField itemCount;
    private JTextField itemTotalPrice;
    private JLabel itemPriceCurrency;
    private JLabel itemCountMeasure;
    private JLabel itemTotalPriceCurrency;
    private JPanel contentPanel;

    OrderItemTO orderItem;
    boolean canEdit;

    public OrderTextItemForm(OrderItemTO orderItem, boolean canEdit) {
        this.orderItem = orderItem;
        this.canEdit = canEdit;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(itemName, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(itemPrice, ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION);
        DataFiltering.setTextLengthLimit(itemCount, ModelFieldsLengths.MAX_TEXT_LENGTH_LONG_VAR);
        DataFiltering.setTextLengthLimit(itemTotalPrice, ModelFieldsLengths.MAX_TEXT_LENGTH_LONG_VAR);

        initListeners();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("order.item.properties.title");
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
        itemName.setText(orderItem.getName());
        itemName.setEditable(orderItem.isTextItem());
        itemPrice.setText(orderItem.getPrice() == null ? null : StringUtils.formatNumber(orderItem.getPrice()));
        itemPriceCurrency.setText(orderItem.getOrder().getCurrency().getSign());
        itemCount.setText(orderItem.getCount() == 0 ? null : String.valueOf(orderItem.getCount()));
        itemCountMeasure.setText(orderItem.getMeasureSign());
        itemTotalPriceCurrency.setText(orderItem.getOrder().getCurrency().getSign());
        refreshTotalPrice();
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(itemName);
        DataValidation.checkNotEmpty(itemPrice);
        DataValidation.checkIsNumber(itemPrice.getText(), itemPrice);
        DataValidation.checkNotEmpty(itemCount);
        DataValidation.checkIsNumberLong(itemCount.getText(), itemCount);
    }

    @Override
    public void saveData() {
        if (orderItem.isTextItem()) {
            orderItem.setText(itemName.getText());
        }
        orderItem.setPrice(StringUtils.parseStringToBigDecimal(itemPrice.getText()));
        orderItem.setCount(Long.valueOf(itemCount.getText()));
    }

    //=============================== Helpers =========================================
    private void refreshTotalPrice() {
        if (StringUtils.isNumber(itemPrice.getText()) && StringUtils.isNumberLong(itemCount.getText())) {
            BigDecimal price = StringUtils.parseStringToBigDecimal(itemPrice.getText());
            BigDecimal count = StringUtils.parseStringToBigDecimal(itemCount.getText());
            itemTotalPrice.setText(StringUtils.formatNumber(price.multiply(count)));
        } else {
            itemTotalPrice.setText(null);
        }
    }

    private void initListeners() {
        itemPrice.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                refreshTotalPrice();
            }
        });

        itemCount.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                refreshTotalPrice();
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
        contentPanel.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("order.item.properties.name"));
        contentPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        itemName = new JTextField();
        itemName.setEditable(false);
        contentPanel.add(itemName, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(300, -1), new Dimension(150, -1), null, 0, false));
        itemPrice = new JTextField();
        contentPanel.add(itemPrice, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        itemPriceCurrency = new JLabel();
        itemPriceCurrency.setText("<Currency>");
        contentPanel.add(itemPriceCurrency, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        itemCount = new JTextField();
        contentPanel.add(itemCount, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("order.item.properties.price"));
        contentPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("order.item.properties.count"));
        contentPanel.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        itemTotalPrice = new JTextField();
        itemTotalPrice.setEditable(false);
        contentPanel.add(itemTotalPrice, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        itemCountMeasure = new JLabel();
        itemCountMeasure.setText("<Meas>");
        contentPanel.add(itemCountMeasure, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        itemTotalPriceCurrency = new JLabel();
        itemTotalPriceCurrency.setText("<Currency>");
        contentPanel.add(itemTotalPriceCurrency, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("order.item.properties.totalPrice"));
        contentPanel.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
