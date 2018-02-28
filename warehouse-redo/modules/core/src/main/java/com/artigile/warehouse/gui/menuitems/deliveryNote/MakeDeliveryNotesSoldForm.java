/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.deliveryNote;

import com.artigile.warehouse.bl.contractors.ContractorService;
import com.artigile.warehouse.bl.finance.CurrencyExchangeService;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.init.InitUtils;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.utils.UIComponentUtils;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 11.03.2010
 */
public class MakeDeliveryNotesSoldForm implements PropertiesForm {
    private JPanel contentPanel;
    private JTextField fieldContractor;
    private JTable fieldDeliveryNotes;
    private JTextField fieldTotalPrice;
    private JTextField fieldBalance;
    private JTextField fieldExpectedPayment;
    private JTextField fieldMadePayment;
    private JComboBox fieldCurrency;
    private JLabel labelTotalCurrency;
    private JLabel labelBalanceCurrency;
    private JLabel labelExpectedPaymentCurrency;
    private JLabel labelMadePaymentCyrrency;
    private JLabel labelNewBalanceCurrency;
    private JTextField fieldNewBalance;
    private JTextArea fieldNotice;

    /**
     * List of delivery notes, that are to be payed for.
     */
    private List<DeliveryNoteTOForReport> deliveryNotes;

    /**
     * Contractor, from shom payment will come.
     */
    private ContractorTO contractor;

    /**
     * Payment, expected from contractor (in chosen currency).
     */
    private BigDecimal expectedPayment;

    /**
     * Payment, made by a contractor.
     */
    private BigDecimal madePayment;

    /**
     * Currency chosen for the payment.
     */
    private CurrencyTO paymentCurrency;

    /**
     * Notice for the payment, entered by user.
     */
    private String notice;

    //============================= Contruction and resulting getters ===============================

