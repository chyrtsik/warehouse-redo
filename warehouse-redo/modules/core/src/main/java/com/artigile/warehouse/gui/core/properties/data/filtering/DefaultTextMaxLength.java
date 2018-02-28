/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.data.filtering;

/**
 * Class of limitation text length in the text components.
 *
 * @author Valery.Barysok, 09.10.2010
 */
public class DefaultTextMaxLength extends TextComponentFilter {

    private int maxLength;

    public DefaultTextMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public String filter(String text) {
        if (text == null || getLength() + text.length() <= maxLength) {
            return text;
        }

        if (getLength() < maxLength) {
            return text.substring(0, maxLength - getLength());
        }

        return "";
    }
}
