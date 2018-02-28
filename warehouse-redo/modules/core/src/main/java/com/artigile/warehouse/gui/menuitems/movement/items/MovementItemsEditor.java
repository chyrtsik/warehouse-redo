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

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.common.listeners.DataChangeAdapter;
import com.artigile.warehouse.bl.common.listeners.DataChangeListener;
import com.artigile.warehouse.bl.movement.MovementService;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchFilter;
import com.artigile.warehouse.domain.DocumentTotals;
import com.artigile.warehouse.gui.core.plugin.FramePlugin;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.command.ReportCommandListImpl;
import com.artigile.warehouse.gui.core.report.controller.TableReport;
import com.artigile.warehouse.gui.core.report.decorator.ReportCommandsDecorator;
import com.artigile.warehouse.gui.menuitems.warehouse.warehousebatch.WarehouseBatchList;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.movement.MovementTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.properties.savers.SplitPaneOrietationSaver;
import com.artigile.warehouse.utils.properties.savers.SplitPaneSaver;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 21.11.2009
 */
public class MovementItemsEditor extends FramePlugin {
    private JPanel contentPanel;
    private JTextField fieldNumber;
    private JTextField fieldState;
    private JButton beginMovement;
    private JButton cancelMovement;
    private JPanel movementItemsSplitterPanel;
    private JSplitPane postingItemsSplitter;
    private JPanel warehouseBatchesPanel;
    private JPanel movementItemsPanel;
    private JTextField fieldCreatedUser;
    private JTextField fieldCreateDate;
    private JTextField fieldFromWarehouse;
    private JTextField fieldTotalCount;
    private JTextField fieldToWarehouse;
    private JTextField fieldTotalPrice;
    private JScrollPane movementPanel;

    /**
     * Movement document, which items are being edited.
     */
    private MovementTOForReport movement;

    private final static MovementService movementService = SpringServiceContext.getInstance().getMovementService();

