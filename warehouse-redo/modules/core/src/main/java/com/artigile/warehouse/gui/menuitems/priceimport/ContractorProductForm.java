/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.priceimport;

import com.artigile.warehouse.bl.priceimport.ContractorProductSourceDataParser;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.init.InitUtils;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.menuitems.contractors.ContractorRatingList;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.priceimport.ContractorProductTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.math.MathUtils;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Valery Barysok, 6/5/11
 */

public class ContractorProductForm implements PropertiesForm {
    private JPanel contentPane;
    private JTextField name;
    private JTextField year;
    private JTextField quantity;
    private JTextField discount;
    private JTextField wholesalePrice;
    private JTextField retailPrice;
    private JComboBox currency;
    private JTextField seller;
    private JRadioButton fieldRating1;
    private JRadioButton fieldRating4;
    private JRadioButton fieldRating2;
    private JRadioButton fieldRating3;
    private JRadioButton fieldRating5;
    private JRadioButton fieldRatingUnknown;
    private JTextField postingDate;
    private JTextArea description;
    private JComboBox measureUnit;
    private JTextField wholesaleDiscountPrice;
    private JTextField retailDiscountPrice;
    private JPanel sourceDataPanel;

    private ContractorProductTOForReport contractorProductTO;
    private boolean canEdit;


    public ContractorProductForm(ContractorProductTOForReport contractorProductTO, boolean canEdit) {
        this.contractorProductTO = contractorProductTO;
        this.canEdit = canEdit;

        $$$setupUI$$$();
        InitUtils.initCurrenciesCombo(currency, null);
        InitUtils.initMeasuresCombo(measureUnit, null);
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("contractor.product.properties.title");
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
        name.setText(contractorProductTO.getName());
        year.setText(contractorProductTO.getYear());
        quantity.setText(contractorProductTO.getQuantity().getValue());

        int discountInt = contractorProductTO.getPriceImport().getContractor().getDiscount();
        discount.setText(String.valueOf(discountInt));

        String wholesalePriceStr = contractorProductTO.getWholesalePrice().getValue();
        wholesalePrice.setText(wholesalePriceStr);
        wholesalePriceStr = StringUtils.parseNumber(wholesalePriceStr);
        if (!wholesalePriceStr.isEmpty()) {
            wholesaleDiscountPrice.setText(String.valueOf(MathUtils.calculateSimpleDiscountPrice(Double.valueOf(wholesalePriceStr), discountInt)));
        }

        String retailPriceStr = contractorProductTO.getRetailPrice().getValue();
        retailPrice.setText(retailPriceStr);
        retailPriceStr = StringUtils.parseNumber(retailPriceStr);
        if (!retailPriceStr.isEmpty()) {
            retailDiscountPrice.setText(String.valueOf(MathUtils.calculateSimpleDiscountPrice(Double.valueOf(retailPriceStr), discountInt)));
        }

        DataExchange.selectComboItem(currency, contractorProductTO.getCurrency());
        DataExchange.selectComboItem(measureUnit, contractorProductTO.getMeasureUnit());
        ContractorTO contractorTO = contractorProductTO.getPriceImport().getContractor();
        seller.setText(contractorTO.getName());

        if (contractorTO.getRating() == null)
            fieldRatingUnknown.setSelected(true);
        else if (contractorTO.getRating().equals(ContractorRatingList.RATING_1.getContractorRating()))
            fieldRating1.setSelected(true);
        else if (contractorTO.getRating().equals(ContractorRatingList.RATING_2.getContractorRating()))
            fieldRating2.setSelected(true);
        else if (contractorTO.getRating().equals(ContractorRatingList.RATING_3.getContractorRating()))
            fieldRating3.setSelected(true);
        else if (contractorTO.getRating().equals(ContractorRatingList.RATING_4.getContractorRating()))
            fieldRating4.setSelected(true);
        else if (contractorTO.getRating().equals(ContractorRatingList.RATING_5.getContractorRating()))
            fieldRating5.setSelected(true);

        postingDate.setText(StringUtils.formatDateTime(contractorProductTO.getPostingDate()));
        description.setText(contractorProductTO.getDescription());

        loadSourceData();
    }

    /**
     * Loads source data for this product and initializes required components.
     */
    private void loadSourceData() {
        // Load source data for this product
        String sourceDataStr = contractorProductTO.getSourceData();
        if (StringUtils.containsSymbols(sourceDataStr)) {
            // Parse source data
            Map<String, String> sourceDataMap = ContractorProductSourceDataParser.parse(sourceDataStr);

            // Prepare required parameters to rendering data table
            String[] columnNames = new String[sourceDataMap.size()];
            Object[][] rowData = new Object[1][sourceDataMap.size()];
            int index = 0;
            for (Map.Entry<String, String> pair : sourceDataMap.entrySet()) {
                String columnName = pair.getKey();
                columnNames[index] = columnName.substring(columnName.indexOf(":") + 1);
                rowData[0][index] = pair.getValue();
                index++;
            }
            addSourceDataTable(columnNames, rowData);
            sourceDataPanel.setVisible(true);
        } else {
            sourceDataPanel.setVisible(false);
        }
    }

