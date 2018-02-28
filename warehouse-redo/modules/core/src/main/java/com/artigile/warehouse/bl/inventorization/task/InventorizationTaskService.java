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
import com.artigile.warehouse.bl.common.verifications.Verification;
import com.artigile.warehouse.bl.common.verifications.Verifications;
import com.artigile.warehouse.dao.InventorizationTaskDAO;
import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.inventorization.InventorizationItem;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTask;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTaskState;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTaskType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.dto.inventorization.task.InventorizationTaskTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.transofmers.InventorizationTaskTransformer;
import com.artigile.warehouse.utils.transofmers.UserTransformer;
import com.artigile.warehouse.utils.transofmers.WarehouseTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Borisok V.V., 02.10.2009
 */
@Transactional(rollbackFor = BusinessException.class)
public class InventorizationTaskService {

    private InventorizationTaskDAO inventorizationTaskDAO;

    //======================== Constructors and initializers  ==========================
    public InventorizationTaskService() { /* Default constructor */ }

    //===================================== Listeners support ==========================
    private ArrayList<InventorizationTaskChangeListener> listeners = new ArrayList<InventorizationTaskChangeListener>();

    public void addListener(InventorizationTaskChangeListener listener){
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    private void fireTaskStateChaged(List<InventorizationTask> tasks) throws BusinessException {
        for (InventorizationTaskChangeListener listener : listeners){
            listener.onTasksStateChaged(tasks);
        }
    }

    //==================================== Operations ==================================
    /**
     * Loads list of inventorization tasks for given warehouse.
     * @param warehouse - warehouse for filtering tasks list. May be null, if tasks for all warehouses are required.
     * @return
     */
    public List<InventorizationTaskTO> getListForWarehouse(WarehouseTOForReport warehouse) {
        List<InventorizationTask> list;
        if (warehouse != null){
            // List of tasks for concrete warehouse.
            list = inventorizationTaskDAO.getActualListForWarehouse(WarehouseTransformer.transform(warehouse));
        }
        else{
            // List of all tasks.
            list = inventorizationTaskDAO.getAllActualTasks();
        }
        return InventorizationTaskTransformer.transformList(list);
    }

    /**
     * Tries to begin processing for gived inventorization tasks.
     * @param tasksTO list of task to begin.
     */
    public void beginInventorizationTasks(List<InventorizationTaskTO> tasksTO) throws BusinessException {
        //1. At first we are to validate, if inventorization tasks are valid to be processed.
        Verification verification = new BeforeBeginInventorizationTaskVerification();
        List<InventorizationTask> tasks = new ArrayList<InventorizationTask>();

        for (InventorizationTaskTO taskTO : tasksTO){
            InventorizationTask task = InventorizationTaskTransformer.transform(taskTO);
            Verifications.ensureVerificationPasses(task, verification);
            tasks.add(task);
        }

        //2. Changing states of inventorization items, linked with inventorization tasks, because inventorization items
        // state has the same meaning, that inventorization tasks state.
        User worker = UserTransformer.transformUser(WareHouse.getUserSession().getUser());
        Date today = Calendar.getInstance().getTime();

        for (InventorizationTask task : tasks) {
            //Saving new task state.
            task.setState(InventorizationTaskState.IN_PROCESS);
            task.setWorker(worker);
            task.setWorkBegin(today);
            inventorizationTaskDAO.save(task);
        }
        //Notification about changing of task state.
        fireTaskStateChaged(tasks);
    }

    /**
     * Tries to mark inventorization tasks as completed.
     * @param tasksTO list of tasks to become completed.
     */
    public void completeInventorizationTasks(List<InventorizationTaskTO> tasksTO) throws BusinessException {
        //1. At first we are to validate, if inventorization tasks are valid to be completed.
        Verification verification = new BeforeCompleteInventorizationTaskVerification();
        List<InventorizationTask> tasks = new ArrayList<InventorizationTask>();

        for (InventorizationTaskTO taskTO : tasksTO){
            InventorizationTask task = InventorizationTaskTransformer.transform(taskTO);
            Verifications.ensureVerificationPasses(task, verification);
            tasks.add(task);
        }

        //2. Changing states of inventorization items, linked with inventorization tasks, because inventorization items
        // state has the same meaning, that inventorization tasks state.
        User worker = UserTransformer.transformUser(WareHouse.getUserSession().getUser());
        Date today = Calendar.getInstance().getTime();

        for (InventorizationTask task : tasks) {
            //Saving new task state.
            task.setState(InventorizationTaskState.PROCESSED);
            task.setWorker(worker);
            task.setWorkEnd(today);
            inventorizationTaskDAO.save(task);
        }
        //Notification about changing of task state.
        fireTaskStateChaged(tasks);
    }

    /**
     * Tries to cancel processing for gived inventorization tasks items.
     * @param tasksTO - list of task to begin inventorization.
     */
    public void cancelInventorizationTasks(List<InventorizationTaskTO> tasksTO) throws BusinessException {
        //1. At first we are to validate, if inventorization tasks are valid to be cancelled.
        Verification verification = new BeforeCancelInventorizationTaskVerification();
        List<InventorizationTask> tasks = new ArrayList<InventorizationTask>();

        for (InventorizationTaskTO taskTO : tasksTO){
            InventorizationTask task = InventorizationTaskTransformer.transform(taskTO);
            Verifications.ensureVerificationPasses(task, verification);
            tasks.add(task);
        }

        //2. Changing states of inventorization items, linked with inventorization tasks, because order items state are
        //has the same meaning, that inventorization tasks state.
        for (InventorizationTask task : tasks){
            //Saving new task state.
            task.setState(InventorizationTaskState.NOT_PROCESSED);
            task.setProcessingResult(null);
            task.setWorker(null);
            task.setWorkBegin(null);
            task.setFoundCount(null);
            inventorizationTaskDAO.save(task);
        }
        //Notification about changing of task state.
        fireTaskStateChaged(tasks);
    }

    /**
     * This method is used to perform operations with tasks just after they have been printed.
     * @param tasks
     */
    public void postPrintTasks(List<InventorizationTaskTO> tasks) {
        for (InventorizationTaskTO taskTO : tasks){
            if (!taskTO.getPrinted()){
                //We need to update printed state of non printed tasks.
                InventorizationTask task = inventorizationTaskDAO.get(taskTO.getId());
                task.setPrinted(true);
                inventorizationTaskDAO.save(task);
                InventorizationTaskTransformer.update(taskTO, task);
            }
        }
    }

    /**
     * This method is used to
     * @param taskTO - inventorization task, which found count has beed changed.
     * @param newCount - new found value value.
     */
    public void saveNewTaskFoundCount(InventorizationTaskTO taskTO, Long newCount) {
        InventorizationTask task = inventorizationTaskDAO.get(taskTO.getId());
        task.setFoundCountAndRefresh(newCount);
        inventorizationTaskDAO.save(task);
        InventorizationTaskTransformer.update(taskTO, task);
    }

    /**
     * Creates inventorization task.
     * @param taskType - type (warehouse worker's action) of inventorization task.
     * @param item - inventorization item which used for creating task.
     */
    public InventorizationTask createTask(InventorizationTaskType taskType, InventorizationItem item) {
        InventorizationTask task = new InventorizationTask();

        task.setInventorizationType(taskType);
        task.setState(InventorizationTaskState.getInitialState());
        task.setInventorizationItem(item);
        task.setDetailBatch(item.getDetailBatch());
        task.setCountMeas(item.getCountMeas());
        task.setStoragePlace(item.getStoragePlace());
        task.setNumber(inventorizationTaskDAO.getNextAvailableNumber());
        task.setWorkCreate(Calendar.getInstance().getTime());
        inventorizationTaskDAO.save(task);

        return task;
    }

    public InventorizationTask getTaskById(long taskId) {
        return inventorizationTaskDAO.get(taskId);
    }

    //=================================== Spring setters =====================================
    public void setInventorizationTaskDAO(InventorizationTaskDAO inventorizationTaskDAO) {
        this.inventorizationTaskDAO = inventorizationTaskDAO;
    }
}
