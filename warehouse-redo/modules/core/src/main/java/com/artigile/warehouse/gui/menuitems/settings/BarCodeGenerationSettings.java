/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.settings;

import com.artigile.warehouse.bl.detail.DetailBatchService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.plugin.FramePlugin;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.utils.UIComponentUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * @author Aliaksandr Chyrtsik
 * @since 07.06.13
 */
public class BarCodeGenerationSettings extends FramePlugin {
    private JPanel contentPane;
    private JButton applySettings;
    private JTextField fieldBarCodePrefix;
    private JTextField fieldBarCodeArticleLength;
    private JCheckBox fieldBarCodeGenerateControlNumber;
    private JButton generateBarCodes;

    private final DetailBatchService detailBatchService = SpringServiceContext.getInstance().getDetailBatchesService();

    public BarCodeGenerationSettings() {
        DataFiltering.setTextLengthLimit(fieldBarCodePrefix, detailBatchService.getBarCodePrefixMaxLength());
        initListeners();
        loadSettings();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("barcode.generation.settings.title");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPane;
    }

    private void initListeners() {
        boolean canSaveSettings = new PermissionCommandAvailability(PermissionType.EDIT_BARCODE_GENERATION_SETTINGS).isAvailable(null);
        applySettings.setEnabled(canSaveSettings);
        generateBarCodes.setEnabled(canSaveSettings);
        if (canSaveSettings) {
            applySettings.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    // Check input data
                    try {
                        validateSettings();
                    } catch (DataValidationException e) {
                        UIComponentUtils.showCommentOnComponent(e.getComponent(), e.getMessage());
                        return;
                    }
                    //Save settings as validation passed.
                    saveSettings();
                }
            });

            generateBarCodes.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    generateBarCodesForCatalog();
                }
            });
        }
    }

    private void generateBarCodesForCatalog() {
        int itemsWithoutBarCode = detailBatchService.getNumberOfDetailBatchesWithoutBarCode();
        if (itemsWithoutBarCode == 0) {
            //Nothing to update.
            MessageDialogs.showInfo(I18nSupport.message("barcode.generation.settings.generation.nothing.to.generate"));
            return;
        } else if (!MessageDialogs.showConfirm(
                I18nSupport.message("barcode.generation.settings.generation.confirmation.title"),
                I18nSupport.message("barcode.generation.settings.generation.confirmation.message", itemsWithoutBarCode))) {
            //User did not confirm generation of bar codes.
            return;
        }

        //Generate bar codes for catalog.
        detailBatchService.generateBarCodes();
        MessageDialogs.showInfo(I18nSupport.message("barcode.generation.settings.generation.success"));
    }

    private void loadSettings() {
        fieldBarCodePrefix.setText(detailBatchService.getBarCodePrefix());
        fieldBarCodeArticleLength.setText(String.valueOf(detailBatchService.getBarCodeArticleLength()));
        fieldBarCodeGenerateControlNumber.setSelected(detailBatchService.isBarCodeGenerateControlNumber());
    }

    private void validateSettings() throws DataValidationException {
        DataValidation.checkNotEmpty(fieldBarCodePrefix);
        DataValidation.checkIsNumberInteger(fieldBarCodeArticleLength.getText(), fieldBarCodeArticleLength);
        DataValidation.checkValueRangeLong(Long.valueOf(fieldBarCodeArticleLength.getText()), fieldBarCodeArticleLength,
                detailBatchService.getBarCodeMinArticleLength(), detailBatchService.getBarCodeMaxArticleLength());
    }

    private void saveSettings() {
        detailBatchService.setBarCodePrefix(fieldBarCodePrefix.getText());
        detailBatchService.setBarCodeArticleLength(Integer.valueOf(fieldBarCodeArticleLength.getText()));
        detailBatchService.setBarCodeGenerateControlNumber(fieldBarCodeGenerateControlNumber.isSelected());
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
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 5), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 3, new Insets(1, 1, 4, 1), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ResourceBundle.getBundle("i18n/warehouse").getString("barcode.generation.settings.title"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(-16777216)));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("barcode.generation.settings.barcode.prefix"));
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("barcode.generation.settings.barcode.article.length"));
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldBarCodePrefix = new JTextField();
        panel1.add(fieldBarCodePrefix, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldBarCodeArticleLength = new JTextField();
        panel1.add(fieldBarCodeArticleLength, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        fieldBarCodeGenerateControlNumber = new JCheckBox();
        this.$$$loadButtonText$$$(fieldBarCodeGenerateControlNumber, ResourceBundle.getBundle("i18n/warehouse").getString("barcode.generation.settings.barcode.generate.control.number.value"));
        panel1.add(fieldBarCodeGenerateControlNumber, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("barcode.generation.settings.barcode.generate.control.number.label"));
        panel1.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel2.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        applySettings = new JButton();
        applySettings.setEnabled(false);
        this.$$$loadButtonText$$$(applySettings, ResourceBundle.getBundle("i18n/warehouse").getString("common.apply.button"));
        panel2.add(applySettings, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generateBarCodes = new JButton();
        generateBarCodes.setEnabled(false);
        this.$$$loadButtonText$$$(generateBarCodes, ResourceBundle.getBundle("i18n/warehouse").getString("barcode.generation.settings.generate"));
        panel2.add(generateBarCodes, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
