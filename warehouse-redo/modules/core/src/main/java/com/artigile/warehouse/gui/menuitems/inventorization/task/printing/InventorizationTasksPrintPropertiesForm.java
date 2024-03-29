/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.inventorization.task.printing;

import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * @author Borisok V.V., 03.10.2009
 */
public class InventorizationTasksPrintPropertiesForm implements PropertiesForm {
    private JRadioButton fieldPrintAll;
    private JRadioButton fieldPrintNotPrinted;
    private JRadioButton fieldPrintSelected;
    private JPanel contentPanel;

    private InventorizationTasksPrintOptions printOptions;

    public InventorizationTasksPrintPropertiesForm(InventorizationTasksPrintOptions printOptions) {
        this.printOptions = printOptions;
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("inventorization.task.print.form.title");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public boolean canSaveData() {
        return true;
    }

    @Override
    public void loadData() {
        fieldPrintAll.setSelected(printOptions.getWhatToPrint().equals(InventorizationTasksFilterType.ALL));
        fieldPrintNotPrinted.setSelected(printOptions.getWhatToPrint().equals(InventorizationTasksFilterType.NOT_PRINTED));
        fieldPrintSelected.setSelected(printOptions.getWhatToPrint().equals(InventorizationTasksFilterType.SELECTED));
    }

    @Override
    public void validateData() throws DataValidationException {
        boolean value = fieldPrintAll.isSelected() || fieldPrintNotPrinted.isSelected() || fieldPrintSelected.isSelected();
        DataValidation.checkCondition(value, null, "inventorization.task.print.form.message.choose.printing.document");
    }

    @Override
    public void saveData() {
        if (fieldPrintAll.isSelected()) {
            printOptions.setWhatToPrint(InventorizationTasksFilterType.ALL);
        } else if (fieldPrintNotPrinted.isSelected()) {
            printOptions.setWhatToPrint(InventorizationTasksFilterType.NOT_PRINTED);
        } else if (fieldPrintSelected.isSelected()) {
            printOptions.setWhatToPrint(InventorizationTasksFilterType.SELECTED);
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
        contentPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        fieldPrintAll = new JRadioButton();
        this.$$$loadButtonText$$$(fieldPrintAll, ResourceBundle.getBundle("i18n/warehouse").getString("complectingTask.printForm.all"));
        panel1.add(fieldPrintAll, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldPrintNotPrinted = new JRadioButton();
        this.$$$loadButtonText$$$(fieldPrintNotPrinted, ResourceBundle.getBundle("i18n/warehouse").getString("complectingTask.printForm.notPrinted"));
        panel1.add(fieldPrintNotPrinted, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldPrintSelected = new JRadioButton();
        this.$$$loadButtonText$$$(fieldPrintSelected, ResourceBundle.getBundle("i18n/warehouse").getString("complectingTask.printForm.selected"));
        panel1.add(fieldPrintSelected, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(fieldPrintAll);
        buttonGroup.add(fieldPrintNotPrinted);
        buttonGroup.add(fieldPrintSelected);
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
