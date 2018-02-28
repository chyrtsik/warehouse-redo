/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.properties;

import com.artigile.warehouse.bl.properties.PropertiesService;
import com.artigile.warehouse.utils.SpringServiceContext;

import static com.artigile.warehouse.utils.Utils.*;

/**
 * @author Valery Barysok, 28.12.2009
 */
public class Properties {
    private static PropertiesService propertiesService = SpringServiceContext.getInstance().getPropertiesService();

    private Properties() {
    }

    /**
     * Provided for parallelism with the <tt>getProperty</tt> method.
     * Enforces use of strings for property keys and values.
     *
     * @param key the key to be placed into this property list.
     * @param value the value corresponding to <tt>key</tt>.
     * @see #getProperty
     */
    public static synchronized void setProperty(String key, String value) {
        propertiesService.setProperty(key, value);
    }

    /**
     * Searches for the property with the specified key in this property list.
     * The method returns <code>null</code> if the property is not found.
     *
     * @param   key   the property key.
     * @return  the value in this property list with the specified key value.
     * @see     #setProperty
     */
    public static String getProperty(String key) {
        return propertiesService.getProperty(key);
    }

    /**
     * Searches for the property with the specified key in this property list.
     * The method returns <code>null</code> if the property is not found or
     * can not be converted to Integer.
     *
     * @param   key   the property key.
     * @return  the value in this property list with the specified key value.
     * @see     #setProperty
     */
    public static Integer getPropertyAsInteger(String key) {
        return getInteger(getProperty(key));
    }

    /**
     * Searches for the property with the specified key in this property list.
     * The method returns <code>null</code> if the property is not found or
     * can not be converted to Long.
     *
     * @param   key   the property key.
     * @return  the value in this property list with the specified key value.
     * @see     #setProperty
     */
    public static Long getPropertyAsLong(String key) {
        return getLong(getProperty(key));
    }

    /**
     * Searches for the property with the specified key in this property list.
     * The method returns <code>null</code> if the property is not found or
     * can not be converted to Float.
     *
     * @param   key   the property key.
     * @return  the value in this property list with the specified key value.
     * @see     #setProperty
     */
    public static Float getPropertyAsFloat(String key) {
        return getFloat(getProperty(key));
    }

    /**
     * Searches for the property with the specified key in this property list.
     * The method returns <code>null</code> if the property is not found or
     * can not be converted to Double.
     *
     * @param   key   the property key.
     * @return  the value in this property list with the specified key value.
     * @see     #setProperty
     */
    public static Double getPropertyAsDouble(String key) {
        return getDouble(getProperty(key));
    }

    /**
     * Searches for the property with the specified key in this property list.
     * The method returns <code>null</code> if the property is not found or
     * can not be converted to Byte.
     *
     * @param   key   the property key.
     * @return  the value in this property list with the specified key value.
     * @see     #setProperty
     */
    public static Byte getPropertyAsByte(String key) {
        return getByte(getProperty(key));
    }

    /**
     * Searches for the property with the specified key in this property list.
     * The method returns <code>null</code> if the property is not found or
     * can not be converted to Short.
     *
     * @param   key   the property key.
     * @return  the value in this property list with the specified key value.
     * @see     #setProperty
     */
    public static Short getPropertyAsShort(String key) {
        return getShort(getProperty(key));
    }

    /**
     * Searches for the property with the specified key in this property list.
     * The method returns <code>null</code> if the property is not found or
     * can not be converted to Boolean.
     *
     * @param   key   the property key.
     * @return  the value in this property list with the specified key value.
     * @see     #setProperty
     */
    public static Boolean getPropertyAsBoolean(String key){
        return getBoolean(getProperty(key));
    }
}
