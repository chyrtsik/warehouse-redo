/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils;

import com.artigile.warehouse.utils.custom.types.CompositeNumber;
import com.artigile.warehouse.utils.custom.types.VariantPrice;
import com.artigile.warehouse.utils.custom.types.VariantQuantity;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.formatter.FormatUtils;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.*;
import java.util.*;

/**
 * @author Shyrik, 15.12.2008
 */

/**
 * Utilities for working with strings.
 */
public final class StringUtils {

    public static final String EMPTY_STRING = "";
    
    /**
     * Number formatter for all numbers in application.
     */
    private static NumberFormat systemNumberFormat = FormatUtils.getDecimalFormatInstance(
            FormatUtils.getDefaultPattern(),
            FormatUtils.getDefaultDecimalSeparator(),
            FormatUtils.getDefaultGroupSeparator());

    /**
     * It used to format BigDecimal numbers with defined precision.
     * See: formatNumber(BigDecimal, int)
     */
    private static DecimalFormat bigDecimalFormatWithPrecision = new DecimalFormat();

    /**
     * Date formatter for all dates in application.
     */
    static private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    /**
     * Time formatter for all times in application.
     */
    static private DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * Time formatter for all dates + times in application.
     */
    static private DateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    /**
     * Local builder of strings.
     * Don't forget to clear before using (@see clearStrBuilder():void).
     * It used for economy memory.
     */
    private static StringBuilder strBuilder = new StringBuilder();


    private StringUtils() {
    }

    /**
     * Split string info part with the help of the delimiter.
     *
     * @param valuesString the string with values that should be parsed.
     * @param delimiter    string delimeter.
     * @return - list with parsed values.
     */
    public static List<String> delimitedStringToList(String valuesString, String delimiter) {
        if (valuesString == null) {
            return new ArrayList<String>();
        }

        StringTokenizer tokenizer = new StringTokenizer(valuesString, delimiter, false);
        List<String> values = new ArrayList<String>();
        while (tokenizer.hasMoreElements()) {
            values.add(tokenizer.nextToken());
        }
        return values;
    }


    /**
     * Make delimited string from list of values.
     *
     * @param values    list of values from what the string will be constructed.
     * @param delimiter delimeter which will be inserted between string values.
     * @return string representation of values in list, delimeted with {@code deilmeter}.
     */
    public static String listToDelimitedString(List<String> values, String delimiter) {
        if (values == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String value : values) {
            if (!first) {
                builder.append(delimiter);
            } else {
                first = false;
            }
            builder.append(value);
        }
        return builder.toString();
    }

    /**
     * Constructs application standard date formatter.
     * @return date formatter object.
     */
    public static DateFormat getDateFormat() {
        return dateFormat;
    }

    /**
     * Constructs application standard time formatter.
     * @return date formatter object.
     */
    public static DateFormat getTimeFormat() {
        return timeFormat;
    }

    /**
     * Constructs application standard date and time formatter.
     * @return date formatter object.
     */
    public static DateFormat getDateTimeFormat() {
        return dateTimeFormat;
    }

    /**
     * Use this method for supporting one format of decimal numbers through the whole application.
     *
     * @param number - number to format.
     * @return formated number string.
     */
    public static String formatNumber(Number number) {
        return number == null ? EMPTY_STRING : systemNumberFormat.format(number);
    }

    /**
     * Format money amount to string using system number format and standard number of decimals.
     * @param amount amount to format.
     * @return formatted amount.
     */
    public static String formatMoneyAmount(BigDecimal amount){
        return formatNumber(amount, 2); //Using default money precision (hardcoded for now).
    }

    /**
     * Use this method for supporting one format of decimal numbers through the whole application.
     * Method if private to private hardcoding of number of decimals. Use formatMoneyAmount to format sums of money.
     *
     * @param number  number to format.
     * @param precision precision after comma
     * @return formatted number string.
     */
    private static String formatNumber(BigDecimal number, int precision) {
        if (number == null) {
            return EMPTY_STRING;
        } else {
            bigDecimalFormatWithPrecision.applyPattern(buildPrecisionFormat(precision));
            return bigDecimalFormatWithPrecision.format(number);
        }
    }

    /**
     * Use this method for formatting date time fields.
     * @param dateTime date and time to be formatted.
     * @return formatted date time value.
     */
    public static String formatDateTime(Date dateTime){
        return dateTime == null ? "" : getDateTimeFormat().format(dateTime);
    }

    /**
     * Use this method for formatting date fields.
     * @param date date to be formatted.
     * @return formatted date value.
     */
    public static String formatDate(Date date){
        return date == null ? "" : getDateFormat().format(date);
    }

    /**
     * Use this method for formatting time fields.
     * @param time time to be formatted.
     * @return formatted time value.
     */
    public static String formatTime(Date time){
        return time == null ? "" : getTimeFormat().format(time);
    }

