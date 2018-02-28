/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.orders;

import com.artigile.warehouse.domain.orders.OrderItemState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class OrderItemFilter {

    private Long detailBatchID;

    private List<OrderItemState> states;


    /* Constructors
    ------------------------------------------------------------------------------------------------------------------*/
    public OrderItemFilter() {
        this.states = new ArrayList<OrderItemState>();
    }


    /* Setters and getters
    ------------------------------------------------------------------------------------------------------------------*/
    public Long getDetailBatchID() {
        return detailBatchID;
    }

    public void setDetailBatchID(Long detailBatchID) {
        this.detailBatchID = detailBatchID;
    }

    public List<OrderItemState> getStates() {
        return states;
    }

    public void setStates(List<OrderItemState> states) {
        this.states = states;
    }

    public void setStates(OrderItemState... states) {
        this.states = Arrays.asList(states);
    }
}
