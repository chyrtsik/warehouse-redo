/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.inventorization.items;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.common.listeners.DataChangeAdapter;
import com.artigile.warehouse.bl.common.listeners.DataChangeListener;
import com.artigile.warehouse.bl.inventorization.inventorization.InventorizationService;
import com.artigile.warehouse.domain.inventorization.InventorizationState;
import com.artigile.warehouse.gui.core.plugin.FramePlugin;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.controller.TableReport;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.inventorization.InventorizationTOForReport;
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
 * @author Borisok V.V., 07.10.2009
 */
public class InventorizationItemsEditor extends FramePlugin {
    private JTextField fieldState;
    private JTextField fieldNumber;
    private JTextField fieldCreateDate;
    private JTextField fieldCreatedUser;
    private JButton processInventorization;
    private JPanel contentPanel;
    private JPanel inventorizationItemsPanel;
    private JTextField fieldFinishedUser;
    private JTextField fieldFinishedDate;
    private JTextField fieldResult;

    private InventorizationTOForReport inventorization;

    private final InventorizationService inventorizationService = SpringServiceContext.getInstance().getInventorizationService();

    private final DataChangeListener inventorizationChangeListener = new DataChangeAdapter() {

        private void refresh(Object data) {
            InventorizationTOForReport changedInventorization = (InventorizationTOForReport) data;
            if (inventorization.getId() == changedInventorization.getId()) {
                inventorization = changedInventorization;
                refreshControls();
            }
        }

        @Override
        public void afterChange(Object changedData) {
            refresh(changedData);
        }

        @Override
        public void afterDelete(Object deletedData) {
            refresh(deletedData);
        }
    };

    public InventorizationItemsEditor(long inventorizationId) {
        inventorization = inventorizationService.getInventorization(inventorizationId);
        initListeners();
        refreshControls();
        initInventorizationItemList(inventorizationItemsPanel);
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("inventorization.items.editor.title", inventorization.getNumber());
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    protected void onFrameOpened() {
        //We should react on changes of inventorization to keep editor in actual state.
        super.onFrameOpened();
        SpringServiceContext.getInstance().getDataChangeNolifier().addDataChangeListener(InventorizationTOForReport.class, inventorizationChangeListener);
    }

    @Override
    protected void onFrameClosed() {
        //We should not forget ungegister inventorization changes listener to prevent memory leaks.
        super.onFrameClosed();
        SpringServiceContext.getInstance().getDataChangeNolifier().removeDataChangeListener(InventorizationTOForReport.class, inventorizationChangeListener);
    }

    private void initListeners() {
        processInventorization.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onChangeInventorizationState();
            }
        });
    }

    private void onChangeInventorizationState() {
        try {
            InventorizationState state = inventorization.getState();
            if (state.equals(InventorizationState.NOT_PROCESSED)) {
                inventorizationService.beginProcessingInventorization(inventorization);
                //} else if (state.equals(InventorizationState.IN_PROCESS)) {
                //    inventorizationService.onCompleteInventorizationTask(inventorization);
            } else if (state.equals(InventorizationState.PROCESSED)) {
                inventorizationService.closeInventorization(inventorization);
            }
        } catch (BusinessException e) {
            MessageDialogs.showWarning(contentPanel, e.getMessage());
            return;
        } catch (Exception e) {
            MessageDialogs.showError(contentPanel, e);
            return;
        }

        //Change view to conform new inventorization state.
        refreshControls();
    }

    private void refreshControls() {
        //Init user interface of inventorization editor according to state of the inventorization.
        initInventorizationProperties();
        initInventorizationItemList(inventorizationItemsPanel);

        if (inventorization.getState().equals(InventorizationState.NOT_PROCESSED)) {
            processInventorization.setEnabled(true);
            processInventorization.setText(I18nSupport.message("inventorization.command.start.process"));
        } else if (inventorization.getState().equals(InventorizationState.IN_PROCESS)) {
            processInventorization.setEnabled(false);
            processInventorization.setText(I18nSupport.message("inventorization.command.start.process"));
        } else if (inventorization.getState().equals(InventorizationState.PROCESSED)) {
            processInventorization.setEnabled(true);
            processInventorization.setText(I18nSupport.message("inventorization.command.finish.work"));
        } else if (inventorization.getState().equals(InventorizationState.CLOSED)) {
            processInventorization.setEnabled(false);
            processInventorization.setText(I18nSupport.message("inventorization.command.finish.work"));
        }
    }

    private void initInventorizationItemList(JComponent container) {
        container.removeAll();
        InventorizationItemsList inventorizationItemsList = new InventorizationItemsList(inventorization.getId());
        container.add(new TableReport(inventorizationItemsList, this).getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
        container.revalidate();
    }

    private void initInventorizationProperties() {
        fieldNumber.setText(String.valueOf(inventorization.getNumber()));
        fieldResult.setText(inventorization.getResult() == null ? "" : inventorization.getResult().getName());
        fieldState.setText(inventorization.getState().getName());
        fieldCreateDate.setText(inventorization.getCreateDateAsText());
        fieldCreatedUser.setText(inventorization.getCreateUserDisplayName());
        fieldFinishedDate.setText(inventorization.getCloseDateAsText());
        fieldFinishedUser.setText(inventorization.getCloseUserDisplayName());
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
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(5, 5, 5, 5), -1, -1));
        scrollPane1.setViewportView(panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.createDate"));
        panel3.add(label1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.createUser"));
        panel3.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCreatedUser = new JTextField();
        fieldCreatedUser.setEditable(false);
        panel3.add(fieldCreatedUser, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(130, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.closeUser"));
        panel3.add(label3, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCreateDate = new JTextField();
        fieldCreateDate.setEditable(false);
        panel3.add(fieldCreateDate, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(130, -1), null, 0, false));
        fieldFinishedUser = new JTextField();
        fieldFinishedUser.setEditable(false);
        panel3.add(fieldFinishedUser, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(130, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.closeDate"));
        panel3.add(label4, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldFinishedDate = new JTextField();
        fieldFinishedDate.setEditable(false);
        panel3.add(fieldFinishedDate, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(130, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.number"));
        panel4.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldNumber = new JTextField();
        fieldNumber.setEditable(false);
        panel4.add(fieldNumber, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(70, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.result"));
        panel4.add(label6, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldResult = new JTextField();
        fieldResult.setEditable(false);
        panel4.add(fieldResult, new GridConstraints(0, 3, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(120, -1), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.state"));
        panel5.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldState = new JTextField();
        fieldState.setEditable(false);
        panel5.add(fieldState, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        processInventorization = new JButton();
        this.$$$loadButtonText$$$(processInventorization, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.command.start.process"));
        panel6.add(processInventorization, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel6.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        inventorizationItemsPanel = new JPanel();
        inventorizationItemsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(inventorizationItemsPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        label1.setLabelFor(fieldCreateDate);
        label2.setLabelFor(fieldCreatedUser);
        label3.setLabelFor(fieldFinishedUser);
        label4.setLabelFor(fieldFinishedDate);
        label5.setLabelFor(fieldNumber);
        label7.setLabelFor(fieldState);
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
