/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.orders.items;

import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.utils.UIComponentUtils;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 09.04.2009
 */

/**
 * Form for editing warehouse batch count to be put into order as one of order subitems.
 */
public class WarehouseBatchForOrderForm {
    private JTextField fieldStoragePlace;
    private JTextField fieldNotice;
    private JTextField fieldWarehouse;
    private JTextField fieldAvailableCount;
    private JTextField fieldCount;
    private JTextField fieldFoundCount;
    private JButton acceptRevertAll;
    private JPanel contentPanel;
    private JTextField fieldCountToAppend;
    private JLabel labelCountToAppend;
    private JLabel labelStoragePlace;
    private JLabel labelNotice;
    private JLabel labelWarehouse;
    private JLabel labelAvailableCount;
    private JLabel labelCount;
    private JLabel labelFoundCount;
    private JTextField fieldReceiptDate;
    private JLabel labelReceiptDate;

    private WarehouseBatchTO warehouseBatch;
    private WarehouseBatchForOrderFormOwner owner;
    private long lastCount;
    /**
     * Amount of really found wares of this warehouse batch (user to indicate problems
     * during complecting process).
     */
    private Long foundCount;

    /**
     * If true, than field for appending of count should be shown.
     */
    private boolean appendCount;

    /**
     * @param warehouseBatch - warehouse batch, for which this form is beeng created.
     * @param choosenCount   - count of details, already have been put into order item.
     * @param owner          - owner of this warehouse batch form.
     */
    public WarehouseBatchForOrderForm(WarehouseBatchTO warehouseBatch, long choosenCount, Long foundCount,
                                      boolean appendCount, boolean showTitle, WarehouseBatchForOrderFormOwner owner) {
        this.warehouseBatch = warehouseBatch;
        this.foundCount = foundCount;
        this.appendCount = appendCount;
        this.owner = owner;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(fieldCount, ModelFieldsLengths.MAX_TEXT_LENGTH_LONG_VAR);
        DataFiltering.setTextLengthLimit(fieldCountToAppend, ModelFieldsLengths.MAX_TEXT_LENGTH_LONG_VAR);

        initFormControls(showTitle);
        initFields();
        initListeners();

        if (choosenCount > 0) {
            accept(choosenCount);
        }
    }

    private void initFormControls(boolean showTitle) {
        //Initialization of controls (cannot be made through GUI designer).
        //1. Title initialization.
        labelWarehouse.setVisible(showTitle);
        labelStoragePlace.setVisible(showTitle);
        labelNotice.setVisible(showTitle);
        labelReceiptDate.setVisible(showTitle);
        labelAvailableCount.setVisible(showTitle);
        labelCount.setVisible(showTitle);
        labelFoundCount.setVisible(showTitle && owner.hasComplectingProblems());

        //2. Fields initialization.
        fieldWarehouse.setFocusable(false);
        fieldStoragePlace.setFocusable(false);
        fieldNotice.setFocusable(false);
        fieldReceiptDate.setFocusable(false);
        fieldAvailableCount.setFocusable(false);

        UIComponentUtils.makeSelectingAllTextOnFocus(fieldCount);
        UIComponentUtils.makeSelectingAllTextOnFocus(fieldCountToAppend);

        if (appendCount) {
            fieldCount.setEditable(false);
            fieldCount.setFocusable(false);
            labelCountToAppend.setVisible(true);
            fieldCountToAppend.setVisible(true);
        } else {
            labelCountToAppend.setVisible(false);
            fieldCountToAppend.setVisible(false);
        }

        if (!SpringServiceContext.getInstance().getWarehouseBatchService().isTrackPostingItem()) {
            labelReceiptDate.setVisible(false);
            fieldReceiptDate.setVisible(false);
        }
    }

    private void initFields() {
        fieldWarehouse.setText(warehouseBatch.getWarehouse().getName());
        fieldStoragePlace.setText(warehouseBatch.getStoragePlace().getSign());
        fieldNotice.setText(warehouseBatch.getNotice());
        fieldReceiptDate.setText(StringUtils.formatDate(warehouseBatch.getReceiptDate()));
        fieldAvailableCount.setText(String.valueOf(warehouseBatch.getAvailableCount()));
        getCountEditorField().setText(null);

        fieldFoundCount.setVisible(owner.hasComplectingProblems());
        fieldFoundCount.setText(foundCount == null ? null : foundCount.toString());
    }

