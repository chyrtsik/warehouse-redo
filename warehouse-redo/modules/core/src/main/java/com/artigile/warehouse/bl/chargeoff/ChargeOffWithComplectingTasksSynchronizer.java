/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.chargeoff;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.complecting.ComplectingTaskChangeAdapter;
import com.artigile.warehouse.bl.complecting.ComplectingTaskService;
import com.artigile.warehouse.bl.deliveryNote.DeliveryNoteService;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchChangeDocument;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchService;
import com.artigile.warehouse.domain.chargeoff.ChargeOff;
import com.artigile.warehouse.domain.chargeoff.ChargeOffItem;
import com.artigile.warehouse.domain.chargeoff.ChargeOffReason;
import com.artigile.warehouse.domain.complecting.ComplectingTask;
import com.artigile.warehouse.domain.complecting.ComplectingTaskState;
import com.artigile.warehouse.domain.orders.Order;
import com.artigile.warehouse.domain.orders.OrderSubItem;
import com.artigile.warehouse.domain.warehouse.Warehouse;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.UserTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Shyrik, 19.12.2009
 */

/**
 * This class reacts on changes in complecting tasks to perform appropriate
 * actions on charge off documents.
 */
@Transactional(rollbackFor = BusinessException.class)
public class ChargeOffWithComplectingTasksSynchronizer extends ComplectingTaskChangeAdapter {
    private ComplectingTaskService complectingTaskService;

    private ChargeOffService chargeOffService;

    private WarehouseBatchService warehouseBatchService;

    private DeliveryNoteService deliveryNoteService;

    public void initialize(){
        //This class listens for changes made in complecting tasks.
        complectingTaskService.addListener(this);
    }

    @Override
    public void onComplectingTasksStateChanged(List<ComplectingTask> tasks, ComplectingTaskState oldState, ComplectingTaskState newState) throws BusinessException {
        if (newState.equals(ComplectingTaskState.SHIPPED)){
            //1. Split tasks into groups: to another warehouse and to for the order (to the customer).
            List<ComplectingTask> tasksForMovement = new ArrayList<ComplectingTask>();
            List<ComplectingTask> tasksForOrder = new ArrayList<ComplectingTask>();

            for (ComplectingTask task : tasks){
                if (task.getMovementItem() != null){
                    tasksForMovement.add(task);
                }
                else if (task.getOrderSubItem() != null){
                    tasksForOrder.add(task);
                }
                else{
                    throw new RuntimeException("ChargeOffWithComplectingTasksSynchronizer.onComplectingTasksStateChanged: unsupported complecting task type.");
                }
            }

            //2. Perform charge off operation for wares, that are has been shipped for movement.
            ShippingFromWarehouseContext context = new ShippingFromWarehouseContext();
            context.setNextChargeOffNumber(chargeOffService.getNextAvailableNumber());
            context.setNextDeliveryNoteNumber(deliveryNoteService.getNextDeliveryNoteNumber());

            if (tasksForMovement.size() > 0){
                shipComplectingTasksForMovements(tasksForMovement, context);
            }

            //3. Perform charge off operation for wares, that are has been shipped for order.
            if (tasksForOrder.size() > 0){
                shipComplectingTasksForOrders(tasksForOrder, context);
            }
        }
    }

    /**
     * Performs change off operations to warehouse for given complecting tasks.
     * @param tasks
     * @param context
     * @throws BusinessException
     */
    private void shipComplectingTasksForMovements(List<ComplectingTask> tasks, ShippingFromWarehouseContext context) throws BusinessException {
        //1. Split wares according to their target warehouse.
        Map<Warehouse, List<ComplectingTask>> tasksByWarehouse = new HashMap<Warehouse, List<ComplectingTask>>();
        for (ComplectingTask task : tasks){
            Warehouse targetWarehouse = task.getMovementItem().getMovement().getToWarehouse();
            if (!tasksByWarehouse.containsKey(targetWarehouse)){
                tasksByWarehouse.put(targetWarehouse, new ArrayList<ComplectingTask>());
            }
            tasksByWarehouse.get(targetWarehouse).add(task);
        }

        //2. Charge wares off into delivery notes, that are sent to appropriate warehouses.
        Warehouse fromWarehouse = tasks.get(0).getMovementItem().getMovement().getFromWarehouse();
        for (Warehouse destinationWarehouse : tasksByWarehouse.keySet()){
            shipComplectingTasksToWarehouse(context, fromWarehouse,  destinationWarehouse, tasksByWarehouse.get(destinationWarehouse));
        }
    }

