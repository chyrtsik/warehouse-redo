/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.movement.items;

import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.dto.movement.MovementItemTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 22.11.2009
 */
public class MovementItemForm implements PropertiesForm {
    private JPanel contentPanel;
    private JTextField fieldItemType;
    private JTextField fieldItemName;
    private JTextField fieldItemMisc;
    private JTextField fieldItemNotice;
    private JTextField fieldWarehouseNotice;
    private JLabel fieldAvailCountMeas;
    private JTextField fieldAvailCount;
    private JTextField fieldCount;
    private JLabel fieldCountMeas;
    private JTextField fieldFromWarehouse;
    private JTextField fieldFromStoragePlace;
    private JTextField fieldAppendCount;
    private JLabel appendCountLabel;

    private MovementItemTO movementItem;
    private boolean canEdit;
    private boolean appendCount;

    private WarehouseTOForReport lastToWarehouse;

    public MovementItemForm(MovementItemTO movementItem, boolean canEdit, boolean appendCount) {
        this.movementItem = movementItem;
        this.canEdit = canEdit;
        this.appendCount = appendCount;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(fieldCount, ModelFieldsLengths.MAX_TEXT_LENGTH_LONG_VAR);
        DataFiltering.setTextLengthLimit(fieldAppendCount, ModelFieldsLengths.MAX_TEXT_LENGTH_LONG_VAR);

        fieldItemType.setFocusable(false);
        fieldItemName.setFocusable(false);
        fieldItemMisc.setFocusable(false);
        fieldItemNotice.setFocusable(false);
        fieldWarehouseNotice.setFocusable(false);
        fieldAvailCount.setFocusable(false);
        fieldFromWarehouse.setFocusable(false);
        fieldFromStoragePlace.setFocusable(false);
        if (appendCount) {
            fieldCount.setEditable(false);
            fieldCount.setFocusable(false);
            appendCountLabel.setVisible(true);
            fieldAppendCount.setVisible(true);
        } else {
            appendCountLabel.setVisible(false);
            fieldAppendCount.setVisible(false);
        }
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("movement.item.properties.title");
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
        fieldItemType.setText(movementItem.getItemType());
        fieldItemName.setText(movementItem.getItemName());
        fieldItemMisc.setText(movementItem.getItemMisc());
        fieldItemNotice.setText(movementItem.getItemNotice());
        fieldWarehouseNotice.setText(movementItem.getWarehouseNotice());
        fieldAvailCount.setText(movementItem.getAvailableCount() == null ? null : String.valueOf(movementItem.getAvailableCount()));
        fieldAvailCountMeas.setText(String.valueOf(movementItem.getCountMeas().getSign()));
        fieldFromWarehouse.setText(movementItem.getMovement().getFromWarehouse().getName());
        fieldFromStoragePlace.setText(movementItem.getFromStoragePlace().getSign());
        fieldCount.setText(movementItem.getCount() == null ? null : String.valueOf(movementItem.getCount()));
        fieldCountMeas.setText(movementItem.getCountMeas().getSign());
    }

    @Override
    public void validateData() throws DataValidationException {
        if (!fieldCount.getText().isEmpty()) {
            DataValidation.checkIsNumberLong(fieldCount.getText(), fieldCount);
            DataValidation.checkValueRangeLong(Long.valueOf(fieldCount.getText()), fieldCount, 1, movementItem.getAvailableCount());
        }
        if (appendCount) {
            DataValidation.checkNotEmpty(fieldAppendCount);
            DataValidation.checkIsNumber(fieldAppendCount.getText(), fieldAppendCount);
            DataValidation.checkPositiveValue(Long.valueOf(fieldAppendCount.getText()), fieldAppendCount);
        }
    }

    @Override
    public void saveData() {
        movementItem.setCount(getTotalItemCount());
    }

    private Long getTotalItemCount() {
        if (appendCount) {
            long count = fieldCount.getText().isEmpty() ? 0 : Long.valueOf(fieldCount.getText());
            long countToAppend = Long.valueOf(fieldAppendCount.getText());
            return count + countToAppend;
        } else {
            return fieldCount.getText().isEmpty() ? null : Long.valueOf(fieldCount.getText());
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
        contentPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("movement.item.properties.wareForMovement")));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("movement.item.properties.itemType"));
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldItemType = new JTextField();
        fieldItemType.setEditable(false);
        panel1.add(fieldItemType, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(100, -1), null, 0, false));
        fieldItemNotice = new JTextField();
        fieldItemNotice.setEditable(false);
        panel1.add(fieldItemNotice, new GridConstraints(2, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("movement.item.properties.itemNotice"));
        panel1.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldWarehouseNotice = new JTextField();
        fieldWarehouseNotice.setEditable(false);
        panel1.add(fieldWarehouseNotice, new GridConstraints(3, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("movement.item.properties.warehouseNotice"));
        panel1.add(label3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("movement.item.properties.itemName"));
        panel1.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldItemName = new JTextField();
        fieldItemName.setEditable(false);
        panel1.add(fieldItemName, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("movement.item.properties.itemMisc"));
        panel1.add(label5, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldItemMisc = new JTextField();
        fieldItemMisc.setEditable(false);
        panel1.add(fieldItemMisc, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("movement.item.properties.availableCount"));
        panel1.add(label6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldAvailCount = new JTextField();
        fieldAvailCount.setEditable(false);
        panel2.add(fieldAvailCount, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(100, -1), null, 0, false));
        fieldAvailCountMeas = new JLabel();
        fieldAvailCountMeas.setText("<meas>");
        panel2.add(fieldAvailCountMeas, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("movement.item.properties.location")));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("movement.item.properties.fromWareouse"));
        panel3.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldFromWarehouse = new JTextField();
        fieldFromWarehouse.setEditable(false);
        panel3.add(fieldFromWarehouse, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(150, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("i18n/warehouse").getString("movement.item.properties.fromStoragePlace"));
        panel3.add(label8, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldFromStoragePlace = new JTextField();
        fieldFromStoragePlace.setEditable(false);
        panel3.add(fieldFromStoragePlace, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(150, -1), null, 0, false));
        final JLabel label9 = new JLabel();
        this.$$$loadLabelText$$$(label9, ResourceBundle.getBundle("i18n/warehouse").getString("movement.item.properties.countToMove"));
        panel3.add(label9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldCount = new JTextField();
        fieldCount.setEditable(true);
        panel4.add(fieldCount, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(80, -1), new Dimension(80, -1), null, 0, false));
        fieldCountMeas = new JLabel();
        fieldCountMeas.setText("<meas>");
        panel4.add(fieldCountMeas, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        appendCountLabel = new JLabel();
        appendCountLabel.setText("+");
        panel4.add(appendCountLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldAppendCount = new JTextField();
        panel4.add(fieldAppendCount, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(80, -1), new Dimension(80, -1), null, 0, false));
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


    //================================ Helpers ====================================================

}
