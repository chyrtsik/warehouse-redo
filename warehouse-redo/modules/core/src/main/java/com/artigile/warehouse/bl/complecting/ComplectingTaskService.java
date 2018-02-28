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
import com.artigile.warehouse.bl.common.verifications.Verification;
import com.artigile.warehouse.bl.common.verifications.VerificationResult;
import com.artigile.warehouse.bl.common.verifications.Verifications;
import com.artigile.warehouse.dao.ComplectingTaskDAO;
import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.complecting.ComplectingTask;
import com.artigile.warehouse.domain.complecting.ComplectingTaskState;
import com.artigile.warehouse.domain.movement.MovementItem;
import com.artigile.warehouse.domain.orders.OrderSubItem;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.dto.complecting.ComplectingTaskTO;
import com.artigile.warehouse.utils.transofmers.ComplectingTaskTransformer;
import com.artigile.warehouse.utils.transofmers.UserTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Shyrik, 28.04.2009
 */
@Transactional(rollbackFor = BusinessException.class)
public class ComplectingTaskService {
    
    private ComplectingTaskDAO complectingTaskDAO;

    //================================ Constructors ===========================================
    public ComplectingTaskService(){
    }

    //=============================== Listeners support =====================================
    private ArrayList<ComplectingTaskChangeListener> listeners = new ArrayList<ComplectingTaskChangeListener>();

