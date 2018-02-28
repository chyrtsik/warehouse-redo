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
import com.artigile.warehouse.domain.complecting.*;
import com.artigile.warehouse.domain.orders.*;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Shyrik, 12.12.2009
 */

/**
 * Class for synchronization of complecting tasks with orders.
 */
@Transactional(rollbackFor = BusinessException.class)
public class ComplectingTasksWithOrdersSynchronizer extends OrderChangeAdapter {
    private OrderService ordersService;

    private ComplectingTaskService complectingTaskService;

    //===================== Construction and initialization ===============================
    public ComplectingTasksWithOrdersSynchronizer(){
    }

    public void initialize(){
        ordersService.addListener(this);
    }

    //======================= OrderChangeListener =========================================
    @Override
    public void onOrderStateChanged(Order order, OrderState oldState, OrderState newState) throws BusinessException {
        if (order.getState() == OrderState.READY_FOR_COLLECTION && oldState == OrderState.CONSTRUCTION){
            //Creating complecting tasks for order, has been moved to complecting.
            createComplectingTasksForOrder(order);
        }
        else if (order.getState() == OrderState.CONSTRUCTION && oldState == OrderState.READY_FOR_COLLECTION){
            //Deleting all lemplecting tasks for this order.
            deleteComplectingTasksForOrder(order);
        }
    }

    @Override
    public void onOrderItemAdded(OrderItem orderItem) throws BusinessException {
        if (!orderItem.isDetail()){
            //We only interested in detaul order items.
            return;
        }
        else if (!orderItem.getOrder().getState().isInProcessing() &&
                 orderItem.getOrder().getState() != OrderState.READY_FOR_COLLECTION)
        {
            //And only items, whose orders are in complecting process, may cause changes.
            return;
        }

        //Order is being complecting. So, we make new order item to be in complecting to.
        for (OrderSubItem subItem : orderItem.getSubItems()){
            ComplectingTask task = complectingTaskService.createComplectingTaskForOrderSubItem(subItem);
            subItem.getComplectingTasks().add(task);
        }
    }

    @Override
    public void onBeforeOrderItemDeleted(OrderItem orderItem) throws BusinessException {
        if (!orderItem.isDetail()){
            //We only interested in detail order items.
            return;
        }
        else if (!orderItem.isEditableState()){
            //And only items with editable states.
            return;
        }

        for (OrderSubItem subItem : orderItem.getSubItems()){
            onOrderSubItemDeleted(subItem);
        }
    }

    @Override
    public void onOrderItemChanged(OrderItem orderItem) throws BusinessException {
        if (!orderItem.isDetail()){
            //We only interested in detaul order items.
            return;
        }
        else if (!orderItem.getOrder().getState().isInProcessing()){
            //And only items, whose orders are in complecting process, may cause changes.
            return;
        }

        //Order is being complecting. Changing count of the wares may cause changing in the list of
        //warehouse worker's complecting tasks.
        for (OrderSubItem subItem : orderItem.getSubItems()){
            long countDiff = subItem.getAmount() - subItem.getNeededCount();
            if (countDiff == 0){
                continue;
            }

            //We should make some changes in complecting tasks list to cope with changes in order item.
            processOrderSubItemCountChange(subItem, countDiff);

            //Notify order sub item about possible changes of it's complecting tasks.
            ordersService.recalculateOrderSubItemState(subItem);
        }
    }

    @Override
    public void onOrderItemStateChanged(OrderItem orderItem, OrderItemState oldState, OrderItemState newState) throws BusinessException {
        if (newState == OrderItemState.READY_FOR_SHIPPING){
            for (OrderSubItem subItem : orderItem.getSubItems()) {
                for (UncomplectingTask task : subItem.getUncomplectingTasks()) {
                    task.setState(UncomplectingTaskState.CLOSED);
                }
            }
        }
    }

    @Override
    public void onOrderSubItemDeleted(OrderSubItem orderSubItem) throws BusinessException {
        for (ComplectingTask task : orderSubItem.getComplectingTasks()) {
            complectingTaskService.deleteComplectingTask(task);
            ComplectingTaskState state = task.getState();
            if (state.isBefore(ComplectingTaskState.SHIPPED)) {
                // We should create uncomplecting task for deleted order sub item.
                SpringServiceContext.getInstance().getUncomplectingTaskService()
                    .createTask(orderSubItem, UncomplectingTaskType.REMOVE_ORDER_ITEM, orderSubItem.getAmount());
            }
        }
    }

