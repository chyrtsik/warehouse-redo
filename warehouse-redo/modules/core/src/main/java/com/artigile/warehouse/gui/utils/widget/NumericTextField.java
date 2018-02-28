/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.utils.widget;


import com.artigile.warehouse.utils.StringUtils;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.math.BigDecimal;

/**
 * @author IoaN, Dec 14, 2008
 */

/**
 * Text field, that allows only numbers to be entered into it.
 */
public class NumericTextField extends JTextField {
    /**
     * Defines if the text input field can contain only integers.
     */
    private boolean isIntegersOnly;

    /**
     * Defines, if numbers should be positive.
     */
    private boolean isPositiveOnly;

    //Add other constructors as required. If you do,
    //be sure to call the "addFilter" method
    public NumericTextField() {
        this(false, true);
    }

    public NumericTextField(boolean isIntegersOnly, boolean isPositiveOnly) {
        this.isIntegersOnly = isIntegersOnly;
        this.isPositiveOnly = isPositiveOnly;
        addFilter();
    }

    public Number getNumber() {
        if (getText().trim().equals("")) {
            return isIntegersOnly ? 0 : BigDecimal.ZERO;
        }
        else if (getText().trim().equals("-")){
            return isIntegersOnly ? 0 : BigDecimal.ZERO;
        }
        return isIntegersOnly ? new Integer(getText()) : new BigDecimal(getText());
    }

    //Add an instance of NumericDocumentFilter as a
    //document filter to the current text field
    private void addFilter() {
        ((AbstractDocument) this.getDocument()).
                setDocumentFilter(new NumericDocumentFilter());
    }

    class NumericDocumentFilter extends DocumentFilter {
        public void insertString(FilterBypass fb,
                                 int offset, String string, AttributeSet attr)
                throws BadLocationException {

            if (string == null) return;
            string=string.replace(",", ".");
            if (isStringNumeric(string)) {
                super.insertString(fb, offset, string, attr);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        public void replace(FilterBypass fb, int offset,
                            int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text == null) return;
            text=text.replace(",", ".");
            if (isStringNumeric(text)) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        private boolean isStringNumeric(String string) {
            if (string == null){
                return false;
            }
            if (isPositiveOnly){
                if (string.trim().substring(0, 0).equals("-")){
                    return false;
                }
            }
            else{
                if (string.trim().equals("-")){
                    return true;
                }
            }
            if (isIntegersOnly) {
                return StringUtils.isNumberLong(string);
            }
            else{
                return string.trim().equals(".") || StringUtils.isNumber(string);
            }
        }
    }
}