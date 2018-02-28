/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.inventorization.inventorization;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.inventorization.task.InventorizationTaskChangeAdapter;
import com.artigile.warehouse.bl.inventorization.task.InventorizationTaskService;
import com.artigile.warehouse.domain.inventorization.Inventorization;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTask;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Shyrik, 25.10.2009
 */

/**
 * This clas is intendedfor synchronization inventorization with other document types.
 * It work's like mediator + listener to remove dependencies between inventorization
 * and other documents.
 */
@Transactional(rollbackFor = BusinessException.class)
public class InventorizationSynchronizer extends InventorizationTaskChangeAdapter {

    /**
     * For using methods of inventorization task service
     */
    private InventorizationTaskService inventorizationTaskService;

    public InventorizationSynchronizer() { /* Default constructor */ }

    public void initialize(){
        //This class listens for changes made in inventorization tasks.
        inventorizationTaskService.addListener(this);        
    }

    //======================= Synchronization with inventrization tasks ==========================
    @Override
    public void onTasksStateChaged(List<InventorizationTask> tasks) throws BusinessException {
        Set<Inventorization> affectedInventorizations = new HashSet<Inventorization>();
        //1. Refresh inventorization items, connected with changed inventorization tasks.
        for (InventorizationTask task : tasks){
            task.getInventorizationItem().refreshProcessingState();
            affectedInventorizations.add(task.getInventorizationItem().getInventorization());
        }
        //2. Refresh affected inventorizations.
        for (Inventorization inventorization : affectedInventorizations){
            inventorization.refreshProcessingState();
        }
    }

    //================================ Spring setters ================================
    public void setInventorizationTaskService(InventorizationTaskService inventorizationTaskService) {
        this.inventorizationTaskService = inventorizationTaskService;
    }
}
