/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties;

import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.artigile.warehouse.utils.properties.savers.PropertiesDialogSaver;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jetbrains.annotations.PropertyKey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

public class ReadOnlyPropertiesDialog extends JDialog {
    private JPanel mainPanel;
    private JButton buttonClose;
    private JPanel contentPanel;

    /**
     * Strategy, that covers concrete properties and helps dialog.
     */
    private ReadOnlyPropertiesForm propertiesForm;

    /**
     * If true then dialog position and site is saved on closing and restored on showing.
     */
    private boolean enableSave;

    public ReadOnlyPropertiesDialog(ReadOnlyPropertiesForm propertiesForm) {
        this(propertiesForm, null, null, true);
    }

    public ReadOnlyPropertiesDialog(ReadOnlyPropertiesForm propertiesForm, Component ownerComponent, boolean enableSave) {
        this(propertiesForm, null, ownerComponent, enableSave);
    }

    public ReadOnlyPropertiesDialog(ReadOnlyPropertiesForm propertiesForm, @PropertyKey(resourceBundle = "i18n.warehouse") String btnCloseText) {
        this(propertiesForm, btnCloseText, null, true);
    }

    public ReadOnlyPropertiesDialog(ReadOnlyPropertiesForm propertiesForm, String btnCloseText, Component ownerComponent, boolean enableSave) {
        super(getOwnerWindow(ownerComponent));
        this.propertiesForm = propertiesForm;
        if (btnCloseText != null) {
            buttonClose.setText(I18nSupport.message(btnCloseText));
        }
        init();
    }

    private static Window getOwnerWindow(Component component) {
        if (component != null) {
            Component currentComponent = component;
            while (currentComponent != null) {
                if (currentComponent instanceof Window) {
                    return (Window) currentComponent;
                }
                currentComponent = currentComponent.getParent();
            }
            LoggingFacade.logWarning("Cannot determine root window for dialog displaying. Use default frame as dialog owner.");
            return WareHouse.getMainFrame();
        } else {
            return WareHouse.getMainFrame();
        }
    }

    @Override
    public void dispose() {
        if (enableSave) {
            PropertiesDialogSaver.store(this, getFrameId());
        }
        super.dispose();
    }

    /**
     * Executes dialog.
     *
     * @return - true, if OK button has been pressed.
     */
    public void run() {
        //Loading initial data into controls.
        propertiesForm.loadData();
        initPosition();
        setVisible(true);
    }

    //============================= Implementation helpers ===================================

    private void init() {
        setModal(true);
        setResizable(false);
        setContentPane(mainPanel);
        setTitle(propertiesForm.getTitle());

        JPanel formContentPanel = propertiesForm.getContentPanel();
        contentPanel.add(formContentPanel, new GridConstraints(0, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null));

        initListeners();
    }

    private void initPosition() {
        pack();
        int x = WareHouse.getGlobalWidth() / 2 - getWidth() / 2 + WareHouse.getGlobalLeft();
        int y = WareHouse.getGlobalHeight() / 2 - getHeight() / 2 + WareHouse.getGlobalTop();
        setBounds(x >= 0 ? x : 0, y >= 0 ? y : 0, getWidth(), getHeight());

        if (enableSave) {
            PropertiesDialogSaver.restore(this, getFrameId());
        }
    }

    private void initListeners() {
        getRootPane().setDefaultButton(buttonClose);
        buttonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        mainPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Child classes might implement this method to return identifier for extended frame identification
     *
     * @return
     */
    protected String getFrameId() {
        return propertiesForm.getClass().getCanonicalName();
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
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonClose = new JButton();
        this.$$$loadButtonText$$$(buttonClose, ResourceBundle.getBundle("i18n/warehouse").getString("common.close.button"));
        panel2.add(buttonClose, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(contentPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
        return mainPanel;
    }
}
