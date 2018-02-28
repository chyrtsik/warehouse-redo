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
public abstract class NumberComparator<T extends Comparable<T>> extends BaseFilterComparator {

    protected T value;

    protected abstract T parse(String text);

    @Override
    public void setFilterText(String filterText) {
        super.setFilterText(filterText);
        this.value = parse(filterText);
        this.accept = this.value != null;
    }

    @Override
    public int compareTo(Object o) {
        T v = (T) o;
        return this.value.compareTo(v);
    }
}
