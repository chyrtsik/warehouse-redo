/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.priceimport;

import com.artigile.warehouse.adapter.spi.DataAdapterConfigView;
import com.artigile.warehouse.adapter.spi.DataAdapterFactory;
import com.artigile.warehouse.adapter.spi.DataAdapterUI;
import com.artigile.warehouse.adapter.spi.impl.DomainColumn;
import com.artigile.warehouse.bl.priceimport.ContractorProductDomainColumnType;
import com.artigile.warehouse.bl.priceimport.SellerSettingsService;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.init.InitUtils;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.browser.BrowseResult;
import com.artigile.warehouse.gui.menuitems.contractors.ContractorsList;
import com.artigile.warehouse.gui.utils.UIComponentUtils;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.priceimport.ContractorPriceImportTO;
import com.artigile.warehouse.utils.dto.priceimport.SellerSettingsTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.ResourceBundle;

/**
 * @author Valery Barysok, 6/11/11
 */

public class PriceImportForm implements PropertiesForm {
    private JPanel contentPane;
    private JTextField seller;
    private JButton sellerBrowse;
    private JTextArea description;
    private JPanel adapterConfiguration;
    private JComboBox currency;
    private JComboBox measureUnit;
    private JComboBox importDataAdapter;
    private JComboBox conversionCurrency;
    private JTextField conversionExchangeRate;

    private DataAdapterConfigView configView;
    private String lastSelectedAdapterUid;

    private ContractorTO selectedSeller;
    private ContractorPriceImportTO priceImport;

    private boolean canEdit;

