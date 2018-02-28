/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */


package com.artigile.warehouse.gui.menuitems.orders.items;

import com.artigile.warehouse.gui.core.report.controller.CellEditingStrategy;
import com.artigile.warehouse.utils.dto.orders.OrderItemTO;

/**
 * @author: Vadim.Zverugo
 */

/**
 * Base class of order items fields editing strategy.
 */
public abstract class OrderItemCellEditingStrategy implements CellEditingStrategy {
    private OrderItemEditingAvailability availability = new OrderItemEditingAvailability();

    @Override
    public boolean isEditable(Object reportItem) {
        return availability.isAvaible((OrderItemTO) reportItem);
    }
    
    @Override
    public void saveValue(Object reportItem, Object newValue) {
        OrderItemTO orderItem = (OrderItemTO) reportItem;
        doSave(orderItem, newValue);
    }

    /**
     * Implement this method to perform correct saving of posting item's field value.
     * @param orderItem
     * @param newValue
     */
    protected abstract void doSave(OrderItemTO orderItem, Object newValue);
}
