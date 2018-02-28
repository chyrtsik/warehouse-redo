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

import com.artigile.warehouse.adapter.spi.DataAdapterConfigView;
import com.artigile.warehouse.adapter.spi.DataAdapterFactory;
import com.artigile.warehouse.adapter.spi.DataAdapterUI;
import com.artigile.warehouse.adapter.spi.impl.DomainColumn;
import com.artigile.warehouse.bl.postings.PostingItemsIdentityType;
import com.artigile.warehouse.bl.postings.PostingItemsImportConfiguration;
import com.artigile.warehouse.bl.postings.PostingService;
import com.artigile.warehouse.bl.warehouse.StoragePlaceFilter;
import com.artigile.warehouse.bl.warehouse.WarehouseFilter;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.data.init.InitUtils;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.utils.UIComponentUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.postings.PostingTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ResourceBundle;

/**
 * Form for configuring import of posting items from external source.
 *
 * @author Aliaksandr.Chyrtsik, 07.11.11
 */
public class ImportPostingItemsForm implements PropertiesForm {
    private JPanel contentPanel;
    private JComboBox fieldCurrency;
    private JComboBox fieldMeasureUnit;
    private JComboBox fieldImportDataAdapter;
    private JPanel adapterConfigurationPanel;
    private JComboBox fieldItemsIdentityType;
    private JComboBox fieldStoragePlace;
    private JComboBox fieldWarehouse;

    private String lastSelectedAdapterUid;

    private PostingItemsImportConfiguration importConfiguration;
    private DataAdapterConfigView configView;

    public ImportPostingItemsForm(long postingId) {
        importConfiguration = new PostingItemsImportConfiguration();
        importConfiguration.setPostingId(postingId);

        InitUtils.initComboFromEnumeration(fieldItemsIdentityType, PostingItemsIdentityType.values(), null);
        InitUtils.initCurrenciesCombo(fieldCurrency, null);
        InitUtils.initMeasuresCombo(fieldMeasureUnit, null);
        InitUtils.initWarehousesCombo(fieldWarehouse, WarehouseFilter.createOnlyAvailableForPostingsFilter(), null);
        initStoragePlacesCombo();
        InitUtils.initAdapterCombo(fieldImportDataAdapter, null);

        initListeners();
    }

    private void initStoragePlacesCombo() {
        // List of storage places depends from a selected warehouse
        ListItem selectedWarehouse = (ListItem) fieldWarehouse.getSelectedItem();
        if (selectedWarehouse != null) {
            InitUtils.initStoragePlacesCombo(fieldStoragePlace,
                    StoragePlaceFilter.createAvailableForPostingsFilter(((WarehouseTOForReport) selectedWarehouse.getValue()).getId()),
                    null);
        } else {
            fieldStoragePlace.removeAllItems();
        }
    }