    /**
     * Creates tasks for warehouse worker when there is a new order that is ready to be complected.
     * @param order
     * @throws BusinessException
     */
    private void createComplectingTasksForOrder(Order order) throws BusinessException {
        for (OrderItem item : order.getItems()){
            if (!item.isDetail()){
                //Only details (they are considered to lie at the warehouse) needed their tasks.
                continue;
            }
            for (OrderSubItem subItem : item.getSubItems()){
                ComplectingTask newTask = complectingTaskService.createComplectingTaskForOrderSubItem(subItem);
                subItem.getComplectingTasks().add(newTask);
            }
        }
    }

    /**
     * Deletes all complecting tasks for given order.
     * @param order
     * @throws BusinessException
     */
    private void deleteComplectingTasksForOrder(Order order) throws BusinessException {
        for (OrderItem item : order.getItems()){
            if (!item.isDetail()){
                //Only details (they are considered to lie atthe warehouse) needed their taks.
                continue;
            }
            for (OrderSubItem subItem : item.getSubItems()){
                deleteComplectingTasksForOrderSubItem(subItem);
            }
        }
    }

    private void processOrderSubItemCountChange(OrderSubItem subItem, long countDiff) throws BusinessException {
        //First we search for complecting tasks, that allows us to process the difference between the old
        //and new counts of ware needed.
        boolean doneByChangingComplectingTasks = false;
        for (int i=subItem.getComplectingTasks().size()-1; i>=0; i--){
            ComplectingTask task = subItem.getComplectingTasks().get(i);
            if (countDiff > 0){
                if (task.getState() == ComplectingTaskState.NOT_PROCESSED){
                    //We may safely increase count of details for tasks, because this
                    //tasks hasn't been taken into processing by warehouse worker.
                    task.setNeededCount(task.getNeededCount() + countDiff);
                    doneByChangingComplectingTasks = true;
                    break;
                }
            }
            else{
                if (task.getState() == ComplectingTaskState.NOT_PROCESSED){
                    //We may decrease of count needed, or delete complecting task, that has become useless.
                    if (task.getNeededCount() > Math.abs(countDiff)){
                        //We may make big task smaller.
                        task.setNeededCount(task.getNeededCount()  + countDiff);
                        doneByChangingComplectingTasks = true;
                        break;
                    }
                    else {
                        //This task is not used now.
                        countDiff += task.getNeededCount();
                        deleteComplectingTaskForOrderSubItem(subItem, task);
                        if (countDiff == 0){
                            doneByChangingComplectingTasks = true;
                            break;
                        }
                    }
                }
                else if (task.getState() == ComplectingTaskState.PROCESSED &&
                         task.getFoundCount() < task.getNeededCount()){
                    //We may decrease needed amount of wares in problem complecting task.
                    //It can help us both to process order item changing and to solve a problem in complecting.
                    long availableToDecrease = task.getNeededCount() - task.getFoundCount();
                    if (availableToDecrease >= Math.abs(countDiff)){
                        //All changes of ware count in the order item can be satisfied by this problem task.
                        task.setNeededCount(task.getNeededCount() + countDiff);
                        task.setFoundCount(task.getFoundCount());
                        complectingTaskService.fireComplectingTaskStateChanged(task, task.getState(), task.getState());
                        doneByChangingComplectingTasks = true;
                        break;
                    }
                    else if (availableToDecrease < Math.abs(countDiff)){
                        //Task can satisfy only a part of count changing.
                        task.setNeededCount(task.getNeededCount() - availableToDecrease);
                        countDiff += availableToDecrease;
                        break;
                    }
                }
            }
        }

        if (!doneByChangingComplectingTasks){
            //There is no simple way to process difference in count except creating new task for warehouse worker.
            if (countDiff > 0){
                //Try to found uncomplecting task. If we will found it, and it will be undone, we may revert it.
                long revertedCountInUncomplectingTasks = 0;
                for (UncomplectingTask uncomplectingTask : subItem.getUncomplectingTasks()){
                    if (uncomplectingTask.getType() == UncomplectingTaskType.DECREASE_AMOUNT_OF_WARES &&
                        uncomplectingTask.getState() == UncomplectingTaskState.NOT_PROCESSED)
                    {
                        if (uncomplectingTask.getCountToProcess() <= countDiff){
                            //Uncomplecting task is not used now.
                            revertedCountInUncomplectingTasks += uncomplectingTask.getCountToProcess();
                            countDiff -= uncomplectingTask.getCountToProcess();
                            SpringServiceContext.getInstance().getUncomplectingTaskService().deleteTask(uncomplectingTask);
                        }
                        else{
                            //We can satisfy a part of change by changing of uncomplecting task.
                            revertedCountInUncomplectingTasks += uncomplectingTask.getCountToProcess() - countDiff;
                            uncomplectingTask.setCountToProcess(uncomplectingTask.getCountToProcess() - countDiff);
                            countDiff = 0;
                            break;
                        }
                    }

                    if (countDiff == 0){
                        break;
                    }
                }

                if (revertedCountInUncomplectingTasks > 0){
                    //We should restore needed count of done complecting tasks to show new amount of ware needed.
                    for (ComplectingTask complectingTask : subItem.getComplectingTasks()){
                        long taskCountDiff = complectingTask.getFoundCount() - complectingTask.getNeededCount();
                        if (complectingTask.getState() == ComplectingTaskState.PROCESSED && taskCountDiff > 0){
                            if (taskCountDiff >= revertedCountInUncomplectingTasks){
                                complectingTask.setNeededCount(complectingTask.getNeededCount() + revertedCountInUncomplectingTasks);
                                break;
                            }
                            else{
                                revertedCountInUncomplectingTasks -= taskCountDiff;
                                complectingTask.setNeededCount(complectingTask.getFoundCount());
                            }
                        }
                    }
                }

                if (countDiff > 0){
                    //We should create new (and unprocessed) complecting task.
                    ComplectingTask newTask = complectingTaskService.createComplectingTaskForOrderSubItemDifference(subItem, countDiff);
                    subItem.getComplectingTasks().add(newTask);
                }
            }
            else{
                //Try to find not done uncomplecting tasks and to change them.
                long countForUncomplectingTasks = -countDiff;
                for (UncomplectingTask uncomplectingTask : subItem.getUncomplectingTasks()){
                    if (uncomplectingTask.getType() == UncomplectingTaskType.DECREASE_AMOUNT_OF_WARES &&
                        uncomplectingTask.getState() != UncomplectingTaskState.PROCESSED)
                    {
                        uncomplectingTask.setCountToProcess(uncomplectingTask.getCountToProcess() + countForUncomplectingTasks);
                        countForUncomplectingTasks = 0;
                        break;
                    }
                }

                if (countForUncomplectingTasks > 0){
                    //We should create uncomplecting task for deleted wares.
                    SpringServiceContext.getInstance().getUncomplectingTaskService()
                        .createTask(subItem, UncomplectingTaskType.DECREASE_AMOUNT_OF_WARES, countForUncomplectingTasks);
                }

                //At last we decrease count of wares in done complecting tasks. In this case we process
                //complecting tasks in reverse order, because later complecting tasks is considered to
                //be less important, than earlier ones.
                for (int i=subItem.getComplectingTasks().size()-1; i>=0; i--){
                    ComplectingTask task = subItem.getComplectingTasks().get(i);
                    if (task.getState() != ComplectingTaskState.NOT_PROCESSED){
                        if (task.getNeededCount() <= Math.abs(countDiff)){
                            countDiff += task.getNeededCount();
                            task.setNeededCount(0);
                        }
                        else{
                            task.setNeededCount(task.getNeededCount() + countDiff);
                            countDiff = 0;
                        }
                    }
                    if (countDiff == 0){
                        break;
                    }
                }
                assert(countDiff == 0); //Uncomplecting amount of wares should be fully compensated.
            }
        }
    }

