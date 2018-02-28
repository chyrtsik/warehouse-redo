/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.admin.license.licenses;

import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ResourceBundle;

/**
 * @author Aliaksandr.Chyrtsik, 04.08.11
 */
public class AddLicenseForm implements PropertiesForm {
    private JPanel contentPane;
    private JRadioButton loadLicenseFromFileRadioButton;
    private JRadioButton enterLicenseDataManuallyRadioButton;
    private JTextArea fieldLicenseData;
    private JTextField fieldLicenseFile;
    private JButton browseLicenseFile;
    private JLabel labelLicenseFile;
    private JLabel labelLicenseData;

    /**
     * License data (final data entered by user or loaded from file).
     */
    private String licenseData;

    public AddLicenseForm() {
        initListeners();
        enableControls();
    }

    public String getLicenseData() {
        return licenseData;
    }

    private void initListeners() {
        loadLicenseFromFileRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                enableControls();
            }
        });
        enterLicenseDataManuallyRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                enableControls();
            }
        });
        browseLicenseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoadLicenseFromFile();
            }
        });
    }

    private void onLoadLicenseFromFile() {
        //Import license data from file.
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(I18nSupport.message("license.properties.license.file.extension.comment"), "lic"));
        if (fileChooser.showOpenDialog(getContentPanel()) == JFileChooser.APPROVE_OPTION) {
            fieldLicenseFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void enableControls() {
        //1. Enable/disable controls for loading license from file.
        boolean loadLicenseEnabled = loadLicenseFromFileRadioButton.isSelected();
        fieldLicenseFile.setEnabled(loadLicenseEnabled);
        browseLicenseFile.setEnabled(loadLicenseEnabled);
        labelLicenseFile.setEnabled(loadLicenseEnabled);

        //2. Enable/disable controls for entering license data manually.
        boolean enterLicenseDataEnabled = enterLicenseDataManuallyRadioButton.isSelected();
        fieldLicenseData.setEnabled(enterLicenseDataEnabled);
        labelLicenseData.setEnabled(enterLicenseDataEnabled);
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("license.create.title");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPane;
    }

    @Override
    public boolean canSaveData() {
        return true;
    }

    @Override
    public void loadData() {
    }

    @Override
    public void validateData() throws DataValidationException {
        if (loadLicenseFromFileRadioButton.isSelected()) {
            DataValidation.checkNotEmpty(fieldLicenseFile);
            try {
                licenseData = loadLicenseFromFile(fieldLicenseFile.getText());
            } catch (IOException e) {
                DataValidation.failRes(fieldLicenseFile, "license.error.cannot.load.license.file", e.getLocalizedMessage());
            }
        } else if (enterLicenseDataManuallyRadioButton.isSelected()) {
            DataValidation.checkNotEmpty(fieldLicenseData);
        } else {
            throw new AssertionError("Not supported license entering mode");
        }
    }

    @Override
    public void saveData() {
        if (loadLicenseFromFileRadioButton.isSelected()) {
            //Do nothing. License data is already loaded.
        } else if (enterLicenseDataManuallyRadioButton.isSelected()) {
            licenseData = fieldLicenseData.getText();
        } else {
            throw new AssertionError("Not supported license entering mode");
        }
    }

    private String loadLicenseFromFile(String fileName) throws IOException {
        StringBuilder buffer = new StringBuilder();
        Reader reader = new FileReader(fileName);
        int len;
        char[] chr = new char[1024];
        try {
            while ((len = reader.read(chr)) > 0) {
                buffer.append(chr, 0, len);
            }
        } finally {
            reader.close();
        }
        return buffer.toString();
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
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldLicenseFile = new JTextField();
        fieldLicenseFile.setEditable(false);
        panel1.add(fieldLicenseFile, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        browseLicenseFile = new JButton();
        browseLicenseFile.setText("...");
        panel1.add(browseLicenseFile, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelLicenseFile = new JLabel();
        this.$$$loadLabelText$$$(labelLicenseFile, ResourceBundle.getBundle("i18n/warehouse").getString("license.create.fileName"));
        panel1.add(labelLicenseFile, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(20, -1), null, null, 0, false));
        loadLicenseFromFileRadioButton = new JRadioButton();
        loadLicenseFromFileRadioButton.setSelected(true);
        this.$$$loadButtonText$$$(loadLicenseFromFileRadioButton, ResourceBundle.getBundle("i18n/warehouse").getString("license.create.option.loadFromLile"));
        panel1.add(loadLicenseFromFileRadioButton, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enterLicenseDataManuallyRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(enterLicenseDataManuallyRadioButton, ResourceBundle.getBundle("i18n/warehouse").getString("license.create.option.enterManually"));
        panel1.add(enterLicenseDataManuallyRadioButton, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelLicenseData = new JLabel();
        this.$$$loadLabelText$$$(labelLicenseData, ResourceBundle.getBundle("i18n/warehouse").getString("license.create.licenseData"));
        panel1.add(labelLicenseData, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(31);
        panel1.add(scrollPane1, new GridConstraints(3, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(450, 100), null, null, 0, false));
        fieldLicenseData = new JTextArea();
        fieldLicenseData.setLineWrap(true);
        scrollPane1.setViewportView(fieldLicenseData);
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(loadLicenseFromFileRadioButton);
        buttonGroup.add(enterLicenseDataManuallyRadioButton);
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
