/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.filter;

import com.artigile.swingx.filter.comparator.FilterComparator;
import com.artigile.swingx.filter.comparator.impl.StringComparator;
import org.jdesktop.swingx.decorator.Filter;

import java.util.ArrayList;

/**
 * @author Valery Barysok, 16.12.2009
 */
public abstract class BaseFilter extends Filter {
    protected ArrayList<Integer> toPrevious;
    protected String text;
    protected FilterComparator comparator = new StringComparator();

    public BaseFilter(String text, int column) {
        super(column);
        this.text = text;
        this.comparator.setFilterText(text);
    }

    public abstract BaseFilter makeClone();

    public void setComparator(FilterComparator comparator) {
        this.comparator = comparator;
    }

    /**
     *
     * @param value
     * @return
     */
    protected abstract boolean filterCheck(Object value);

    @Override
    public int getSize() {
        return toPrevious.size();
    }

    @Override
    protected void init() {
        toPrevious = new ArrayList<Integer>();
    }

    @Override
    protected void reset() {
        toPrevious.clear();
        int inputSize = getInputSize();
        fromPrevious = new int[inputSize];  // fromPrevious is inherited protected
        for (int i = 0; i < inputSize; ++i) {
            fromPrevious[i] = -1;
        }
    }

    @Override
    protected void filter() {
        final boolean accept = comparator.isAccept();
        int inputSize = getInputSize();
        int current = 0;
        for (int i = 0; i < inputSize; ++i) {
            if (!accept || test(i)) {
                toPrevious.add(i);
                // generate inverse map entry while we are here
                fromPrevious[i] = current++;
            }
        }
    }

    /**
     * Tests whether the given row (in this filter's coordinates) should
     * be added. <p>
     *
     * @param row the row to test
     * @return true if the row should be added, false if not.
     */
    private boolean test(int row) {
        // ask the adapter if the column should be includes
        if (!adapter.isTestable(getColumnIndex())) {
            return false;
        }

        //String text = getInputString(row, getColumnIndex());
        Object value = getInputValue(row, getColumnIndex());

        //return isEmpty(text) ? false : filterCheck(text.toLowerCase());
        return filterCheck(value);
    }

    @Override
    protected int mapTowardModel(int row) {
        return toPrevious.get(row);
    }

    /**
     * @param text
     * @return
     */
    protected boolean isEmpty(String text) {
        return (text == null) || (text.length() == 0);
    }
}
