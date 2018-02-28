/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.inventorization.task;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.inventorization.inventorization.InventorizationChangeAdapter;
import com.artigile.warehouse.bl.inventorization.inventorization.InventorizationService;
import com.artigile.warehouse.domain.inventorization.Inventorization;
import com.artigile.warehouse.domain.inventorization.InventorizationItem;
import com.artigile.warehouse.domain.inventorization.InventorizationState;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTask;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTaskState;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTaskType;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Shyrik, 25.10.2009
 */

/**
 * This class reacts on changes in other documents to perform appropriate
 * actions on inventorization tasks.
 */
@Transactional(rollbackFor = BusinessException.class)
public class InventorizationTaskSynchronizer extends InventorizationChangeAdapter{
    
    private InventorizationService inventorizationService;

    private InventorizationTaskService inventorizationTaskService;

    public InventorizationTaskSynchronizer() { /* Default constructor */ }

    public void initialize(){
        //Listens for changes made in inventorizations to synchronize tasks with them.
        inventorizationService.addListener(this);
    }

    //=================== Synchronization with inventorization ==================================
    @Override
    public void onInventorizationStateChanged(Inventorization inventorization, InventorizationState oldState, InventorizationState newState) throws BusinessException {
        if (newState.equals(InventorizationState.IN_PROCESS)) {
            // make up to date inventorization items before creaing inventorization tasks
            for (InventorizationItem item : inventorization.getItems()) {
                item.setNeededCount(item.getWarehouseBatch().getAmount());
            }
            //When inventarization begins, we should create appropriate tasks for it.
            for (InventorizationItem item : inventorization.getItems()) {
                InventorizationTask task = inventorizationTaskService.createTask(InventorizationTaskType.INVENTORIZATION, item);
                item.setInventorizationTask(task);
            }
        }
        else if (newState.equals(InventorizationState.CLOSED)) {
            //When inventorization closed, inventorization tasks also should be closed.
            for (InventorizationItem item : inventorization.getItems()) {
               item.getInventorizationTask().setState(InventorizationTaskState.CLOSED);
            }
        }
   }

    //=============================== Spring setters ==================================
    public void setInventorizationService(InventorizationService inventorizationService) {
        this.inventorizationService = inventorizationService;
    }

    public void setInventorizationTaskService(InventorizationTaskService inventorizationTaskService) {
        this.inventorizationTaskService = inventorizationTaskService;
    }
}