    /**
     * Deletes complecting tasks for order sub item.
     * @param subItem - order sub item.
     */
    private void deleteComplectingTasksForOrderSubItem(OrderSubItem subItem) throws BusinessException {
        if (subItem.getState() != OrderItemState.NOT_PROCESSED){
            throw new BusinessException(I18nSupport.message("complectingTask.delete.error.orderItemIsAlreadyProcessing"));
        }
        for (int i = subItem.getComplectingTasks().size() - 1; i >= 0; i--){
            ComplectingTask taskToDelete = subItem.getComplectingTasks().get(i);
            deleteComplectingTaskForOrderSubItem(subItem, taskToDelete);
        }
    }

    /**
     * Deletes complecting task for order sub item.
     * @param subItem - order sub item.
     * @param task - task to be deleted.
     */
    private void deleteComplectingTaskForOrderSubItem(OrderSubItem subItem, ComplectingTask task) throws BusinessException {
        if (subItem.getState() != OrderItemState.NOT_PROCESSED){
            throw new BusinessException(I18nSupport.message("complectingTask.delete.error.orderItemIsAlreadyProcessing"));
        }
        subItem.getComplectingTasks().remove(task);
        complectingTaskService.deleteComplectingTask(task);
    }

    //============================ Spring setters =============================
    public void setOrdersService(OrderService ordersService) {
        this.ordersService = ordersService;
    }

    public void setComplectingTaskService(ComplectingTaskService complectingTaskService) {
        this.complectingTaskService = complectingTaskService;
    }
}