    private void addSourceDataTable(String[] columnNames, Object[][] rowData) {
        // Create scrollable JTable using obtained parameters
        JTable sourceDataTable = new JTable(rowData, columnNames);
        sourceDataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        sourceDataTable.setCellSelectionEnabled(false);
        sourceDataTable.setGridColor(Color.WHITE);

        JScrollPane sourceDataScrollPane = new JScrollPane(sourceDataTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        sourceDataScrollPane.setPreferredSize(new Dimension(-1, 60));

        sourceDataPanel.add(sourceDataScrollPane, GridLayoutUtils.getGrowingAndFillingCellConstraints());
    }

    @Override
    public void validateData() throws DataValidationException {
        if (!year.getText().isEmpty()) {
            DataValidation.checkIsNumberInteger(year.getText(), year);
        }
        DataValidation.checkNotEmpty(quantity);
        DataValidation.checkNotEmpty(wholesalePrice);
        DataValidation.checkNotEmpty(retailPrice);
        DataValidation.checkSelected(currency);
        DataValidation.checkSelected(measureUnit);
    }

    @Override
    public void saveData() {
        contractorProductTO.setName(name.getText());
        contractorProductTO.setYear(year.getText());
        contractorProductTO.setDescription(description.getText());
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
        contentPane.setLayout(new GridLayoutManager(15, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.properties.name"));
        contentPane.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.properties.year"));
        contentPane.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.properties.quantity"));
        contentPane.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.properties.wholesale.price"));
        contentPane.add(label4, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.properties.retail.price"));
        contentPane.add(label5, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.properties.currency"));
        contentPane.add(label6, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.properties.price.import.contractor.name"));
        contentPane.add(label7, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.properties.price.import.contractor.rating"));
        contentPane.add(label8, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        name = new JTextField();
        name.setEditable(false);
        contentPane.add(name, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        year = new JTextField();
        year.setEditable(false);
        contentPane.add(year, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        quantity = new JTextField();
        quantity.setEditable(false);
        contentPane.add(quantity, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        wholesalePrice = new JTextField();
        wholesalePrice.setEditable(false);
        contentPane.add(wholesalePrice, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        retailPrice = new JTextField();
        retailPrice.setEditable(false);
        contentPane.add(retailPrice, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        currency = new JComboBox();
        currency.setEnabled(false);
        contentPane.add(currency, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        seller = new JTextField();
        seller.setEditable(false);
        contentPane.add(seller, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setEnabled(true);
        contentPane.add(panel1, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldRating1 = new JRadioButton();
        fieldRating1.setEnabled(false);
        fieldRating1.setText("1");
        panel1.add(fieldRating1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldRating4 = new JRadioButton();
        fieldRating4.setEnabled(false);
        fieldRating4.setText("4");
        panel1.add(fieldRating4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldRating2 = new JRadioButton();
        fieldRating2.setEnabled(false);
        fieldRating2.setText("2");
        panel1.add(fieldRating2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldRating3 = new JRadioButton();
        fieldRating3.setEnabled(false);
        fieldRating3.setText("3");
        panel1.add(fieldRating3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldRating5 = new JRadioButton();
        fieldRating5.setEnabled(false);
        fieldRating5.setText("5");
        panel1.add(fieldRating5, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldRatingUnknown = new JRadioButton();
        fieldRatingUnknown.setEnabled(false);
        this.$$$loadButtonText$$$(fieldRatingUnknown, ResourceBundle.getBundle("i18n/warehouse").getString("contractors.properties.radioButtonText.unknownRating"));
        panel1.add(fieldRatingUnknown, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        postingDate = new JTextField();
        postingDate.setEditable(false);
        contentPane.add(postingDate, new GridConstraints(12, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label9 = new JLabel();
        this.$$$loadLabelText$$$(label9, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.properties.posting.date"));
        contentPane.add(label9, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        this.$$$loadLabelText$$$(label10, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.properties.description"));
        contentPane.add(label10, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPane.add(scrollPane1, new GridConstraints(13, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 60), null, 0, false));
        description = new JTextArea();
        scrollPane1.setViewportView(description);
        final JLabel label11 = new JLabel();
        this.$$$loadLabelText$$$(label11, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.properties.measure.unit"));
        contentPane.add(label11, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        measureUnit = new JComboBox();
        measureUnit.setEnabled(false);
        contentPane.add(measureUnit, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        this.$$$loadLabelText$$$(label12, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.properties.discount"));
        contentPane.add(label12, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        discount = new JTextField();
        discount.setEditable(false);
        contentPane.add(discount, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label13 = new JLabel();
        this.$$$loadLabelText$$$(label13, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.properties.wholesale.price.discount"));
        contentPane.add(label13, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        this.$$$loadLabelText$$$(label14, ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.properties.retail.price.discount"));
        contentPane.add(label14, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        wholesaleDiscountPrice = new JTextField();
        wholesaleDiscountPrice.setEditable(false);
        contentPane.add(wholesaleDiscountPrice, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        retailDiscountPrice = new JTextField();
        retailDiscountPrice.setEditable(false);
        contentPane.add(retailDiscountPrice, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        sourceDataPanel = new JPanel();
        sourceDataPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(sourceDataPanel, new GridConstraints(14, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        sourceDataPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ResourceBundle.getBundle("i18n/warehouse").getString("contractor.product.properties.source.data.title")));
        label11.setLabelFor(measureUnit);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(fieldRating2);
        buttonGroup.add(fieldRating2);
        buttonGroup.add(fieldRatingUnknown);
        buttonGroup.add(fieldRating1);
        buttonGroup.add(fieldRating5);
        buttonGroup.add(fieldRating4);
        buttonGroup.add(fieldRating3);
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
        return contentPane;
    }
}
