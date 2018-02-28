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
 * Give the ability to apply chain of text filters
 *
 * @author Valery.Barysok, 09.10.2010
 */
public class CompoundTextFilter extends TextComponentFilter {

    TextComponentFilter filter;
    TextComponentFilter secondFilter;

    public CompoundTextFilter(TextComponentFilter filter, TextComponentFilter secondFilter) {
        this.filter = filter;
        this.secondFilter = secondFilter;
    }

    @Override
    public String filter(String text) {
        return secondFilter.filter(filter.filter(text));
    }
}
