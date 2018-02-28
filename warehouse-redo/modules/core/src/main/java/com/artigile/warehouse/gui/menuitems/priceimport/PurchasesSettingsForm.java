/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.priceimport;

import com.artigile.warehouse.gui.core.plugin.FramePlugin;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.utils.UIComponentUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.EmailConfigTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class PurchasesSettingsForm extends FramePlugin {

    private JPanel contentPanel;
    private JTextField serverHost;
    private JTextField serverPort;
    private JTextField accountUsername;
    private JTextField priceListRequestMessageSubject;
    private JPasswordField accountPassword;
    private JButton applyPurchasesSettings;
    private JTextField selectedPositionsPurchaseMessageSubject;

    /**
     * Editable e-mail configuration
     */
    private EmailConfigTO emailConfig;


    /* Construction
    ------------------------------------------------------------------------------------------------------------------*/
    public PurchasesSettingsForm() {
        $$$setupUI$$$();
        initListeners();
        initComponents();
    }

    private void initListeners() {
        PurchasesSettingsListener purchasesSettingsListener = new PurchasesSettingsListener();
        serverHost.getDocument().addDocumentListener(purchasesSettingsListener);
        serverPort.getDocument().addDocumentListener(purchasesSettingsListener);
        accountUsername.getDocument().addDocumentListener(purchasesSettingsListener);
        accountPassword.getDocument().addDocumentListener(purchasesSettingsListener);
        priceListRequestMessageSubject.getDocument().addDocumentListener(purchasesSettingsListener);
        selectedPositionsPurchaseMessageSubject.getDocument().addDocumentListener(purchasesSettingsListener);

        applyPurchasesSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onApplyPurchasesSettings();
            }
        });
    }

    private void initComponents() {
        emailConfig = SpringServiceContext.getInstance().getEmailConfigService().getAppEmailConfig();
        if (emailConfig == null) {
            emailConfig = new EmailConfigTO();
        }
        serverHost.setText(emailConfig.getServerHost());
        serverPort.setText(emailConfig.getServerPort() == 0 ? StringUtils.EMPTY_STRING : String.valueOf(emailConfig.getServerPort()));
        accountUsername.setText(emailConfig.getAccountUsername());
        accountPassword.setText(emailConfig.getAccountPassword());
        priceListRequestMessageSubject.setText(emailConfig.getPriceListRequestMessageSubject());
        selectedPositionsPurchaseMessageSubject.setText(emailConfig.getSelectedPositionsPurchaseMessageSubject());
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public String getTitle() {
        return I18nSupport.message("purchases.settings.title");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    private void onApplyPurchasesSettings() {
        // Check input data
        try {
            validatePurchasesSettings();
        } catch (DataValidationException e) {
            UIComponentUtils.showCommentOnComponent(e.getComponent(), e.getMessage());
            return;
        }

        // Create configuration instance
        emailConfig.setServerHost(serverHost.getText());
        emailConfig.setServerPort(Integer.valueOf(serverPort.getText()));
        emailConfig.setAccountUsername(accountUsername.getText());
        emailConfig.setAccountPassword(StringUtils.toString(accountPassword.getPassword()));
        emailConfig.setPriceListRequestMessageSubject(priceListRequestMessageSubject.getText());
        emailConfig.setSelectedPositionsPurchaseMessageSubject(selectedPositionsPurchaseMessageSubject.getText());

        SpringServiceContext.getInstance().getEmailConfigService().mergeEmailConfig(emailConfig);
        applyPurchasesSettings.setEnabled(false);
    }

    private void validatePurchasesSettings() throws DataValidationException {
        DataValidation.checkNotEmpty(serverHost);
        DataValidation.checkMaxLength(serverHost.getText(), 100, serverHost);

        DataValidation.checkNotEmpty(serverPort);
        DataValidation.checkIsNumberInteger(serverPort.getText(), serverPort);

        DataValidation.checkNotEmpty(accountUsername);
        DataValidation.checkMaxLength(accountUsername.getText(), 100, accountUsername);

        DataValidation.checkNotEmpty(accountPassword);
        DataValidation.checkMaxLength(StringUtils.toString(accountPassword.getPassword()), 100, accountPassword);

        DataValidation.checkNotEmpty(priceListRequestMessageSubject);
        DataValidation.checkMaxLength(priceListRequestMessageSubject.getText(), 100, priceListRequestMessageSubject);

        DataValidation.checkNotEmpty(selectedPositionsPurchaseMessageSubject);
        DataValidation.checkMaxLength(selectedPositionsPurchaseMessageSubject.getText(), 100, selectedPositionsPurchaseMessageSubject);
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
        contentPanel.setLayout(new GridLayoutManager(5, 1, new Insets(4, 2, 2, 2), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 5, new Insets(1, 1, 4, 1), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ResourceBundle.getBundle("i18n/warehouse").getString("purchases.settings.email.service.title"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(-16777216)));
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("purchases.settings.email.service.properties.server.host"));
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("purchases.settings.email.service.properties.account.username"));
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("purchases.settings.email.service.properties.account.password"));
        panel1.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(150, -1), null, 0, false));
        serverHost = new JTextField();
        panel1.add(serverHost, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        accountUsername = new JTextField();
        panel1.add(accountUsername, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        accountPassword = new JPasswordField();
        panel1.add(accountPassword, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        serverPort = new JTextField();
        panel1.add(serverPort, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), new Dimension(50, -1), 0, false));
        final JLabel label4 = new JLabel();
        label4.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("purchases.settings.email.service.properties.server.port"));
        panel1.add(label4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        contentPanel.add(spacer4, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel2.add(spacer5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        applyPurchasesSettings = new JButton();
        applyPurchasesSettings.setEnabled(false);
        this.$$$loadButtonText$$$(applyPurchasesSettings, ResourceBundle.getBundle("i18n/warehouse").getString("common.apply.button"));
        panel2.add(applyPurchasesSettings, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(1, 1, 4, 1), -1, -1));
        contentPanel.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ResourceBundle.getBundle("i18n/warehouse").getString("purchases.settings.price.list.request.title")));
        final JLabel label5 = new JLabel();
        label5.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("purchases.settings.price.list.request.properties.message.subject"));
        panel3.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(150, -1), null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel3.add(spacer6, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        priceListRequestMessageSubject = new JTextField();
        panel3.add(priceListRequestMessageSubject, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(1, 1, 4, 1), -1, -1));
        contentPanel.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ResourceBundle.getBundle("i18n/warehouse").getString("purchases.settings.selected.positions.purchase.title")));
        final JLabel label6 = new JLabel();
        label6.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("purchases.settings.selected.positions.purchase.properties.message.subject"));
        panel4.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(150, -1), null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel4.add(spacer7, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        selectedPositionsPurchaseMessageSubject = new JTextField();
        panel4.add(selectedPositionsPurchaseMessageSubject, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
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


    /* Action listeners
    ------------------------------------------------------------------------------------------------------------------*/
    private class PurchasesSettingsListener implements DocumentListener {

        private void enableApplyingEmailConfig() {
            applyPurchasesSettings.setEnabled(true);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            enableApplyingEmailConfig();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            enableApplyingEmailConfig();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            enableApplyingEmailConfig();
        }
    }
}
