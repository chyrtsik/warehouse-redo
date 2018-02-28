/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */
package com.artigile.swingx.filter.comparator.impl;

import com.artigile.swingx.filter.comparator.BaseFilterComparator;

import java.text.Collator;

/**
 * @author Valery Barysok, 19.12.2009
 */
public final class StringComparator extends BaseFilterComparator {

    @Override
    public void setFilterText(String filterText) {
        super.setFilterText(filterText);
        this.accept = !isEmpty(filterText);
    }

    @Override
    public int compareTo(Object o) {
        String text = o.toString();
        Collator collator = Collator.getInstance();
        return collator.compare(filterText.toLowerCase(), text.toLowerCase());
    }

    @Override
    public boolean validate(Object value) {
        return value != null && !isEmpty(value.toString());
    }

    private boolean isEmpty(String text) {
        return (text == null) || text.isEmpty();
    }
}
