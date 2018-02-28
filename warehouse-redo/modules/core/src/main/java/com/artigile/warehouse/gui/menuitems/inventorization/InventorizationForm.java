/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.inventorization;

import com.artigile.warehouse.bl.inventorization.inventorization.InventorizationService;
import com.artigile.warehouse.domain.inventorization.InventorizationType;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.init.InitUtils;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.browser.BrowseResult;
import com.artigile.warehouse.gui.core.report.controller.TreeReport;
import com.artigile.warehouse.gui.core.report.controller.TreeReportViewType;
import com.artigile.warehouse.gui.menuitems.warehouse.storageplace.StoragePlaceTreeReport;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.inventorization.InventorizationTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 29.09.2009
 */
public class InventorizationForm implements PropertiesForm {
    private JPanel contentPanel;
    private JTextField fieldNumber;
    private JButton generateNumber;
    private JTextField fieldState;
    private JTextField fieldCreateDate;
    private JTextField fieldCreatedUser;
    private JTextArea fieldNotice;
    private JComboBox fieldInventorizationType;
    private JTextField fieldCloseDate;
    private JTextField fieldCloseUser;
    private JTextField fieldResult;
    private JComboBox fieldWarehouse;
    private JList fieldStoragePlaces;
    private JButton browseStoragePlaces;

    private InventorizationTOForReport inventorization;
    private boolean canEdit;

    /**
     * Last selected warehouse id.
     */
    private long lastWarehouseId;

