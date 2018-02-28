/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.purchase.items;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.common.listeners.DataChangeAdapter;
import com.artigile.warehouse.bl.common.listeners.DataChangeListener;
import com.artigile.warehouse.bl.needs.WareNeedService;
import com.artigile.warehouse.bl.purchase.PurchaseService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.plugin.FramePlugin;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.command.ReportCommandListImpl;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSource;
import com.artigile.warehouse.gui.core.report.controller.TableReport;
import com.artigile.warehouse.gui.core.report.decorator.ReportCommandsDecorator;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.core.switchable.SwitchableView;
import com.artigile.warehouse.gui.core.switchable.SwitchableViewItem;
import com.artigile.warehouse.gui.menuitems.details.batches.DetailBatchesList;
import com.artigile.warehouse.gui.menuitems.details.catalog.DetailCatalog;
import com.artigile.warehouse.gui.menuitems.details.catalog.DetailCatalogBatchesListFactory;
import com.artigile.warehouse.gui.menuitems.needs.WareNeedItemsList;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.details.DetailGroupTO;
import com.artigile.warehouse.utils.dto.needs.WareNeedItemTO;
import com.artigile.warehouse.utils.dto.needs.WareNeedTO;
import com.artigile.warehouse.utils.dto.purchase.PurchaseItemTO;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTOForReport;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 02.03.2009
 */
public class PurchaseItemsEditor extends FramePlugin {
    private JPanel contentPanel;
    private JPanel purchaseItemsSplitterPanel;
    private JSplitPane purchaseItemsSplitter;
    private JPanel wareNeedItemsPanel;
    private JPanel purchaseItemsPanel;
    private JTextField fieldState;
    private JTextField fieldNumber;
    private JTextField fieldCreatedUser;
    private JTextField fieldCreateDate;
    private JTextField fieldContractor;
    private JTextField fieldContractorBalance;
    private JLabel fieldContractorBalanceCurrency;
    private JTextField fieldCurrency;
    private JLabel fieldTotalPriceCurrency;
    private JTextField fieldTotalPrice;
    private JButton waitPurchase;
    private SwitchableView detailBatchesView;

    private PurchaseTOForReport purchase;

    private WareNeedTO wareNeed;

