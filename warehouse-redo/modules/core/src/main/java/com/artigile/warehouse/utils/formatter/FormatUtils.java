/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.formatter;

import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public abstract class FormatUtils {

    /**
     * Default system options
     * For more information about patterns see http://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html
     */
    private static final String DEFAULT_PATTERN = "#.######"; // Not null
    private static final Character DEFAULT_DECIMAL_SEPARATOR = '.'; // Not null
    private static final Character DEFAULT_GROUP_SEPARATOR = ' '; // Not null

    /**
     * Formatter that used ONLY to validate patterns
     */
    private static DecimalFormat checkPatternDecimalFormat = new DecimalFormat();
    
    
    /* Number formatting
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * @param number Number for formatting
     * @param pattern Customizing pattern (@see http://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html)
     * @param decimalSeparator Separator between integer and fractional parts
     * @param groupSeparator Separator between groups of integer part
     * @return String representation of the number, formatted using the given parameters
     */
    public static String format(Number number,
                                String pattern,
                                Character decimalSeparator,
                                Character groupSeparator) {
        DecimalFormat decimalFormat = getDecimalFormatInstance(pattern, decimalSeparator, groupSeparator);
        return decimalFormat.format(number);
    }

    /**
     * @param string String for parsing
     * @param formatter Required format of the output number
     * @return Number that converted from the given string
     */
    public static Number parse(String string, NumberFormat formatter) {
        Number number = null;
        // Try to parse string...
        try {
            number = formatter.parse(string);
        } catch (ParseException ignored) {}

        // If string is not parsed using formatter ...
        if (number == null) {
            number = Double.valueOf(StringUtils.parseNumber(string));
        }
        return number;
    }

    /**
     * @param pattern Customizing pattern (@see http://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html)
     * @param decimalSeparator Separator between integer and fractional parts
     * @param groupSeparator Separator between groups of integer part
     * @return New instance of the decimal format with the given parameters.
     */
    public static DecimalFormat getDecimalFormatInstance(String pattern, Character decimalSeparator, Character groupSeparator) {
        DecimalFormat decimalFormat;
        try {
            decimalFormat = new DecimalFormat(pattern);
        } catch (Exception e) {
            decimalFormat = new DecimalFormat();
            LoggingFacade.logError(e);
        }
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        if (decimalSeparator != null) {
            decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
        }
        if (groupSeparator != null) {
            decimalFormatSymbols.setGroupingSeparator(groupSeparator);
        }
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        return decimalFormat;
    }

    public static boolean isValidPattern(String pattern) {
        try {
            checkPatternDecimalFormat.applyPattern(pattern);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /* Getters
    ------------------------------------------------------------------------------------------------------------------*/
    public static String getDefaultPattern() {
        return DEFAULT_PATTERN;
    }
    
    public static Character getDefaultDecimalSeparator() {
        return DEFAULT_DECIMAL_SEPARATOR;
    }

    public static Character getDefaultGroupSeparator() {
        return DEFAULT_GROUP_SEPARATOR;
    }
}