    /**
     * Parse string to date.
     * @param dateStr string to parse. Null and empty strings are parsed as null.
     * @return date parsed.
     */
    public static Date parseDate(String dateStr) {
        if (hasValue(dateStr)){
            try {
                return getDateFormat().parse(dateStr);
            } catch (ParseException e) {
                LoggingFacade.logWarning("Error parsing date string: " + dateStr + ". Please ensure that this is not a bug or incorrect data.");
            }
        }
        return null;
    }

    /**
     * Parses string into big decimal.
     *
     * @param strForParsing - string that should be parsed.
     * @return - parsed number.
     */
    public static BigDecimal parseStringToBigDecimal(String strForParsing) {
        return parseStringToBigDecimal(strForParsing, false);
    }

    /**
     * Parses string into big decimal.
     *
     * @param strForParsing - string that should be parsed.
     * @param emptyAllows - strForParsing may be null or empty.
     * @return - parsed number.
     */
    public static BigDecimal parseStringToBigDecimal(String strForParsing, boolean emptyAllows) {
        if (emptyAllows && isStringNullOrEmpty(strForParsing)) {
            return null;
        } else {
            // Parse, using system formatter
            Number number = FormatUtils.parse(strForParsing, systemNumberFormat);
            if (number instanceof Long) {
                return BigDecimal.valueOf((Long) number);
            } else if (number instanceof Double) {
                return BigDecimal.valueOf((Double) number);
            } else {
                return BigDecimal.ZERO;
            }
        }
    }

