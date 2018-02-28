/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties;

import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.data.validation.SilentDataValidationException;
import com.artigile.warehouse.gui.utils.UIComponentUtils;
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

/**
 * @author Shyrik, 07.12.2008
 */

/**
 * Class implements logic, that is common for all dialog with properties.
 */
public class PropertiesDialog extends JDialog {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JButton buttonOK;
    private JButton buttonCancel;

    /**
     * Strategy, that covers concrete properties and helps dialog.
     */
    private PropertiesForm propertiesForm;

    /**
     * If true then dialog position and site is saved on closing and restored on showing.
     */
    private boolean enableSave;

    /**
     * Result of operation with dialog. If true then user pressed OK, is false then user pressed Cancel.
     */
    private boolean editResult;

    /**
     * @param propertyForm - Properties form to be shown in properties dialog.
     */
    public PropertiesDialog(PropertiesForm propertyForm) {
        this(propertyForm, null, null, true);
    }

    public PropertiesDialog(PropertiesForm propertyForm, Component ownerComponent, boolean enableSave) {
        this(propertyForm, null, ownerComponent, enableSave);
    }

    public PropertiesDialog(PropertiesForm propertyForm, @PropertyKey(resourceBundle = "i18n.warehouse") String btnOkText) {
        this(propertyForm, btnOkText, null, true);
    }

    public PropertiesDialog(PropertiesForm propertyForm, String btnOkText, Component ownerComponent, boolean enableSave) {
        super(getOwnerWindow(ownerComponent));
        this.propertiesForm = propertyForm;
        this.enableSave = enableSave;
        if (btnOkText != null) {
            buttonOK.setText(I18nSupport.message(btnOkText));
        }

        if (propertyForm instanceof PropertiesOwnerWanting) {
            getRootPane().setDefaultButton(buttonOK);
            //Property strategy wants to manipulate with properties page.
            PropertiesOwnerWanting wantingOwnerProp = (PropertiesOwnerWanting) propertyForm;
            wantingOwnerProp.setPropertiesOwner(new PropertiesOwner() {
                public void fireOK() {
                    onOK();
                }

                public void fireCancel() {
                    onCancel();
                }
            });
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

    private Container getBeforeTabbedPane(Container container) {
        Container prevContainer = container;
        container = container.getParent();
        while (container != null) {
            if (container instanceof JTabbedPane) {
                return prevContainer;
            }
            prevContainer = container;
            container = container.getParent();
        }
        return null;
    }

    private void onOK() {
        try {
            //Validate data before saving it.
            propertiesForm.validateData();
        } catch (SilentDataValidationException e) {
            //On silent failure do nothing except interrupting processing of data saving.
            return;
        } catch (DataValidationException e) {
            JComponent comp = e.getComponent();
            if (comp != null) {
                //Message for a specified control.
                comp.requestFocusInWindow();
                Container container = getBeforeTabbedPane(comp);
                if (container != null) {
                    JTabbedPane tabbedPane = (JTabbedPane) container.getParent();
                    tabbedPane.setSelectedComponent(container);
                }

                UIComponentUtils.showCommentOnComponent(comp, e.getMessage());
            } else {
                //Message for the whole window.
                JOptionPane.showMessageDialog(this, e.getMessage(), I18nSupport.message("warning"), JOptionPane.WARNING_MESSAGE);
            }
            return;
        }

        //Saving data entered in the controls.
        propertiesForm.saveData();

        editResult = true;
        dispose();
    }

    private void onCancel() {
        editResult = false;
        dispose();
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
    public boolean run() {
        //Loading initial data into controls.
        propertiesForm.loadData();
        initPosition();

        editResult = false;
        setVisible(true);
        return editResult;
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
        if (propertiesForm.canSaveData()) {
            buttonOK.setEnabled(true);
            getRootPane().setDefaultButton(buttonOK);
            buttonOK.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onOK();
                }
            });
        } else {
            //User cannot saveValue changes. He doesn't needs OK button handler at all.
            buttonOK.setEnabled(false);
            getRootPane().setDefaultButton(buttonCancel);
        }

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        mainPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Child classes might implement this method to return identifier for extended frame identification
     *
     * @return
     */
    protected String getFrameId
    () {
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
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(contentPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(10, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        this.$$$loadButtonText$$$(buttonOK, ResourceBundle.getBundle("i18n/warehouse").getString("common.ok.button"));
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(75, -1), null, null, 0, false));
        buttonCancel = new JButton();
        this.$$$loadButtonText$$$(buttonCancel, ResourceBundle.getBundle("i18n/warehouse").getString("common.cancel.button"));
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(75, -1), null, null, 0, false));
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
