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

/**
 * @author Shyrik, 25.10.2009
 */
public final class MiscUtils {
    private MiscUtils(){
    }

    /**
     * Returns true, is both objects are null or they are equal.
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean objectsEquals(Object obj1, Object obj2) {
        return obj1 == null ? obj2 == null : obj1.equals(obj2);
    }

    /**
     * Calculate object hashcode with respect to null object reference.
     * @param obj
     * @return
     */
    public static int objectHashCode(Object obj){
        return obj == null ? 0 : obj.hashCode();
    }

    /**
     * Checkes, if two strings are equal including the case, where one of strings is null.
     * @param string1
     * @param string2
     * @return
     */
    public static boolean stringsEquals(String string1, String string2) {
        return objectsEquals(string1 == null ? "" : string1, string2 == null ? "" : string2);
    }
}
