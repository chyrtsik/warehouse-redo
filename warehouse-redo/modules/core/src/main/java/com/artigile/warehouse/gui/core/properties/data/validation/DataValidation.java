/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.data.validation;

import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import org.jetbrains.annotations.PropertyKey;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * @author Shyrik, 07.12.2008
 */

/**
 * Utils class for data validation. Contains common checks of the data.
 */
public final class DataValidation {
    /**
     * Use this method to indicate fail of data validation.
     *
     * @param component
     * @param message
     * @throws DataValidationException
     */
    public static void fail(JComponent component, String message) throws DataValidationException {
        throw new DataValidationException(component, message);
    }

    public static void fail(String message) throws DataValidationException {
        throw new DataValidationException(message);
    }

    /**
     * Fails data validation with no error shown.
     * @throws DataValidationException
     */
    public static void failSilent() throws DataValidationException {
        throw new SilentDataValidationException();
    }

    /**
     * Simular to the fail, by gets a resource id of the message string.
     *
     * @param component
     * @param key       - resource key of the message.
     * @param args      - optional list of aguments, substituted into the message.
     * @throws DataValidationException
     */
    public static void failRes(JComponent component, @PropertyKey(resourceBundle = "i18n.warehouse") String key,
                               Object... args) throws DataValidationException {
        fail(component, I18nSupport.message(key, args));
    }

    public static void failRes(@PropertyKey(resourceBundle = "i18n.warehouse") String key,
                               Object... args) throws DataValidationException {
        fail(I18nSupport.message(key, args));
    }

    /**
     * Validates boolean condition. is condition is false, data validation fails.
     *
     * @param condition
     * @param component
     * @param failMessageKey
     * @throws DataValidationException
     */
    public static void checkCondition(boolean condition, JComponent component, @PropertyKey(resourceBundle = "i18n.warehouse") String failMessageKey) throws DataValidationException {
        if (!condition) {
            failRes(component, failMessageKey);
        }
    }

    /**
     * Use this method to check if the value has beed entered into text box by user.
     *
     * @param textComponent
     * @throws DataValidationException
     */
    public static void checkNotEmpty(JTextComponent textComponent) throws DataValidationException {
        checkNotEmpty(textComponent.getText(), textComponent);
    }

    /**
     * Use this method to check if the value is entered by user.
     *
     * @param value
     * @param component
     * @throws DataValidationException
     */
    public static void checkNotEmpty(String value, JComponent component) throws DataValidationException {
        if (value == null || value.isEmpty()) {
            failRes(component, "validation.enter.value");
        }
    }

    /**
     * Use this method to check minimal length of string.
     *
     * @param value
     * @param minLength
     * @param component
     * @throws DataValidationException
     */
    public static void checkMinLength(String value, int minLength, JComponent component) throws DataValidationException {
        if ((value == null && minLength > 0) || (value != null && value.length() < minLength)) {
            failRes(component, "validation.min.length", minLength);
        }
    }

    /**
     * Use this method to check maximal length of string.
     *
     * @param value
     * @param maxLength
     * @param component
     * @throws DataValidationException
     */
    public static void checkMaxLength(String value, int maxLength, JComponent component) throws DataValidationException {
        if (value.length() > maxLength) {
            failRes(component, "validation.max.length", maxLength);
        }
    }

    /**
     * Use this method to check range length of string.
     *
     * @param value
     * @param minLength
     * @param maxLength
     * @param component
     * @throws DataValidationException
     */
    public static void checkRangeLength(String value, int minLength, int maxLength, JComponent component) throws DataValidationException {
        checkMinLength(value, minLength, component);
        checkMaxLength(value, maxLength, component);
    }

    /**
     * Checks if the text1 is equals text2 and shows baloon message near  {@code component}
      * @param text1 value 1 to compare with value 2
     * @param text2 value 2 to compare with value 1
     * @param component the component near what message ballon will appear.
     */
    public static void checkEquals(String text1, String text2,JComponent component) throws DataValidationException {
         if (text1==null?text2==null:!text1.equals(text2)) {
            failRes(component, "validation.passwords.not.equals");
        }
    }

    /**
     * Use this method to check value not to be null.
     *
     * @param value
     * @param component
     */
    public static void checkNotNull(Object value, JComponent component) throws DataValidationException {
        checkNotNull(value, component, "validation.enter.value");
    }

    /**
     * Use this method to check value not to be null.
     *
     * @param value
     * @param component
     */
    public static void checkNotNull(Object value, JComponent component, @PropertyKey(resourceBundle = "i18n.warehouse") String key) throws DataValidationException {
        if (value == null) {
            failRes(component, key);
        }
    }

    /**
     * Checks, if there are selected item in specified combo box.
     *
     * @param comboBox
     */
    public static void checkSelected(JComboBox comboBox) throws DataValidationException {
        if (comboBox.getSelectedIndex() == -1) {
            failRes(comboBox, "validation.select.value");
        }
    }

