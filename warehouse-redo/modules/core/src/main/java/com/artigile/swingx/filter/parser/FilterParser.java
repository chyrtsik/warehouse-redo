/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.filter.parser;

/**
 * @author Valery Barysok, 14.12.2009
 */
public class FilterParser {

    private static String EQUAL = "=";
    private static String GREATER = ">";
    private static String EQUAL_GREATER = ">=";
    private static String LESS = "<";
    private static String EQUAL_LESS = "<=";

    private FilterParser() {
    }

    public static SearchItem parse(String text) {
        text = leftTrim(text);
        if (text.startsWith(EQUAL)) {
            return new SearchItem(SearchTypeConst.EQUAL_SEARCH, text.substring(EQUAL.length()));
        } else if (text.startsWith(EQUAL_LESS)) {
            return new SearchItem(SearchTypeConst.EQUAL_LESS_SEARCH, text.substring(EQUAL_LESS.length()));
        } else if (text.startsWith(EQUAL_GREATER)) {
            return new SearchItem(SearchTypeConst.EQUAL_GREATER_SEARCH, text.substring(EQUAL_GREATER.length()));
        } else if (text.startsWith(LESS)) {
            return new SearchItem(SearchTypeConst.LESS_SEARCH, text.substring(LESS.length()));
        } else if (text.startsWith(GREATER)) {
            return new SearchItem(SearchTypeConst.GREATER_SEARCH, text.substring(GREATER.length()));
        }

        return new SearchItem(SearchTypeConst.DEFAULT_SEARCH, text);
    }

    /**
     * Returns a copy of the string, with leading whitespace
     * omitted.
     *
     * @return A copy of this string with leading white
     *         space removed, or this string if it has
     *         no leading white space.
     */
    private static String leftTrim(String text) {
        for (int i = 0; i < text.length(); ++i) {
            if (text.charAt(i) > ' ') {
                return text.substring(i);
            }
        }

        return text;
    }
}
