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
import com.artigile.warehouse.bl.complecting.ComplectingTaskChangeAdapter;
import com.artigile.warehouse.bl.complecting.ComplectingTaskService;
import com.artigile.warehouse.domain.complecting.ComplectingTask;
import com.artigile.warehouse.domain.complecting.ComplectingTaskState;
import com.artigile.warehouse.domain.orders.OrderSubItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Shyrik, 12.12.2009
 */

/**
 * Class for synchronization of orders with changes, mane in complecting tasks.
 */
@Transactional(rollbackFor = BusinessException.class)
public class OrderWithComplectingTasksSynchronizer extends ComplectingTaskChangeAdapter {
    private ComplectingTaskService complectingTaskService;
    private OrderService orderService;

    //======================== Construction and initialization =============================
    public OrderWithComplectingTasksSynchronizer(){
    }

    public void initialize(){
        complectingTaskService.addListener(this);
    }

    //============================ ComplectingTaskChangeAdapter =============================
    @Override
    public void onComplectingTasksStateChanged(List<ComplectingTask> tasks, ComplectingTaskState oldState, ComplectingTaskState newState) throws BusinessException {
        for (ComplectingTask task : tasks){
            OrderSubItem orderSubItem = task.getOrderSubItem();
            if (orderSubItem != null){
                orderService.recalculateOrderSubItemState(orderSubItem);
            }
        }
    }

    //===================================== Spring setters ================================
    public void setComplectingTaskService(ComplectingTaskService complectingTaskService) {
        this.complectingTaskService = complectingTaskService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
