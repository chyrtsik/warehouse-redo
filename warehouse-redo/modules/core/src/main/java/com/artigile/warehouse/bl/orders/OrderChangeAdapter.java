/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.orders;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.domain.orders.*;

/**
 * @author: Vadim.Zverugo
 */
public class OrderChangeAdapter implements OrderChangeListener {

    /**
     * Called, when order state has jast been changed.
     * @param order order, which state has been changed.
     * @param oldState old state of the order.
     * @param newState new state of the order.
     * @throws BusinessException may be thrown by listener if he cannot perform operation, related to this event.
     */
    @Override
    public void onOrderStateChanged(Order order, OrderState oldState, OrderState newState) throws BusinessException {
      
    }

    /**
     * Called when new order item has beed added to the order.
     * @param orderItem
     * @throws BusinessException - may be thrown by listener if he cannot perform operation, related to this event.
     */
    @Override
    public void onOrderItemAdded(OrderItem orderItem) throws BusinessException {

    }

    /**
     * Called when order item is ready to be deleted.
     * @param orderItem - deleted order item.
     * @throws BusinessException may be thrown by listener if he cannot perform operation, related to this event.
     */
    @Override
    public void onBeforeOrderItemDeleted(OrderItem orderItem) throws BusinessException {

    }

    /**
     * Called when order item changed.
     * @param orderItem - changed order item.
     * @throws BusinessException may be thrown by listener if he cannot perform operation, related to this event.
     */
    @Override
    public void onOrderItemChanged(OrderItem orderItem) throws BusinessException {

    }

    /**
     * Called, when order item state has just been changed.
     * @param orderItem order, which state has been changed.
     * @param oldState old state of the order.
     * @param newState new state of the order.
     * @throws BusinessException may be thrown by listener if he cannot perform operation, related to this event.
     */
    @Override
    public void onOrderItemStateChanged(OrderItem orderItem, OrderItemState oldState, OrderItemState newState) throws BusinessException {

    }

    /**
     * Called when order sub items is ready deleted.
     * @param orderSubItem - order sub item to be deleted.
     * @throws BusinessException may be thrown by listener if he cannot perform operation, related to this event.
     */
    @Override
    public void onOrderSubItemDeleted(OrderSubItem orderSubItem) throws BusinessException {

    }

    /**
     * Called when order is deleted.
     * @param order which is deleted
     * @throws BusinessException may be thrown by listener if he cannot perform operation, related to this event.
     */
    @Override
    public void onBeforeOrderDeleted(Order order) throws BusinessException {

    }
}