    public InventorizationForm(InventorizationTOForReport inventorization, boolean canEdit) {
        this.inventorization = inventorization;
        this.canEdit = canEdit;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(fieldNumber, ModelFieldsLengths.MAX_TEXT_LENGTH_LONG_VAR);
        DataFiltering.setTextLengthLimit(fieldNotice, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        InitUtils.initComboFromEnumeration(fieldInventorizationType, InventorizationType.values(), null);
        InitUtils.initWarehousesCombo(fieldWarehouse, null);
        initListeners();
    }

    //======================== PropertiesForm implementation =========================
    @Override
    public String getTitle() {
        return inventorization.isNew() ? I18nSupport.message("inventorization.properties.title.create") : I18nSupport.message("inventorization.properties.title.properties");
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
        if (!inventorization.isNew()) {
            generateNumber.setVisible(false);
        }

        fieldNumber.setText(inventorization.getNumber() == 0 ? null : String.valueOf(inventorization.getNumber()));
        fieldState.setText(inventorization.getState().getName());
        fieldResult.setText(inventorization.getResult() == null ? null : inventorization.getResult().getName());
        fieldCreatedUser.setText(inventorization.getCreateUser() == null ? null : inventorization.getCreateUser().getDisplayName());
        fieldCreateDate.setText(inventorization.getCreateDate() == null ? null : StringUtils.getDateTimeFormat().format(inventorization.getCreateDate()));
        fieldCloseUser.setText(inventorization.getCloseUser() == null ? null : inventorization.getCloseUser().getDisplayName());
        fieldCloseDate.setText(inventorization.getCloseDate() == null ? null : StringUtils.getDateTimeFormat().format(inventorization.getCloseDate()));
        fieldNotice.setText(inventorization.getNotice());

        DataExchange.selectComboItem(fieldInventorizationType, inventorization.getType());
        DataExchange.selectComboItem(fieldWarehouse, inventorization.getWarehouse());

        List<ListItem> storagePlaces = new ArrayList<ListItem>();
        for (StoragePlaceTOForReport place : inventorization.getStoragePlacesToCheck()) {
            storagePlaces.add(new ListItem(place.getSign(), place));
        }
        DataExchange.setListItems(fieldStoragePlaces, storagePlaces);
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(fieldNumber);
        DataValidation.checkIsNumberLong(fieldNumber.getText(), fieldNumber);
        if (!getInventorizationService().isUniqueInventorizationNumber(Long.valueOf(fieldNumber.getText()), inventorization.getId())) {
            DataValidation.failRes(fieldNumber, "inventorization.properties.error.numberNotUnique");
        }
        DataValidation.checkSelected(fieldInventorizationType);
        DataValidation.checkSelected(fieldWarehouse);
        DataValidation.checkCondition(fieldStoragePlaces.getModel().getSize() > 0, fieldStoragePlaces, "inventorization.properties.error.noStogatePlacesForCheck");
    }

    @Override
    public void saveData() {
        inventorization.setNumber(Long.valueOf(fieldNumber.getText()));
        inventorization.setNotice(fieldNotice.getText());
        inventorization.setType((InventorizationType) DataExchange.getComboSelection(fieldInventorizationType));
        inventorization.setWarehouse((WarehouseTOForReport) DataExchange.getComboSelection(fieldWarehouse));

        List<ListItem> storagePlaceItems = DataExchange.getListItems(fieldStoragePlaces);
        inventorization.getStoragePlacesToCheck().clear();
        for (ListItem placeItem : storagePlaceItems) {
            inventorization.getStoragePlacesToCheck().add((StoragePlaceTOForReport) placeItem.getValue());
        }
    }

    //================================ Helpers ===================================
    private void initListeners() {
        generateNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onGenerateInventorizationNumber();
            }
        });

        fieldWarehouse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSelectWarehouse((WarehouseTOForReport) DataExchange.getComboSelection(fieldWarehouse));
            }
        });

        browseStoragePlaces.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBrowseStoragePlaces();
            }
        });
    }

    private void onGenerateInventorizationNumber() {
        //Generating new inventorization number.
        fieldNumber.setText(String.valueOf(getInventorizationService().getNextAvailableInventorizationNumber()));
    }

    private void onSelectWarehouse(WarehouseTOForReport warehouse) {
        //Updates storage place tree to refleft selected new warehouse for check.
        if (warehouse == null || lastWarehouseId != warehouse.getId()) {
            lastWarehouseId = warehouse == null ? 0 : warehouse.getId();
            DataExchange.setListItems(fieldStoragePlaces, null);
            browseStoragePlaces.setEnabled(warehouse != null);
        }
    }

    private void onBrowseStoragePlaces() {
        //Open warehouse storage places to choose places to check.
        TreeReport storagePlaceTree = new TreeReport(new StoragePlaceTreeReport(lastWarehouseId), TreeReportViewType.TREE_TABLE);
        BrowseResult result = Dialogs.runTreeBrowser(storagePlaceTree, I18nSupport.message("inventorization.browse.storage.places"), true, true);
        if (result.isOk()) {
            //Show selected storage places.
            List<ListItem> listItems = new ArrayList<ListItem>();
            for (Object placeObj : result.getSelectedItems()) {
                StoragePlaceTOForReport place = (StoragePlaceTOForReport) placeObj;
                listItems.add(new ListItem(place.getSign(), place));
            }
            DataExchange.setListItems(fieldStoragePlaces, listItems);
        }
    }

    private InventorizationService getInventorizationService() {
        return SpringServiceContext.getInstance().getInventorizationService();
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
        panel1.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.state"));
        panel1.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldState = new JTextField();
        fieldState.setEditable(false);
        panel1.add(fieldState, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.result"));
        panel1.add(label2, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldResult = new JTextField();
        fieldResult.setEditable(false);
        panel1.add(fieldResult, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.number"));
        panel1.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldNumber = new JTextField();
        panel2.add(fieldNumber, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        generateNumber = new JButton();
        generateNumber.setText("=");
        panel2.add(generateNumber, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.createUser"));
        panel3.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCreatedUser = new JTextField();
        fieldCreatedUser.setEditable(false);
        panel3.add(fieldCreatedUser, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.createDate"));
        panel3.add(label5, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.closeUser"));
        panel3.add(label6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.closeDate"));
        panel3.add(label7, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCloseUser = new JTextField();
        fieldCloseUser.setEditable(false);
        panel3.add(fieldCloseUser, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldCreateDate = new JTextField();
        fieldCreateDate.setEditable(false);
        panel3.add(fieldCreateDate, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldCloseDate = new JTextField();
        fieldCloseDate.setEditable(false);
        panel3.add(fieldCloseDate, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.type"));
        panel4.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldInventorizationType = new JComboBox();
        fieldInventorizationType.setEditable(false);
        panel4.add(fieldInventorizationType, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        this.$$$loadLabelText$$$(label9, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.warehouse"));
        panel4.add(label9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldWarehouse = new JComboBox();
        panel4.add(fieldWarehouse, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        this.$$$loadLabelText$$$(label10, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.storagePlaces"));
        label10.setVerticalAlignment(0);
        label10.setVerticalTextPosition(0);
        panel4.add(label10, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel5.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        fieldStoragePlaces = new JList();
        scrollPane1.setViewportView(fieldStoragePlaces);
        browseStoragePlaces = new JButton();
        browseStoragePlaces.setText("...");
        panel5.add(browseStoragePlaces, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        this.$$$loadLabelText$$$(label11, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.properties.notice"));
        panel4.add(label11, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel4.add(scrollPane2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 50), null, null, 0, false));
        fieldNotice = new JTextArea();
        scrollPane2.setViewportView(fieldNotice);
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
