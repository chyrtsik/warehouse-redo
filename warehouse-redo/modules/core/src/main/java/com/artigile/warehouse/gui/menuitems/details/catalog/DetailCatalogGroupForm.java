/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.catalog;

import com.artigile.warehouse.bl.detail.DetailTypeService;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.choosedialog.ChooseDialogResult;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailGroupTO;
import com.artigile.warehouse.utils.dto.details.DetailTypeTO;
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
 * @author Shyrik, 04.01.2009
 */
public class DetailCatalogGroupForm implements PropertiesForm {
    private JPanel contentPanel;
    private JTextField groupName;
    private JTextArea groupDescription;
    private JList groupDetailTypes;
    private JButton browseDetailTypes;
    private JCheckBox groupEnableAutomaticGrouping;

    DetailGroupTO detailGroup;
    boolean canEdit;
    DetailCatalogStructureEditingStrategy.CatalogGroupChecker groupChecker;

    public DetailCatalogGroupForm(DetailGroupTO detailGroup, boolean canEdit, DetailCatalogStructureEditingStrategy.CatalogGroupChecker groupChecker) {
        this.detailGroup = detailGroup;
        this.canEdit = canEdit;
        this.groupChecker = groupChecker;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(groupName, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(groupDescription, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        browseDetailTypes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBrowseDetailTypes();
            }
        });

        //Enabling of automatic grouping is only allowed for groups without children.
        groupEnableAutomaticGrouping.setEnabled(detailGroup.getChildGroups().isEmpty());
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("detail.catalog.group.properties.title");
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
        groupName.setText(detailGroup.getName());
        groupDescription.setText(detailGroup.getDescription());
        DataExchange.setListItems(groupDetailTypes, makeList(detailGroup.getDetailTypes()));
        groupEnableAutomaticGrouping.setSelected(detailGroup.getEnableAutoSubGroups());
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(groupName);
        if (!groupChecker.isUniqueName(groupName.getText(), detailGroup)) {
            DataValidation.failRes(groupName, "detail.catalog.group.properties.name.already.exists");
        }
    }

    @Override
    public void saveData() {
        detailGroup.setName(groupName.getText());
        detailGroup.setDescription(groupDescription.getText());
        detailGroup.setDetailTypes(makeDetailTypesList(DataExchange.getListItems(groupDetailTypes)));
        detailGroup.setEnableAutoSubGroups(groupEnableAutomaticGrouping.isSelected());
    }

    //==================================== User input processing =================================================
    private void onBrowseDetailTypes() {
        //Showing dialog for choosing detail types to be in group.
        List<ListItem> availableDetailTypes = makeList(getDetailTypesService().getAllDetailTypesFull());
        List<ListItem> selectedDetailTypes = DataExchange.getListItems(groupDetailTypes);
        String title = I18nSupport.message("detail.catalog.group.properties.choose.detailTypes.title");

        ChooseDialogResult result = Dialogs.runChooseListDialog(title, availableDetailTypes, selectedDetailTypes);
        if (result.isOk()) {
            DataExchange.setListItems(groupDetailTypes, result.getSelectedItems());
        }
    }

    //======================================= Helpers =============================================================
    private DetailTypeService getDetailTypesService() {
        return SpringServiceContext.getInstance().getDetailTypesService();
    }

    private List<ListItem> makeList(List<DetailTypeTO> detailTypes) {
        List<ListItem> items = new ArrayList<ListItem>();
        for (DetailTypeTO detailType : detailTypes) {
            items.add(new ListItem(detailType.getName(), detailType));
        }
        return items;
    }

    private List<DetailTypeTO> makeDetailTypesList(List<ListItem> items) {
        List<DetailTypeTO> detailTypes = new ArrayList<DetailTypeTO>();
        for (ListItem item : items) {
            detailTypes.add((DetailTypeTO) item.getValue());
        }
        return detailTypes;
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
        contentPanel.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("detail.catalog.group.properties.name"));
        contentPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        groupName = new JTextField();
        contentPanel.add(groupName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("detail.catalog.group.properties.description"));
        contentPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPanel.add(scrollPane1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(300, 50), null, null, 0, false));
        groupDescription = new JTextArea();
        scrollPane1.setViewportView(groupDescription);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("detail.catalog.group.properties.detailTypesInGroup"));
        panel1.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        browseDetailTypes = new JButton();
        browseDetailTypes.setText("...");
        panel1.add(browseDetailTypes, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        contentPanel.add(scrollPane2, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        groupDetailTypes = new JList();
        scrollPane2.setViewportView(groupDetailTypes);
        groupEnableAutomaticGrouping = new JCheckBox();
        this.$$$loadButtonText$$$(groupEnableAutomaticGrouping, ResourceBundle.getBundle("i18n/warehouse").getString("detail.catalog.group.properties.enableAutoSubGroups"));
        contentPanel.add(groupEnableAutomaticGrouping, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        label1.setLabelFor(groupName);
        label2.setLabelFor(groupDescription);
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