    /**
     * Performs change off operations to order for given complecting tasks.
     * @param tasks
     * @param context
     * @throws BusinessException
     */
    private void shipComplectingTasksForOrders(List<ComplectingTask> tasks, ShippingFromWarehouseContext context) throws BusinessException {
        //1. Split wares according to their orders.
        Map<Order, List<ComplectingTask>> tasksByOrder = new HashMap<Order, List<ComplectingTask>>();
        for (ComplectingTask task : tasks){
            Order order = task.getOrderSubItem().getOrderItem().getOrder();
            if (!tasksByOrder.containsKey(order)){
                tasksByOrder.put(order, new ArrayList<ComplectingTask>());
            }
            tasksByOrder.get(order).add(task);
        }

        //2. Charge wares off into delivery notes for every separated order.
        Warehouse fromWarehouse = tasks.get(0).getOrderSubItem().getWarehouseBatch().getStoragePlace().getWarehouse();
        for (Order order : tasksByOrder.keySet()){
            shipComplectingTasksToOrder(context, fromWarehouse, order, tasksByOrder.get(order));
        }
    }

    /**
     * Processing shipping of complecting tasks for the movement to the given warehouse.
     * @param context context of operation.
     * @param fromWarehouse warehouse, from which wares will be charged off.
     * @param destinationWarehouse warehouse, to which wares will be shipped.
     * @param tasks complecting tasks for shipping.    @throws BusinessException
     */
    private void shipComplectingTasksToWarehouse(ShippingFromWarehouseContext context, Warehouse fromWarehouse, Warehouse destinationWarehouse,
                                                 List<ComplectingTask> tasks) throws BusinessException {
        //1. Creating charge off items.
        List<ChargeOffItem> chargeOffItems = new ArrayList<ChargeOffItem>();

        long currentNumber = 1;
        for (ComplectingTask task : tasks){
            //Creating charge off item for movement item.
            ChargeOffItem chargeOffItem = new ChargeOffItem();
            chargeOffItem.setNumber(currentNumber++);
            chargeOffItem.setStoragePlace(task.getMovementItem().getFromStoragePlace());
            chargeOffItem.setDetailBatch(task.getMovementItem().getDetailBatch());
            chargeOffItem.setShelfLifeDate(task.getMovementItem().getShelfLifeDate());
            chargeOffItem.setWarehouseNotice(task.getMovementItem().getWarehouseNotice());
            chargeOffItem.setAmount(task.getMovementItem().getAmount());
            chargeOffItem.setCountMeas(task.getMovementItem().getCountMeas());
            chargeOffItems.add(chargeOffItem);
            task.setChargeOffItem(chargeOffItem);

            //Performing charge off for warehouse batch. Before this operation we should unlink
            //movement item from warehouse batch, because during charge off warehouse batch may be deleted.
            long warehouseBatchId = task.getMovementItem().getWarehouseBatch().getId();
            task.getMovementItem().setWarehouseBatch(null);
            WarehouseBatchChangeDocument document = WarehouseBatchChangeDocument.createChargeOffDocument(chargeOffItem);
            warehouseBatchService.performReservedWareChargeOff(warehouseBatchId, chargeOffItem.getAmount(), document);
        }

        //2. Creating charge off.
        long chargeOffNumber = context.getNextChargeOffNumber();
        context.setNextChargeOffNumber(chargeOffNumber + 1);

        ChargeOff chargeOff = new ChargeOff();
        chargeOff.setNumber(chargeOffNumber);
        chargeOff.setPerformDate(Calendar.getInstance().getTime());
        chargeOff.setPerformer(UserTransformer.transformUser(WareHouse.getUserSession().getUser())); //TODO: eliminate this reference to presentation tier.
        chargeOff.setMovement(tasks.get(0).getMovementItem().getMovement());
        chargeOff.setReason(ChargeOffReason.MOVING_WARES);
        chargeOff.setWarehouse(fromWarehouse);
        chargeOff.setNotice(I18nSupport.message("chargeOff.notice.forMovementToWarehouse", destinationWarehouse.getName()));
        chargeOff.setItems(chargeOffItems);
        chargeOffService.saveChargeOff(chargeOff);

        //3. Creating delivery note for charge off.
        deliveryNoteService.createDeliveryNoteForChargeOff(context, chargeOff, destinationWarehouse);
    }

