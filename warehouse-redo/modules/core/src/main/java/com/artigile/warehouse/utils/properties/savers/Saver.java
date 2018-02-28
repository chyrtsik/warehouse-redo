/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.properties.savers;

/**
 * @author Valery Barysok, 30.12.2009
 */
public abstract class Saver {

    protected static String getId(Class clazz, String id) {
        return clazz.getCanonicalName() + ".{" + id + "}";
    }

    protected static String getId(String frameId, String id) {
        return frameId + ".{" + id + "}";
    }
}
