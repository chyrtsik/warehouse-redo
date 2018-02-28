/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.orders;

import com.artigile.warehouse.bl.contractors.ContractorService;
import com.artigile.warehouse.bl.finance.CurrencyExchangeService;
import com.artigile.warehouse.bl.orders.OrderService;
import com.artigile.warehouse.domain.orders.OrderReservingType;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.init.InitUtils;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.browser.BrowseResult;
import com.artigile.warehouse.gui.menuitems.contractors.ContractorsList;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.AccountTO;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.LoadPlaceTO;
import com.artigile.warehouse.utils.dto.orders.OrderTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 07.01.2009
 */
public class OrderForm implements PropertiesForm {
    private JPanel contentPanel;
    private JTextField orderNumber;
    private JXDatePicker orderCreateDate;
    private JButton generateOrderNumber;
    private JTextField orderState;
    private JTextField orderContractor;
    private JButton browseContractor;
    private JComboBox orderLoadPlace;
    private JComboBox orderCurrency;
    private JTextField orderContractorBalance;
    private JTextField orderTotalPrice;
    private JTextField orderVatRate;
    private JTextField orderCreatedUser;
    private JXDatePicker orderLoadDate;
    private JLabel orderTotalPriceCurrency;
    private JTextArea orderNotice;
    private JComboBox orderReservingType;
    private JTextField orderTotalPriceWithVat;
    private JTextField orderVat;
    private JLabel orderTotalPriceWithVatCurrency;
    private JLabel orderVatCurrency;
    private JLabel orderContractorBalanceCurrency;

    private OrderTOForReport order;
    private boolean canEdit;

    private ContractorTO selectedContractor;
    
    private static final int VAT_RATE_MIN;
    private static final int VAT_RATE_MAX;

    /**
     * Define constants
     */
    static {
        Properties appProperties = SpringServiceContext.getInstance().getApplicationProperties();
        VAT_RATE_MIN = Integer.parseInt(appProperties.getProperty("vat.rate.min"));
        VAT_RATE_MAX = Integer.parseInt(appProperties.getProperty("vat.rate.max"));
    }

