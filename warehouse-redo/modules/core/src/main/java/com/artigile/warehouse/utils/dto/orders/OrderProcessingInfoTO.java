/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.orders;

import com.artigile.warehouse.utils.custom.types.CompositeNumber;

/**
 * @author Shyrik, 25.06.2009
 */

public class OrderProcessingInfoTO {
    private long id;
    private OrderTOForReport order;
    private Long complectedItemsCount;
    private Long itemsCount;

    //================================ Calculated getters ======================================
    public CompositeNumber getComplected(){
        if (order.getState().isInProcessing()){
            return new CompositeNumber("{0} / {1}", new Long[]{complectedItemsCount, itemsCount});
        }
        return null;
    }

    //================================ Getters and setters ======================================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrderTOForReport getOrder() {
        return order;
    }

    public void setOrder(OrderTOForReport order) {
        this.order = order;
    }

    public long getComplectedItemsCount() {
        return complectedItemsCount;
    }

    public void setComplectedItemsCount(Long complectedItemsCount) {
        this.complectedItemsCount = complectedItemsCount;
    }

    public long getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(Long itemsCount) {
        this.itemsCount = itemsCount;
    }
}