    public PurchaseItemsEditor(long purchaseId) {
        purchase = getPurchaseService().getPurchaseForReport(purchaseId);
        WareNeedService wareNeedsService = SpringServiceContext.getInstance().getWareNeedsService();
        wareNeed = wareNeedsService.getWareNeedForEditing(wareNeedsService.getMainWareNeedId());

        initListeners();
        initPurchaseProperties();
        refreshPurchaseState();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("purchase.items.editor.title", purchase.getNumber());
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    private final DataChangeListener purchaseChangeListener = new DataChangeAdapter() {
        @Override
        public void afterChange(Object changedData) {
            PurchaseTOForReport changedPurchase = (PurchaseTOForReport) changedData;
            if (purchase.getId() == changedPurchase.getId()) {
                boolean refresh = purchase.canBeEdited() != changedPurchase.canBeEdited();
                purchase = changedPurchase;
                //Init user interface of purchase editor according to state of the purchase.
                initPurchaseProperties();
                if (refresh) {
                    refreshPurchaseState();
                }
            }
        }
    };

    @Override
    protected void onFrameOpened() {
        super.onFrameOpened();

        //We should react on changes of purchase to keep editor in actual state.
        SpringServiceContext.getInstance().getDataChangeNolifier().addDataChangeListener(PurchaseTOForReport.class, purchaseChangeListener);
        SplitPaneSaver.restore(purchaseItemsSplitter, getFrameId());
        SplitPaneOrietationSaver.restore(purchaseItemsSplitter, PurchaseItemsEditor.class);
    }

    @Override
    protected void onFrameClosed() {
        SplitPaneSaver.store(purchaseItemsSplitter, getFrameId());

        super.onFrameClosed();

        //We should not forget to unregister order changes listener to prevent memory leaks.
        SpringServiceContext.getInstance().getDataChangeNolifier().removeDataChangeListener(PurchaseTOForReport.class, purchaseChangeListener);
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
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 5), -1, -1));
        scrollPane1.setViewportView(panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        waitPurchase = new JButton();
        this.$$$loadButtonText$$$(waitPurchase, ResourceBundle.getBundle("i18n/warehouse").getString("purchase.command.wait"));
        panel2.add(waitPurchase, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("purchase.properties.contractor"));
        panel3.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldContractor = new JTextField();
        fieldContractor.setEditable(false);
        panel3.add(fieldContractor, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(2, 4, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldTotalPrice = new JTextField();
        fieldTotalPrice.setEditable(false);
        panel4.add(fieldTotalPrice, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("purchase.properties.totalPrice"));
        panel4.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldTotalPriceCurrency = new JLabel();
        fieldTotalPriceCurrency.setText("<Currency>");
        panel4.add(fieldTotalPriceCurrency, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("purchase.properties.createDate"));
        panel3.add(label3, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCreateDate = new JTextField();
        fieldCreateDate.setEditable(false);
        panel3.add(fieldCreateDate, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("purchase.properties.createdUser"));
        panel3.add(label4, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCreatedUser = new JTextField();
        fieldCreatedUser.setEditable(false);
        panel3.add(fieldCreatedUser, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel5, new GridConstraints(2, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldContractorBalance = new JTextField();
        fieldContractorBalance.setEditable(false);
        panel5.add(fieldContractorBalance, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("purchase.properties.contractorBalance"));
        panel5.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldContractorBalanceCurrency = new JLabel();
        fieldContractorBalanceCurrency.setText("<Currency>");
        panel5.add(fieldContractorBalanceCurrency, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new GridConstraints(0, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("purchase.properties.currency"));
        panel6.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCurrency = new JTextField();
        fieldCurrency.setEditable(false);
        panel6.add(fieldCurrency, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("purchase.properties.number"));
        panel3.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldNumber = new JTextField();
        fieldNumber.setEditable(false);
        panel3.add(fieldNumber, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("i18n/warehouse").getString("purchase.properties.state"));
        panel3.add(label8, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldState = new JTextField();
        fieldState.setEditable(false);
        panel3.add(fieldState, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        purchaseItemsSplitterPanel = new JPanel();
        purchaseItemsSplitterPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(purchaseItemsSplitterPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        purchaseItemsSplitter = new JSplitPane();
        purchaseItemsSplitter.setDividerLocation(300);
        purchaseItemsSplitter.setDividerSize(6);
        purchaseItemsSplitterPanel.add(purchaseItemsSplitter, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(300, 150), null, null, 0, false));
        wareNeedItemsPanel = new JPanel();
        wareNeedItemsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        purchaseItemsSplitter.setLeftComponent(wareNeedItemsPanel);
        purchaseItemsPanel = new JPanel();
        purchaseItemsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        purchaseItemsSplitter.setRightComponent(purchaseItemsPanel);
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

    //========================================= Helpers ===========================================

    /**
     * Command implementation for adding new item to the purchase with filling purchase item's form.
     */
    private class AddItemToPurchaseCommand extends CustomCommand {
        private AddItemToPurchaseCommand() {
            super(new ResourceCommandNaming("purchase.items.editor.addToPosting.command"), getAddItemAvailability());
        }

        @Override
        protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
            //Try to add new detail item to the purchase.
            WareNeedItemTO wareNeedItem = (WareNeedItemTO) context.getCurrentReportItem();
            PurchaseItemTO newPurchaseItem = new PurchaseItemTO(purchase, wareNeedItem);
            PropertiesForm prop = new PurchaseItemForm(newPurchaseItem, true);
            if (Dialogs.runProperties(prop)) {
                onAddNewPurchaseItem(newPurchaseItem);
                return true;
            }
            return false;
        }
    }


    private class AddToPurchaseCommand extends CustomCommand {

        protected AddToPurchaseCommand() {
            super(new ResourceCommandNaming("purchase.items.editor.addToPosting.command"), getAddItemAvailability());
        }

        @Override
        protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
            DetailBatchTO detailBatchToAdd = (DetailBatchTO) context.getCurrentReportItem();
            WareNeedItemTO wareNeedItem = new WareNeedItemTO(wareNeed, detailBatchToAdd, true);
            wareNeedItem.setAutoCreated(true);
            PurchaseItemTO newPurchaseItem = new PurchaseItemTO(purchase, wareNeedItem);
            PropertiesForm prop = new PurchaseWareNeedItemForm(newPurchaseItem, true);
            if (Dialogs.runProperties(prop)) {
                onAddNewWareNeedItem(wareNeedItem, null);
                onAddNewPurchaseItem(newPurchaseItem);
                return true;
            }

            return false;
        }
    }

    private void onAddNewWareNeedItem(WareNeedItemTO newWareNeedItem, WareNeedItemTO existsWareNeedItem) {
        if (existsWareNeedItem == null) {
            wareNeed.addNewItem(newWareNeedItem);
        } else {
            existsWareNeedItem.setBuyPrice(newWareNeedItem.getBuyPrice());
            existsWareNeedItem.setBuyCurrency(newWareNeedItem.getBuyCurrency());
            existsWareNeedItem.setCount(existsWareNeedItem.getCount() + newWareNeedItem.getCount());
            existsWareNeedItem.setCreateDateTime(newWareNeedItem.getCreateDateTime());
            existsWareNeedItem.setCreatedUser(newWareNeedItem.getCreatedUser());
            existsWareNeedItem.setNotice(newWareNeedItem.getNotice());
            existsWareNeedItem.setMaxPrice(newWareNeedItem.getMaxPrice());
            existsWareNeedItem.setMinYear(newWareNeedItem.getMinYear());
            existsWareNeedItem.setCustomer(newWareNeedItem.getCustomer());
            existsWareNeedItem.setMisc(newWareNeedItem.getMisc());
            SpringServiceContext.getInstance().getWareNeedsService().saveWareNeedItem(existsWareNeedItem);
        }
    }

    private AvailabilityStrategy getAddItemAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_PURCHASE_ITEMS);
    }

    /**
     * Adding new item to posting items list.
     *
     * @param newPurchaseItem
     * @return
     */
    private void onAddNewPurchaseItem(PurchaseItemTO newPurchaseItem) {
        try {
            getPurchaseService().addItemToPurchase(newPurchaseItem);
        } catch (BusinessException e) {
            MessageDialogs.showWarning(e.getMessage());
        }

    }

    public PurchaseService getPurchaseService() {
        return SpringServiceContext.getInstance().getPurchaseService();
    }

    private void initListeners() {
        waitPurchase.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onWaitPurchase();
            }
        });
    }

    private void onWaitPurchase() {
        //Make purchase waiting for wares from the supplier.
        try {
            getPurchaseService().waitPurchase(purchase);
        } catch (BusinessException e) {
            MessageDialogs.showWarning(contentPanel, e.getMessage());
            return;
        }

        //Change view to conform new purchase state.
        initPurchaseProperties();
        refreshPurchaseState();
    }

    private void refreshPurchaseState() {
        if (purchase.canBeEdited()) {
            //Init interface for editing purchase, when it is constructing.
            waitPurchase.setEnabled(true);
            purchaseItemsSplitterPanel.add(purchaseItemsSplitter, GridLayoutUtils.getGrowingAndFillingCellConstraints());
            initWareNeedItemsList();
            initPurchaseItemsList(purchaseItemsPanel);
        } else {
            //Init interface for commited purchase.
            waitPurchase.setEnabled(false);
            initPurchaseItemsList(purchaseItemsSplitterPanel);
        }

        purchaseItemsSplitterPanel.revalidate();
    }

    private void initPurchaseProperties() {
        fieldState.setText(purchase.getState().getName());
        fieldNumber.setText(String.valueOf(purchase.getNumber()));
        fieldCreateDate.setText(purchase.getCreateDateAsText());
        fieldCreatedUser.setText(purchase.getCreatedUser().getDisplayName());
        fieldCurrency.setText(purchase.getCurrency().getSign());
        fieldContractor.setText(purchase.getContractor().getName());
        fieldContractorBalance.setText(StringUtils.formatNumber(purchase.getContractor().getAccount(purchase.getCurrency().getId()).getCurrentBalance()));
        fieldContractorBalanceCurrency.setText(purchase.getCurrency().getSign());
        fieldTotalPrice.setText(StringUtils.formatNumber(purchase.getTotalPrice()));
        fieldTotalPriceCurrency.setText(purchase.getCurrency().getSign());
    }

    private void initWareNeedItemsList() {
        java.util.List<SwitchableViewItem> viewItems = new ArrayList<SwitchableViewItem>();

        viewItems.add(new SwitchableViewItem() {
            @Override
            public String getName() {
                return I18nSupport.message("wareNeed.items.editor.title");
            }

            @Override
            public Component getCreateViewComponent() {
                //Decorates detail batches list with new commands for adding purchase items to list.
                ReportCommandList additionalCommands = new ReportCommandListImpl();
                additionalCommands.add(new AddItemToPurchaseCommand());
                additionalCommands.setDefaultCommandForRow(additionalCommands.get(0));

                WareNeedItemsList wareNeedItemsList = new WareNeedItemsList(getMainWareNeed().getId(), PurchaseItemsEditor.this.getClass().getCanonicalName());
                ReportCommandsDecorator decoratedNeedItemsList = new ReportCommandsDecorator(wareNeedItemsList, additionalCommands);
                TableReport tableReport = new TableReport(decoratedNeedItemsList, PurchaseItemsEditor.this);
                return tableReport.getContentPanel();
            }
        });

        viewItems.add(new SwitchableViewItem() {
            @Override
            public String getName() {
                return I18nSupport.message("detail.batches.list.title");
            }

            @Override
            public Component getCreateViewComponent() {
                //Decorates detail batches list with new commands for adding ware need items to list.
                DetailBatchesList detailBatchesList = new DetailBatchesList("DetailBatchesList.in.PurchaseItemsEditor");
                ReportCommandsDecorator decoratedBatchesList = new ReportCommandsDecorator(detailBatchesList, createDetailBatchesDecoratedCommands());
                TableReport detailBatchesReport = new TableReport(decoratedBatchesList, PurchaseItemsEditor.this);
                return detailBatchesReport.getContentPanel();
            }
        });

        viewItems.add(new SwitchableViewItem() {
            @Override
            public String getName() {
                return I18nSupport.message("detail.catalog.list.properties.title");
            }

            @Override
            public Component getCreateViewComponent() {
                DetailCatalog detailsCatalog = new DetailCatalog(PurchaseItemsEditor.this, new DetailCatalogBatchesListFactory() {
                    private DetailBatchesList detailBatchesList;
                    private ReportDataSource decoratedBatchesList;

                    @Override
                    public ReportDataSource createDetailBatchesDataSource(DetailGroupTO catalogGroup) {
                        if (detailBatchesList == null) {
                            //Create new list only on first request.
                            detailBatchesList = new DetailBatchesList(catalogGroup, "DetailBatchesCatalog.in.PurchaseItemsEditor");
                            decoratedBatchesList = new ReportCommandsDecorator(detailBatchesList, createDetailBatchesDecoratedCommands());
                        } else {
                            //Reuse created data source (just change filter to load appropriate list of details).
                            detailBatchesList.setCatalogGroup(catalogGroup);
                        }
                        return decoratedBatchesList;
                    }
                }, true);
                return detailsCatalog.getContentPanel();
            }
        });

        detailBatchesView = new SwitchableView(viewItems, -1);

        wareNeedItemsPanel.removeAll();
        wareNeedItemsPanel.add(detailBatchesView.getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
    }

    private ReportCommandList createDetailBatchesDecoratedCommands() {
        ReportCommandList additionalCommands = new ReportCommandListImpl();
        additionalCommands.add(new AddToPurchaseCommand());
        additionalCommands.setDefaultCommandForRow(additionalCommands.get(0));
        return additionalCommands;
    }

    private WareNeedTO getMainWareNeed() {
        WareNeedService wareNeedService = SpringServiceContext.getInstance().getWareNeedsService();
        return wareNeedService.getWareNeedForEditing(wareNeedService.getMainWareNeedId());
    }

    private void initPurchaseItemsList(JComponent container) {
        container.removeAll();
        PurchaseItemsList purchaseItemsList = new PurchaseItemsList(purchase);
        container.add(new TableReport(purchaseItemsList, this).getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
    }
}
