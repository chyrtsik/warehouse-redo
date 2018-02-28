/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao.generic.filter;

import java.util.List;

/**
 * @author IoaN, 22.11.2008
 */
public interface DataFilter {
    /**
     * Gets collection of criteria for filter.
     *
     * @return the collection of criteria
     */
    List<FilterCriterion> getCriteria();

    /**
     * Builds where-clause of the query.
     *
     * @return query
     */
    String buildQueryCondition();

    /**
     * Gets unique key. Used for building a query to define unique parameter names.
     *
     * @param key unique key
     */
    void setUniqueKey(String key);
}
