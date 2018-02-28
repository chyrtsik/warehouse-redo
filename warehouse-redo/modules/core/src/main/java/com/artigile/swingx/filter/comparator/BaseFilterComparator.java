/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */
package com.artigile.swingx.filter.comparator;

/**
 * @author Valery Barysok, 19.12.2009
 */
public abstract class BaseFilterComparator implements FilterComparator {

    protected String filterText;
    protected boolean accept = false;

    @Override
    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

    @Override
    public String getFilterText() {
        return filterText;
    }

    @Override
    public boolean isAccept() {
        return accept;
    }

    @Override
    public boolean validate(Object value) {
        return value != null;
    }
}