    public void addListener(ComplectingTaskChangeListener listener){
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    public void removeListener(ComplectingTaskChangeListener listener){
        listeners.remove(listener);
    }

    void fireComplectingTaskStateChanged(ComplectingTask task, ComplectingTaskState oldState, ComplectingTaskState newState) throws BusinessException {
        List<ComplectingTask> tasks = new ArrayList<ComplectingTask>();
        tasks.add(task);
        fireComplectingTasksStateChanged(tasks, oldState, newState);
    }
    
    private void fireComplectingTasksStateChanged(List<ComplectingTask> tasks, ComplectingTaskState oldState, ComplectingTaskState newState) throws BusinessException {
        for (ComplectingTaskChangeListener listener : listeners){
            listener.onComplectingTasksStateChanged(tasks, oldState, newState);
        }
    }

    //============================= Operations =========================================================
    /**
     * Loads list of complecting tasks for given warehouse.
     * @param filter filter for filtering tasks list. May be null, if tasks for all complecting tasks are required.
     * @return
     */
    public List<ComplectingTaskTO> getListByFilter(ComplectingTaskFilter filter) {
        if (filter != null){
            //List of tasks for concrete warehouse.
            return ComplectingTaskTransformer.transformList(complectingTaskDAO.getListByFilter(filter));
        }
        else{
            //List of all tasks.
            return ComplectingTaskTransformer.transformList(complectingTaskDAO.getAll()); 
        }
    }

    /**
     * Tries to begin complecting process for given complecting tasks.
     * @param tasksTO list of task to begin complecting.
     */
    public void beginComplecting(List<ComplectingTaskTO> tasksTO) throws BusinessException {
        //1. At first we are to validate, if complecting tasks are valid to be being complected.
        Verification verification = new BeforeBeginComplectingVerification();
        List<ComplectingTask> tasks = new ArrayList<ComplectingTask>();

        for (ComplectingTaskTO taskTO : tasksTO){
            ComplectingTask task = ComplectingTaskTransformer.transform(taskTO);
            ComplectingTaskTransformer.update(task, taskTO);

            Verifications.ensureVerificationPasses(task, verification);
            tasks.add(task);
        }

        //2. Make appropriate changes in complecting tasks.
        User worker = UserTransformer.transformUser(WareHouse.getUserSession().getUser()); //TODO: refactor to eliminate query into presentation layer!!!
        Date today = Calendar.getInstance().getTime();

        for (ComplectingTask task : tasks){
            ComplectingTaskState oldState = task.getState();

            task.setState(ComplectingTaskState.PROCESSING);
            task.setWorker(worker);
            task.setWorkBegin(today);
            complectingTaskDAO.save(task);

            fireComplectingTaskStateChanged(task, oldState, task.getState());
        }
    }

    /**
     * Tries to cancel complecting process for given complecting tasks.
     * @param tasksTO list of tasks to cancel complecting.
     */
    public void cancelComplecting(List<ComplectingTaskTO> tasksTO) throws BusinessException {
        //1. At first we are to validate, if complecting tasks are valid to be cancelled.
        Verification verification = new BeforeCancelComplectingVerification();
        List<ComplectingTask> tasks = new ArrayList<ComplectingTask>();

        for (ComplectingTaskTO taskTO : tasksTO){
            ComplectingTask task = ComplectingTaskTransformer.transform(taskTO);
            ComplectingTaskTransformer.update(task, taskTO);

            Verifications.ensureVerificationPasses(task, verification);
            tasks.add(task);
        }

        //2. Make appropriate changes in complecting tasks.
        for (ComplectingTask task : tasks){
            ComplectingTaskState oldState = task.getState();

            task.setState(ComplectingTaskState.NOT_PROCESSED);
            task.setWorker(null);
            task.setWorkBegin(null);
            task.setFoundCount(null);
            complectingTaskDAO.save(task);

            fireComplectingTaskStateChanged(task, oldState, task.getState());
        }
    }

    /**
     * Make complecting tasks ready for shipping.
     * @param taskIds task ids to be places into ready for shipping list.
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException
     */
    public void makeReadyForShipping(List<Long> taskIds) throws BusinessException {
        //1. At first we are to validate, if complecting tasks are valid to become ready for shipping.
        Verification verification = new BeforeMakeReadyForShippingVerification();
        List<ComplectingTask> tasks = new ArrayList<ComplectingTask>();

        for (Long taskId : taskIds) {
            ComplectingTask task = complectingTaskDAO.get(taskId);
            VerificationResult result = Verifications.performVerification(task, verification);
            if (result.isFailed()) {
                throw new BusinessException(result.getFailReason());
            }
            tasks.add(task);
        }

        //2. Changing states complecting tasks.
        for (ComplectingTask task : tasks) {
            ComplectingTaskState oldState = task.getState();

            task.setState(ComplectingTaskState.READY_FOR_SHIPPING);
            complectingTaskDAO.save(task);

            fireComplectingTaskStateChanged(task, oldState, task.getState());
        }
    }

    /**
     * This method is used to perform operations with tasks just after they have been printed.
     * @param tasks
     */
    public void postPrintTasks(List<ComplectingTaskTO> tasks) {
        for (ComplectingTaskTO taskTO : tasks){
            if (!taskTO.getPrinted()){
                //We need to update printed state of non printed tasks.
                ComplectingTask task = complectingTaskDAO.get(taskTO.getId());
                task.setPrinted(true);
                complectingTaskDAO.save(task);
                ComplectingTaskTransformer.update(taskTO, task);
            }
        }
    }

    /**
     * This method is used to perform operations with stickers just after they have been printed.
     * @param stickers
     */
    public void postPrintStickers(List<ComplectingTaskTO> stickers) {
        for (ComplectingTaskTO taskTO : stickers){
            if (!taskTO.getStickerPrinted()){
                //We need to update printed state of non printed tasks.
                ComplectingTask task = complectingTaskDAO.get(taskTO.getId());
                task.setStickerPrinted(true);
                complectingTaskDAO.save(task);
                ComplectingTaskTransformer.update(taskTO, task);
            }
        }
    }

    /**
     * This method is used to
     * @param taskTO - complecting task, which found count has beed changed.
     * @param newCount - new found value value.
     */
    public void saveNewTaskFoundCount(ComplectingTaskTO taskTO, Long newCount) throws BusinessException {
        ComplectingTask task = complectingTaskDAO.get(taskTO.getId());
        ComplectingTaskState oldState = task.getState();

        task.setFoundCount(newCount);
        if (newCount == null){
            //Complecting task is considered to be reopened by warehouse worker.
            task.setWorkEnd(null);
            task.setState(ComplectingTaskState.PROCESSING);
        }
        else{
            //Complecting task was finished by warehouse worker.
            task.setWorkEnd(Calendar.getInstance().getTime());
            task.setState(ComplectingTaskState.PROCESSED);
        }
        complectingTaskDAO.save(task);

        fireComplectingTaskStateChanged(task, oldState, task.getState());

        ComplectingTaskTransformer.update(taskTO, task);
    }

    /**
     * Makes given list of complecting tasks shipped from warehouse. Appropriate documents are created
     * automatically.
     * @param complectingTaskIds identifiers of complecting tasks to be shipped.
     */
    public void makeTasksShippedFromWarehouse(List<Long> complectingTaskIds) throws BusinessException{
        ComplectingTaskFilter filter = new ComplectingTaskFilter();
        filter.setTaskIds(complectingTaskIds.toArray(new Long[complectingTaskIds.size()]));
        List<ComplectingTask> tasks = complectingTaskDAO.getListByFilter(filter);

        //1. Check, that all complecting tasks is allowed to be shipped.
        Verification verification = new BeforeComplectingTaskShippingVerification();
        for (ComplectingTask task : tasks){
            Verifications.ensureVerificationPasses(task, verification);
        }

        //2. Change state of complecting tasks.
        for (ComplectingTask task : tasks){
            task.setState(ComplectingTaskState.SHIPPED);
            complectingTaskDAO.save(task);
        }
        fireComplectingTasksStateChanged(tasks, ComplectingTaskState.READY_FOR_SHIPPING, ComplectingTaskState.SHIPPED);
    }

    /**
     * Create new task for processing given order sub item.
     * @param subItem
     */
    public ComplectingTask createComplectingTaskForOrderSubItem(OrderSubItem subItem) throws BusinessException {
        return createComplectingTaskForOrderSubItemDifference(subItem, subItem.getAmount());
    }

    /**
     * Creates complecting task for given order order sub item, ut with given count of wares.
     * @param subItem - order sub item for which task is being created.
     * @param countDiff - count of wares for task.
     * @return
     */
    public ComplectingTask createComplectingTaskForOrderSubItemDifference(OrderSubItem subItem, long countDiff) throws BusinessException {
        ComplectingTask newTask = new ComplectingTask();

        newTask.setState(ComplectingTaskState.getInitialState());
        newTask.setOrderSubItem(subItem);
        newTask.setNeededCount(countDiff);
        complectingTaskDAO.save(newTask);

        return newTask;
    }

    /**
     * Creates complecting task for given movement item.
     * @param movementItem
     * @return
     * @throws BusinessException
     */
    public ComplectingTask createComplectingTaskForMovementItem(MovementItem movementItem) throws BusinessException {
        ComplectingTask newTask = new ComplectingTask();

        newTask.setState(ComplectingTaskState.getInitialState());
        newTask.setMovementItem(movementItem);
        newTask.setNeededCount(movementItem.getAmount());
        complectingTaskDAO.save(newTask);

        return newTask;
    }

    /**
     * Deletes complecting tasks for movement item specified.
     * @param task complecting task to be deleted.
     * @throws BusinessException
     */
    public void deleteComplectingTask(ComplectingTask task) throws BusinessException {
        complectingTaskDAO.remove(task);
    }

    public ComplectingTask getTaskById(long taskId) {
        return complectingTaskDAO.get(taskId);
    }

    //========================= Spring setters ==================================
    public void setComplectingTaskDAO(ComplectingTaskDAO complectingTaskDAO) {
        this.complectingTaskDAO = complectingTaskDAO;
    }
}