    /**
     * Checks, if the given value is number (integer or fractional)
     *
     * @param value
     * @param component
     * @throws DataValidationException
     */
    public static void checkIsNumber(String value, JComponent component) throws DataValidationException {
        if (!StringUtils.isNumber(value)) {
            failRes(component, "validation.not.a.number");
        }
    }

    /**
     * Checks, if the given value is number (long)
     *
     * @param value
     * @param component
     * @throws DataValidationException
     */
    public static void checkIsNumberLong(String value, JComponent component) throws DataValidationException {
        if (!StringUtils.isNumberLong(value)) {
            failRes(component, "validation.not.a.number.long");
        }
    }

    /**
     * Checks, if the given value is number (long)
     *
     * @param value
     * @param component
     * @throws DataValidationException
     */
    public static void checkIsNumberInteger(String value, JComponent component) throws DataValidationException {
        if (!StringUtils.isNumberInteger(value)) {
            failRes(component, "validation.not.a.number.long");
        }
    }

    /**
     * Checks, if the given value is null, empty string, or value if number (double).
     *
     * @param value
     * @param component
     * @throws DataValidationException
     */
    public static void checkIsNumberOrIsEmpty(String value, JComponent component) throws DataValidationException {
        if (value != null && !value.isEmpty()) {
            checkIsNumber(value, component);
        }
    }

    /**
     * Checks, of the given value is a valid date.
     *
     * @param value
     * @param component
     * @throws DataValidationException
     */
    public static void checkIsDate(String value, JTextField component) throws DataValidationException {
        DateFormat format = StringUtils.getDateFormat();
        try {
            format.parse(value);
        } catch (ParseException e) {
            failRes(component, "validation.not.a.date");
        }
    }

    /**
     * Checks, if the given value is in range between min and max (including min and max)
     *
     * @param value
     * @param component
     * @param min
     * @throws DataValidationException
     */
    public static void checkValueMinLong(long value, JComponent component, long min) throws DataValidationException {
        if (value < min) {
            fail(component, I18nSupport.message("validation.out.of.min", min));
        }
    }

    /**
     * Checks, if the given value is in range between min and max (including min and max)
     *
     * @param value     checking value
     * @param component checking component
     * @throws DataValidationException
     */
    public static void checkPositiveValue(long value, JComponent component) throws DataValidationException {
        if (value <= 0) {
            fail(component, I18nSupport.message("validation.not.positive.number"));
        }
    }

    public static void checkPositiveValue(int value, JComponent component) throws DataValidationException {
        if (value <= 0) {
            fail(component, I18nSupport.message("validation.not.positive.number"));
        }
    }

    /**
     * Checks, if the given value is in range between min and max (including min and max)
     *
     * @param value
     * @param component
     * @param max
     * @throws DataValidationException
     */
    public static void checkValueMaxLong(long value, JComponent component, long max) throws DataValidationException {
        if (value > max) {
            fail(component, I18nSupport.message("validation.out.of.max", max));
        }
    }

    /**
     * Checks, if the given value is in range between min and max (including min and max)
     *
     * @param value
     * @param component
     * @param min
     * @param max
     * @throws DataValidationException
     */
    public static void checkValueRangeLong(long value, JComponent component, long min, long max) throws DataValidationException {
        if (value < min || value > max) {
            fail(component, I18nSupport.message("validation.out.of.range", min, max));
        }
    }

    /**
     * Checks, if the given value is in range between min and max (including min and max)
     *
     * @param value
     * @param component
     * @param min
     * @throws DataValidationException
     */
    public static void checkValueMinDouble(double value, JComponent component, double min) throws DataValidationException {
        if (value < min) {
            fail(component, I18nSupport.message("validation.out.of.min", min));
        }
    }

    /**
     * Checks, if the given value is in range between min and max (including min and max)
     *
     * @param value
     * @param component
     * @param max
     * @throws DataValidationException
     */
    public static void checkValueMaxDouble(double value, JComponent component, double max) throws DataValidationException {
        if (value > max) {
            fail(component, I18nSupport.message("validation.out.of.max", max));
        }
    }

    /**
     * Checks, if the given value is in range between min and max (including min and max)
     *
     * @param value
     * @param component
     * @param min
     * @param max
     * @throws DataValidationException
     */
    public static void checkValueRangeDouble(double value, JComponent component, double min, double max) throws DataValidationException {
        if (value < min || value > max) {
            fail(component, I18nSupport.message("validation.out.of.range", min, max));
        }
    }

    /**
     * Checks if given text box has decimal number entered.
     * @param textComponent
     */
    public static void checkIsNumber(JTextField textComponent) throws DataValidationException {
        checkIsNumber(textComponent.getText(), textComponent);
    }

    /**
     * Checks if given text box has positive decimal number entered.
     * @param textComponent
     */
    public static void checkIsPositiveNumber(JTextField textComponent) throws DataValidationException {
        String stringValue = textComponent.getText();
        checkIsNumber(stringValue, textComponent);
        BigDecimal numberValue = StringUtils.parseStringToBigDecimal(stringValue);
        if ( numberValue.compareTo(BigDecimal.ZERO) < 0 ){
            failRes(textComponent, "validation.not.positive.number");
        }
    }
}
