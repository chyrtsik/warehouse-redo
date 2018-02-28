/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.needs;

import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.init.ComboBoxFillOptions;
import com.artigile.warehouse.gui.core.properties.data.init.InitUtils;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.browser.BrowseResult;
import com.artigile.warehouse.gui.menuitems.contractors.ContractorsList;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.needs.WareNeedItemTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 28.02.2009
 */
public class WareNeedItemForm implements PropertiesForm {

    private JPanel contentPanel;

    private JTextField fieldNumber;

    private JTextField fieldState;

    private JTextField fieldName;

    private JTextField fieldMisc;

    private JTextField fieldCount;

    private JTextField fieldMaxPrice;

    private JComboBox fieldCurrency;

    private JTextField fieldCustomer;

    private JButton browseCustomer;

    private JTextArea fieldNotice;

    private JLabel fieldCountMeas;

    private JLabel fieldCurrencyLabel;

    private WareNeedItemTO wareNeedItem;

    private boolean canEdit;

    private ContractorTO selectedCustomer;

    public WareNeedItemForm(WareNeedItemTO wareNeedItem, boolean canEdit) {
        this.wareNeedItem = wareNeedItem;
        this.canEdit = canEdit;
        this.selectedCustomer = wareNeedItem.getCustomer();

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(fieldCount, ModelFieldsLengths.MAX_TEXT_LENGTH_LONG_VAR);
//        DataFiltering.setTextLengthLimit(fieldMinYear, ModelFieldsLengths.MAX_TEXT_LENGTH_YEAR);
        DataFiltering.setTextLengthLimit(fieldMaxPrice, ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION);
        DataFiltering.setTextLengthLimit(fieldMisc, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(fieldNotice, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(fieldName, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        InitUtils.initCurrenciesCombo(fieldCurrency, new ComboBoxFillOptions().setAddNotSelectedItem(true));
        initListeners();
        initFields();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("wareNeed.item.properties.title");
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
        fieldNumber.setText(wareNeedItem.getFullNumber() == null ? null : StringUtils.formatNumber(wareNeedItem.getFullNumber()));
        fieldState.setText(wareNeedItem.getState().getName());
        fieldName.setText(wareNeedItem.getNeedName());
        fieldMisc.setText(wareNeedItem.getNeedMisc());
        fieldCountMeas.setText(wareNeedItem.getDetailBatch() == null ? null : wareNeedItem.getDetailBatch().getCountMeas().getSign());
        fieldCount.setText(wareNeedItem.getCount() == null ? null : String.valueOf(wareNeedItem.getCount()));
//        fieldMinYear.setText(wareNeedItem.getMinYear() == null ? null : String.valueOf(wareNeedItem.getMinYear()));
        fieldMaxPrice.setText(StringUtils.formatNumber(wareNeedItem.getMaxPrice()));
        fieldCurrencyLabel.setText(wareNeedItem.getCurrency() == null ? null : wareNeedItem.getCurrency().getSign());
        DataExchange.selectComboItem(fieldCurrency, wareNeedItem.getCurrency());
        fieldCustomer.setText(selectedCustomer == null ? null : selectedCustomer.getName());
        fieldNotice.setText(wareNeedItem.getNotice());
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(fieldName);
        DataValidation.checkNotEmpty(fieldCount);
        DataValidation.checkIsNumberLong(fieldCount.getText(), fieldCount);
        DataValidation.checkIsPositiveNumber(fieldCount);
        DataValidation.checkIsNumberOrIsEmpty(fieldNumber.getText(), fieldNumber);
        DataValidation.checkIsNumberOrIsEmpty(fieldMaxPrice.getText(), fieldMaxPrice);
//        DataValidation.checkIsNumberOrIsEmpty(fieldMinYear.getText(), fieldMinYear);
        CurrencyTO currency = (CurrencyTO) DataExchange.getComboSelection(fieldCurrency);
        if (!fieldMaxPrice.getText().isEmpty()) {
            DataValidation.checkNotNull(currency, fieldCurrency, "validation.select.value");
        }
    }

    @Override
    public void saveData() {
        if (wareNeedItem.isText()) wareNeedItem.setName(fieldName.getText());
        if (wareNeedItem.isText()) wareNeedItem.setMisc(fieldMisc.getText());
        wareNeedItem.setCount(fieldCount.getText().isEmpty() ? null : Long.valueOf(fieldCount.getText()));
//        wareNeedItem.setMinYear(fieldMinYear.getText().isEmpty() ? null : Long.valueOf(fieldMinYear.getText()));
        wareNeedItem.setMaxPrice(fieldMaxPrice.getText().isEmpty() ? null : StringUtils.parseStringToBigDecimal(fieldMaxPrice.getText()));
        wareNeedItem.setCurrency((CurrencyTO) DataExchange.getComboSelection(fieldCurrency));
        wareNeedItem.setCustomer(selectedCustomer);
        wareNeedItem.setNotice(fieldNotice.getText());
    }

    //=================================== Helpers and user imput processing ======================================
    private void initListeners() {
        browseCustomer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //Show browser for choosing the customer.
                BrowseResult result = Dialogs.runBrowser(new ContractorsList());
                if (result.isOk()) {
                    selectedCustomer = (ContractorTO) result.getSelectedItems().get(0);
                    fieldCustomer.setText(selectedCustomer.getName());
                }
            }
        });

        fieldCurrency.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //Update form to refrect changing of currency.
                CurrencyTO currency = (CurrencyTO) DataExchange.getComboSelection(fieldCurrency);
                fieldCurrencyLabel.setText(currency == null ? null : currency.getSign());
            }
        });
    }

    private void initFields() {
        fieldNumber.setFocusable(false);
        fieldState.setFocusable(false);
        fieldCustomer.setFocusable(false);
        fieldName.setEditable(wareNeedItem.isText());
        fieldName.setFocusable(wareNeedItem.isText());
        fieldMisc.setEditable(wareNeedItem.isText());
        fieldMisc.setFocusable(wareNeedItem.isText());
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
        contentPanel.setLayout(new GridLayoutManager(7, 5, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("wareNeed.item.properties.name"));
        contentPanel.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldName = new JTextField();
        contentPanel.add(fieldName, new GridConstraints(1, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldNumber = new JTextField();
        fieldNumber.setEditable(false);
        contentPanel.add(fieldNumber, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("wareNeed.item.properties.number"));
        contentPanel.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("wareNeed.item.properties.misc"));
        contentPanel.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldMisc = new JTextField();
        contentPanel.add(fieldMisc, new GridConstraints(2, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("wareNeed.item.properties.count"));
        contentPanel.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldState = new JTextField();
        fieldState.setEditable(false);
        contentPanel.add(fieldState, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("wareNeed.item.properties.state"));
        contentPanel.add(label5, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("wareNeed.item.properties.maxPrice"));
        contentPanel.add(label6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCurrency = new JComboBox();
        contentPanel.add(fieldCurrency, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("wareNeed.item.properties.customer"));
        contentPanel.add(label7, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(5, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldCustomer = new JTextField();
        fieldCustomer.setEditable(false);
        panel1.add(fieldCustomer, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        browseCustomer = new JButton();
        browseCustomer.setText("...");
        panel1.add(browseCustomer, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("i18n/warehouse").getString("wareNeed.item.properties.notice"));
        label8.setVerticalAlignment(0);
        contentPanel.add(label8, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPanel.add(scrollPane1, new GridConstraints(6, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 50), null, null, 0, false));
        fieldNotice = new JTextArea();
        scrollPane1.setViewportView(fieldNotice);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel2, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldCountMeas = new JLabel();
        fieldCountMeas.setText("<meas>");
        panel2.add(fieldCountMeas, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCount = new JTextField();
        panel2.add(fieldCount, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel3, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldCurrencyLabel = new JLabel();
        fieldCurrencyLabel.setText("<currency>");
        panel3.add(fieldCurrencyLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(10, -1), null, null, 0, false));
        fieldMaxPrice = new JTextField();
        panel3.add(fieldMaxPrice, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
