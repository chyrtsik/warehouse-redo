/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.orders.items;

import com.artigile.warehouse.bl.finance.CurrencyExchangeService;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchService;
import com.artigile.warehouse.domain.orders.OrderItemProcessingResult;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.gui.utils.UIComponentUtils;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.orders.OrderItemTO;
import com.artigile.warehouse.utils.dto.orders.OrderSubItemTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;

/**
 * @author Shyrik, 08.04.2009
 */
public class OrderDetailItemForm implements PropertiesForm, WarehouseBatchForOrderFormOwner {
    private JPanel contentPanel;
    private JLabel fieldDetailType;
    private JTextField fieldDetailName;
    private JTextField fieldDetailMisc;
    private JTextField fieldDetailCount;
    private JTextField fieldDetailPrice;
    private JTextField fieldPrice;
    private JTextField fieldCount;
    private JTextField fieldCountToAppend;
    private JTextField fieldRemainingCount;
    private JLabel fieldCountMeas;
    private JPanel warehouseBatchesPanel;
    private JPanel noWarehouseBatchesPanel;
    private JTextArea fieldNotice;
    private JLabel labelCountToAppend;

    private boolean canEdit;
    private boolean appendCount;
    private OrderItemTO orderItem;
    private List<WarehouseBatchForOrderForm> batchForms = new ArrayList<WarehouseBatchForOrderForm>();
    private long lastCount;

