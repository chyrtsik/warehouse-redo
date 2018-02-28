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

import com.artigile.swingx.filter.impl.*;
import com.artigile.swingx.filter.parser.SearchItem;
import com.artigile.swingx.filter.parser.SearchTypeConst;
import org.jdesktop.swingx.decorator.Filter;

import java.util.regex.Pattern;

/**
 * @author Valery Barysok, 16.12.2009
 */
public class FilterFactory {

    private static WildcardSupport wildcardSupport = new DefaultWildcardSupport();

    private FilterFactory() {
    }

    public static Filter createFilter(SearchItem item, int column) {
        SearchTypeConst searchType = item.getSearchType();
        switch (searchType) {
            case EQUAL_LESS_SEARCH: return new EqualLessFilter(item.getText(), column);
            case EQUAL_GREATER_SEARCH: return new EqualGreaterFilter(item.getText(), column);
            case EQUAL_SEARCH: return new EqualFilter(item.getText(), column);
            case LESS_SEARCH: return new LessFilter(item.getText(), column);
            case GREATER_SEARCH: return new GreaterFilter(item.getText(), column);
        }

        // DEFAULT_SEARCH
        return new CustomPatternFilter(wildcardSupport.convert(item.getText()), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE, column);
    }
}
