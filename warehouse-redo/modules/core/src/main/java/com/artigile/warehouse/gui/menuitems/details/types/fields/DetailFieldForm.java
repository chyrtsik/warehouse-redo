/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.types.fields;

import com.artigile.warehouse.domain.details.DetailFieldType;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 15.12.2008
 */
public class DetailFieldForm implements PropertiesForm {
    private JTextField fieldName;
    private JCheckBox fieldMandatory;
    private JComboBox fieldType;
    private JTextField fieldSortNum;
    private JTextField fieldCatalogGroupNum;
    private JPanel valueTypePanel;
    private JPanel contentPanel;
    private JLabel sortNumLabel;
    private JLabel groupNumLabel;

    private EnumValuesForm enumValuesForm = new EnumValuesForm();
    private TemplateValueForm templateForm = new TemplateValueForm();

    private final DetailFieldTO field;
    private final boolean canEdit;
    private final DetailFieldsEditingStrategy.DetailFieldChecker fieldChecker;


    public DetailFieldForm(DetailFieldTO field, boolean useSortAndGroupNumbers, boolean canEdit,
                           DetailFieldType[] availableTypes, DetailFieldsEditingStrategy.DetailFieldChecker fieldChecker) {
        this.field = field;
        this.canEdit = canEdit;
        this.fieldChecker = fieldChecker;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(fieldName, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(fieldSortNum, ModelFieldsLengths.MAX_TEXT_LENGTH_LONG_VAR);

        for (DetailFieldType type : availableTypes) {
            if (fieldChecker.isAvailableFieldType(type) || (field.getType() != null && field.getType().equals(type))) {
                fieldType.addItem(new ListItem(type.getName(), type));
            }
        }

        fieldType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTypeForm();
            }
        });

        fieldName.setEditable(!field.isPredefined());

        sortNumLabel.setVisible(useSortAndGroupNumbers);
        fieldSortNum.setVisible(useSortAndGroupNumbers);
        groupNumLabel.setVisible(useSortAndGroupNumbers);
        fieldCatalogGroupNum.setVisible(useSortAndGroupNumbers);
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("detail.field.properties.title");
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
        fieldName.setText(field.getName());
        fieldMandatory.setSelected(field.getMandatory());
        DataExchange.selectComboItem(fieldType, field.getType());
        fieldSortNum.setText(field.getSortNum() == null ? "" : field.getSortNum().toString());
        fieldCatalogGroupNum.setText(field.getCatalogGroupNum() == null ? "" : field.getCatalogGroupNum().toString());

        enumValuesForm.setEnumValues(field.getEnumValues());
        templateForm.setTemplate(field.getTemplate());
        refreshTypeForm();
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(fieldName);
        if (!isUniqueFieldName(fieldName.getText())) {
            DataValidation.failRes(fieldName, "detail.field.properties.field.already.exists");
        }

        DataValidation.checkSelected(fieldType);
        DetailFieldType type = (DetailFieldType) DataExchange.getComboSelection(fieldType);
        if (type == DetailFieldType.ENUM) {
            enumValuesForm.validateData();
        } else if (type == DetailFieldType.TEMPLATE_TEXT) {
            templateForm.validateData();
        }

        if (!fieldSortNum.getText().isEmpty()) {
            DataValidation.checkIsNumberLong(fieldSortNum.getText(), fieldSortNum);
        }

        if (!fieldCatalogGroupNum.getText().isEmpty()) {
            //Only mandatory fields are allowed to be included into catalog grouping.
            DataValidation.checkCondition(fieldMandatory.isSelected(), fieldCatalogGroupNum, "detail.field.validation.error.notMandatoryFieldUsedForGrouping");
            DataValidation.checkIsNumberInteger(fieldCatalogGroupNum.getText(), fieldCatalogGroupNum);
        }
    }

    @Override
    public void saveData() {
        field.setName(fieldName.getText());
        field.setMandatory(fieldMandatory.isSelected());
        field.setType((DetailFieldType) DataExchange.getComboSelection(fieldType));
        field.setSortNum(fieldSortNum.getText().isEmpty() ? null : Long.valueOf(fieldSortNum.getText()));
        field.setCatalogGroupNum(fieldCatalogGroupNum.getText().isEmpty() ? null : Integer.valueOf(fieldCatalogGroupNum.getText()));

        if (field.getType() == DetailFieldType.ENUM) {
            field.setEnumValues(enumValuesForm.getEnumValues());
            field.setTemplate(null);
        } else if (field.getType() == DetailFieldType.TEMPLATE_TEXT) {
            field.setTemplate(templateForm.getTemplate());
            field.setEnumValues(null);
        }
    }

    private boolean isUniqueFieldName(String name) {
        //Checks, is the new field name is unique in the set of te details fields.
        return fieldChecker.isUniqueFieldName(name, field);
    }

    private void packDialog() {
        Container parent = contentPanel.getParent();
        while (parent != null) {
            if (parent instanceof JDialog) {
                JDialog dialog = (JDialog) parent;
                dialog.pack();
                break;
            }
            parent = parent.getParent();
        }
    }

    private void refreshTypeForm() {
        //Now we are to show apropriate form for the specific value type.
        if (valueTypePanel.getComponentCount() > 0) {
            valueTypePanel.removeAll();
        }

        if (fieldType.getSelectedIndex() == -1) {
            valueTypePanel.revalidate();
            return;
        }

        DetailFieldType type = (DetailFieldType) DataExchange.getComboSelection(fieldType);
        if (type == DetailFieldType.ENUM) {
            valueTypePanel.add(enumValuesForm.getContentPanel(), new GridConstraints());
        } else if (type == DetailFieldType.TEMPLATE_TEXT) {
            valueTypePanel.add(templateForm.getContentPanel(), new GridConstraints());
        }

        if (type.isEditableByUser()) {
            fieldMandatory.setEnabled(true);
        } else {
            //Calculated fields cannot be mandatory.
            fieldMandatory.setEnabled(false);
            fieldMandatory.setSelected(false);
        }

        valueTypePanel.revalidate();
        packDialog();
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
        panel1.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("detail.field.properties.name"));
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldName = new JTextField();
        panel1.add(fieldName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldSortNum = new JTextField();
        panel1.add(fieldSortNum, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        sortNumLabel = new JLabel();
        this.$$$loadLabelText$$$(sortNumLabel, ResourceBundle.getBundle("i18n/warehouse").getString("detail.field.properties.sortNum"));
        panel1.add(sortNumLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldMandatory = new JCheckBox();
        this.$$$loadButtonText$$$(fieldMandatory, ResourceBundle.getBundle("i18n/warehouse").getString("detail.field.properties.mandatory"));
        panel1.add(fieldMandatory, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        groupNumLabel = new JLabel();
        this.$$$loadLabelText$$$(groupNumLabel, ResourceBundle.getBundle("i18n/warehouse").getString("detail.field.properties.groupNum"));
        panel1.add(groupNumLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCatalogGroupNum = new JTextField();
        panel1.add(fieldCatalogGroupNum, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("detail.field.properties.type"));
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldType = new JComboBox();
        panel1.add(fieldType, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valueTypePanel = new JPanel();
        valueTypePanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(valueTypePanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
