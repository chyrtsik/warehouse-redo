/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.batchesImport;

import com.artigile.warehouse.adapter.spi.DataAdapterConfigView;
import com.artigile.warehouse.adapter.spi.DataAdapterFactory;
import com.artigile.warehouse.adapter.spi.DataAdapterUI;
import com.artigile.warehouse.adapter.spi.impl.DomainColumn;
import com.artigile.warehouse.bl.detail.DetailBatchImportService;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.init.InitUtils;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.browser.BrowseResult;
import com.artigile.warehouse.gui.menuitems.details.types.DetailTypesList;
import com.artigile.warehouse.gui.utils.UIComponentUtils;
import com.artigile.warehouse.utils.MiscUtils;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.details.DetailBatchImportTO;
import com.artigile.warehouse.utils.dto.details.DetailTypeTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * @author Aliaksandr.Chyrtsik, 06.11.11
 */
public class CreateDetailBatchImportForm implements PropertiesForm {
    private JPanel contentPanel;
    private JTextField fieldDetailType;
    private JButton browseDetailType;
    private JComboBox fieldCurrency;
    private JComboBox fieldMeasureUnit;
    private JComboBox fieldImportDataAdapter;
    private JTextArea fieldDescription;
    private JPanel adapterConfigurationPanel;

    private DataAdapterConfigView configView;
    private String lastSelectedAdapterUid;
    private DetailTypeTOForReport selectedDetailType;

    private DetailBatchImportTO detailBatchImport;
    private boolean canEdit;

    public CreateDetailBatchImportForm(DetailBatchImportTO detailBatchImport, boolean canEdit) {
        this.detailBatchImport = detailBatchImport;
        this.canEdit = canEdit;

        DataFiltering.setTextLengthLimit(fieldDescription, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        InitUtils.initAdapterCombo(fieldImportDataAdapter, null);
        InitUtils.initCurrenciesCombo(fieldCurrency, null);
        InitUtils.initMeasuresCombo(fieldMeasureUnit, null);
        initListeners();
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

        browseDetailType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBrowseDetailType();
            }
        });
    }

    private void refreshAdapterConfigurationView() {
        //Recreates UI for import adapter configuration.
        DataAdapterFactory factory = SpringServiceContext.getInstance().getDataImportService().getDataAdapterFactoryByAdapterUid(lastSelectedAdapterUid);
        if (selectedDetailType != null && factory != null) {
            DetailBatchImportService service = SpringServiceContext.getInstance().getDetailBatchImportService();
            java.util.List<DomainColumn> priceColumns = service.getDomainColumnsForDetailType(selectedDetailType.getId());

            DataAdapterUI dataAdapterUI = factory.createDataAdapterUI(priceColumns);
            configView = dataAdapterUI.getConfigView();

            JComponent viewCompoment = configView.getView();
            adapterConfigurationPanel.removeAll();
            adapterConfigurationPanel.add(viewCompoment, new GridConstraints(0, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    viewCompoment.getMinimumSize(), viewCompoment.getPreferredSize(), viewCompoment.getMaximumSize()));
            contentPanel.revalidate();
            UIComponentUtils.packDialog(contentPanel);
        }
    }

    private void onBrowseDetailType() {
        //Show browser for choosing/creating contractor.
        BrowseResult result = Dialogs.runBrowser(new DetailTypesList());
        if (result.isOk()) {
            onDetailTypeSelected((DetailTypeTOForReport) result.getSelectedItems().get(0));

        }
    }

    private void onDetailTypeSelected(DetailTypeTOForReport newDetailType) {
        if (!MiscUtils.objectsEquals(selectedDetailType, newDetailType)) {
            selectedDetailType = newDetailType;
            fieldDetailType.setText(selectedDetailType == null ? "" : selectedDetailType.getName());
            refreshAdapterConfigurationView();
        }
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("detail.batch.import.properties.title");
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
        DataExchange.selectComboItemLeaveDefault(fieldCurrency, detailBatchImport.getCurrency());
        DataExchange.selectComboItemLeaveDefault(fieldMeasureUnit, detailBatchImport.getMeasureUnit());
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(fieldDetailType);
        DataValidation.checkSelected(fieldCurrency);
        DataValidation.checkSelected(fieldMeasureUnit);
        DataValidation.checkSelected(fieldImportDataAdapter);
        if (configView != null) {
            configView.validateData();
        }
    }

    @Override
    public void saveData() {
        detailBatchImport.setDetailType(selectedDetailType);
        detailBatchImport.setCurrency((CurrencyTO) DataExchange.getComboSelection(fieldCurrency));
        detailBatchImport.setMeasureUnit((MeasureUnitTO) DataExchange.getComboSelection(fieldMeasureUnit));
        detailBatchImport.setAdapterUid((String) DataExchange.getComboSelection(fieldImportDataAdapter));

        if (configView != null) {
            detailBatchImport.setAdapterConf(configView.getConfigurationString());
        }

        detailBatchImport.setDescription(fieldDescription.getText());
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
        contentPanel.setLayout(new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("detail.batch.import.properties.detailType"));
        contentPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        fieldDetailType = new JTextField();
        fieldDetailType.setEditable(false);
        fieldDetailType.setText("");
        panel1.add(fieldDetailType, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        browseDetailType = new JButton();
        browseDetailType.setText("...");
        panel1.add(browseDetailType, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("detail.batch.import.properties.currency"));
        contentPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCurrency = new JComboBox();
        contentPanel.add(fieldCurrency, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("detail.batch.import.properties.measureUnit"));
        contentPanel.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldMeasureUnit = new JComboBox();
        contentPanel.add(fieldMeasureUnit, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("data.import.create.adapter"));
        contentPanel.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(29, 27), null, 0, false));
        fieldImportDataAdapter = new JComboBox();
        contentPanel.add(fieldImportDataAdapter, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(30);
        contentPanel.add(scrollPane1, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, new Dimension(200, 60), null, null, 0, false));
        fieldDescription = new JTextArea();
        fieldDescription.setText("");
        scrollPane1.setViewportView(fieldDescription);
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("data.import.create.description"));
        contentPanel.add(label5, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        adapterConfigurationPanel = new JPanel();
        adapterConfigurationPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(adapterConfigurationPanel, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