    public MovementItemsEditor(long movementId) {
        movement = movementService.getMovement(movementId);
        initListeners();
        refreshControls();
        initEditorReports();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("movement.items.editor.title", movement.getNumber());
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    private final DataChangeListener movementChangeListener = new DataChangeAdapter() {
        @Override
        public void afterChange(Object changedData) {
            MovementTOForReport changedMovement = (MovementTOForReport) changedData;
            if (movement.getId() == changedMovement.getId()) {
                movement = changedMovement;
                refreshControls();
            }
        }
    };

    @Override
    protected void onFrameOpened() {
        super.onFrameOpened();
        //We should react on changes of movement to keep editor in actual state.
        SpringServiceContext.getInstance().getDataChangeNolifier().addDataChangeListener(MovementTOForReport.class, movementChangeListener);

        SplitPaneSaver.restore(postingItemsSplitter, getFrameId());
        SplitPaneOrietationSaver.restore(postingItemsSplitter, MovementItemsEditor.class);
    }

    @Override
    protected void onFrameClosed() {
        SplitPaneSaver.store(postingItemsSplitter, getFrameId());

        super.onFrameClosed();
        //We should not forget unregister movement changes listener to prevent memory leaks.
        SpringServiceContext.getInstance().getDataChangeNolifier().removeDataChangeListener(MovementTOForReport.class, movementChangeListener);
    }

    private void initListeners() {
        beginMovement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBeginMovement();
            }
        });
        cancelMovement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancelMovement();
            }
        });
    }

    private void onBeginMovement() {
        try {
            //Begin processing of movement.
            movementService.beginMovement(movement.getId());

            //Change view to conform new movement state.
            initEditorReports();
            refreshControls();
        } catch (BusinessException e) {
            MessageDialogs.showWarning(contentPanel, e.getMessage());
        }
    }

    private void onCancelMovement() {
        try {
            //Cancel processing of the movement.
            movementService.cancelMovement(movement.getId());

            //Change view to conform new movement state.
            initEditorReports();
            refreshControls();
        } catch (BusinessException e) {
            MessageDialogs.showWarning(contentPanel, e.getMessage());
        }
    }

    private void refreshControls() {
        //Init user interface of movement editor according to state of the movement.
        initMovementProperties();
        initButtons();
        movementPanel.revalidate();
    }

    private void initButtons() {
        if (movement.canBeginMovement()) {
            beginMovement.setVisible(true);
            cancelMovement.setVisible(false);
        } else if (movement.canCancelMovement()) {
            beginMovement.setVisible(false);
            cancelMovement.setVisible(true);
        } else {
            beginMovement.setVisible(false);
            cancelMovement.setVisible(false);
        }
    }

    private void initEditorReports() {
        if (MovementItemsEditingStrategy.getAddItemAvailability(movement).isAvailable(null)) {
            //Initialize interface when we can add new items to movement.
            movementItemsSplitterPanel.removeAll();
            initWarehouseBatchesList(warehouseBatchesPanel);
            initMovementItemList(movementItemsPanel);
            movementItemsSplitterPanel.add(postingItemsSplitter, GridLayoutUtils.getGrowingAndFillingCellConstraints());
        } else {
            //We cannot add new items into movement. So, we needn't warehouse batches list.
            initMovementItemList(movementItemsSplitterPanel);
        }
    }

    private void initMovementProperties() {
        fieldNumber.setText(String.valueOf(movement.getNumber()));
        fieldState.setText(movement.getState().getName());
        fieldCreateDate.setText(StringUtils.formatDateTime(movement.getCreateDate()));
        fieldCreatedUser.setText(movement.getCreateUser() == null ? null : movement.getCreateUser().getDisplayName());
        fieldFromWarehouse.setText(movement.getFromWarehouse().getName());
        fieldToWarehouse.setText(movement.getToWarehouse().getName());

        DocumentTotals movementTotals = movementService.getMovementTotals(movement.getId());
        fieldTotalCount.setText(StringUtils.formatCountsWithMeasures(movementTotals.getTotalCounts()));
        fieldTotalPrice.setText(StringUtils.formatAmountsWithCurrencies(movementTotals.getTotalPrices()));
    }

    private void initWarehouseBatchesList(JComponent container) {
        //Decorates warehouse batches list with new commands for adding movement items to list.
        ReportCommandList additionalCommands = new ReportCommandListImpl();
        additionalCommands.add(new AddItemToMovementCommand(movement));
        additionalCommands.setDefaultCommandForRow(additionalCommands.get(0));

        container.removeAll();

        WarehouseBatchFilter filter = new WarehouseBatchFilter();
        filter.setWarehouseId(movement.getFromWarehouse().getId());
        WarehouseBatchList warehouseBatchList = new WarehouseBatchList(filter, this.getClass().getCanonicalName());

        ReportCommandsDecorator decoratedWarehouseBatchesList = new ReportCommandsDecorator(warehouseBatchList, additionalCommands);
        container.add(new TableReport(decoratedWarehouseBatchesList, this).getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
        container.revalidate();
    }

    private void initMovementItemList(JComponent container) {
        container.removeAll();
        MovementItemsList movementItemsList = new MovementItemsList(movement.getId());
        container.add(new TableReport(movementItemsList, this).getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
        container.revalidate();
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
        movementPanel = new JScrollPane();
        contentPanel.add(movementPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(5, 5, 5, 5), -1, -1));
        movementPanel.setViewportView(panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 8, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("movement.properties.fromWarehouse"));
        panel3.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldFromWarehouse = new JTextField();
        fieldFromWarehouse.setEditable(false);
        panel3.add(fieldFromWarehouse, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("movement.properties.number"));
        panel3.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldNumber = new JTextField();
        fieldNumber.setEditable(false);
        panel3.add(fieldNumber, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(70, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("movement.properties.state"));
        panel3.add(label3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldState = new JTextField();
        fieldState.setEditable(false);
        panel3.add(fieldState, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("movement.properties.toWarehouse"));
        panel3.add(label4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldToWarehouse = new JTextField();
        fieldToWarehouse.setEditable(false);
        panel3.add(fieldToWarehouse, new GridConstraints(2, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("movement.properties.totalPrice"));
        panel3.add(label5, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldTotalPrice = new JTextField();
        fieldTotalPrice.setEditable(false);
        panel3.add(fieldTotalPrice, new GridConstraints(2, 5, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldTotalCount = new JTextField();
        fieldTotalCount.setEditable(false);
        panel3.add(fieldTotalCount, new GridConstraints(1, 5, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("movement.properties.totalCount"));
        panel3.add(label6, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("movement.properties.createUser"));
        panel3.add(label7, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCreatedUser = new JTextField();
        fieldCreatedUser.setEditable(false);
        panel3.add(fieldCreatedUser, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(130, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("i18n/warehouse").getString("movement.properties.createDate"));
        panel3.add(label8, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCreateDate = new JTextField();
        fieldCreateDate.setEditable(false);
        panel3.add(fieldCreateDate, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(130, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        beginMovement = new JButton();
        this.$$$loadButtonText$$$(beginMovement, ResourceBundle.getBundle("i18n/warehouse").getString("movement.items.editor.command.beginMovement"));
        panel4.add(beginMovement, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        cancelMovement = new JButton();
        this.$$$loadButtonText$$$(cancelMovement, ResourceBundle.getBundle("i18n/warehouse").getString("movement.items.editor.commamd.cancelMovement"));
        panel4.add(cancelMovement, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        movementItemsSplitterPanel = new JPanel();
        movementItemsSplitterPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(movementItemsSplitterPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        postingItemsSplitter = new JSplitPane();
        postingItemsSplitter.setDividerLocation(270);
        postingItemsSplitter.setDividerSize(6);
        movementItemsSplitterPanel.add(postingItemsSplitter, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(300, 150), null, null, 0, false));
        warehouseBatchesPanel = new JPanel();
        warehouseBatchesPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        postingItemsSplitter.setLeftComponent(warehouseBatchesPanel);
        movementItemsPanel = new JPanel();
        movementItemsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        postingItemsSplitter.setRightComponent(movementItemsPanel);
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