    public MakeDeliveryNotesSoldForm(List<DeliveryNoteTOForReport> deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
        this.contractor = deliveryNotes.get(0).getContractor();

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(fieldMadePayment, ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION);
        DataFiltering.setTextLengthLimit(fieldNotice, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        initControls();
    }

    public BigDecimal getMadePayment() {
        return madePayment;
    }

    public CurrencyTO getPaymentCurrency() {
        return paymentCurrency;
    }

    public String getNotice() {
        return notice;
    }

    public ContractorTO getContractor() {
        return contractor;
    }

    //========================= PropertiesForm interface ==============================
    @Override
    public String getTitle() {
        return I18nSupport.message("deliveryNote.makeSoldForm.title");
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
        fieldContractor.setText(contractor.getName());
        DataExchange.selectComboItem(fieldCurrency, getCommonDeliveryNotesCurrecy());
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkSelected(fieldCurrency);
        DataValidation.checkIsNumber(fieldMadePayment.getText(), fieldMadePayment);
        BigDecimal madePayment = StringUtils.parseStringToBigDecimal(fieldMadePayment.getText());
        if (madePayment.compareTo(BigDecimal.ZERO) < 0) {
            DataValidation.failRes("deliveryNote.makeSoldForm.validation.paymentShouldBeCreaterThanZero");
        }
    }

    @Override
    public void saveData() {
        madePayment = StringUtils.parseStringToBigDecimal(fieldMadePayment.getText());
        paymentCurrency = (CurrencyTO) DataExchange.getComboSelection(fieldCurrency);
        notice = fieldNotice.getText();
    }

    //=================================== Helpers ===================================

    private CurrencyTO getCommonDeliveryNotesCurrecy() {
        //Finds a currency, that is a common for all delivery notes.
        CurrencyTO currency = null;
        for (DeliveryNoteTOForReport deliveryNote : deliveryNotes) {
            if (currency == null) {
                currency = deliveryNote.getCurrency();
            } else if (!currency.equals(deliveryNote.getCurrency())) {
                return null;
            }
        }
        return currency;
    }

    private void initControls() {
        initListeners();

        InitUtils.initCurrenciesCombo(fieldCurrency, null);

        fieldDeliveryNotes.addColumn(new TableColumn(0));
        fieldDeliveryNotes.addColumn(new TableColumn(1));
    }

    private void initListeners() {
        fieldCurrency.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCurrencyChanged();
            }
        });

        fieldMadePayment.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                refreshNewBalance();
            }
        });
    }

    private void onCurrencyChanged() {
        refreshCurrencyLabels();
        refreshPriceFields();
        UIComponentUtils.packDialog(contentPanel);
    }

    private void refreshCurrencyLabels() {
        CurrencyTO currency = (CurrencyTO) DataExchange.getComboSelection(fieldCurrency);
        String currencySign = currency == null ? "" : currency.getSign();
        labelTotalCurrency.setText(currencySign);
        labelBalanceCurrency.setText(currencySign);
        labelExpectedPaymentCurrency.setText(currencySign);
        labelMadePaymentCyrrency.setText(currencySign);
        labelNewBalanceCurrency.setText(currencySign);
    }

    private void refreshPriceFields() {
        CurrencyTO currency = (CurrencyTO) DataExchange.getComboSelection(fieldCurrency);
        if (currency == null) {
            //Currency is not selected. So, there is nothing to show.
            clearPriceFields();
            return;
        }

        //Calculate and show prices in currency selected.
        List<BigDecimal> deliveryNotePrices = calculateDeliveryNotePrices(currency.getId());
        fieldDeliveryNotes.setModel(createDeliveryNotesModel(deliveryNotePrices));

        BigDecimal totalPrice = calculateTotalPrice(deliveryNotePrices);
        fieldTotalPrice.setText(StringUtils.formatNumber(totalPrice));

        BigDecimal contractorBalance = getContractorBalance(currency.getId());
        fieldBalance.setText(StringUtils.formatNumber(contractorBalance));

        expectedPayment = totalPrice.subtract(contractorBalance);
        fieldExpectedPayment.setText(StringUtils.formatNumber(expectedPayment));

        refreshNewBalance();
    }

    private void refreshNewBalance() {
        //Refreshing made payment field and fields, depending on it.
        if (StringUtils.isNumber(fieldMadePayment.getText())) {
            BigDecimal madePayment = StringUtils.parseStringToBigDecimal(fieldMadePayment.getText());
            fieldNewBalance.setText(StringUtils.formatNumber(madePayment.subtract(expectedPayment)));
        } else {
            fieldNewBalance.setText("");
        }
    }

    private TableModel createDeliveryNotesModel(List<BigDecimal> deliveryNotePrices) {
        //Creating table model for delivery notes table.
        String columns[] = new String[]{
            I18nSupport.message("deliveryNote.makeSoldForm.column.deliveryNoteNumber"),
            I18nSupport.message("deliveryNote.makeSoldForm.column.deliveryNotePrice"),
        };

        int rowCount = deliveryNotes.size();
        Object rows[][] = new Object[rowCount][2];
        for (int i = 0; i < rowCount; i++) {
            rows[i][0] = deliveryNotes.get(i).getNumber();
            if (deliveryNotePrices == null) {
                rows[i][1] = I18nSupport.message("deliveryNote.makeSoldForm.chooseCurrency");
            } else {
                rows[i][1] = StringUtils.formatNumber(deliveryNotePrices.get(i));
            }
        }

        return new DefaultTableModel(rows, columns);
    }


    public BigDecimal getContractorBalance(long currencyId) {
        //Loads current contractor balance in the currency specified.
        ContractorService contractorService = SpringServiceContext.getInstance().getContractorService();
        return contractorService.getAccountByContractorAndCurrency(contractor.getId(), currencyId).getCurrentBalance();
    }

    private BigDecimal calculateTotalPrice(List<BigDecimal> prices) {
        //Calculates total prices of given.
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (BigDecimal price : prices) {
            totalPrice = totalPrice.add(price);
        }
        return totalPrice;
    }

    private List<BigDecimal> calculateDeliveryNotePrices(long toCurrencyId) {
        //Calculating delivery note prices.
        CurrencyExchangeService exchangeService = SpringServiceContext.getInstance().getCurencyExchangeService();
        List<BigDecimal> deliveryNotePrices = new ArrayList<BigDecimal>();
        for (DeliveryNoteTOForReport deliveryNote : deliveryNotes) {
            long fromCurrencyId = deliveryNote.getCurrency().getId();
            if (fromCurrencyId != toCurrencyId) {
                BigDecimal price = exchangeService.convert(toCurrencyId, fromCurrencyId, deliveryNote.getTotalPrice());
                deliveryNotePrices.add(price);
            } else {
                deliveryNotePrices.add(deliveryNote.getTotalPrice());
            }
        }
        return deliveryNotePrices;
    }

    private void clearPriceFields() {
        fieldDeliveryNotes.setModel(createDeliveryNotesModel(null));
        fieldTotalPrice.setText("");
        fieldBalance.setText("");
        fieldExpectedPayment.setText("");
        fieldNewBalance.setText("");
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
        contentPanel.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("deliveryNote.makeSoldForm.contractor"));
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldContractor = new JTextField();
        fieldContractor.setEditable(false);
        panel1.add(fieldContractor, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("deliveryNote.makeSoldForm.totalPrice"));
        panel2.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("deliveryNote.makeSoldForm.currentBalance"));
        panel2.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("deliveryNote.makeSoldForm.expectedPayment"));
        panel2.add(label4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldBalance = new JTextField();
        fieldBalance.setEditable(false);
        panel3.add(fieldBalance, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        labelBalanceCurrency = new JLabel();
        labelBalanceCurrency.setText("<cur>");
        panel3.add(labelBalanceCurrency, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldExpectedPayment = new JTextField();
        fieldExpectedPayment.setEditable(false);
        panel4.add(fieldExpectedPayment, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        labelExpectedPaymentCurrency = new JLabel();
        labelExpectedPaymentCurrency.setText("<cur>");
        panel4.add(labelExpectedPaymentCurrency, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldTotalPrice = new JTextField();
        fieldTotalPrice.setEditable(false);
        panel5.add(fieldTotalPrice, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        fieldCurrency = new JComboBox();
        panel5.add(fieldCurrency, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelTotalCurrency = new JLabel();
        labelTotalCurrency.setText("<cur>");
        panel5.add(labelTotalCurrency, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        contentPanel.add(separator1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("deliveryNote.makeSoldForm.madePayment"));
        panel6.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel7, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldMadePayment = new JTextField();
        panel7.add(fieldMadePayment, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        labelMadePaymentCyrrency = new JLabel();
        labelMadePaymentCyrrency.setText("<cur>");
        panel7.add(labelMadePaymentCyrrency, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("deliveryNote.makeSoldForm.newBalance"));
        panel6.add(label6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel8, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldNewBalance = new JTextField();
        fieldNewBalance.setEditable(false);
        panel8.add(fieldNewBalance, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        labelNewBalanceCurrency = new JLabel();
        labelNewBalanceCurrency.setText("<cur>");
        panel8.add(labelNewBalanceCurrency, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPanel.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 100), null, null, 0, false));
        fieldDeliveryNotes = new JTable();
        fieldDeliveryNotes.setPreferredScrollableViewportSize(new Dimension(250, 150));
        scrollPane1.setViewportView(fieldDeliveryNotes);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel9, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("deliveryNote.makeSoldForm.notice"));
        panel9.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel9.add(scrollPane2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 30), new Dimension(-1, 50), null, 0, false));
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