    private void initListeners() {
        fieldImportDataAdapter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object value = DataExchange.getComboSelection(fieldImportDataAdapter);
                if (value != null) {
                    String uid = value.toString();
                    if (uid.equals(lastSelectedAdapterUid)) {
                        return;
                    } else {
                        lastSelectedAdapterUid = uid;
                    }
                    refreshAdapterConfigurationView();
                }
            }
        });

        fieldItemsIdentityType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAdapterConfigurationView();
            }
        });

        fieldWarehouse.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Re-init combo box with the storage places, because warehouse was changed
                initStoragePlacesCombo();
            }
        });
    }

    private void refreshAdapterConfigurationView() {
        PostingItemsIdentityType selectedIdentityType = (PostingItemsIdentityType) DataExchange.getComboSelection(fieldItemsIdentityType);
        if (selectedIdentityType == null) {
            return;
        }
        DataAdapterFactory factory = SpringServiceContext.getInstance().getDataImportService().getDataAdapterFactoryByAdapterUid(lastSelectedAdapterUid);
        if (factory == null) {
            return;
        }

        //Recreate UI for import adapter configuration.
        PostingService service = SpringServiceContext.getInstance().getPostingsService();
        java.util.List<DomainColumn> priceColumns = service.getDomainColumnsForIdentifyType(selectedIdentityType);

        DataAdapterUI dataAdapterUI = factory.createDataAdapterUI(priceColumns);
        configView = dataAdapterUI.getConfigView();

        JComponent viewComponent = configView.getView();
        adapterConfigurationPanel.removeAll();
        adapterConfigurationPanel.add(viewComponent, new GridConstraints(0, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                viewComponent.getMinimumSize(), viewComponent.getPreferredSize(), viewComponent.getMaximumSize()));
        contentPanel.revalidate();
        UIComponentUtils.packDialog(contentPanel);
    }

    public PostingItemsImportConfiguration getImportConfiguration() {
        return importConfiguration;
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("posting.items.import.properties.title");
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
        PostingTOForReport posting = SpringServiceContext.getInstance().getPostingsService().getPostingForReport(importConfiguration.getPostingId());
        DataExchange.selectComboItemLeaveDefault(fieldItemsIdentityType, importConfiguration.getItemsIdentityType());
        DataExchange.selectComboItemLeaveDefault(fieldCurrency, posting.getDefaultCurrency());
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkSelected(fieldItemsIdentityType);
        DataValidation.checkSelected(fieldCurrency);
        DataValidation.checkSelected(fieldMeasureUnit);
        DataValidation.checkSelected(fieldWarehouse);
        DataValidation.checkSelected(fieldStoragePlace);
        DataValidation.checkSelected(fieldImportDataAdapter);
        if (configView != null) {
            configView.validateData();
        }
    }

    @Override
    public void saveData() {
        importConfiguration.setItemsIdentityType((PostingItemsIdentityType) DataExchange.getComboSelection(fieldItemsIdentityType));
        importConfiguration.setCurrency((CurrencyTO) DataExchange.getComboSelection(fieldCurrency));
        importConfiguration.setMeasureUnit((MeasureUnitTO) DataExchange.getComboSelection(fieldMeasureUnit));

        // Transformation from StoragePlaceTOForReport to StoragePlaceTO
        StoragePlaceTOForReport selectedStoragePlace = (StoragePlaceTOForReport) DataExchange.getComboSelection(fieldStoragePlace);
        importConfiguration.setStoragePlace(SpringServiceContext.getInstance().getStoragePlaceService().get(selectedStoragePlace.getId()));

        importConfiguration.setDataAdapterUid((String) DataExchange.getComboSelection(fieldImportDataAdapter));
        if (configView != null) {
            importConfiguration.setDataAdapterConfiguration(configView.getConfigurationString());
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
        contentPanel.setLayout(new GridLayoutManager(7, 2, new Insets(0, 0, 0, 0), -1, -1));
        fieldCurrency = new JComboBox();
        contentPanel.add(fieldCurrency, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("posting.items.import.properties.buy.currency"));
        contentPanel.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("posting.items.import.properties.measureUnit"));
        contentPanel.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldMeasureUnit = new JComboBox();
        contentPanel.add(fieldMeasureUnit, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("data.import.create.adapter"));
        contentPanel.add(label3, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(29, 27), null, 0, false));
        fieldImportDataAdapter = new JComboBox();
        contentPanel.add(fieldImportDataAdapter, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        adapterConfigurationPanel = new JPanel();
        adapterConfigurationPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(adapterConfigurationPanel, new GridConstraints(6, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldItemsIdentityType = new JComboBox();
        contentPanel.add(fieldItemsIdentityType, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(300, -1), null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("posting.items.import.properties.determine.items.by.fields"));
        contentPanel.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("posting.items.import.properties.warehouse"));
        contentPanel.add(label5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("posting.items.import.properties.storagePlace"));
        contentPanel.add(label6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldWarehouse = new JComboBox();
        contentPanel.add(fieldWarehouse, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldStoragePlace = new JComboBox();
        contentPanel.add(fieldStoragePlace, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
