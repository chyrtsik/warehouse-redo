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

/**
 * @author Shyrik, 10.01.2009
 */

public interface OrderItemsChangeListener {
    /**
     * Called, when new item have been added to the order.
     * @param newItem added item.
     */
    public void itemAdded(OrderItemTO newItem);

    /**
     * Called, when item have been removel from the order.
     * @param removedItem removed item.
     */
    public void itemRemoved(OrderItemTO removedItem);

    /**
     * Called, when order item have been changed.
     * @param changedItem
     */
    public void itemChanged(OrderItemTO changedItem);
}
