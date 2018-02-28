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

import com.artigile.warehouse.bl.priceimport.ContractorProductFilter;
import com.artigile.warehouse.gui.core.plugin.FramePlugin;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.init.InitUtils;
import com.artigile.warehouse.gui.core.report.controller.TableReport;
import com.artigile.warehouse.gui.menuitems.priceimport.contractorProduct.AllContractorProductList;
import com.artigile.warehouse.gui.menuitems.priceimport.contractorProduct.SelectedContractorProductList;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Valery Barysok, 6/4/11
 */

public class FilteredContractorProductList extends FramePlugin {
    private JTextField nameMask;
    private JButton search;
    private JPanel contractorProductListPanel;
    private JPanel contentPanel;
    private JCheckBox ignoreSpecialSymbols;
    private JCheckBox selectedPositions;
    private JSplitPane contractorProductListSplitPanel;
    private JPanel allContractorProductsPanel;
    private JPanel selectedContractorProductsPanel;
    private JTextField extraCharge;
    private JComboBox conversionCurrency;
    private JLabel exchangeRateLabel;
    private JLabel loadedContractorProductsStatus;
    private JPanel dataNavigationPanel;
    private JButton nextPositions;
    private JButton prevPositions;
    private JComboBox supplier;

    /**
     * All filtered products
     */
    private AllContractorProductList allContractorProducts;
    private ContractorProductFilter allContractorProductsFilter = new ContractorProductFilter();
    private TableReport allContractorProductsTableReport;

    /**
     * Selected products by user
     */
    private SelectedContractorProductList selectedContractorProducts;
    private ContractorProductFilter selectedContractorProductsFilter = new ContractorProductFilter();

    /**
     * Default currency
     */
    private CurrencyTO defaultCurrency;

    /**
     * Maps with exchange rates between currencies
     */
    private Map<Long, Map<Long, Double>> exchangeRateMap;

    /**
     * Available suppliers for search
     */
    private List<ContractorTO> supplierList;


    /* Construction
    ------------------------------------------------------------------------------------------------------------------*/
    public FilteredContractorProductList() {
        $$$setupUI$$$();

        hideSelectedPositions();
        initAllContractorProducts();
        initSelectedContractorProducts();
        initCurrencies();
        initSuppliers();
        initListeners();

        refreshDataNavigationPanel();
    }

    private void initAllContractorProducts() {
        allContractorProducts = new AllContractorProductList(allContractorProductsFilter);
        allContractorProductsPanel.removeAll();
        allContractorProductsTableReport = new TableReport(allContractorProducts, this);
        allContractorProductsPanel.add((allContractorProductsTableReport).getContentPanel(),
                GridLayoutUtils.getGrowingAndFillingCellConstraints());
    }

    private void initSelectedContractorProducts() {
        selectedContractorProducts = new SelectedContractorProductList(selectedContractorProductsFilter);
        selectedContractorProductsPanel.removeAll();
        selectedContractorProductsPanel.add((new TableReport(selectedContractorProducts, this)).getContentPanel(),
                GridLayoutUtils.getGrowingAndFillingCellConstraints());
    }

    @SuppressWarnings("unchecked")
    private void initCurrencies() {
        // Load default currency and exchange rates
        defaultCurrency = SpringServiceContext.getInstance().getCurrencyService().getDefaultCurrencyTO();
        exchangeRateMap = SpringServiceContext.getInstance().getCurencyExchangeService().getExchangeRateMap();

        // Init combo box with currencies for conversion
        InitUtils.initCurrenciesCombo(conversionCurrency, null);
        if (defaultCurrency != null) {
            conversionCurrency.setSelectedItem(defaultCurrency);
            allContractorProducts.setConversionCurrency(defaultCurrency);
            selectedContractorProducts.setConversionCurrency(defaultCurrency);
        } else {
            String defaultItem = "<...>";
            conversionCurrency.addItem(defaultItem);
            conversionCurrency.setSelectedItem(defaultItem);
        }
    }

    @SuppressWarnings("unchecked")
    private void initSuppliers() {
        // Init combo box with available suppliers for searching
        String defaultItem = I18nSupport.message("contractor.product.list.supplier.default.item");
        supplier.addItem(defaultItem);
        supplierList = SpringServiceContext.getInstance().getContractorService().getImportedPriceListsContractors();
        for (ContractorTO contractor : supplierList) {
            supplier.addItem(contractor.getName());
        }
    }

