/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.filter.impl;

import com.artigile.swingx.filter.BaseFilter;

/**
 * @author Valery Barysok, 16.12.2009
 */
public final class LessFilter extends BaseFilter {

    public LessFilter(String filterText, int column) {
        super(filterText, column);
    }

    @Override
    public BaseFilter makeClone() {
        BaseFilter filter = new LessFilter(text, getColumnIndex());
        filter.setComparator(comparator);
        return filter;
    }

    @Override
    protected boolean filterCheck(Object value) {
        return comparator.validate(value) && comparator.compareTo(value) > 0;
    }
}
