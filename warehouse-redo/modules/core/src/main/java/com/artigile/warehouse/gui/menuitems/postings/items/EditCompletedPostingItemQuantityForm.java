/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.postings.items;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.detail.DetailBatchService;
import com.artigile.warehouse.bl.postings.PostingService;
import com.artigile.warehouse.bl.warehouse.StoragePlaceService;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchChangeDocument;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchService;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Form to correct product quantity after posting completion.
 *
 * @author vadim.zverugo (vadim.zverugo@artigile.by)
 */
public class EditCompletedPostingItemQuantityForm implements PropertiesForm {

    private JPanel contentPanel;
    private JTextField storageWarehouseTextField;
    private JTextField storagePlaceTextField;
    private JTextField balanceQuantityTextField;
    private JLabel quantityMeasureUnitBalanceLabel;
    private JTextField productTextField;
    private JLabel quantityMeasureUnitPostedLabel;
    private JTextField postedQuantityTextField;
    private JTextField reservedTextField;
    private JLabel reservedMeasureUnitPostedLabel;

    private WarehouseBatchService warehouseBatchService = SpringServiceContext.getInstance().getWarehouseBatchService();
    private DetailBatchService detailBatchService = SpringServiceContext.getInstance().getDetailBatchesService();
    private StoragePlaceService storagePlaceService = SpringServiceContext.getInstance().getStoragePlaceService();
    private PostingService postingService = SpringServiceContext.getInstance().getPostingsService();

    private PostingItemTO postingItem;

    private WarehouseBatchTO warehouseBatch;


    public EditCompletedPostingItemQuantityForm(PostingItemTO postingItem) {
        this.postingItem = postingItem;
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("posting.item.edit.completed.quantity.title");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public boolean canSaveData() {
        return true;
    }

    @Override
    public void loadData() {
        this.warehouseBatch = warehouseBatchService.getWarehouseBatchForPostingItem(postingItem.getId());

        productTextField.setText(postingItem.getDetailBatch().getName()); // Product
        storageWarehouseTextField.setText(postingItem.getWarehouse().getName()); // Warehouse
        storagePlaceTextField.setText(postingItem.getStoragePlace().getSign()); // Place

        String measureUnitSign = postingItem.getDetailBatch().getCountMeas().getSign();
        balanceQuantityTextField.setText("" + getTotalCount()); // Current balance
        reservedTextField.setText("" + getReservedCount()); // Current balance
        quantityMeasureUnitBalanceLabel.setText(measureUnitSign);
        postedQuantityTextField.setText("" + postingItem.getCount()); // Posted quantity
        quantityMeasureUnitPostedLabel.setText(measureUnitSign);
        reservedMeasureUnitPostedLabel.setText(measureUnitSign);
    }

    private long getBalanceQuantity() {
        if (warehouseBatch == null) {
            return 0;
        }
        return warehouseBatch.getCount() - warehouseBatch.getReservedCount();
    }

    private long getTotalCount() {
        if (warehouseBatch == null) {
            return 0;
        }
        return warehouseBatch.getCount();
    }

    private long getReservedCount() {
        if (warehouseBatch == null) {
            return 0;
        }
        return warehouseBatch.getReservedCount();
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(postedQuantityTextField);
        if (!StringUtils.isNumberLong(postedQuantityTextField.getText())) {
            throw new DataValidationException(postedQuantityTextField, I18nSupport.message("validation.not.a.number.long"));
        }
        DataValidation.checkIsPositiveNumber(postedQuantityTextField);
        long inputQuantity = Long.valueOf(postedQuantityTextField.getText());
        if (getBalanceQuantity() < postingItem.getCount() && inputQuantity < postingItem.getCount()) {
            DataValidation.checkValueMinLong(inputQuantity,
                    postedQuantityTextField,
                    postingItem.getCount() - getBalanceQuantity());
        }
    }

    @Override
    public void saveData() {
        long inputQuantity = Long.valueOf(postedQuantityTextField.getText());
        // If quantity has been changed...
        if (inputQuantity != postingItem.getCount()) {
            WarehouseBatchChangeDocument document = WarehouseBatchChangeDocument.createPostingDocument(
                    postingService.getPostingItemById(postingItem.getId()));
            boolean updatePostingItem = false;
            // If quantity has been decreased...
            if (inputQuantity < postingItem.getCount()) {
                try {
                    warehouseBatchService.performWareChargeOff(warehouseBatch.getId(), postingItem.getCount() - inputQuantity, document);
                    updatePostingItem = true;
                } catch (BusinessException e) {
                    LoggingFacade.logError(e);
                }
                // If quantity has been increased...
            } else if (inputQuantity > postingItem.getCount()) {
                DetailBatch detailBatch = detailBatchService.getDetailBatchById(postingItem.getDetailBatch().getId());
                StoragePlace storagePlace = storagePlaceService.getStoragePlaceById(postingItem.getStoragePlace().getId());
                try {
                    warehouseBatchService.performWareIncome(detailBatch, inputQuantity - postingItem.getCount(), storagePlace, postingItem.getNotice(), document);
                    updatePostingItem = true;
                } catch (BusinessException e) {
                    LoggingFacade.logError(e);
                }
            }
            if (updatePostingItem) {
                postingItem.setCount(inputQuantity);
                postingService.savePostingItem(postingItem);
            }
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
        contentPanel.setLayout(new GridLayoutManager(7, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.edit.completed.quantity.warehouse"));
        contentPanel.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        contentPanel.add(spacer1, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPanel.add(spacer2, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        storageWarehouseTextField = new JTextField();
        storageWarehouseTextField.setEditable(false);
        contentPanel.add(storageWarehouseTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(270, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.edit.completed.quantity.place"));
        contentPanel.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        storagePlaceTextField = new JTextField();
        storagePlaceTextField.setEditable(false);
        contentPanel.add(storagePlaceTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(270, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.edit.completed.quantity.product"));
        contentPanel.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        productTextField = new JTextField();
        productTextField.setEditable(false);
        contentPanel.add(productTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(270, -1), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        balanceQuantityTextField = new JTextField();
        balanceQuantityTextField.setEditable(false);
        balanceQuantityTextField.setHorizontalAlignment(2);
        panel1.add(balanceQuantityTextField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        quantityMeasureUnitBalanceLabel = new JLabel();
        quantityMeasureUnitBalanceLabel.setHorizontalAlignment(10);
        quantityMeasureUnitBalanceLabel.setHorizontalTextPosition(11);
        quantityMeasureUnitBalanceLabel.setText("Label");
        panel1.add(quantityMeasureUnitBalanceLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.edit.completed.quantity.balance"));
        contentPanel.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel2, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel2.add(spacer4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        quantityMeasureUnitPostedLabel = new JLabel();
        quantityMeasureUnitPostedLabel.setText("Label");
        panel2.add(quantityMeasureUnitPostedLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        postedQuantityTextField = new JTextField();
        panel2.add(postedQuantityTextField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.edit.completed.quantity.posted"));
        contentPanel.add(label5, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("posting.item.edit.completed.quantity.reserved"));
        contentPanel.add(label6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel3, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel3.add(spacer5, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        reservedMeasureUnitPostedLabel = new JLabel();
        reservedMeasureUnitPostedLabel.setText("Label");
        panel3.add(reservedMeasureUnitPostedLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reservedTextField = new JTextField();
        reservedTextField.setEditable(false);
        panel3.add(reservedTextField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
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
