/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao.generic.builder;



import com.artigile.warehouse.dao.generic.filter.DataFilter;
import com.artigile.warehouse.dao.generic.filter.SortingInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is builder implementation for single table queries with
 * filtering and sorting support.
 *
 * @author ihar
 *         <p/>
 *         $Id$
 */
public class SimpleQueryBuilder implements QueryBuilder {

    /**
     * Data filters.
     */
    private List<DataFilter> filters;

    /**
     * Query first segment (can contain select, from and join subsegments).
     */
    private String fromPart;

    /**
     * Query segment for sorting.
     */
    private String sortingPart;

    /**
     * Constructs a SimpleQueryBuilder instance with empty SELECT and WHERE segments.
     */
    public SimpleQueryBuilder() {
        filters = new ArrayList<DataFilter>();
    }

    /**
     * Constructs a SimpleQueryBuilder instance with specified SELECT segment.
     *
     * @param fromSegment the query first segment (can contain select,
     *                    from and join subsegments)
     */
    public SimpleQueryBuilder(String fromSegment) {
        this();
        fromPart = fromSegment;
    }

    /**
     * Sets a sorting order type.
     *
     * @param sortInfo the sorting order type
     */
    public void setSortOrder(SortingInfo sortInfo) {
        sortingPart = String.format("order by %1$s %2$s",
                sortInfo.getColumn(), sortInfo.getType().getValue());
    }

    /**
     * Sets a sorting order type using a ready query.
     *
     * @param sortQuery the sorting query
     */
    public void setSortOrder(String sortQuery) {
        sortingPart = String.format("order by %1$s", sortQuery);
    }

    /**
     * Adds expressions for data filtering.
     *
     * @param filters Data filters.
     */
    public void addFilters(DataFilter... filters) {
        addFilters(Arrays.asList(filters));
    }

    /**
     * Adds expressions for data filtering.
     *
     * @param filtersList Data filters.
     */
    public void addFilters(List<? extends DataFilter> filtersList) {
        filters.addAll(filtersList);
    }

    private String buildFilterPart() {
        List<String> filterQueries = new ArrayList<String>();
        int counter = 1;
        for (DataFilter filter : filters) {
            if (filter != null) {
                String uniqueKey = filter.getClass().getSimpleName() + "_" + counter;
                counter++;
                filter.setUniqueKey(uniqueKey);
                filterQueries.add(filter.buildQueryCondition());
            }
        }

        return QueryBuilderUtils.AND(filterQueries);
    }

    /**
     * Builds a query string using HQL query language.
     *
     * @return the query string
     */
    public String buildQuery() {
        StringBuilder query = new StringBuilder();

        //noinspection HardCodedStringLiteral
        query.append(String.format("%1$s ", fromPart));

        String filterPart = buildFilterPart();
        if (filterPart.length() > 0) {
            //noinspection HardCodedStringLiteral
            query.append(String.format("where %1$s ", filterPart));
        }

        if (sortingPart != null && sortingPart.trim().length() > 0) {
            query.append(sortingPart);
        }

        return query.toString();
    }

    /**
     * Get from part.
     *
     * @return the fromPart
     */
    protected String getFromPart() {
        return fromPart;
    }

    /**
     * Get sorting part.
     *
     * @return the sortingPart
     */
    protected String getSortingPart() {
        return sortingPart;
    }
}
