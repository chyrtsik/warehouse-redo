/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.bugreport;

import com.artigile.warehouse.bl.common.exceptions.BugReportProperties;
import com.artigile.warehouse.bl.common.exceptions.CannotPerformOperationException;
import com.artigile.warehouse.bl.mail.EmailService;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.utils.UIComponentUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * @author Borisok V.V., 17.09.2009
 */

/**
 * Dialog for sending bug report to development team.
 */
public class BugReport {
    /**
     * Service for sending e-mail notifications to the developers about an error in programm.
     */
    private static EmailService emailService = SpringServiceContext.getInstance().getEmailService();

    private JPanel contentPanel;
    private JTextField name;
    private JTextField company;
    private JTextField phone;
    private JTextField email;
    private JTextArea summaryInfo;
    private JComboBox reproducibility;
    private JButton showAdditionalInfo;
    private JPanel additionalInfoPanel;
    private Throwable exception;

    public BugReport(Throwable exception) {
        this.exception = exception;
        initComponents();
        initListeners();
    }

    private void initComponents() {
        additionalInfoPanel.setVisible(false);
        showAdditionalInfo.setVisible(true);
        reproducibility.addItem(I18nSupport.message("bugreport.reprodubility.always.value"));
        reproducibility.addItem(I18nSupport.message("bugreport.reprodubility.sometimes.value"));
        reproducibility.addItem(I18nSupport.message("bugreport.reprodubility.random.value"));

        UserTO user = WareHouse.getUserSession().getUser();
        name.setText(user.getDisplayName());
        email.setText(user.getEmail());
    }

    private void initListeners() {
        showAdditionalInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                additionalInfoPanel.setVisible(true);
                showAdditionalInfo.setVisible(false);
                contentPanel.revalidate();
                UIComponentUtils.packDialog(contentPanel);
            }
        });
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public void onOK() {
        BugReportProperties properties = new BugReportProperties();
        properties.setName(name.getText());
        properties.setCompany(company.getText());
        properties.setPhone(phone.getText());
        properties.setEmail(email.getText());
        properties.setSummary(summaryInfo.getText());
        properties.setReproducibility(reproducibility.getModel().getSelectedItem().toString());
        properties.setException(exception);
        try {
            emailService.sendErrorReport(properties);
            MessageDialogs.showInfo(I18nSupport.message("bugreport.email.sent"));
        } catch (CannotPerformOperationException e) {
            MessageDialogs.showWarning(e.getLocalizedMessage());
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
        contentPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("bugreport.reproducibility.label"));
        panel1.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reproducibility = new JComboBox();
        reproducibility.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("bugreport.reproducibility.tooltip"));
        panel1.add(reproducibility, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(350, 100), null, null, 0, false));
        summaryInfo = new JTextArea();
        summaryInfo.setLineWrap(true);
        summaryInfo.setText("");
        summaryInfo.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("bugreport.summary.information.tooltip"));
        scrollPane1.setViewportView(summaryInfo);
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("bugreport.summary.information.label"));
        panel1.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        additionalInfoPanel = new JPanel();
        additionalInfoPanel.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(additionalInfoPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("bugreport.name.label"));
        additionalInfoPanel.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("bugreport.company.label"));
        additionalInfoPanel.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("bugreport.phone.label"));
        additionalInfoPanel.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("bugreport.email.label"));
        additionalInfoPanel.add(label6, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        name = new JTextField();
        name.setText("");
        name.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("bugreport.name.tooltip"));
        additionalInfoPanel.add(name, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        company = new JTextField();
        company.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("bugreport.company.tooltip"));
        additionalInfoPanel.add(company, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        phone = new JTextField();
        phone.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("bugreport.phone.tooltip"));
        additionalInfoPanel.add(phone, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        email = new JTextField();
        email.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("bugreport.email.tooltip"));
        additionalInfoPanel.add(email, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        showAdditionalInfo = new JButton();
        this.$$$loadButtonText$$$(showAdditionalInfo, ResourceBundle.getBundle("i18n/warehouse").getString("bugreport.additional.button"));
        panel2.add(showAdditionalInfo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        label1.setLabelFor(reproducibility);
        label2.setLabelFor(summaryInfo);
        label3.setLabelFor(name);
        label4.setLabelFor(company);
        label5.setLabelFor(phone);
        label6.setLabelFor(email);
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