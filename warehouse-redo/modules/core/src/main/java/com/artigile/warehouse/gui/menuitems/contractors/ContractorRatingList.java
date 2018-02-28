/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.contractors;

/**
 * @author: Vadim.Zverugo
 */

/**
 * Possibilities ratings of contractor.
 */
public enum ContractorRatingList {

    /**
     * Rating 1 is highest
     */
    RATING_1 (1),

    RATING_2 (2),

    RATING_3 (3),

    RATING_4 (4),

    /**
     * Rating 5 is lower
     */
    RATING_5 (5),

    /**
     * If rating of contractor is unknown
     */
    RATING_UNKNOWN (null);

    private Integer contractorRating;

    ContractorRatingList(Integer contractorRating) {
        this.contractorRating = contractorRating;
    }

    public Integer getContractorRating() {
        return contractorRating;
    }
}