    public OrderDetailItemForm(OrderItemTO orderItem, boolean canEdit, boolean appendCount) {
        this.canEdit = canEdit;
        this.orderItem = orderItem;
        this.appendCount = appendCount;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(fieldPrice, ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION);
        DataFiltering.setTextLengthLimit(fieldNotice, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(fieldCount, ModelFieldsLengths.MAX_TEXT_LENGTH_LONG_VAR);
        DataFiltering.setTextLengthLimit(fieldCountToAppend, ModelFieldsLengths.MAX_TEXT_LENGTH_LONG_VAR);

        initFormControls();
        initWarehouseBatchesPanel();
        initListeners();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("order.detailItem.properties.title");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public boolean canSaveData() {
        return canEdit;
    }

    @Override
    public void loadData() {
        final DetailBatchTO detailBatch = orderItem.getDetailBatch();
        fieldDetailType.setText(detailBatch.getType());
        fieldDetailName.setText(detailBatch.getName());
        fieldDetailMisc.setText(detailBatch.getMisc());
        fieldDetailCount.setText(String.valueOf(detailBatch.getAvailCount()));
        if (detailBatch.getNeedRecalculate()) {
            fieldDetailCount.setForeground(Color.RED);
        }
        fieldDetailPrice.setText(makePriceText(detailBatch.getCurrency(), detailBatch.getSellPrice()));
        fieldPrice.setText(StringUtils.formatNumber(orderItem.getPrice()));
        fieldCountMeas.setText(detailBatch.getCountMeas().getSign());
        fieldNotice.setText(orderItem.getNotice());

        fieldCount.setText(orderItem.getCount() == 0 ? null : String.valueOf(orderItem.getCount()));
        lastCount = orderItem.getCount();

        //Showing details from which warehouse batches have been placed into order subitems.
        for (OrderSubItemTO subItem : orderItem.getSubItems()) {
            for (WarehouseBatchForOrderForm batchForm : batchForms) {

                WarehouseBatchTO wb = subItem.getWarehouseBatch();
                if (wb != null && batchForm.getWarehouseBatch().getId() == wb.getId()) {
                    //We don't care about updating count and remaining count manually, because it is been
                    // done during "accepting" details from warehouse batch.
                    batchForm.initAcceptedCount(subItem.getCount(), orderItem.isReserved());
                }
            }
        }
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkCondition(orderItem.getDetailBatch().getAvailCount() >= (getTotalCount() - orderItem.getCount()), warehouseBatchesPanel, "order.detailItem.properties.notEnoughBatchesAtWarehouses");
        DataValidation.checkNotEmpty(fieldPrice);
        DataValidation.checkIsNumber(fieldPrice.getText(), fieldPrice);

        String count = getCountEditorField().getText();
        DataValidation.checkNotEmpty(count, getCountEditorField());
        DataValidation.checkIsNumberLong(count, getCountEditorField());
        DataValidation.checkValueRangeLong(Long.valueOf(getCountEditorField().getText()), getCountEditorField(), 1, getAvailableDetailsCount());

        if (appendCount) {
            String countToAppend = fieldCountToAppend.getText();
            DataValidation.checkNotEmpty(countToAppend, fieldCountToAppend);
            DataValidation.checkIsNumber(countToAppend, fieldCountToAppend);
            DataValidation.checkCondition(Long.valueOf(countToAppend) > 0, fieldCountToAppend, "order.detailItem.properties.must.be.positive");
        }

        if (getRemainingCount() > 0) {
            DataValidation.failRes(fieldRemainingCount, "order.detailItem.properties.notEnoughBatchesChoosen");
        }
    }

    @Override
    public void saveData() {
        orderItem.setPrice(StringUtils.parseStringToBigDecimal(fieldPrice.getText()));
        orderItem.setNotice(fieldNotice.getText());
        orderItem.setCount(getTotalCount());

        //Saving from which warehouse batches we should take details for this order item.
        //During this process we delete not used order subitems and create new, if needed.
        List<OrderSubItemTO> usedSubItems = new ArrayList<OrderSubItemTO>();
        for (WarehouseBatchForOrderForm batchForm : batchForms) {
            if (batchForm.getTotalCount() > 0) {
                boolean subItemFound = false;
                for (OrderSubItemTO subItem : orderItem.getSubItems()) {
                    if (batchForm.getWarehouseBatch().getId() == subItem.getWarehouseBatch().getId()) {
                        //One of already existing subitems can be reused.
                        subItem.setCount(batchForm.getTotalCount());
                        usedSubItems.add(subItem);
                        subItemFound = true;
                    }
                }
                if (!subItemFound) {
                    //We are to create new sub item for warehouse batch, not used earlier for this order item.
                    OrderSubItemTO newSubItem = new OrderSubItemTO(orderItem, batchForm.getWarehouseBatch());
                    newSubItem.setCount(batchForm.getTotalCount());
                    usedSubItems.add(newSubItem);
                }
            }
        }
        orderItem.setSubItems(usedSubItems);
    }

    private long getTotalCount() {
        if (appendCount) {
            return Long.valueOf(fieldCount.getText()) + Long.valueOf(fieldCountToAppend.getText());
        } else {
            return Long.valueOf(fieldCount.getText());
        }
    }

    public JTextField getCountEditorField() {
        //Active field for editing of count depends on current mode of form working: edit of append count.
        return appendCount ? fieldCountToAppend : fieldCount;
    }

    //======================== Implementation of WarehouseBatchForOrderFormOwner ======================================

    @Override
    public long getRemainingCount() {
        return fieldRemainingCount.getText().isEmpty() ? 0 : Long.valueOf(fieldRemainingCount.getText());
    }

    @Override
    public void onWarehouseBatchCountChanged(WarehouseBatchForOrderForm source) {
        refreshRemainingCount();
    }

    @Override
    public boolean hasComplectingProblems() {
        for (OrderSubItemTO subItem : orderItem.getSubItems()) {
            if (subItem.getProcessingResult() == OrderItemProcessingResult.PROBLEM) {
                return true;
            }
        }
        return false;
    }

    //============================ Helpers and UI commands processing ===================================

    private String makePriceText(CurrencyTO currency, BigDecimal price) {
        CurrencyTO orderCurrency = orderItem.getOrder().getCurrency();
        BigDecimal priceInDestCurrency = price;
        if (currency.getId() != orderCurrency.getId()) {
            //We need to convert price into appropriate currency.
            CurrencyExchangeService exchangeService = SpringServiceContext.getInstance().getCurencyExchangeService();
            priceInDestCurrency = exchangeService.convert(orderCurrency.getId(), currency.getId(), price);
        }
        return MessageFormat.format("{0} {1}", orderCurrency.getSign(), StringUtils.formatMoneyAmount(priceInDestCurrency));
    }

    private void initFormControls() {
        //Initialization of forms controls.
        UIComponentUtils.makeSelectingAllTextOnFocus(fieldPrice);
        UIComponentUtils.makeSelectingAllTextOnFocus(getCountEditorField());
        UIComponentUtils.makeSelectingAllTextOnFocus(fieldCountToAppend);

        fieldDetailName.setFocusable(false);
        fieldDetailMisc.setFocusable(false);
        fieldDetailCount.setFocusable(false);
        fieldDetailPrice.setFocusable(false);
        fieldRemainingCount.setFocusable(false);

        if (appendCount) {
            fieldCount.setEditable(false);
            fieldCount.setFocusable(false);
            labelCountToAppend.setVisible(true);
            fieldCountToAppend.setVisible(true);
        } else {
            labelCountToAppend.setVisible(false);
            fieldCountToAppend.setVisible(false);
        }
    }

    private void initWarehouseBatchesPanel() {
        //Creates batchForms for each warehouse batch of detail.
        batchForms.clear();
        warehouseBatchesPanel.removeAll();
        WarehouseBatchService warehouseBatchService = SpringServiceContext.getInstance().getWarehouseBatchService();
        List<WarehouseBatchTO> batches = sortBatches(warehouseBatchService.getWarehouseBatchesForDetailBatch(orderItem.getDetailBatch().getId()));
        if (batches.size() > 0) {
            noWarehouseBatchesPanel.setVisible(false);
            warehouseBatchesPanel.setLayout(new GridLayoutManager(batches.size(), 1));
            for (int i = 0; i < batches.size(); i++) {
                GridConstraints constraints = GridLayoutUtils.getGrowingAndFillingCellConstraints();
                constraints.setRow(i);

                Long foundCount = getFoundCountForBatch(batches.get(i));
                WarehouseBatchForOrderForm batchForm = new WarehouseBatchForOrderForm(batches.get(i), 0, foundCount, appendCount, i == 0, this);
                warehouseBatchesPanel.add(batchForm.getContentPanel(), constraints);
                batchForms.add(batchForm);
            }
        } else {
            //There are no detail batches at the warehouse.
            noWarehouseBatchesPanel.setVisible(true);
        }
    }

    private List<WarehouseBatchTO> sortBatches(List<WarehouseBatchTO> batches) {
        //Sorting warehouse batches. Items at the same warehouse, than current user, should be
        //at the beginning of the list. After this items are sorted by receipt date.
        Collections.sort(batches, new Comparator<WarehouseBatchTO>() {
            @Override
            public int compare(WarehouseBatchTO first, WarehouseBatchTO second) {
                if (first.getWarehouse().getId() == second.getWarehouse().getId()) {
                    return compareReceiptDate(first, second);
                }
                long userWarehouseId = WareHouse.getUserSession().getUserWarehouse().getId();
                if (first.getWarehouse().getId() == userWarehouseId) {
                    return -1;
                } else if (second.getWarehouse().getId() == userWarehouseId) {
                    return 1;
                } else {
                    return compareReceiptDate(first, second);
                }
            }

            private int compareReceiptDate(WarehouseBatchTO first, WarehouseBatchTO second) {
                Date firstDate = first.getReceiptDate();
                Date secondDate = second.getReceiptDate();
                if (firstDate == null) {
                    return secondDate == null ? 0 : 1;
                } else if (secondDate == null) {
                    return -1;
                } else {
                    return firstDate.compareTo(secondDate);
                }
            }
        });
        return batches;
    }

    /**
     * If order item is being complecting and there is a problem and problem is connected with given warehouse
     * batch, this method calculates really found amout of wares, represented with this warehouse batch.
     *
     * @param warehouseBatch - warehouse batch to be checked.
     * @return
     */
    private Long getFoundCountForBatch(WarehouseBatchTO warehouseBatch) {
        Long foundCount = null;
        for (OrderSubItemTO subItem : orderItem.getSubItems()) {
            WarehouseBatchTO wb = subItem.getWarehouseBatch();
            if (wb != null && warehouseBatch.getId() == wb.getId() &&
                    subItem.getProcessingResult() == OrderItemProcessingResult.PROBLEM) {
                //There is a problem with this warehouse batch and order sub item.
                foundCount = subItem.getFoundCount();
                break;
            }
        }
        return foundCount;
    }

    private void initListeners() {
        getCountEditorField().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    //Enter key used to apply new count.
                    onApplyNewCount();
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    //Escape user to cancel editing of count.
                    onCancelCountEditing();
                    e.consume();
                }
            }
        });

        getCountEditorField().addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                onApplyNewCount();
            }
        });

        getCountEditorField().setInputVerifier(new InputVerifier() {
            public boolean verify(JComponent input) {
                return false;
            }

            public boolean shouldYieldFocus(JComponent input) {
                return validateCount();
            }
        });
    }

    private boolean validateCount() {
        if (getCountEditorField().getText().isEmpty()) {
            return true;
        }
        try {
            DataValidation.checkIsNumberLong(getCountEditorField().getText(), getCountEditorField());
            DataValidation.checkValueRangeLong(Long.valueOf(getCountEditorField().getText()), getCountEditorField(), 1, getAvailableDetailsCount());
        } catch (DataValidationException ex) {
            UIComponentUtils.showCommentOnComponent(getCountEditorField(), ex.getMessage());
            getCountEditorField().selectAll();
            return false;
        }
        return true;
    }

    private long getAvailableDetailsCount() {
        long availableCount = 0;
        for (WarehouseBatchForOrderForm batchForm : batchForms) {
            availableCount += batchForm.getAvailableCount();
        }
        return availableCount;
    }

    private void onApplyNewCount() {
        if (validateCount()) {
            //Apply new count of needed details for this order item.
            if (!getCountEditorField().getText().isEmpty()) {
                lastCount = Long.valueOf(getCountEditorField().getText());
            } else {
                lastCount = 0;
            }
            resetWarehouseBatchedCounts();

            //Performing smart filling of the form for some particular cases.
            if (batchForms.size() == 1) {
                //There is only one batch of details at the warehouse.
                batchForms.get(0).acceptAll();
            } else if (orderItem.getDetailBatch().getCount() <= lastCount) {
                //All available details will be placed into this order item.
                for (WarehouseBatchForOrderForm batchForm : batchForms) {
                    batchForm.acceptAll();
                }
            }
        }
    }

    private void onCancelCountEditing() {
        //Cancel editing of count field (no changed will be made in form).
        getCountEditorField().setText(lastCount > 0 ? String.valueOf(lastCount) : null);
        getCountEditorField().selectAll();
    }

    private void resetWarehouseBatchedCounts() {
        //"Unchoose" all details, that have been placed into the order as last.
        for (WarehouseBatchForOrderForm batchForm : batchForms) {
            batchForm.revertAll();
        }
    }

    private void refreshRemainingCount() {
        //Refreshes count details, that are needed to be placed into order item to complete order item editing.
        long remainingCount = getCountEditorField().getText().isEmpty() ? 0 : Long.valueOf(getCountEditorField().getText());
        for (WarehouseBatchForOrderForm batchForm : batchForms) {
            remainingCount -= batchForm.getAcceptedCount();
        }
        fieldRemainingCount.setText(String.valueOf(remainingCount));
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
        contentPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 6, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.detailName"));
        panel1.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldDetailName = new JTextField();
        fieldDetailName.setEditable(false);
        panel1.add(fieldDetailName, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.detailMisc"));
        panel1.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.detailCount"));
        panel1.add(label3, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.detailPrice"));
        panel1.add(label4, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.price"));
        panel1.add(label5, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldDetailMisc = new JTextField();
        fieldDetailMisc.setEditable(false);
        fieldDetailMisc.setHorizontalAlignment(0);
        panel1.add(fieldDetailMisc, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        fieldDetailCount = new JTextField();
        fieldDetailCount.setEditable(false);
        fieldDetailCount.setHorizontalAlignment(0);
        panel1.add(fieldDetailCount, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        fieldDetailPrice = new JTextField();
        fieldDetailPrice.setEditable(false);
        fieldDetailPrice.setHorizontalAlignment(0);
        panel1.add(fieldDetailPrice, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        fieldPrice = new JTextField();
        fieldPrice.setHorizontalAlignment(0);
        panel1.add(fieldPrice, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        fieldDetailType = new JLabel();
        fieldDetailType.setText("<Type>");
        panel1.add(fieldDetailType, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.remainingCount"));
        panel3.add(label6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldRemainingCount = new JTextField();
        fieldRemainingCount.setEditable(false);
        fieldRemainingCount.setHorizontalAlignment(0);
        panel3.add(fieldRemainingCount, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        fieldCountMeas = new JLabel();
        fieldCountMeas.setHorizontalAlignment(0);
        fieldCountMeas.setHorizontalTextPosition(2);
        fieldCountMeas.setText("<Meas>");
        panel4.add(fieldCountMeas, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setHorizontalAlignment(0);
        label7.setHorizontalTextPosition(4);
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.count"));
        panel4.add(label7, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldCount = new JTextField();
        fieldCount.setHorizontalAlignment(0);
        panel5.add(fieldCount, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        labelCountToAppend = new JLabel();
        labelCountToAppend.setHorizontalAlignment(0);
        labelCountToAppend.setText("+");
        panel5.add(labelCountToAppend, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCountToAppend = new JTextField();
        fieldCountToAppend.setHorizontalAlignment(0);
        panel5.add(fieldCountToAppend, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel2.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel2.add(spacer4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel6, new GridConstraints(2, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel6.setBorder(BorderFactory.createTitledBorder(""));
        warehouseBatchesPanel = new JPanel();
        warehouseBatchesPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(warehouseBatchesPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel6.add(spacer5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel6.add(spacer6, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        noWarehouseBatchesPanel = new JPanel();
        noWarehouseBatchesPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(noWarehouseBatchesPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setHorizontalAlignment(0);
        label8.setHorizontalTextPosition(0);
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.warehouseBatch.noDetailsPresent"));
        noWarehouseBatchesPanel.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        this.$$$loadLabelText$$$(label9, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.notice"));
        panel7.add(label9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel7.add(spacer7, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel7.add(scrollPane1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 50), null, null, 0, false));
        fieldNotice = new JTextArea();
        fieldNotice.setLineWrap(true);
        fieldNotice.setWrapStyleWord(false);
        scrollPane1.setViewportView(fieldNotice);
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
    public JComponent $$$getRootComponent$$$() {
        return contentPanel;
    }
}
