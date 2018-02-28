/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils;

import java.math.BigDecimal;

/**
 * @author Valery Barysok, 30.12.2009
 */
public class Utils {

    public static Integer getInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double getDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Byte getByte(String value) {
        try {
            return Byte.parseByte(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Float getFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long getLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Short getShort(String value) {
        try {
            return Short.parseShort(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static BigDecimal getBigDecimal(String value) {
        Double val = getDouble(value);
        return val != null ? BigDecimal.valueOf(val) : null;
    }

    public static Boolean getBoolean(String value){
        return value == null ? null : Boolean.valueOf(value);
    }
}
