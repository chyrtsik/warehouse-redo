/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

/*
 * @(#) SortOrder.java created on 09.01.2008
 */
package com.artigile.warehouse.dao.generic.filter;


/**
 * Sorting order type enumeration.
 *
 * @author ihar
 */
public enum SortingOrder {

    /**
     * Ascending sorting.
     */
    ASCENDING("asc"),

    /**
     * Descending sorting.
     */
    DESCENDING("desc");

    /**
     * Sorting order name.
     */
    private String value;

    /**
     * Constructor.
     *
     * @param value the sorting order name
     */
    SortingOrder(String value) {
        this.value = value;
    }

    /**
     * Gets a sorting order name.
     *
     * @return the sorting order name
     */
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return getValue();
    }

    /**
     * Returns next sorting order.
     *
     * @param sortingOrder the current sorting order
     * @return the next sorting order
     */
    public static SortingOrder nextSortingType(final SortingOrder sortingOrder) {
        SortingOrder result;
        switch (sortingOrder) {
            case ASCENDING:
                result = SortingOrder.DESCENDING;
                break;
            case DESCENDING:
                result = SortingOrder.ASCENDING;
                break;
            default:
                result = sortingOrder;
                break;
		}
		return result;
	}
}
