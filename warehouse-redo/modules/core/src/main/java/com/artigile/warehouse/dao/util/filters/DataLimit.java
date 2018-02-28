/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao.util.filters;

/**
 * Limit search results.
 * Used in filters.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class DataLimit {

    /**
     * First retrieved result
     */
    private int firstResult;

    /**
     * Max retrieved results
     */
    private Integer maxResults;


    /* Constructors
    ------------------------------------------------------------------------------------------------------------------*/
    public DataLimit() {}

    public DataLimit(int maxResults) {
        this.maxResults = maxResults;
    }

    public DataLimit(int firstResult, int maxResults) {
        this.firstResult = firstResult;
        this.maxResults = maxResults;
    }


    /* Getters and setters
    ------------------------------------------------------------------------------------------------------------------*/
    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }
}
