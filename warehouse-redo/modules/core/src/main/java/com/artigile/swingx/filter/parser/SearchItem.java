/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.filter.parser;

/**
 * @author Valery Barysok, 16.12.2009
 */
public class SearchItem {
    private SearchTypeConst searchType;
    private String text;

    public SearchItem(SearchTypeConst searchType, String text) {
        this.searchType = searchType;
        this.text = text;
    }

    public SearchTypeConst getSearchType() {
        return searchType;
    }

    public String getText() {
        return text;
    }
}