    /**
     * * Processing shipping of complecting tasks for the movement to the given order.
     * @param context context of current shipping from warehouse operation.
     * @param fromWarehouse warehouse, from which wares will be charged off.
     * @param order order, to which wares will be charged off.
     * @param tasks complecting tasks for shipping.
     * @throws BusinessException
     */

    private void shipComplectingTasksToOrder(ShippingFromWarehouseContext context, Warehouse fromWarehouse, Order order, List<ComplectingTask> tasks) throws BusinessException {
        //1. Creating charge off items.
        List<ChargeOffItem> chargeOffItems = new ArrayList<ChargeOffItem>();
        Map<ChargeOffItem, BigDecimal> itemPrices = new HashMap<ChargeOffItem, BigDecimal>();
        Map<OrderSubItem, Long> orderSubItemToWarehouseBatch = new HashMap<OrderSubItem, Long>();

        long currentNumber = 1;
        for (ComplectingTask task : tasks){
            //Creating charge off item for order item.
            OrderSubItem orderSubItem = task.getOrderSubItem();

            ChargeOffItem chargeOffItem = new ChargeOffItem();
            chargeOffItem.setNumber(currentNumber++);
            chargeOffItem.setStoragePlace(orderSubItem.getStoragePlace());
            chargeOffItem.setDetailBatch(orderSubItem.getOrderItem().getDetailBatch());
            chargeOffItem.setWarehouseNotice(orderSubItem.getWarehouseNotice());
            chargeOffItem.setAmount(task.getFoundCount());
            chargeOffItem.setCountMeas(orderSubItem.getOrderItem().getDetailBatch().getCountMeas());
            chargeOffItems.add(chargeOffItem);
            task.setChargeOffItem(chargeOffItem);

            itemPrices.put(chargeOffItem, orderSubItem.getOrderItem().getPrice());

            //Performing charge off for warehouse batch. Before this operation we should unlink
            //order sub item from warehouse batch, because during charge off warehouse batch may be deleted.
            long warehouseBatchId = 0;
            if (orderSubItem.getWarehouseBatch() != null){
                warehouseBatchId = orderSubItem.getWarehouseBatch().getId();
                orderSubItem.setWarehouseBatch(null);
                orderSubItemToWarehouseBatch.put(orderSubItem, warehouseBatchId);
            }
            else{
               warehouseBatchId = orderSubItemToWarehouseBatch.get(orderSubItem);
            }

            WarehouseBatchChangeDocument document = WarehouseBatchChangeDocument.createChargeOffDocument(chargeOffItem);
            warehouseBatchService.performReservedWareChargeOff(warehouseBatchId, chargeOffItem.getAmount(), document);
        }

        //2. Creating charge off.
        long chargeOffNumber = context.getNextChargeOffNumber();
        context.setNextChargeOffNumber(chargeOffNumber + 1);

        ChargeOff chargeOff = new ChargeOff();
        chargeOff.setNumber(chargeOffNumber);
        chargeOff.setPerformDate(Calendar.getInstance().getTime());
        chargeOff.setPerformer(UserTransformer.transformUser(WareHouse.getUserSession().getUser())); //TODO: eliminate this reference to presentation tier.
        chargeOff.setOrder(order);
        chargeOff.setReason(ChargeOffReason.SHIPPING_TO_CUSTOMER);
        chargeOff.setWarehouse(fromWarehouse);
        chargeOff.setNotice(I18nSupport.message("chargeOff.notice.forOrder", order.getNumber()));
        chargeOff.setItems(chargeOffItems);
        chargeOffService.saveChargeOff(chargeOff);

        //3. Creating delivery note for charge off.
        deliveryNoteService.createDeliveryNoteToContractor(context, chargeOff, order.getContractor(), order.getCurrency(), order.getVatRate(), itemPrices);
    }

    //================================ Spring setters ================================
    public void setComplectingTaskService(ComplectingTaskService complectingTaskService) {
        this.complectingTaskService = complectingTaskService;
    }

    public void setChargeOffService(ChargeOffService chargeOffService) {
        this.chargeOffService = chargeOffService;
    }

    public void setWarehouseBatchService(WarehouseBatchService warehouseBatchService) {
        this.warehouseBatchService = warehouseBatchService;
    }

    public void setDeliveryNoteService(DeliveryNoteService deliveryNoteService) {
        this.deliveryNoteService = deliveryNoteService;
    }
}
