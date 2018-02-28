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

import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.AccountTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ResourceBundle;

public class PutOrWithdrawForm implements PropertiesForm {
    private JPanel contentPane;
    private JLabel operationName;
    private JLabel currencyName;
    private JTextField fieldCurrentBalance;
    private JTextField fieldChangeAmount;
    private JTextField fieldNewBalance;
    private JTextField notice;

    private boolean isPutOperation;

    private AccountTO accountTO;

    public PutOrWithdrawForm(boolean isPutOperation, AccountTO accountTO) {
        this.isPutOperation = isPutOperation;
        this.accountTO = accountTO;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(fieldChangeAmount, ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION);
        DataFiltering.setTextLengthLimit(notice, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        operationName.setText(isPutOperation ? I18nSupport.message("account.put") : I18nSupport.message("account.withdraw"));
        fieldCurrentBalance.setFocusable(false);
        fieldNewBalance.setFocusable(false);

        initListeners();
    }

    //========================== PropertiesForm implementation =============================

    @Override
    public String getTitle() {
        return isPutOperation ? I18nSupport.message("account.put.title") : I18nSupport.message("account.withdraw.title");
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
        fieldCurrentBalance.setText(StringUtils.formatNumber(accountTO.getCurrentBalance()));
        fieldNewBalance.setText(StringUtils.formatNumber(accountTO.getCurrentBalance()));
        fieldChangeAmount.setText(null);
        currencyName.setText(accountTO.getCurrency().getName());
        notice.setText(null);
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(fieldChangeAmount);
        DataValidation.checkIsPositiveNumber(fieldChangeAmount);
        DataValidation.checkNotEmpty(notice);
    }

    @Override
    public void saveData() {
        accountTO.setCurrentBalance(StringUtils.parseStringToBigDecimal(fieldNewBalance.getText()));
        accountTO.setNotice(notice.getText());
    }

    //========================== Helpers ====================================================

    private void initListeners() {
        fieldChangeAmount.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                //Recalculating new balance depending on value entered.
                if (StringUtils.isNumber(fieldChangeAmount.getText())) {
                    BigDecimal changeOfBalance = StringUtils.parseStringToBigDecimal(fieldChangeAmount.getText());
                    if (changeOfBalance.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal newBalance = isPutOperation
                                ? accountTO.getCurrentBalance().add(changeOfBalance)
                                : accountTO.getCurrentBalance().subtract(changeOfBalance);

                        fieldNewBalance.setText(StringUtils.formatNumber(newBalance));
                    } else {
                        fieldNewBalance.setText(null);
                    }
                } else {
                    fieldNewBalance.setText(null);
                }
            }
        });
        fieldChangeAmount.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                fieldChangeAmount.selectAll();
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("account.put.or.withdraw.currency"));
        contentPane.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currencyName = new JLabel();
        currencyName.setText("<currency>");
        contentPane.add(currencyName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("account.put.or.withdraw.current.balance"));
        contentPane.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("account.put.or.withdraw.new.balance"));
        contentPane.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        operationName = new JLabel();
        operationName.setText("<operation>");
        contentPane.add(operationName, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCurrentBalance = new JTextField();
        fieldCurrentBalance.setEditable(false);
        contentPane.add(fieldCurrentBalance, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldNewBalance = new JTextField();
        fieldNewBalance.setEditable(false);
        contentPane.add(fieldNewBalance, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldChangeAmount = new JTextField();
        contentPane.add(fieldChangeAmount, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        notice = new JTextField();
        contentPane.add(notice, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("account.put.or.withdraw.notice"));
        contentPane.add(label4, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
