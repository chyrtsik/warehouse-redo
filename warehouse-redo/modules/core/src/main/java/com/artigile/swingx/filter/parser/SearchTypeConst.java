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
 * @author Valery Barysok, 14.12.2009
 */
public enum SearchTypeConst {
    DEFAULT_SEARCH(-1),
    EQUAL_SEARCH(0),
    GREATER_SEARCH(1),
    EQUAL_GREATER_SEARCH(2),
    LESS_SEARCH(3),
    EQUAL_LESS_SEARCH(4);

    private int searchType;

    SearchTypeConst(int searchType) {
        this.searchType = searchType;
    }

    public int getSearchType() {
        return searchType;
    }
}
