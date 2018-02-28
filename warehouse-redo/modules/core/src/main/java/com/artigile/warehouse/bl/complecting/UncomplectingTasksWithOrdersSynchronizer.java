/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.complecting;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.orders.OrderChangeAdapter;
import com.artigile.warehouse.bl.orders.OrderService;
import com.artigile.warehouse.domain.complecting.UncomplectingTask;
import com.artigile.warehouse.domain.complecting.UncomplectingTaskState;
import com.artigile.warehouse.domain.orders.Order;
import com.artigile.warehouse.domain.orders.OrderItem;
import com.artigile.warehouse.domain.orders.OrderState;
import com.artigile.warehouse.domain.orders.OrderSubItem;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Valery Barysok, 10.03.2010
 */

/**
 * Class for synchronization uncomplecting tasks with orders.
 */
@Transactional(rollbackFor = BusinessException.class)
public class UncomplectingTasksWithOrdersSynchronizer extends OrderChangeAdapter {
    
    private OrderService ordersService;

    private UncomplectingTaskService uncomplectingTaskService;

    public UncomplectingTasksWithOrdersSynchronizer() {
    }

    public void initialize(){
        ordersService.addListener(this);
    }

    @Override
    public void onOrderStateChanged(Order order, OrderState oldState, OrderState newState) throws BusinessException {
        if (order.getState() == OrderState.SHIPPED) {
            closeUncomplectingTasksForOrder(order);
        }
    }

    @Override
    public void onBeforeOrderDeleted(Order order) throws BusinessException {
        for (OrderItem orderItem : ordersService.getOrderItems(order)) {
            for (OrderSubItem orderSubItem : orderItem.getSubItems()) {
                for (UncomplectingTask uncomplectingTask : orderSubItem.getUncomplectingTasks()) {
                    uncomplectingTask.setState(UncomplectingTaskState.CLOSED);
                }
            }
        }
    }

    /**
     * Iterate through all order items where for each order item
     * iterate through all order subitems where for each order subitem
     * iterate through all uncomplecting task and turn him into
     * "close" state if it already in state "processed"
     * @param order
     */
    private void closeUncomplectingTasksForOrder(Order order) {
        for (OrderItem item : order.getItems()) {
            if (!item.isDetail()) {
                continue;
            }
            for (OrderSubItem subItem : item.getSubItems()){
                for (UncomplectingTask task : subItem.getUncomplectingTasks()) {
                    uncomplectingTaskService.updateTaskState(task);
                }
            }
        }
    }

    //============================ Spring setters =============================
    public void setOrdersService(OrderService ordersService) {
        this.ordersService = ordersService;
    }

    public void setUncomplectingTaskService(UncomplectingTaskService uncomplectingTaskService) {
        this.uncomplectingTaskService = uncomplectingTaskService;
    }
}
