/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.i18n;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author IoaN, 22.11.2008
 */
public final class I18nSupport {
    private I18nSupport(){
    }

    @NonNls
    private static final ResourceBundle props = ResourceBundle.getBundle("i18n/warehouse");

    public static String message(@PropertyKey(resourceBundle = "i18n.warehouse")
    String key, Object... params) {
        String value = props.getString(key);
        if (params.length > 0) return MessageFormat.format(value, params);
        return value;
    }
}