    /**
     * Checks, if the given string is valid lng value.
     *
     * @param value the value that will be analysed if it is number.
     * @return true is string value represents number, otherwise returns false.
     */
    public static boolean isNumberLong(String value) {
        try {
            Long.valueOf(value);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks, if the given string is valid integer value.
     *
     * @param value the value that will be analysed if it is number.
     * @return true is string value represents number, otherwise returns false.
     */
    public static boolean isNumberInteger(String value) {
        try {
            Integer.valueOf(value);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks, if the given string is valid number (integer or fractional).
     *
     * @param value the string value that will be analysed.
     * @return true if string is number , otherwise returns false.
     */
    public static boolean isNumber(String value) {
        try {
            parseStringToBigDecimal(value);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Extracts file extension from given file name.
     *
     * @param fileName parses the file extention from string.
     * @return string file extension from given file name.
     */
    public static String getFileExtension(String fileName) {
        if (fileName != null) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return null;
    }

    /**
     * Builds string for formattring numbers with presision.
     * @param precision number of digits that should be displayed after comma.
     * @return pattern for number with presision.
     */
    private static String buildPrecisionFormat(int precision) {
        StringBuilder format = new StringBuilder("#.");
        for (int i = 0; i < precision; i++) {
            format.append("#");
        }
        return format.toString();
    }

    /**
     * Returns MD5 hash value for given string.
     * @param string string to be hashed using md5.
     * @return md5 result for given string.
     */
    public static String getStringMD5(String string) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            LoggingFacade.logError("Cannot create MD5 digest provider.", e);
            throw new RuntimeException(e);
        }
        messageDigest.update(string.getBytes(), 0, string.length());
        return new BigInteger(1, messageDigest.digest()).toString(16);
    }

    public static String prepareFilter(String filter) {
        String result = filter.replaceAll("(%)", "\\\\$1");
        result = result.replaceAll("(_)", "\\\\$1");
        result = result.replaceAll("(\\*)", "%");
        result = result.replaceAll("(\\?)", "_");
        return result;
    }

    public static String toString(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else if (value instanceof CompositeNumber) {
            CompositeNumber number = (CompositeNumber) value;
            return number.toString();
        } else if (value instanceof Number) {
            return StringUtils.formatNumber((Number) value);
        } else if (value instanceof VariantQuantity) {
            VariantQuantity quantity = (VariantQuantity) value;
            if (quantity.isNumber()) {
                return StringUtils.formatNumber(quantity.getQuantity());
            } else {
                return quantity.getValue();
            }
        } else if (value instanceof VariantPrice) {
            VariantPrice price = (VariantPrice) value;
            if (price.isNumber()) {
                return StringUtils.formatNumber(price.getPrice());
            } else {
                return price.getValue();
            }
        } else if (value instanceof Date) {
            return StringUtils.getDateTimeFormat().format((Date)value);
        }
        else if (value instanceof Boolean){
            return (Boolean)value ? I18nSupport.message("detail.field.value.yes") : I18nSupport.message("detail.field.value.no");
        }
        return null;
    }

    public static String simplifyName(String name) {
        return name.replaceAll("[^\\p{L}|\\p{N}]", "").replaceAll("\\|", "");
    }

    public static boolean isStringNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean hasValue(String string){
        return !isStringNullOrEmpty(string);
    }

    /**
     * @param string String for checking
     * @return True - if the given string contains any symbols (excluding whitespaces)
     *         False - if the given string null, empty or contains only whitespaces
     */
    public static boolean containsSymbols(String string) {
        return string != null && !string.trim().isEmpty();
    }

    public static String toString(char[] characters) {
        StringBuilder strBuilder = new StringBuilder();
        for (Character character : characters) {
            strBuilder.append(character);
        }
        return strBuilder.toString();
    }

    /**
     * Removes all spaces in the given string.
     * It faster than 'replaceAll()'.
     *
     * @param string String for transformation
     * @return String without spaces
     */
    public static String removeSpaces(String string) {
        StringBuilder strBuilder = new StringBuilder();
        for (Character character : string.toCharArray()) {
            if (character != ' ') {
                strBuilder.append(character);
            }
        }
        return strBuilder.toString();
    }

    /**
     * @param items Items for merging at the one string
     * @return Merged string
     */
    public static synchronized String buildString(Object... items) {
        clearStrBuilder();
        // Merge items
        for (Object item : items) {
            strBuilder.append(item);
        }
        return strBuilder.toString();
    }

    private static void clearStrBuilder() {
        if (strBuilder.length() > 0) {
            strBuilder.delete(0, strBuilder.length());
        }
    }

    /**
     * Removes all non-numeric characters from the string.
     *
     * @param string initial string.
     * @return Numeric string.
     */
    public static String parseNumber(String string) {
        String result = "";
        if (!isStringNullOrEmpty(string)) {
            result = string.replaceAll("[^0123456789,.-]", "");
            result = result.replaceAll(",", ".");
        }
        return result;
    }

    /**
     * Format counts together with measurement units.
     * @param countByMeasureUnit pairs of measure units and count in these measure units.
     * @return formatted counts.
     */
    public static String formatCountsWithMeasures(Map<MeasureUnitTO, ? extends Number> countByMeasureUnit) {
        StringBuilder countsBuilder = new StringBuilder();
        for (MeasureUnitTO meas : countByMeasureUnit.keySet()){
            if (countsBuilder.length() > 0){
                countsBuilder.append(", ");
            }
            countsBuilder.append(formatNumber(countByMeasureUnit.get(meas))).append(" ").append(meas.getSign());
        }
        return countsBuilder.toString();
    }

    /**
     * Format amounts together with currencies.
     * @param amountByCurrency pairsof currencies and amounts in these currencies.
     * @return formatted amounts.
     */
    public static String formatAmountsWithCurrencies(Map<CurrencyTO, BigDecimal> amountByCurrency) {
        StringBuilder amountsBuilder = new StringBuilder();
        for (CurrencyTO currency : amountByCurrency.keySet()){
            if (amountsBuilder.length() > 0){
                amountsBuilder.append(", ");
            }
            amountsBuilder.append(formatNumber(amountByCurrency.get(currency))).append(" ").append(currency.getSign());
        }
        return amountsBuilder.toString();
    }

    /* Number formatter
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * Updates current number formatter.
     *
     * @param pattern New pattern
     * @param decimalSeparator New separator between integer and fractional parts of number
     * @param groupSeparator New separator between groups of integer part of number
     */
    public static void updateNumberFormatter(String pattern, Character decimalSeparator, Character groupSeparator) {
        systemNumberFormat = FormatUtils.getDecimalFormatInstance(pattern, decimalSeparator, groupSeparator);
    }

    /**
     * Generate new bar code using options specified.
     * @param barCodePrefix prefix of the bar code.
     * @param articleNum article number.
     * @param articleLength length of article in ber code.
     * @param generateCheckSum true when check sum number should be generated.
     * @return generated bar code.
     */
    public static String generateBarCode(String barCodePrefix, long articleNum, int articleLength, boolean generateCheckSum) {
        StringBuilder barCodeBuilder = new StringBuilder();
        //1. Bar code prefix.
        barCodeBuilder.append(barCodePrefix);

        //2. Article number.
        String articleStr = String.valueOf(articleNum);
        int zerosCount = articleLength - articleStr.length();
        while (zerosCount > 0){
            barCodeBuilder.append("0");
            zerosCount--;
        }
        barCodeBuilder.append(articleStr);

        //3. Check sum digit.
        if (generateCheckSum){
            barCodeBuilder.append(calculateBarCodeCheckSum(barCodeBuilder.toString()));
        }
        return barCodeBuilder.toString();
    }

    private static int calculateBarCodeCheckSum(String barCode) {
        //Calculation of check sum digit for bar code.
        int val=0;
        for(int i=0; i<barCode.length(); i++){
            val += Integer.valueOf(String.valueOf(barCode.charAt(i))) * ((i%2==0) ? 1 : 3);
        }
        int checksum_digit = 10 - (val % 10);
        return checksum_digit == 10 ? 0 : checksum_digit;
    }
}
