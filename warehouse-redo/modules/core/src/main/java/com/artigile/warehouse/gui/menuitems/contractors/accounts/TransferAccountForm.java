/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.contractors.accounts;

import com.artigile.warehouse.bl.finance.CurrencyExchangeService;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.AccountTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.ResourceBundle;

public class TransferAccountForm implements PropertiesForm {
    private JPanel contentPane;
    private JComboBox fieldFromAccount;
    private JComboBox fieldToAccount;
    private JTextField fieldCurrentBalanceFrom;
    private JTextField fieldCurrentBalanceTo;
    private JTextField fieldNewBalanceTo;
    private JTextField fieldNewBalanceFrom;
    private JTextField fieldOutcome;
    private JTextField fieldIncome;
    private JCheckBox fieldAllowEditRate;
    private JTextField fieldFromRate;
    private JTextField notice;

    private List<AccountTO> accountTOList;
    private AccountTO defaultFromAccount;
    private BigDecimal changeOfFromBalance;
    private BigDecimal changeOfToBalance;

    /**
     * The abs rounding context. Used to round the numbers
     */
    private static final MathContext mathContext = new MathContext(5, RoundingMode.HALF_DOWN);

    private CurrencyExchangeService currencyExchangeService = SpringServiceContext.getInstance().getCurencyExchangeService();

