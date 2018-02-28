/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.priceimport;

/**
 * Filter for loading list of price list imports.
 *
 * @author Valery.Barysok
 */
public class PriceImportFilter {

    /**
     * If set then represents the maximum count of results to return.
     */
    private Integer maxResultsCount;


    /* Getters and setters
    ------------------------------------------------------------------------------------------------------------------*/
    public Integer getMaxResultsCount() {
        return maxResultsCount;
    }

    public void setMaxResultsCount(Integer maxResultsCount) {
        this.maxResultsCount = maxResultsCount;
    }
}
