/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.complecting.UncomplectingTask;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.complecting.UncomplectingTaskTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 13.06.2009
 */
public final class UncomplectingTaskTransformer {
    private UncomplectingTaskTransformer(){
    }

    public static UncomplectingTaskTO transform(UncomplectingTask task) {
        UncomplectingTaskTO taskTO = new UncomplectingTaskTO();
        update(taskTO, task);
        return taskTO;
    }

    public static List<UncomplectingTaskTO> transformList(List<UncomplectingTask> tasks) {
        List<UncomplectingTaskTO> tasksTO = new ArrayList<UncomplectingTaskTO>();
        for (UncomplectingTask task : tasks){
            tasksTO.add(transform(task));            
        }
        return tasksTO;
    }

    public static UncomplectingTask transform(UncomplectingTaskTO taskTO) {
        UncomplectingTask task = SpringServiceContext.getInstance().getUncomplectingTaskService().getTaskById(taskTO.getId());
        if (task == null){
            task = new UncomplectingTask();
        }
        return task;
    }

    public static void update(UncomplectingTaskTO taskTO, UncomplectingTask task) {
        taskTO.setId(task.getId());
        taskTO.setType(task.getType());
        taskTO.setOrderSubItem(OrdersTransformer.transformSubItem(task.getOrderSubItem()));
        taskTO.setState(task.getState());
        taskTO.setCountToProcess(task.getCountToProcess());
        taskTO.setWorker(UserTransformer.transformUser(task.getWorker()));
        taskTO.setCreateDate(task.getCreateDate());
        taskTO.setDoneDate(task.getDoneDate());
    }

    private static void update(UncomplectingTask task, UncomplectingTaskTO taskTO) {
        task.setType(taskTO.getType());
        task.setOrderSubItem(OrdersTransformer.transformSubItem(taskTO.getOrderSubItem()));
        task.setState(taskTO.getState());
        task.setCountToProcess(taskTO.getCountToProcess());
        task.setWorker(UserTransformer.transformUser(taskTO.getWorker()));
        task.setCreateDate(taskTO.getCreateDate());
        task.setDoneDate(taskTO.getDoneDate());
    }
}
