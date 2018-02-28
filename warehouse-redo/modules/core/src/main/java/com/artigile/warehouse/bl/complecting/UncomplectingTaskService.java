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
import com.artigile.warehouse.dao.UncomplectingTaskDAO;
import com.artigile.warehouse.domain.complecting.UncomplectingTask;
import com.artigile.warehouse.domain.complecting.UncomplectingTaskState;
import com.artigile.warehouse.domain.complecting.UncomplectingTaskType;
import com.artigile.warehouse.domain.orders.Order;
import com.artigile.warehouse.domain.orders.OrderState;
import com.artigile.warehouse.domain.orders.OrderSubItem;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.complecting.UncomplectingTaskTO;
import com.artigile.warehouse.utils.transofmers.UncomplectingTaskTransformer;
import com.artigile.warehouse.utils.transofmers.UserTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Shyrik, 13.06.2009
 */
@Transactional(rollbackFor = BusinessException.class)
public class UncomplectingTaskService {
    
    private UncomplectingTaskDAO uncomplectingTaskDAO;

    public UncomplectingTaskService(){
    }

    /**
     * Loads list of uncomplecting tasks for given warehouse.
     * @param filter filter for filtering tasks list. May be null, if tasks for all uncomplecting tasks are required.
     * @return
     */
    public List<UncomplectingTaskTO> getListByFilter(UncomplectingTaskFilter filter) {
        if (filter != null){
            //List of tasks for concrete warehouse.
            return UncomplectingTaskTransformer.transformList(uncomplectingTaskDAO.getListByFilter(filter));
        }
        else{
            //List of all tasks.
            return UncomplectingTaskTransformer.transformList(uncomplectingTaskDAO.getAll());
        }
    }

    /**
     * Changes state of uncomplecting task.
     * @param taskTO - task to be changed.
     * @param newState - new state to be set to the task.
     */
    public void changeTaskState(UncomplectingTaskTO taskTO, UserTO workerTO, UncomplectingTaskState newState) {
        UncomplectingTask task = uncomplectingTaskDAO.get(taskTO.getId());
        task.setState(newState);
        if (task.getState() == UncomplectingTaskState.PROCESSED){
            //Task has been done. We need to save, who and when has done it.
            task.setDoneDate(new Date());
            task.setWorker(UserTransformer.transformUser(workerTO));
            updateTaskState(task);
        }
        else if (task.getState() == UncomplectingTaskState.NOT_PROCESSED){
            //Task has been marked as not done.
            task.setDoneDate(null);
            task.setWorker(null);
        }
        else {
            throw new RuntimeException("UncomplectingTaskService.changeTaskState: unsupported task state");
        }
        uncomplectingTaskDAO.save(task);
        UncomplectingTaskTransformer.update(taskTO, task);
    }

    public void updateTaskState(UncomplectingTask task) {
        if (task.getState() == UncomplectingTaskState.PROCESSED) {
            Order order = task.getOrderSubItem().getOrderItem().getOrder();
            OrderState orderState = order.getState();
            if (!orderState.isBefore(OrderState.SHIPPED) || orderState == OrderState.SHIPPED) {
                task.setState(UncomplectingTaskState.CLOSED);
            }
        }
    }

    public UncomplectingTask getTaskById(long id) {
        return uncomplectingTaskDAO.get(id);
    }

    /**
     * Creates uncomplecting task for given order sub item.
     * @param orderSubItem - order sub item, for which complecting task it to be created.
     * @param taskType - type (warehouse worker's action) of complecting task.
     * @param countToProcess - amount of wares to be processed in new task.
     */
    public void createTask(OrderSubItem orderSubItem, UncomplectingTaskType taskType, long countToProcess) {
        UncomplectingTask task = new UncomplectingTask();
        task.setType(taskType);
        task.setOrderSubItem(orderSubItem);
        task.setState(UncomplectingTaskState.getInitialState());
        task.setCountToProcess(countToProcess);
        task.setCreateDate(new Date());
        uncomplectingTaskDAO.save(task);
    }

    /**
     * Removes uncomplecting task from the database.
     * @param uncomplectingTask
     */
    public void deleteTask(UncomplectingTask uncomplectingTask) {
        uncomplectingTaskDAO.remove(uncomplectingTask);
    }

    //=================================== Spring setters =====================================
    public void setUncomplectingTaskDAO(UncomplectingTaskDAO uncomplectingTaskDAO) {
        this.uncomplectingTaskDAO = uncomplectingTaskDAO;
    }
}