    private void initListeners() {
        acceptRevertAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (acceptRevertAll.getText().equals(I18nSupport.message("order.detailItem.properties.warehouseBatch.accept"))) {
                    //Accepts all details from this batch.
                    acceptAll();
                } else {
                    //Reverts all details from this batch.
                    revertAll();
                }
            }
        });
        getCountEditorField().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    //Enter key used to apply new count.
                    onApplyNewCount();
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    //Escape user to cancel editing of count.
                    onCalcelCountEditing();
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
        JTextField countEditorField = getCountEditorField();
        if (countEditorField.getText().isEmpty()) {
            return true;
        }
        try {
            DataValidation.checkIsNumberLong(countEditorField.getText(), countEditorField);
            long maxValue = Math.min(Long.valueOf(fieldAvailableCount.getText()) + lastCount, owner.getRemainingCount() + lastCount);
            DataValidation.checkValueRangeLong(Long.valueOf(countEditorField.getText()), countEditorField, 0, maxValue);
        } catch (DataValidationException ex) {
            UIComponentUtils.showCommentOnComponent(countEditorField, ex.getMessage());
            countEditorField.selectAll();
            return false;
        }
        return true;
    }

    private void onApplyNewCount() {
        //Try to apply new count of details from warehouse batch to be placed into order item.
        if (validateCount()) {
            if (!getCountEditorField().getText().isEmpty()) {
                accept(Long.valueOf(getCountEditorField().getText()));
            } else {
                revertAll();
            }
        }
    }

    private void onCalcelCountEditing() {
        getCountEditorField().setText(lastCount > 0 ? String.valueOf(lastCount) : null);
        getCountEditorField().selectAll();
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public void acceptAll() {
        long availableCount = Long.valueOf(fieldAvailableCount.getText());
        long remainingCount = owner.getRemainingCount();
        accept(Math.min(availableCount, remainingCount));
    }

    private void accept(long count) {
        accept(count, false);
    }

    /**
     * Reverts  placing details into the order item.
     */
    public void revertAll() {
        fieldAvailableCount.setText(String.valueOf(Long.valueOf(fieldAvailableCount.getText()) + lastCount));
        getCountEditorField().setText(null);
        lastCount = 0;
        acceptRevertAll.setText(I18nSupport.message("order.detailItem.properties.warehouseBatch.accept"));
        fireCountChanged();
    }

    /**
     * Initializes accepted count taken from this warehouse batch.
     *
     * @param count    count to be accepted.
     * @param reserved true, if wares has been already reserved.
     */
    public void initAcceptedCount(long count, boolean reserved) {
        if (appendCount) {
            //Simply write given count to the fieldCount. This is done in such way,
            //because when edit count in 'append' mode, we should care only about newly
            //entered count (count to be appended).
            fieldCount.setText(String.valueOf(count));
        } else {
            //Emulating acceptance by user (with updating of all dependent fields).
            accept(count, reserved);
        }
    }

    /**
     * Accepts given count of details. Behaivor
     *
     * @param countToAccept   - count of details to be accepted from this warehouse batch.
     * @param alreadyReserved - true, is details are already reserved, and false, if not.
     */
    private void accept(long countToAccept, boolean alreadyReserved) {
        if (countToAccept > 0 && countToAccept != lastCount) {
            long coundDiff = countToAccept - lastCount;

            if (!alreadyReserved) {
                //We only need update count of available items, if their are not reserved yet.
                //It is because if details were already reserved, available count of details of the warehouse batch
                // would already reflect that.
                fieldAvailableCount.setText(String.valueOf(Long.valueOf(fieldAvailableCount.getText()) - coundDiff));
            }

            getCountEditorField().setText(String.valueOf(countToAccept));
            lastCount = countToAccept;
            acceptRevertAll.setText(I18nSupport.message("order.detailItem.properties.warehouseBatch.revert"));
            fireCountChanged();
        }
    }

    private void fireCountChanged() {
        owner.onWarehouseBatchCountChanged(this);
    }

    /**
     * @return total count of wares to be taken from this warehouse batch.
     */
    public long getTotalCount() {
        if (appendCount) {
            long initialCount = fieldCount.getText().isEmpty() ? 0 : Long.valueOf(fieldCount.getText());
            return initialCount + getAcceptedCount();
        } else {
            return getAcceptedCount();
        }
    }

    /**
     * @return - chosen count of details from this warehouse batch.
     */
    public long getAcceptedCount() {
        return getCountEditorField().getText().isEmpty() ? 0 : Long.valueOf(getCountEditorField().getText());
    }

    /**
     * @return - all available for choosing count of details from this warehouse batch (including already selected
     *         items for this order item).
     */
    public long getAvailableCount() {
        return getAcceptedCount() + Long.valueOf(fieldAvailableCount.getText());
    }

    public WarehouseBatchTO getWarehouseBatch() {
        return warehouseBatch;
    }

    public JTextField getCountEditorField() {
        //Depending on current form working mode active form field may vary as shown below.
        return appendCount ? fieldCountToAppend : fieldCount;
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
        contentPanel.setLayout(new GridLayoutManager(2, 8, new Insets(0, 0, 0, 0), -1, -1));
        fieldStoragePlace = new JTextField();
        fieldStoragePlace.setEditable(false);
        contentPanel.add(fieldStoragePlace, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(100, -1), null, 0, false));
        fieldNotice = new JTextField();
        fieldNotice.setEditable(false);
        contentPanel.add(fieldNotice, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, -1), new Dimension(200, -1), null, 0, false));
        fieldAvailableCount = new JTextField();
        fieldAvailableCount.setEditable(false);
        fieldAvailableCount.setHorizontalAlignment(0);
        contentPanel.add(fieldAvailableCount, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(70, -1), new Dimension(70, -1), null, 0, false));
        acceptRevertAll = new JButton();
        acceptRevertAll.setHorizontalTextPosition(0);
        this.$$$loadButtonText$$$(acceptRevertAll, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.warehouseBatch.accept"));
        acceptRevertAll.putClientProperty("html.disable", Boolean.FALSE);
        acceptRevertAll.putClientProperty("hideActionText", Boolean.FALSE);
        contentPanel.add(acceptRevertAll, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldFoundCount = new JTextField();
        fieldFoundCount.setEditable(false);
        fieldFoundCount.setForeground(Color.red);
        fieldFoundCount.setHorizontalAlignment(0);
        fieldFoundCount.setText("<found>");
        fieldFoundCount.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.warehouseBatch.foundCountToolTip"));
        contentPanel.add(fieldFoundCount, new GridConstraints(1, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldCount = new JTextField();
        fieldCount.setHorizontalAlignment(0);
        fieldCount.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.warehouseBatch.countToolTip"));
        panel1.add(fieldCount, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        fieldCountToAppend = new JTextField();
        fieldCountToAppend.setHorizontalAlignment(0);
        fieldCountToAppend.setText("");
        fieldCountToAppend.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.warehouseBatch.countToAppendToolTip"));
        panel1.add(fieldCountToAppend, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        labelCountToAppend = new JLabel();
        labelCountToAppend.setHorizontalAlignment(0);
        labelCountToAppend.setText("+");
        panel1.add(labelCountToAppend, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelStoragePlace = new JLabel();
        labelStoragePlace.setHorizontalAlignment(0);
        labelStoragePlace.setHorizontalTextPosition(0);
        this.$$$loadLabelText$$$(labelStoragePlace, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.warehouseBatch.storagePlace"));
        contentPanel.add(labelStoragePlace, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelNotice = new JLabel();
        labelNotice.setHorizontalAlignment(0);
        this.$$$loadLabelText$$$(labelNotice, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.warehouseBatch.notice"));
        contentPanel.add(labelNotice, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelAvailableCount = new JLabel();
        labelAvailableCount.setHorizontalAlignment(0);
        labelAvailableCount.setHorizontalTextPosition(0);
        this.$$$loadLabelText$$$(labelAvailableCount, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.warehouseBatch.availableCount"));
        contentPanel.add(labelAvailableCount, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelCount = new JLabel();
        labelCount.setHorizontalAlignment(0);
        labelCount.setHorizontalTextPosition(0);
        this.$$$loadLabelText$$$(labelCount, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.warehouseBatch.countToTake"));
        contentPanel.add(labelCount, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelFoundCount = new JLabel();
        labelFoundCount.setHorizontalAlignment(0);
        labelFoundCount.setHorizontalTextPosition(0);
        this.$$$loadLabelText$$$(labelFoundCount, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.warehouseBatch.foundCount"));
        contentPanel.add(labelFoundCount, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelWarehouse = new JLabel();
        labelWarehouse.setHorizontalAlignment(0);
        labelWarehouse.setHorizontalTextPosition(0);
        this.$$$loadLabelText$$$(labelWarehouse, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.warehouseBatch.warehouse"));
        contentPanel.add(labelWarehouse, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldWarehouse = new JTextField();
        fieldWarehouse.setEditable(false);
        contentPanel.add(fieldWarehouse, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(100, -1), null, 0, false));
        labelReceiptDate = new JLabel();
        labelReceiptDate.setHorizontalAlignment(0);
        labelReceiptDate.setHorizontalTextPosition(0);
        this.$$$loadLabelText$$$(labelReceiptDate, ResourceBundle.getBundle("i18n/warehouse").getString("order.detailItem.properties.warehouseBatch.receiptDate"));
        contentPanel.add(labelReceiptDate, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldReceiptDate = new JTextField();
        fieldReceiptDate.setEditable(false);
        contentPanel.add(fieldReceiptDate, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(100, -1), null, 0, false));
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
