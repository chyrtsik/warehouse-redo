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

import com.artigile.warehouse.domain.inventorization.task.InventorizationTask;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.inventorization.InventorizationItemTO;
import com.artigile.warehouse.utils.dto.inventorization.task.InventorizationTaskTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 02.10.2009
 */
public final class InventorizationTaskTransformer {
    private InventorizationTaskTransformer() {
    }

    public static InventorizationTaskTO transform(InventorizationTask task) {
        if (task == null){
            return null;
        }
        InventorizationTaskTO taskTO = new InventorizationTaskTO();
        taskTO.setInventorizationItem(InventorizationItemTransformer.transform(task.getInventorizationItem(), taskTO));
        update(taskTO, task);
        return taskTO;
    }

    /**
     * This method eliminates cycling when transforming pairs InventorizationTask<-->InventorizationItem.
     * @param task
     * @param itemTO
     * @return
     */
    public static InventorizationTaskTO transform(InventorizationTask task, InventorizationItemTO itemTO) {
        if (task == null){
            return null;
        }
        InventorizationTaskTO taskTO = new InventorizationTaskTO();
        taskTO.setInventorizationItem(itemTO);
        update(taskTO, task);
        return taskTO;
    }

    public static List<InventorizationTaskTO> transformList(List<InventorizationTask> tasks) {
        List<InventorizationTaskTO> tasksTO = new ArrayList<InventorizationTaskTO>();
        for (InventorizationTask task : tasks) {
            tasksTO.add(transform(task));
        }
        return tasksTO;
    }

    public static InventorizationTask transform(InventorizationTaskTO taskTO) {
        if (taskTO == null){
            return null;
        }
        InventorizationTask task = SpringServiceContext.getInstance().getInventorizationTaskService().getTaskById(taskTO.getId());
        if (task == null){
            task = new InventorizationTask();
        }
        return task;
    }

    /**
     * Synchronizes DTO with entity.
     * @param taskTO (in, out) -- DTO to be updated.
     * @param task (in) -- Entity with fresh data.
     */
    public static void update(InventorizationTaskTO taskTO, InventorizationTask task) {
        taskTO.setId(task.getId());
        taskTO.setNumber(task.getNumber());
        taskTO.setState(task.getState());
        taskTO.setInventorizationType(task.getInventorizationType());
        taskTO.setProcessingResult(task.getProcessingResult());
        taskTO.setDetailBatch(DetailBatchTransformer.batchTO(task.getDetailBatch()));
        taskTO.setStoragePlace(StoragePlaceTransformer.transformForReport(task.getStoragePlace()));
        taskTO.setCountMeas(MeasureUnitTransformer.transform(task.getCountMeas()));
        taskTO.setFoundCount(task.getFoundCount());
        taskTO.setPrinted(task.getPrinted());
        taskTO.setWorker(UserTransformer.transformUser(task.getWorker()));
        taskTO.setWorkBegin(task.getWorkBegin());
        taskTO.setWorkEnd(task.getWorkEnd());
    }

    /**
     * Synchonizes entity with DTO.
     * @param task (in, out) -- entity to be updated.
     * @param taskTO (in) -- DTO with fresh data.
     */
    public static void update(InventorizationTask task, InventorizationTaskTO taskTO) {
        task.setNumber(taskTO.getNumber());
        task.setState(taskTO.getState());
        task.setInventorizationType(taskTO.getInventorizationType());
        task.setProcessingResult(taskTO.getProcessingResult());
        task.setDetailBatch(DetailBatchTransformer.batch(taskTO.getDetailBatch()));
        task.setStoragePlace(StoragePlaceTransformer.transform(taskTO.getStoragePlace()));
        task.setCountMeas(MeasureUnitTransformer.transform(taskTO.getCountMeas()));
        task.setFoundCount(taskTO.getFoundCount());
        task.setPrinted(taskTO.getPrinted());
        task.setWorker(UserTransformer.transformUser(taskTO.getWorker()));
        task.setWorkBegin(taskTO.getWorkBegin());
        task.setWorkEnd(taskTO.getWorkEnd());
    }
}