    private void initListeners() {
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSearch();
            }
        });

        selectedPositions.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onChangeSelectedPositions();
            }
        });

        extraCharge.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onChangeExtraCharge();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onChangeExtraCharge();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onChangeExtraCharge();
            }
        });

        conversionCurrency.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onChangeConversionCurrency();
            }
        });

        prevPositions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPrevPositions();
            }
        });
        nextPositions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNextPositions();
            }
        });
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public String getTitle() {
        return I18nSupport.message("contractor.product.list.title");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    protected void onFrameOpened() {
        super.onFrameOpened();
        defineDefaultButton();
        nameMask.requestFocusInWindow();
    }

    private void onSearch() {
        resetDataTableFilters();

        // Update search filter and data
        // 1. Filter by name
        String mask = nameMask.getText();
        if (ignoreSpecialSymbols.isSelected()) {
            mask = StringUtils.buildString('*', StringUtils.simplifyName(mask), '*');
        }
        allContractorProductsFilter.setNameMask(StringUtils.prepareFilter(mask));
        allContractorProductsFilter.setIgnoreSpecialSymbols(ignoreSpecialSymbols.isSelected());

        // 2. Filter by supplier
        if (supplier.getSelectedIndex() != 0 && supplier.getSelectedIndex() != -1) {
            for (ContractorTO selectedSupplier : supplierList) {
                if (supplier.getSelectedItem().equals(selectedSupplier.getName())) {
                    allContractorProductsFilter.setContractorID(selectedSupplier.getId());
                    break;
                }
            }
        } else {
            allContractorProductsFilter.setContractorID(null);
        }

        // Refresh data
        allContractorProductsFilter.getDataLimit().setFirstResult(0);
        allContractorProducts.refreshData();

        // UI updating
        refreshDataNavigationPanel();
        scrollUpSearchResults();
        // Redefine default button
        defineDefaultButton();
    }

    /**
     * Scrolls the search results up.
     */
    private void scrollUpSearchResults() {
        try {
            ((JScrollPane) ((JPanel) allContractorProductsPanel.getComponent(0))
                    .getComponent(0)).getVerticalScrollBar().setValue(0);
        } catch (Exception e) {
            LoggingFacade.logError(e);
        }
    }

    /**
     * Resets filter by name in the data table.
     */
    private void resetDataTableFilters() {
        allContractorProductsTableReport.getReportTable().setFilteredText(0, null);
    }

    /**
     * Defines default button on this form.
     *
     * @see <code>JRootPane.setDefaultButton</code> for more information
     */
    private void defineDefaultButton() {
        JRootPane rootPane = getContentPanel().getRootPane();
        if (rootPane != null) {
            rootPane.setDefaultButton(search);
        }
    }

    private void onChangeSelectedPositions() {
        if (selectedPositions.isSelected()) {
            selectedContractorProductsFilter.setSelected(true);
            selectedContractorProducts.refreshData();
            showSelectedPositions();
        } else {
            hideSelectedPositions();
        }
    }

    private void onChangeConversionCurrency() {
        // Get selected currency
        Object selectedItem = DataExchange.getComboSelection(conversionCurrency);

        // Update prices in the list
        if (selectedItem instanceof CurrencyTO) {
            allContractorProducts.setConversionCurrency((CurrencyTO) selectedItem);
            selectedContractorProducts.setConversionCurrency((CurrencyTO) selectedItem);

            // Refresh exchange rate label
            if (defaultCurrency != null) {
                double exchangeRate = exchangeRateMap.get(((CurrencyTO) selectedItem).getId())
                        .get(defaultCurrency.getId());
                String exchangeRateLabelStr = StringUtils.buildString(exchangeRate, " ", defaultCurrency.getSign(),
                        " / ", ((CurrencyTO) selectedItem).getSign());
                exchangeRateLabel.setText(exchangeRateLabelStr);
            }
        } else {
            allContractorProducts.setConversionCurrency(null);
            selectedContractorProducts.setConversionCurrency(null);
            exchangeRateLabel.setText(null);
        }

        // Update data in the report table
        allContractorProducts.setLoadData(false);
        allContractorProducts.refreshData();
        if (selectedPositions.isSelected()) {
            selectedContractorProducts.setLoadData(false);
            selectedContractorProducts.refreshData();
        }
    }

    @SuppressWarnings("unchecked")
    private void onChangeExtraCharge() {
        String extraChargeStr = extraCharge.getText();

        // Update price in the list
        if (StringUtils.isNumberInteger(extraChargeStr)) {
            allContractorProducts.setExtraCharge(Integer.valueOf(extraChargeStr));
            selectedContractorProducts.setExtraCharge(Integer.valueOf(extraChargeStr));
        } else {
            allContractorProducts.setExtraCharge(0);
            selectedContractorProducts.setExtraCharge(0);
        }

        // Update data in the report table
        allContractorProducts.setLoadData(false);
        allContractorProducts.refreshData();
        if (selectedPositions.isSelected()) {
            selectedContractorProducts.setLoadData(false);
            selectedContractorProducts.refreshData();
        }
    }

    private void onPrevPositions() {
        allContractorProducts.getContractorProductList().toPrev();
        allContractorProducts.setLoadData(false);
        allContractorProducts.refreshData();
        refreshDataNavigationPanel();
    }

    private void onNextPositions() {
        allContractorProducts.getContractorProductList().toNext();
        allContractorProducts.setLoadData(false);
        allContractorProducts.refreshData();
        refreshDataNavigationPanel();
    }

    /**
     * Hides panel with selected positions.
     */
    private void hideSelectedPositions() {
        contractorProductListSplitPanel.getBottomComponent().setVisible(false);
        contractorProductListSplitPanel.setDividerSize(0);
    }

    /**
     * Shows panel with selected positions.
     */
    private void showSelectedPositions() {
        contractorProductListSplitPanel.getBottomComponent().setVisible(true);
        contractorProductListSplitPanel.setDividerSize(10);
        contractorProductListSplitPanel.setDividerLocation(0.5);
    }

    /**
     * Updates UI of the data navigation panel.
     */
    private void refreshDataNavigationPanel() {
        if (allContractorProducts.getContractorProductList().getAllElements().isEmpty()) {
            dataNavigationPanel.setVisible(false);
        } else {
            dataNavigationPanel.setVisible(true);
            prevPositions.setEnabled(!allContractorProducts.getContractorProductList().isBegin());
            nextPositions.setEnabled(!allContractorProducts.getContractorProductList().isEnd());
            String status = I18nSupport.message("contractor.product.list.loadedDataStatus",
                    allContractorProducts.getContractorProductList().from() + 1,
                    allContractorProducts.getContractorProductList().to(),
                    allContractorProducts.getContractorProductList().getAllElementsCount());
            loadedContractorProductsStatus.setText(status);
        }
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
        contentPanel.setLayout(new GridLayoutManager(3, 1, new Insets(3, 0, 2, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 6, new Insets(2, 2, 2, 2), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 4, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.list.search.panel.title")));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(350, -1), new Dimension(350, -1), 0, false));
        nameMask = new JTextField();
        nameMask.setEditable(true);
        panel3.add(nameMask, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.list.name.mask"));
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(125, -1), new Dimension(125, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        supplier = new JComboBox();
        panel4.add(supplier, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.list.supplier"));
        panel2.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel5, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel5.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        search = new JButton();
        this.$$$loadButtonText$$$(search, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.list.search"));
        panel5.add(search, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel2.add(spacer4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        ignoreSpecialSymbols = new JCheckBox();
        ignoreSpecialSymbols.setSelected(true);
        this.$$$loadButtonText$$$(ignoreSpecialSymbols, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.list.ignore.special.symbols"));
        panel2.add(ignoreSpecialSymbols, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 7, new Insets(0, 0, 4, 0), -1, -1));
        panel1.add(panel6, new GridConstraints(1, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.list.additionally.panel.title")));
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.list.extraCharge"));
        panel6.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(125, -1), new Dimension(125, -1), null, 0, false));
        extraCharge = new JTextField();
        panel6.add(extraCharge, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(70, -1), null, 0, false));
        selectedPositions = new JCheckBox();
        this.$$$loadButtonText$$$(selectedPositions, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.list.selectedPositions"));
        panel6.add(selectedPositions, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setHorizontalAlignment(4);
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.list.conversion.currency"));
        panel6.add(label4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel7, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        conversionCurrency = new JComboBox();
        panel7.add(conversionCurrency, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(70, -1), new Dimension(70, -1), 0, false));
        final Spacer spacer5 = new Spacer();
        panel7.add(spacer5, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        exchangeRateLabel = new JLabel();
        exchangeRateLabel.setText("");
        panel7.add(exchangeRateLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel6.add(spacer6, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        separator1.setOrientation(1);
        panel6.add(separator1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        contractorProductListPanel = new JPanel();
        contractorProductListPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(contractorProductListPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        contractorProductListSplitPanel = new JSplitPane();
        contractorProductListSplitPanel.setDividerSize(12);
        contractorProductListSplitPanel.setOrientation(0);
        contractorProductListPanel.add(contractorProductListSplitPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        allContractorProductsPanel = new JPanel();
        allContractorProductsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contractorProductListSplitPanel.setLeftComponent(allContractorProductsPanel);
        selectedContractorProductsPanel = new JPanel();
        selectedContractorProductsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contractorProductListSplitPanel.setRightComponent(selectedContractorProductsPanel);
        dataNavigationPanel = new JPanel();
        dataNavigationPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 5, 0), -1, -1));
        contentPanel.add(dataNavigationPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nextPositions = new JButton();
        nextPositions.setText(">>");
        dataNavigationPanel.add(nextPositions, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        prevPositions = new JButton();
        prevPositions.setText("<<");
        dataNavigationPanel.add(prevPositions, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadedContractorProductsStatus = new JLabel();
        loadedContractorProductsStatus.setText("");
        dataNavigationPanel.add(loadedContractorProductsStatus, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        label1.setLabelFor(nameMask);
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
