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

import com.artigile.warehouse.domain.complecting.ComplectingTask;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.complecting.ComplectingTaskTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 28.04.2009
 */
public final class ComplectingTaskTransformer {
    private ComplectingTaskTransformer(){
    }

    public static ComplectingTaskTO transform(ComplectingTask task) {
        ComplectingTaskTO taskTO = new ComplectingTaskTO();
        update(taskTO, task);
        return taskTO;
    }

    public static List<ComplectingTaskTO> transformList(List<ComplectingTask> tasks) {
        List<ComplectingTaskTO> tasksTO = new ArrayList<ComplectingTaskTO>();
        for (ComplectingTask task : tasks){
            tasksTO.add(transform(task));            
        }
        return tasksTO;
    }

    public static ComplectingTask transform(ComplectingTaskTO taskTO) {
        ComplectingTask task = SpringServiceContext.getInstance().getComplectingTaskService().getTaskById(taskTO.getId());
        if (task == null){
            task = new ComplectingTask();
        }
        return task;
    }

    public static void update(ComplectingTaskTO taskTO, ComplectingTask task) {
        taskTO.setId(task.getId());
        taskTO.setState(task.getState());
        taskTO.setNeededCount(task.getNeededCount());
        taskTO.setFoundCount(task.getFoundCount());
        taskTO.setOrderSubItem(OrdersTransformer.transformSubItem(task.getOrderSubItem()));
        taskTO.setMovementItem(MovementItemTransformer.transform(task.getMovementItem()));
        taskTO.setPrinted(task.isPrinted());
        taskTO.setStickerPrinted(task.isStickerPrinted());
        taskTO.setWorker(UserTransformer.transformUser(task.getWorker()));
        taskTO.setWorkBegin(task.getWorkBegin());
        taskTO.setWorkEnd(task.getWorkEnd());
    }

    public static void update(ComplectingTask task, ComplectingTaskTO taskTO) {
        task.setId(taskTO.getId());
        task.setState(taskTO.getState());
        task.setNeededCount(taskTO.getNeededCount());
        task.setFoundCount(taskTO.getFoundCount());
        task.setOrderSubItem(OrdersTransformer.transformSubItem(taskTO.getOrderSubItem()));
        task.setMovementItem(MovementItemTransformer.transform(taskTO.getMovementItem()));
        task.setPrinted(taskTO.getPrinted());
        task.setStickerPrinted(taskTO.getStickerPrinted());
        task.setWorker(UserTransformer.transformUser(taskTO.getWorker()));
        task.setWorkBegin(taskTO.getWorkBegin());
        task.setWorkEnd(taskTO.getWorkEnd());
    }
}
