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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ordering search results.
 * Used in filters.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class DataOrder {

    /**
     * Order by fields...
     */
    private List<String> orderFields;

    /**
     * Default - ascending
     */
    private OrderType orderType;


    /* Constructors
    ------------------------------------------------------------------------------------------------------------------*/
    public DataOrder(String... orderFields) {
        this.orderFields = new ArrayList<String>(Arrays.asList(orderFields));
        this.orderType = OrderType.ASC;
    }

    public DataOrder(OrderType orderType, String... orderFields) {
        this.orderFields = new ArrayList<String>(Arrays.asList(orderFields));
        this.orderType = orderType;
    }


    /* Getters
    ------------------------------------------------------------------------------------------------------------------*/
    public List<String> getOrderFields() {
        return orderFields;
    }

    public OrderType getOrderType() {
        return orderType;
    }
}