    public OrderForm(OrderTOForReport order, boolean canEdit) {
        this.order = order;
        this.canEdit = canEdit;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(orderNumber, ModelFieldsLengths.MAX_TEXT_LENGTH_LONG_VAR);
        DataFiltering.setTextLengthLimit(orderNotice, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(orderVatRate, 6);

        // Improve tabulation.
        orderState.setFocusable(false);
        orderCreatedUser.setFocusable(false);
        orderContractor.setFocusable(false);
        orderContractorBalance.setFocusable(false);
        orderTotalPrice.setFocusable(false);
        orderTotalPriceWithVat.setFocusable(false);
        orderVat.setFocusable(false);

        // Initialize other controls.
        InitUtils.initLoadPlacesCombo(orderLoadPlace, null);
        InitUtils.initCurrenciesCombo(orderCurrency, null);
        InitUtils.initComboFromEnumeration(orderReservingType, OrderReservingType.values(), null);
        orderCreateDate.setFormats(StringUtils.getDateFormat());
        orderLoadDate.setFormats(StringUtils.getDateFormat());

        initListeners();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("order.properties.title");
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
        orderNumber.setText(order.getNumber() == 0 ? null : Long.valueOf(order.getNumber()).toString());
        orderCreateDate.setDate(order.getCreateDate());
        orderCreatedUser.setText(order.getCreatedUser().getDisplayName());
        orderState.setText(order.getState().getName());
        orderContractor.setText(order.getContractor() == null ? null : order.getContractor().getName());
        orderLoadDate.setDate(order.getLoadDate());
        orderVatRate.setText(order.getVatRate() == null ? null : StringUtils.formatNumber(order.getVatRate()));
        orderNotice.setText(order.getNotice());
        DataExchange.selectComboItem(orderLoadPlace, order.getLoadPlace());
        DataExchange.selectComboItem(orderCurrency, order.getCurrency());
        DataExchange.selectComboItem(orderReservingType, order.getReservingType());

        selectedContractor = order.getContractor();
        refreshContractor(false);

        refreshForNewCurrency();
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(orderNumber);
        DataValidation.checkIsNumberLong(orderNumber.getText(), orderNumber);
        DataValidation.checkValueMinLong(Long.valueOf(orderNumber.getText()), orderNumber, 1);
        if (!getOrdersService().isUniqueOrderNumber(Long.valueOf(orderNumber.getText()), order.getId())) {
            DataValidation.failRes(orderNumber, "order.properties.number.already.exists");
        }
        DataValidation.checkNotNull(orderCreateDate.getDate(), orderCreateDate);
        DataValidation.checkNotEmpty(orderContractor);
        DataValidation.checkSelected(orderLoadPlace);
        DataValidation.checkSelected(orderCurrency);

        // Validate VAT rate
        if (!orderVatRate.getText().isEmpty()) {
            DataValidation.checkIsNumberInteger(orderVatRate.getText(), orderVatRate);
            DataValidation.checkValueRangeLong(Integer.valueOf(orderVatRate.getText()), orderVatRate,
                    VAT_RATE_MIN, VAT_RATE_MAX);
        }

        DataValidation.checkSelected(orderReservingType);
        Date loadDate = orderLoadDate.getDate();
        if (loadDate != null && loadDate.compareTo(orderCreateDate.getDate()) < 0) {
            boolean confirmLoadDate = MessageDialogs.showConfirm(
                    I18nSupport.message("order.properties.confirm.title"),
                    I18nSupport.message("order.properties.confirm.load.date.less.than.order.date"));
            if (!confirmLoadDate) {
                DataValidation.failSilent();
            }
        }
    }

    @Override
    public void saveData() {
        order.setNumber(Long.valueOf(orderNumber.getText()));
        order.setCreateDate(orderCreateDate.getDate());
        order.setContractor(selectedContractor);
        order.setLoadPlace((LoadPlaceTO) DataExchange.getComboSelection(orderLoadPlace));
        order.setLoadDate(orderLoadDate.getDate());
        order.setCurrency((CurrencyTO) DataExchange.getComboSelection(orderCurrency));
        order.setTotalPrice(StringUtils.parseStringToBigDecimal(orderTotalPrice.getText().isEmpty() ? "0" : orderTotalPrice.getText()));
        order.setVatRate(orderVatRate.getText().isEmpty() ? null : StringUtils.parseStringToBigDecimal(orderVatRate.getText()));
        order.setNotice(orderNotice.getText());
        order.setReservingType((OrderReservingType) DataExchange.getComboSelection(orderReservingType));
    }

    //============================== Helpers ===========================================
    private void refreshForNewCurrency() {
        refreshContractorBalance();
        refreshTotalPrice();
        refreshVatFields();
        refreshCurrencyLabels();
    }

    private void refreshTotalPrice() {
        CurrencyTO currency = (CurrencyTO) DataExchange.getComboSelection(orderCurrency);
        if (currency != null && order.getTotalPrice() != null && order.getCurrency() != null) {
            //Display price, converted to the current currency.
            CurrencyExchangeService exchangeService = SpringServiceContext.getInstance().getCurencyExchangeService();
            BigDecimal totalPrice = exchangeService.convert(currency.getId(), order.getCurrency().getId(), order.getTotalPrice());
            orderTotalPrice.setText(StringUtils.formatMoneyAmount(totalPrice));
        } else {
            orderTotalPrice.setText(null);
        }
    }

    private void refreshContractorBalance() {
        CurrencyTO currency = (CurrencyTO) DataExchange.getComboSelection(orderCurrency);
        if (currency != null && selectedContractor != null) {
            ContractorService contractorService = SpringServiceContext.getInstance().getContractorService();
            AccountTO account = contractorService.getAccountByContractorAndCurrency(selectedContractor.getId(), currency.getId());
            if (account != null) {
                orderContractorBalance.setText(StringUtils.formatNumber(account.getCurrentBalance()));
            }
        } else {
            orderContractorBalance.setText(null);
        }
    }

    private void refreshVatFields() {
        String totalPriceText = orderTotalPrice.getText();
        String vatRateText = orderVatRate.getText();
        if (totalPriceText.isEmpty() || !StringUtils.isNumber(totalPriceText) ||
                vatRateText.isEmpty() || !StringUtils.isNumber(vatRateText)) {
            //Cannot calculate VAT.
            orderVat.setText("");
            orderTotalPriceWithVat.setText("");
        } else {
            //Calculate VAT for preview.
            BigDecimal totalPrice = StringUtils.parseStringToBigDecimal(totalPriceText);
            BigDecimal vatRate = StringUtils.parseStringToBigDecimal(vatRateText);
            BigDecimal vat = totalPrice.multiply(vatRate).divide(BigDecimal.valueOf(100));
            orderVat.setText(StringUtils.formatMoneyAmount(vat));
            BigDecimal totalPriceWithVat = totalPrice.add(vat);
            orderTotalPriceWithVat.setText(StringUtils.formatMoneyAmount(totalPriceWithVat));
        }
    }

    private void refreshCurrencyLabels() {
        CurrencyTO currency = (CurrencyTO) DataExchange.getComboSelection(orderCurrency);
        orderContractorBalanceCurrency.setText(currency == null ? null : currency.getSign());
        orderTotalPriceCurrency.setText(currency == null ? null : currency.getSign());
        orderVatCurrency.setText(currency == null ? null : currency.getSign());
        orderTotalPriceWithVatCurrency.setText(currency == null ? null : currency.getSign());
    }

    private void onBrowseContractor() {
        //Show browser for choosing update.
        BrowseResult result = Dialogs.runBrowser(new ContractorsList());
        if (result.isOk()) {
            selectedContractor = (ContractorTO) result.getSelectedItems().get(0);
            refreshContractor(true);
        }
    }

    private void refreshContractor(boolean refreshDependentFields) {
        if (selectedContractor != null) {
            orderContractor.setText(selectedContractor.getName());
            if (refreshDependentFields) {
                DataExchange.selectComboItem(orderCurrency, selectedContractor.getDefaultCurrency());
                DataExchange.selectComboItem(orderLoadPlace, selectedContractor.getDefaultShippingAddress());
            }
        } else {
            orderContractor.setText(null);
            refreshContractorBalance();
        }
    }

    private OrderService getOrdersService() {
        return SpringServiceContext.getInstance().getOrdersService();
    }

    //========================= User input processing =================================
    private void initListeners() {
        generateOrderNumber.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (order.isNew()) {
                    orderNumber.setText(String.valueOf(getOrdersService().getNextAvailableOrderNumber()));
                }
            }
        });

        orderCurrency.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshForNewCurrency();
            }
        });

        browseContractor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBrowseContractor();
            }
        });

        orderVatRate.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                refreshVatFields();
            }
        });
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
        contentPanel.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.number"));
        panel1.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.state"));
        panel1.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        orderNumber = new JTextField();
        panel2.add(orderNumber, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        generateOrderNumber = new JButton();
        generateOrderNumber.setText("=");
        panel2.add(generateOrderNumber, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.createDate"));
        panel1.add(label3, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderState = new JTextField();
        orderState.setEditable(false);
        panel1.add(orderState, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        orderCreateDate = new JXDatePicker();
        orderCreateDate.setLightWeightPopupEnabled(true);
        panel1.add(orderCreateDate, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.createdUser"));
        panel1.add(label4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderCreatedUser = new JTextField();
        orderCreatedUser.setEditable(false);
        panel1.add(orderCreatedUser, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.contractor"));
        panel3.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        orderContractor = new JTextField();
        orderContractor.setEditable(false);
        panel4.add(orderContractor, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        browseContractor = new JButton();
        browseContractor.setText("...");
        panel4.add(browseContractor, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.notice"));
        panel5.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel5.add(scrollPane1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 50), null, null, 0, false));
        orderNotice = new JTextArea();
        scrollPane1.setViewportView(orderNotice);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel6, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.reservingType"));
        panel6.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderReservingType = new JComboBox();
        orderReservingType.setEditable(false);
        panel6.add(orderReservingType, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel7, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.loadPlace"));
        panel7.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderLoadPlace = new JComboBox();
        panel7.add(orderLoadPlace, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        this.$$$loadLabelText$$$(label9, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.currency"));
        panel7.add(label9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderCurrency = new JComboBox();
        panel7.add(orderCurrency, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        this.$$$loadLabelText$$$(label10, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.loadDate"));
        panel7.add(label10, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderLoadDate = new JXDatePicker();
        orderLoadDate.setLightWeightPopupEnabled(true);
        panel7.add(orderLoadDate, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        this.$$$loadLabelText$$$(label11, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.contractorBalance"));
        panel7.add(label11, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        this.$$$loadLabelText$$$(label12, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.totalPrice"));
        panel7.add(label12, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel8, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(200, -1), null, null, 0, false));
        orderTotalPrice = new JTextField();
        orderTotalPrice.setEditable(false);
        orderTotalPrice.setText("");
        panel8.add(orderTotalPrice, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        orderTotalPriceCurrency = new JLabel();
        orderTotalPriceCurrency.setText("<Curr>");
        panel8.add(orderTotalPriceCurrency, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        this.$$$loadLabelText$$$(label13, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.vatRate"));
        panel7.add(label13, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel9, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        orderVatRate = new JTextField();
        panel9.add(orderVatRate, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel9.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel10, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        orderContractorBalance = new JTextField();
        orderContractorBalance.setEditable(false);
        panel10.add(orderContractorBalance, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        orderContractorBalanceCurrency = new JLabel();
        orderContractorBalanceCurrency.setText("<Curr>");
        panel10.add(orderContractorBalanceCurrency, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        this.$$$loadLabelText$$$(label14, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.totalPriceWithVat"));
        panel7.add(label14, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel11, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(200, -1), null, null, 0, false));
        orderTotalPriceWithVat = new JTextField();
        orderTotalPriceWithVat.setEditable(false);
        orderTotalPriceWithVat.setText("");
        panel11.add(orderTotalPriceWithVat, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        orderTotalPriceWithVatCurrency = new JLabel();
        orderTotalPriceWithVatCurrency.setText("<Curr>");
        panel11.add(orderTotalPriceWithVatCurrency, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        this.$$$loadLabelText$$$(label15, ResourceBundle.getBundle("i18n/warehouse").getString("order.properties.vat"));
        panel7.add(label15, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel12, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(200, -1), null, null, 0, false));
        orderVat = new JTextField();
        orderVat.setEditable(false);
        orderVat.setText("");
        panel12.add(orderVat, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        orderVatCurrency = new JLabel();
        orderVatCurrency.setText("<Curr>");
        panel12.add(orderVatCurrency, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
