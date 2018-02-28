/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.warehouse.warehousebatch;

import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchService;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class WarehouseBatchForm implements PropertiesForm {

    private JPanel contentPane;

    private JTextField textCount;

    private JCheckBox needRecalculateCheckBox;

    private JTextArea notice;

    private JTextField detailName;

    private JTextField detailMisc;

    private JTextField warehouse;

    private JTextField storagePlace;

    private JTextField textReservedCount;

    private JLabel fieldBuyCurrency;

    private JTextField fieldReceiptDate;

    private JTextField fieldBuyPrice;

    private JLabel receiptDateLabel;

    private JLabel buyPriceLabel;

    private JXDatePicker shelfLifeDate;

    private boolean canEdit;

    private WarehouseBatchTO warehouseBatchTO;

    public WarehouseBatchForm(WarehouseBatchTO warehouseBatchTO, boolean canEdit) {
        this.warehouseBatchTO = warehouseBatchTO;
        this.canEdit = canEdit;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(notice, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        //Visibility of controls for tracking receipt date and price.
        WarehouseBatchService warehouseBatchService = SpringServiceContext.getInstance().getWarehouseBatchService();
        boolean trackPostingItem = warehouseBatchService.isTrackPostingItem();
        receiptDateLabel.setVisible(trackPostingItem);
        fieldReceiptDate.setVisible(trackPostingItem);
        buyPriceLabel.setVisible(trackPostingItem);
        fieldBuyPrice.setVisible(trackPostingItem);
        fieldBuyCurrency.setVisible(trackPostingItem);
        shelfLifeDate.setFormats(StringUtils.getDateFormat());
        shelfLifeDate.setVisible(warehouseBatchService.isTrackShelfLife());
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("warehousebatch.properties.title");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPane;
    }

    @Override
    public boolean canSaveData() {
        return canEdit;
    }

    @Override
    public void loadData() {
        detailName.setText(warehouseBatchTO.getDetailBatch().getName());
        detailMisc.setText(warehouseBatchTO.getDetailBatch().getMisc());
        warehouse.setText(warehouseBatchTO.getWarehouse().getName());
        storagePlace.setText(warehouseBatchTO.getStoragePlace().getSign());
        textCount.setText(String.valueOf(warehouseBatchTO.getCount()));
        textReservedCount.setText(String.valueOf(warehouseBatchTO.getReservedCount()));
        needRecalculateCheckBox.setSelected(warehouseBatchTO.getNeedRecalculate());
        notice.setText(warehouseBatchTO.getNotice());

        fieldReceiptDate.setText(StringUtils.formatDate(warehouseBatchTO.getReceiptDate()));
        fieldBuyPrice.setText(StringUtils.formatNumber(warehouseBatchTO.getBuyPrice()));
        CurrencyTO buyCurrency = warehouseBatchTO.getBuyCurrency();
        fieldBuyCurrency.setText(buyCurrency == null ? "" : buyCurrency.getSign());

        shelfLifeDate.setDate(warehouseBatchTO.getShelfLifeDate());
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(textCount);
        DataValidation.checkIsNumberLong(textCount.getText(), textCount);
        DataValidation.checkValueMinLong(Long.parseLong(textCount.getText()), textCount, 0);
    }

    @Override
    public void saveData() {
        warehouseBatchTO.setCount(Long.parseLong(textCount.getText()));
        warehouseBatchTO.setNeedRecalculate(needRecalculateCheckBox.isSelected());
        warehouseBatchTO.setShelfLifeDate(shelfLifeDate.getDate());
        warehouseBatchTO.setNotice(notice.getText());
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(8, 4, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.properties.count"));
        contentPane.add(label1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textCount = new JTextField();
        textCount.setEditable(false);
        contentPane.add(textCount, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        needRecalculateCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(needRecalculateCheckBox, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.properties.needrecalculate"));
        contentPane.add(needRecalculateCheckBox, new GridConstraints(3, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.properties.notice"));
        contentPane.add(label2, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPane.add(scrollPane1, new GridConstraints(6, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(300, 50), null, null, 0, false));
        notice = new JTextArea();
        notice.setWrapStyleWord(true);
        scrollPane1.setViewportView(notice);
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.properties.detailName"));
        contentPane.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        detailName = new JTextField();
        detailName.setEditable(false);
        contentPane.add(detailName, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.properties.detailMisc"));
        contentPane.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        detailMisc = new JTextField();
        detailMisc.setEditable(false);
        contentPane.add(detailMisc, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.properties.warehouse"));
        contentPane.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        warehouse = new JTextField();
        warehouse.setEditable(false);
        contentPane.add(warehouse, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.properties.storagePlace"));
        contentPane.add(label6, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        storagePlace = new JTextField();
        storagePlace.setEditable(false);
        contentPane.add(storagePlace, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.properties.reservedCount"));
        contentPane.add(label7, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textReservedCount = new JTextField();
        textReservedCount.setEditable(false);
        contentPane.add(textReservedCount, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        receiptDateLabel = new JLabel();
        this.$$$loadLabelText$$$(receiptDateLabel, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.properties.receiptDate"));
        contentPane.add(receiptDateLabel, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldReceiptDate = new JTextField();
        fieldReceiptDate.setEditable(false);
        contentPane.add(fieldReceiptDate, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buyPriceLabel = new JLabel();
        this.$$$loadLabelText$$$(buyPriceLabel, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.properties.buyPrice"));
        contentPane.add(buyPriceLabel, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldBuyPrice = new JTextField();
        fieldBuyPrice.setEditable(false);
        panel1.add(fieldBuyPrice, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldBuyCurrency = new JLabel();
        fieldBuyCurrency.setText("<buy currency>");
        panel1.add(fieldBuyCurrency, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.properties.shelfLifeDate"));
        contentPane.add(label8, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        shelfLifeDate = new JXDatePicker();
        contentPane.add(shelfLifeDate, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        label1.setLabelFor(textCount);
        label7.setLabelFor(textCount);
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
        return contentPane;
    }
}