    public PriceImportForm(ContractorPriceImportTO priceImport, boolean canEdit) {
        this.priceImport = priceImport;
        this.canEdit = canEdit;

        DataFiltering.setTextLengthLimit(description, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        InitUtils.initAdapterCombo(importDataAdapter, null);
        InitUtils.initCurrenciesCombo(currency, null);
        InitUtils.initMeasuresCombo(measureUnit, null);
        InitUtils.initCurrenciesCombo(conversionCurrency, null);
        refreshConversionExchangeRateField();
        initListeners();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("price.import.create.title");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPane;
    }

    @Override
    public boolean canSaveData() {
        return canEdit;
    }

    @Override
    public void loadData() {
        selectedSeller = priceImport.getContractor();
        refreshContractor();
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(seller);
        DataValidation.checkSelected(currency);
        DataValidation.checkSelected(conversionCurrency);
        DataValidation.checkIsPositiveNumber(conversionExchangeRate);
        DataValidation.checkSelected(measureUnit);
        DataValidation.checkSelected(importDataAdapter);
        if (configView != null) {
            configView.validateData();
        }
    }

    @Override
    public void saveData() {
        priceImport.setContractor(selectedSeller);
        priceImport.setMeasureUnit((MeasureUnitTO) DataExchange.getComboSelection(measureUnit));
        priceImport.setCurrency((CurrencyTO) DataExchange.getComboSelection(currency));
        priceImport.setAdapterUid((String) DataExchange.getComboSelection(importDataAdapter));

        if (configView != null) {
            priceImport.setAdapterConf(configView.getConfigurationString());
        }

        priceImport.setConversionCurrency((CurrencyTO) DataExchange.getComboSelection(conversionCurrency));
        priceImport.setConversionExchangeRate(Double.valueOf(conversionExchangeRate.getText().replace(',', '.')));
        priceImport.setDescription(description.getText());

        storeSellerSettings();
    }

    private void refreshContractor() {
        if (selectedSeller != null) {
            seller.setText(selectedSeller.getName());
            DataExchange.selectComboItem(currency, selectedSeller.getDefaultCurrency());
        } else {
            seller.setText(null);
        }
    }

    private void onBrowseSeller() {
        //Show browser for choosing/creating contractor.
        BrowseResult result = Dialogs.runBrowser(new ContractorsList());
        if (result.isOk()) {
            selectedSeller = (ContractorTO) result.getSelectedItems().get(0);
            refreshContractor();
            restoreSellerSettings();
        }
    }

    private void initListeners() {
        importDataAdapter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object value = DataExchange.getComboSelection(importDataAdapter);
                if (value != null) {
                    String uid = value.toString();
                    if (uid.equals(lastSelectedAdapterUid)) {
                        return;
                    } else {
                        lastSelectedAdapterUid = uid;
                    }

                    DataAdapterFactory factory = SpringServiceContext.getInstance().getDataImportService().getDataAdapterFactoryByAdapterUid(uid);
                    if (factory != null) {
                        java.util.List<DomainColumn> priceColumns = ContractorProductDomainColumnType.enumerateDomainColumns();
                        DataAdapterUI dataAdapterUI = factory.createDataAdapterUI(priceColumns);
                        configView = dataAdapterUI.getConfigView();
                        JComponent viewCompoment = configView.getView();
                        adapterConfiguration.add(viewCompoment, new GridConstraints(0, 0, 1, 1,
                                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                                viewCompoment.getMinimumSize(), viewCompoment.getPreferredSize(), viewCompoment.getMaximumSize()));
                        contentPane.revalidate();
                        UIComponentUtils.packDialog(contentPane);
                    }
                }
            }
        });

        sellerBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBrowseSeller();
            }
        });

        // Currencies combo boxes listeners
        currency.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                refreshConversionExchangeRateField();
            }
        });
        conversionCurrency.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                refreshConversionExchangeRateField();
            }
        });
    }

    /**
     * @return Double representation of the exchange rate between currency in a price list and currency of conversion
     */
    private Double getConversionExchangeRate() {
        long priceListCurrencyID = ((CurrencyTO) DataExchange.getComboSelection(currency)).getId();
        long conversionCurrencyID = ((CurrencyTO) DataExchange.getComboSelection(conversionCurrency)).getId();
        BigDecimal exchangeRate = SpringServiceContext.getInstance().getCurencyExchangeService()
                .getExchangeRate(priceListCurrencyID, conversionCurrencyID);
        return (exchangeRate == null) ? null : exchangeRate.doubleValue();
    }

    /**
     * Refreshes text field with exchange rate.
     */
    private void refreshConversionExchangeRateField() {
        Double conversionExchangeRateDbl = getConversionExchangeRate();
        if (conversionExchangeRateDbl != null) {
            conversionExchangeRate.setText(String.valueOf(conversionExchangeRateDbl));
        }
    }

    private SellerSettingsService getSellerSettingsService() {
        return SpringServiceContext.getInstance().getSellerSettingsService();
    }

    private static UserTO getUserTO() {
        return WareHouse.getUserSession().getUser();
    }

    private void restoreSellerSettings() {
        SellerSettingsTO sellerSettings = getSellerSettingsService().findSellerSettingsBy(getUserTO().getId(), selectedSeller.getId());
        if (sellerSettings != null) {
            CurrencyTO currencyTO = new CurrencyTO();
            currencyTO.setId(sellerSettings.getCurrencyId());
            DataExchange.selectComboItem(currency, currencyTO);

            MeasureUnitTO measureUnitTO = new MeasureUnitTO();
            measureUnitTO.setId(sellerSettings.getMeasureUnitId());
            DataExchange.selectComboItem(measureUnit, measureUnitTO);

            DataExchange.selectComboItem(importDataAdapter, sellerSettings.getImportAdapterUid());

            configView.setConfigurationString(sellerSettings.getAdapterConfig());
        }
    }

    private void storeSellerSettings() {
        SellerSettingsTO sellerSettingsTO = new SellerSettingsTO();
        sellerSettingsTO.setUser(getUserTO());
        sellerSettingsTO.setContractorId(priceImport.getContractor().getId());
        sellerSettingsTO.setCurrencyId(priceImport.getCurrency().getId());
        sellerSettingsTO.setMeasureUnitId(priceImport.getMeasureUnit().getId());
        sellerSettingsTO.setImportAdapterUid(priceImport.getAdapterUid());
        sellerSettingsTO.setAdapterConfig(priceImport.getAdapterConf());
        getSellerSettingsService().saveSellerSettings(sellerSettingsTO);
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(8, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("price.import.create.contractor"));
        contentPane.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        importDataAdapter = new JComboBox();
        contentPane.add(importDataAdapter, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("data.import.create.adapter"));
        contentPane.add(label2, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(29, 27), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        seller = new JTextField();
        seller.setEditable(false);
        seller.setText("");
        panel1.add(seller, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        sellerBrowse = new JButton();
        sellerBrowse.setText("...");
        panel1.add(sellerBrowse, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("data.import.create.description"));
        contentPane.add(label3, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(30);
        contentPane.add(scrollPane1, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, new Dimension(200, 60), null, null, 0, false));
        description = new JTextArea();
        description.setText("");
        scrollPane1.setViewportView(description);
        adapterConfiguration = new JPanel();
        adapterConfiguration.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(adapterConfiguration, new GridConstraints(6, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("price.import.create.currency"));
        contentPane.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currency = new JComboBox();
        contentPane.add(currency, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        measureUnit = new JComboBox();
        contentPane.add(measureUnit, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("price.import.create.measure.unit"));
        contentPane.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("price.import.properties.conversion.currency"));
        contentPane.add(label6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        conversionCurrency = new JComboBox();
        contentPane.add(conversionCurrency, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("price.import.properties.conversion.exchange.rate"));
        contentPane.add(label7, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        conversionExchangeRate = new JTextField();
        contentPane.add(conversionExchangeRate, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        label1.setLabelFor(seller);
        label4.setLabelFor(currency);
        label5.setLabelFor(measureUnit);
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
        return contentPane;
    }
}
