/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.orders.Order;
import com.artigile.warehouse.domain.orders.OrderItem;

import java.util.List;

/**
 * @author Shyrik, 06.01.2009
 */
public interface OrderDAO extends EntityDAO<Order> {

    /**
     * Get order list for report without deleted orders.
     * @return order list without deleted orders.
     */
    List<Order> getOrdersWithoutDeleted();

    /**
     * Loads order by it's unique user defined number.
     * @param number
     * @return
     */
    Order getOrderByNumber(long number);

    /**
     * Returns next available order number for the order.
     * @return
     */
    long getNextAvailableOrderNumber();

    /**
     * Returns next available number of the item in the given order.
     * @param orderId
     * @return
     */
    long getNextAvailableOrderItemNumber(long orderId);

    /**
     * Loads order item by it's id.
     * @param orderItemId
     * @return
     */
    OrderItem getOrderItem(long orderItemId);

    /**
     * Get all items in order.
     * @return all items in order.
     */
    List<OrderItem> getOrderItems(Order order);

    /**
     * Saves order item.
     * @param orderItem
     */
    void saveOrderItem(OrderItem orderItem);

    /**
     * Deletes given order item.
     * @param orderItem
     */
    void deleteOrderItem(OrderItem orderItem);
}