    public TransferAccountForm(List<AccountTO> accountTOList, AccountTO defaultFromAccount) {
        this.accountTOList = accountTOList;
        this.defaultFromAccount = defaultFromAccount;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(fieldOutcome, ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION);
        DataFiltering.setTextLengthLimit(fieldIncome, ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION);
        DataFiltering.setTextLengthLimit(notice, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        fieldCurrentBalanceFrom.setFocusable(false);
        fieldCurrentBalanceTo.setFocusable(false);
        fieldNewBalanceFrom.setFocusable(false);
        fieldNewBalanceTo.setFocusable(false);

        initCurrenciesCombo(fieldFromAccount, null);
        initListeners();
    }

    //=============================== PropertiesForm implementation ===============================

    @Override
    public String getTitle() {
        return I18nSupport.message("account.transfer.title");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPane;
    }

    @Override
    public boolean canSaveData() {
        return true;
    }

    @Override
    public void loadData() {
        DataExchange.selectComboItem(fieldFromAccount, defaultFromAccount);
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkSelected(fieldFromAccount);
        DataValidation.checkSelected(fieldToAccount);
        DataValidation.checkNotEmpty(fieldOutcome);
        DataValidation.checkIsNumber(fieldOutcome);
        DataValidation.checkNotEmpty(fieldIncome);
        DataValidation.checkIsNumber(fieldIncome);
        DataValidation.checkNotEmpty(fieldFromRate);
        DataValidation.checkIsPositiveNumber(fieldFromRate);
        DataValidation.checkNotEmpty(notice);
    }

    @Override
    public void saveData() {
        AccountTO fromAccount = getFromAccount();
        BigDecimal initialFromBalance = fromAccount.getCurrentBalance();
        BigDecimal newFromBalance = StringUtils.parseStringToBigDecimal(fieldNewBalanceFrom.getText());
        changeOfFromBalance = newFromBalance.subtract(initialFromBalance);
        fromAccount.setCurrentBalance(newFromBalance);

        AccountTO toAccount = getToAccount();
        BigDecimal initialToBalance = toAccount.getCurrentBalance();
        BigDecimal newToBalance = StringUtils.parseStringToBigDecimal(fieldNewBalanceTo.getText());
        changeOfToBalance = newToBalance.subtract(initialToBalance);
        toAccount.setCurrentBalance(newToBalance);
        toAccount.setNotice(notice.getText());
    }

    //============================= Form result getters =================================

    public AccountTO getToAccount() {
        return (AccountTO) DataExchange.getComboSelection(fieldToAccount);
    }

    public AccountTO getFromAccount() {
        return (AccountTO) DataExchange.getComboSelection(fieldFromAccount);
    }

    public BigDecimal getUsedExchangeRate() {
        return StringUtils.parseStringToBigDecimal(fieldFromRate.getText());
    }

    public BigDecimal getChangeOfToBalance() {
        return changeOfToBalance;
    }

    public BigDecimal getChangeOfFromBalance() {
        return changeOfFromBalance;
    }

    //============================== Helpers ============================================

    private void initCurrenciesCombo(JComboBox comboBox, AccountTO excludeAccount) {
        //Initializes combo box with list of accounts excluding given one.
        comboBox.removeAllItems();
        for (AccountTO account : accountTOList) {
            if (!account.equals(excludeAccount)) {
                comboBox.addItem(new ListItem(account.getCurrency().getSign(), account));
            }
        }
    }

    private void initListeners() {
        fieldFromAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initCurrenciesCombo(fieldToAccount, (AccountTO) DataExchange.getComboSelection(fieldFromAccount));
                onAccountChanged();
            }
        });
        fieldToAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAccountChanged();
            }
        });

        fieldOutcome.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateCounters(true);
            }
        });

        fieldOutcome.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                fieldOutcome.selectAll();
            }
        });

        fieldIncome.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateCounters(false);
            }
        });

        fieldIncome.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                fieldIncome.selectAll();
            }
        });

        fieldFromRate.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateCounters(true);
            }
        });

        fieldAllowEditRate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldFromRate.setEditable(fieldAllowEditRate.isSelected());
                if (!fieldAllowEditRate.isSelected()) {
                    BigDecimal defaultRate = getDefaultExchangeRate();
                    fieldFromRate.setText(defaultRate == null ? null : StringUtils.formatNumber(defaultRate));
                    updateCounters(true);
                }
            }
        });
    }

    private void onAccountChanged() {
        AccountTO fromAccount = (AccountTO) DataExchange.getComboSelection(fieldFromAccount);
        AccountTO toAccount = (AccountTO) DataExchange.getComboSelection(fieldToAccount);

        //1. First we should load accounts data into fields.
        if (fromAccount != null) {
            //fromCurrency.setText(fromAccount.getCurrency().getSign());
            fieldCurrentBalanceFrom.setText(StringUtils.formatNumber(fromAccount.getCurrentBalance()));
        }
        if (toAccount != null) {
            //toCurrency.setText(toAccount.getCurrency().getSign());
            fieldCurrentBalanceTo.setText(StringUtils.formatNumber(toAccount.getCurrentBalance()));
        }
        resetCalculatedFields();
        resetInputFields();

        if (fromAccount == null || toAccount == null) {
            return;
        }

        //2. Showing parameters, that depends on both currencies.
        BigDecimal defaultRate = getDefaultExchangeRate();
        fieldFromRate.setText(StringUtils.formatNumber(defaultRate));
    }

    private BigDecimal getDefaultExchangeRate() {
        AccountTO fromAccount = (AccountTO) DataExchange.getComboSelection(fieldFromAccount);
        AccountTO toAccount = (AccountTO) DataExchange.getComboSelection(fieldToAccount);
        if (fromAccount == null || toAccount == null) {
            return null;
        }
        return currencyExchangeService.getExchangeRate(fromAccount.getCurrency().getId(), toAccount.getCurrency().getId());
    }

    private void updateCounters(boolean isFirstCurrency) {
        //Recalculate result of transfer using values entered.
        //1. Checks if exchange rate is valid.
        if (!isValidExchangeRate(fieldFromRate.getText())) {
            resetCalculatedFields();
            return;
        }

        //2. Chooses if amout to achange is valid.
        BigDecimal rate = StringUtils.parseStringToBigDecimal(fieldFromRate.getText());
        BigDecimal sumToExchange;
        BigDecimal sumToExchangeInTargetCurrency;
        if (isFirstCurrency) {
            if (!isValidAmountToEchange(fieldOutcome.getText())) {
                resetCalculatedFields();
                return;
            }
            sumToExchange = StringUtils.parseStringToBigDecimal(fieldOutcome.getText());
            sumToExchangeInTargetCurrency = sumToExchange.multiply(rate, mathContext);
            fieldIncome.setText(StringUtils.formatNumber(sumToExchangeInTargetCurrency));
        } else {
            if (!isValidAmountToEchange(fieldIncome.getText())) {
                resetCalculatedFields();
                return;
            }
            sumToExchangeInTargetCurrency = StringUtils.parseStringToBigDecimal(fieldIncome.getText());
            sumToExchange = sumToExchangeInTargetCurrency.divide(rate, mathContext);
            fieldOutcome.setText(StringUtils.formatNumber(sumToExchange));
        }

        BigDecimal newToBalance = (StringUtils.parseStringToBigDecimal(fieldCurrentBalanceTo.getText())).add(sumToExchangeInTargetCurrency);
        BigDecimal newFromBalance = (StringUtils.parseStringToBigDecimal(fieldCurrentBalanceFrom.getText())).subtract(sumToExchange);
        fieldNewBalanceFrom.setText(StringUtils.formatNumber(newFromBalance));
        fieldNewBalanceTo.setText(StringUtils.formatNumber(newToBalance));
    }

    private boolean isValidAmountToEchange(String amountText) {
        //Amount for change should be positive og negative number,
        //Negative numbers may be used for exchanging debts. 
        return StringUtils.isNumber(amountText);
    }

    private boolean isValidExchangeRate(String rateText) {
        //Exchange rate should be a number greater than zero.
        if (!StringUtils.isNumber(rateText)) {
            return false;
        }
        BigDecimal rate = StringUtils.parseStringToBigDecimal(rateText);
        return rate.compareTo(BigDecimal.ZERO) > 0;
    }

    private void resetCalculatedFields() {
        fieldNewBalanceFrom.setText(null);
        fieldNewBalanceTo.setText(null);
    }

    private void resetInputFields() {
        fieldOutcome.setText(null);
        fieldIncome.setText(null);
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
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ResourceBundle.getBundle("i18n/warehouse").getString("currency.transfer.from.title")));
        fieldFromAccount = new JComboBox();
        panel2.add(fieldFromAccount, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        fieldCurrentBalanceFrom = new JTextField();
        fieldCurrentBalanceFrom.setEditable(false);
        panel2.add(fieldCurrentBalanceFrom, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("currency.transfer.available"));
        panel2.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldNewBalanceFrom = new JTextField();
        fieldNewBalanceFrom.setEditable(false);
        panel2.add(fieldNewBalanceFrom, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldOutcome = new JTextField();
        panel2.add(fieldOutcome, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("currency.transfer.new.balance"));
        panel2.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("currency.transfer.outcome"));
        panel2.add(label3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ResourceBundle.getBundle("i18n/warehouse").getString("currency.transfer.to.title")));
        fieldToAccount = new JComboBox();
        panel3.add(fieldToAccount, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        fieldCurrentBalanceTo = new JTextField();
        fieldCurrentBalanceTo.setEditable(false);
        panel3.add(fieldCurrentBalanceTo, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldNewBalanceTo = new JTextField();
        fieldNewBalanceTo.setEditable(false);
        panel3.add(fieldNewBalanceTo, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldIncome = new JTextField();
        panel3.add(fieldIncome, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("currency.transfer.available"));
        panel3.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("currency.transfer.new.balance"));
        panel3.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("currency.transfer.income"));
        panel3.add(label6, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldFromRate = new JTextField();
        fieldFromRate.setEditable(false);
        panel4.add(fieldFromRate, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 24), null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("currency.transfer.exchange"));
        panel4.add(label7, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(31, 24), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(100, -1), null, null, 0, false));
        notice = new JTextField();
        panel4.add(notice, new GridConstraints(1, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("i18n/warehouse").getString("currency.transfer.notice"));
        panel4.add(label8, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldAllowEditRate = new JCheckBox();
        this.$$$loadButtonText$$$(fieldAllowEditRate, ResourceBundle.getBundle("i18n/warehouse").getString("account.transfer.allow.to.enter.rate"));
        panel4.add(fieldAllowEditRate, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(117, 24), null, 0, false));
        final Spacer spacer3 = new Spacer();
        contentPane.add(spacer3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        label1.setLabelFor(fieldCurrentBalanceFrom);
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
