/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.orders;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.common.verifications.VerificationResult;
import com.artigile.warehouse.bl.common.verifications.Verifications;
import com.artigile.warehouse.bl.finance.CurrencyExchangeService;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchService;
import com.artigile.warehouse.dao.OrderDAO;
import com.artigile.warehouse.dao.OrderItemsDAO;
import com.artigile.warehouse.dao.OrderProcessingInfoDAO;
import com.artigile.warehouse.dao.OrderSubItemDAO;
import com.artigile.warehouse.domain.chargeoff.ChargeOffItem;
import com.artigile.warehouse.domain.complecting.ComplectingTask;
import com.artigile.warehouse.domain.complecting.ComplectingTaskState;
import com.artigile.warehouse.domain.complecting.UncomplectingTask;
import com.artigile.warehouse.domain.complecting.UncomplectingTaskState;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNote;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteState;
import com.artigile.warehouse.domain.orders.*;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.orders.OrderItemTO;
import com.artigile.warehouse.utils.dto.orders.OrderSubItemTO;
import com.artigile.warehouse.utils.dto.orders.OrderTO;
import com.artigile.warehouse.utils.dto.orders.OrderTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.OrdersTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 06.01.2009
 */
@Transactional(rollbackFor = BusinessException.class)
public class OrderService {
    
    private OrderDAO ordersDAO;
    private OrderProcessingInfoDAO orderProcessingInfoDAO;
    private OrderItemsDAO orderItemsDAO;
    private OrderSubItemDAO orderSubItemDAO;
    private WarehouseBatchService warehouseBatchService;

    //============================ Constructors =======================================
    public OrderService() {
    }

    //=========================== Listeners support =====================================
    private List<OrderChangeListener> listeners = new ArrayList<OrderChangeListener>();

    public void addListener(OrderChangeListener listener){
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    public void removeListener(OrderChangeListener listener){
        listeners.remove(listener);
    }

    private void fireOrderItemAdded(OrderItem orderItem) throws BusinessException {
        for (OrderChangeListener listener : listeners){
            listener.onOrderItemAdded(orderItem);
        }
    }

    private void fireOrderItemChanged(OrderItem orderItem) throws BusinessException {
        for (OrderChangeListener listener : listeners){
            listener.onOrderItemChanged(orderItem);
        }
    }

    private void fireBeforeOrderItemDeleted(OrderItem orderItem) throws BusinessException {
        for (OrderChangeListener listener : listeners){
            listener.onBeforeOrderItemDeleted(orderItem);
        }
    }

    private void fireOrderSubItemDeleted(OrderSubItem orderSubItem) throws BusinessException {
        for (OrderChangeListener listener : listeners){
            listener.onOrderSubItemDeleted(orderSubItem);
        }
    }

    private void fireOrderStateChanged(Order order, OrderState oldState) throws BusinessException {
        for (OrderChangeListener listener : listeners){
            listener.onOrderStateChanged(order, oldState, order.getState());
        }
    }

    private void fireOrderItemStateChanged(OrderItem orderItem, OrderItemState oldState) throws BusinessException {
        for (OrderChangeListener listener : listeners){
            listener.onOrderItemStateChanged(orderItem, oldState, orderItem.getState());
        }
    }

    private void fireBeforeOrderDeleted(Order order) throws BusinessException {
        for (OrderChangeListener listener : listeners) {
            listener.onBeforeOrderDeleted(order);
        }
    }

    //==================================== Operations =======================================
    
    /**
     * Get all orders without deleted.
     * @return list with non deleted orders.
     */
    public List<OrderTOForReport> getOrdersWithoutDeleted() {
        List<OrderTOForReport> ordersForReport = new ArrayList<OrderTOForReport>();
        List<Order> ordersWithoutDeleted = ordersDAO.getOrdersWithoutDeleted();
        for (Order order : ordersWithoutDeleted) {
            ordersForReport.add(getOrder(order.getId()));
        }
        return ordersForReport;
    }

    public List<OrderItem> getOrderItems(Order order) {
        return ordersDAO.getOrderItems(order);
    }
    
    public List<OrderItemTO> getOrderItemsByFilter(OrderItemFilter filter) {
        List<OrderItemTO> result = new ArrayList<OrderItemTO>();
        if (filter != null) {
            for (OrderItem orderItem : orderItemsDAO.findByFilter(filter)) {
                result.add(OrdersTransformer.transformItem(orderItem));
            }
        }
        return result;
    }

    public OrderTO getOrderFullData(long orderId) {
        return OrdersTransformer.transform(ordersDAO.get(orderId));
    }

    public OrderTOForReport getOrder(long orderId) {
        return OrdersTransformer.transformForReport(ordersDAO.get(orderId));
    }

    public Order getOrderById(long orderId) {
        return ordersDAO.get(orderId);
    }

    public OrderProcessingInfo getOrderProcessingInfo(long processingInfoId) {
        return orderProcessingInfoDAO.get(processingInfoId);
    }

    public OrderItem getOrderItem(long orderItemId) {
        return ordersDAO.getOrderItem(orderItemId);
    }

    public OrderSubItem getOrderSubItem(long orderSubItemId) {
        return orderSubItemDAO.get(orderSubItemId);
    }

    /**
     * To check order against availability of not processed uncomplecting tasks. 
     * @param order which need to check.
     * @return true if order contains not processed uncomplecting tasks.
     * @throws BusinessException
     */
    private boolean isContainsUncomplectingTasks(Order order) throws BusinessException {
        boolean isContain = false;
        for (OrderItem orderItem : getOrderItems(order)) {
            if (isContain) break;
            for (OrderSubItem orderSubItem : orderItem.getSubItems()) {
                if (isContain) break;
                for (UncomplectingTask uncomplectingTask : orderSubItem.getUncomplectingTasks()) {
                    // Checks to first found not processed uncomplecting task.  
                    if (uncomplectingTask.getState() == UncomplectingTaskState.NOT_PROCESSED) {
                        isContain = true;
                        break;
                    }
                }
            }
        }
        return isContain;
    }

    /**
     * Marks order as deleted and don't display.
     * @param orderId if order which need to delete.
     * @return true if order delete from order list.
     * @throws BusinessException
     */
    public boolean deleteOrder(long orderId) throws BusinessException {
        // 1. Check on capacity of removing.
        Order order = ordersDAO.get(orderId);
        if (order == null || !order.isEditableState()) {
            throw new BusinessException(I18nSupport.message("order.error.cannotDeleteOrder"));
        }

        // 2. Delete order from order list and mark his as deleted.
        // Only if order don't contains order items.
        List<OrderItem> orderItems = order.getItems();
        if (!orderItems.isEmpty()){
            throw new BusinessException(I18nSupport.message("order.delete.message.containsOrderItems"));
        }

        // And if order doesn't contain undone uncomplecting tasks.
        if (isContainsUncomplectingTasks(order)) {
            throw new BusinessException(I18nSupport.message("order.delete.message.containsUncomplectingTask"));
        }

        fireBeforeOrderDeleted(order);
        order.setDeleted(true);
        ordersDAO.save(order);
        return true;
    }
  
    public void saveOrder(OrderTOForReport order) {
        Order persistentOrder = ordersDAO.get(order.getId());
        if (persistentOrder == null){
            persistentOrder = new Order();
        }
        else if (persistentOrder.getCurrency().getId() != order.getCurrency().getId()){
            //We needs to recalculate prices of the items in the order in the new currency.
            CurrencyExchangeService exchange = SpringServiceContext.getInstance().getCurencyExchangeService();
            long oldCurrencyId = persistentOrder.getCurrency().getId();
            long newCurrencyId = order.getCurrency().getId();
            for (OrderItem item : persistentOrder.getItems()){
                item.setPrice(exchange.convert(newCurrencyId, oldCurrencyId, item.getPrice()));
            }
        }

        OrdersTransformer.update(persistentOrder, order);
        ordersDAO.save(persistentOrder);
        ordersDAO.flush();
        ordersDAO.refresh(persistentOrder);
        OrdersTransformer.update(order, persistentOrder);
    }

    /**
     * Checks, if the number of the order will be unique.
     * @param number
     * @param orderId
     * @return
     */
    public boolean isUniqueOrderNumber(long number, long orderId) {
        Order sameOrder = ordersDAO.getOrderByNumber(number);
        return sameOrder == null || sameOrder.getId() == orderId;
    }

    public long getNextAvailableOrderNumber() {
        return ordersDAO.getNextAvailableOrderNumber();
    }

    /**
     * Adds new item to the order.
     * @param newOrderItemTO
     */
    public void addItemToOrder(OrderItemTO newOrderItemTO) throws BusinessException {
        //Saving new order item.
        OrderItem newOrderItem = OrdersTransformer.transformItem(newOrderItemTO);
        OrdersTransformer.updateItem(newOrderItem, newOrderItemTO);
        if (newOrderItem.getNumber() == 0){
            newOrderItem.setNumber(ordersDAO.getNextAvailableOrderItemNumber(newOrderItem.getOrder().getId()));
        }
        boolean reservingNeeded = newOrderItem.getOrder().getReservingType() == OrderReservingType.IMMEDIATELY;
        for (int i=0; i<newOrderItem.getSubItems().size(); i++){
            OrderSubItem subItem = newOrderItem.getSubItems().get(i);
            subItem.setNumber(i+1);
            if (reservingNeeded){
                //We need to reserve details at the warehouse exactly now (before order will be saved).
                warehouseBatchService.reserve(subItem.getWarehouseBatch().getId(), subItem.getAmount());
            }
        }

        newOrderItem.setReserved(reservingNeeded);
        ordersDAO.saveOrderItem(newOrderItem);
        ordersDAO.flush();
        ordersDAO.refresh(newOrderItem.getOrder());
        OrdersTransformer.updateItem(newOrderItemTO, newOrderItem);

        //Notify about changing of order items list.
        fireOrderItemAdded(newOrderItem);

        //Refresh order state.
        recalculateOrderState(newOrderItem.getOrder());
    }

    /**
     * Deletes item from the order.
     * @param orderItemId
     */
    public boolean deleteItemFromOrder(long orderItemId) throws BusinessException {
        OrderItem orderItem = ordersDAO.getOrderItem(orderItemId);
        Order order = orderItem.getOrder();

        // 1. Check on capacity of removing.
        if (order == null || !order.isEditableState() || orderItem == null || !orderItem.isEditableState()) {
            throw new BusinessException(I18nSupport.message("order.item.error.cannotDeleteOrderItem"));
        }

        //2. Notify about deleting of order item.
        fireBeforeOrderItemDeleted(orderItem);

        //3. Unreserve order item wares if needed.
        if (orderItem.getReserved()){
            unreserveOrderItem(orderItem);
        }

        //4. Delete item from the order.
        if (order.isEditableState()){
            orderItem.setDeleted(true);
            for (OrderSubItem subItem : orderItem.getSubItems()){
                //...and we should unlink order sub items from warehouse batches, because
                //order sub items now is considered as un existing and should not impact on
                //warehouse batches operations (such as deleting warehouse batches).
                subItem.setWarehouseBatch(null);
            }
            ordersDAO.saveOrderItem(orderItem);
        }

        //5. Refresh order state.
        ordersDAO.refresh(orderItem.getOrder());
        recalculateOrderState(orderItem.getOrder());

        return true;
    }

    private void unreserveOrderItem(OrderItem orderItem) throws BusinessException {
        for (OrderSubItem subItem : orderItem.getSubItems()){
            warehouseBatchService.unreserve(subItem.getWarehouseBatch().getId(), subItem.getAmount());
        }
        orderItem.setReserved(false);
    }

    /**
     * Searches for same order item, a given one.
     * @param orderItemTO
     * @return
     */
    public OrderItemTO findSameOrderItem(OrderItemTO orderItemTO) {
        return OrdersTransformer.transformItem(orderItemsDAO.findSameOrderItem(orderItemTO.getOrder().getId(), orderItemTO.getDetailBatch().getId()));
    }

    /**
     * Saves order item (new or already existing)
     * @param orderItemTO
     */
    public void saveOrderItem(OrderItemTO orderItemTO) throws BusinessException {
        OrderItem persistentOrderItem = ordersDAO.getOrderItem(orderItemTO.getId());
        if (persistentOrderItem == null){
            persistentOrderItem = new OrderItem();
        }
        List<OrderSubItem> oldSubItems = persistentOrderItem.getSubItems();
        // Artificially load child collection
        int oldSubItemsSize = oldSubItems.size();

        //Reserving os wares for order item.
        if (orderItemTO.isReserved()){
            //Comparing old and new counts of details and reserve or unreserve them is needed.
            for (OrderSubItemTO newSubItemTO : orderItemTO.getSubItems()){
                if (newSubItemTO.isNew()){
                    //Reserving new order sub item.
                    warehouseBatchService.reserve(newSubItemTO.getWarehouseBatch().getId(), newSubItemTO.getCount());
                }
                else{
                    //Processing changing of count in existent order sub item.
                    for (OrderSubItem oldSubItem : oldSubItems){
                        if (newSubItemTO.getId() == oldSubItem.getId()){
                            long countDelta = newSubItemTO.getCount() - oldSubItem.getAmount();
                            if (countDelta > 0){
                                warehouseBatchService.reserve(newSubItemTO.getWarehouseBatch().getId(), countDelta);
                            }
                            else if (countDelta < 0){
                                warehouseBatchService.unreserve(newSubItemTO.getWarehouseBatch().getId(), -countDelta);
                            }
                            break;
                        }
                    }
                }
            }
        }

        //Calculate numbers for new subitems.
        long number = 0;
        for (OrderSubItemTO subItemTO : orderItemTO.getSubItems()){
            if (subItemTO.getNumber() > number){
                number = subItemTO.getNumber();
            }
        }
        for (OrderSubItemTO subItemTO : orderItemTO.getSubItems()){
            if (subItemTO.getNumber() == 0){
                subItemTO.setNumber(++number);
            }
        }

        //Actually saving order item.
        OrdersTransformer.updateItem(persistentOrderItem, orderItemTO);
        ordersDAO.saveOrderItem(persistentOrderItem);
        ordersDAO.flush();
        ordersDAO.refresh(persistentOrderItem.getOrder());
        OrdersTransformer.updateItem(orderItemTO, persistentOrderItem);

        //Notify about changing of order item.
        fireOrderItemChanged(persistentOrderItem);

        //Deleting not used old order sub items.
        for (OrderSubItem oldSubItem : oldSubItems){
            boolean subItemUsed = false;
            for (OrderSubItem newSubItem : persistentOrderItem.getSubItems()){
                if (newSubItem.getId() == oldSubItem.getId()){
                    subItemUsed = true;
                    break;
                }
            }
            if (!subItemUsed){
                if (orderItemTO.isReserved()){
                    //Unreserving is necessary to prevent details "locking" as their starage places.
                    warehouseBatchService.unreserve(oldSubItem.getWarehouseBatch().getId(), oldSubItem.getAmount());
                }

                //We should notify about deleting of order sub item.
                fireOrderSubItemDeleted(oldSubItem);

                if (oldSubItem.getState() == OrderItemState.NOT_PROCESSED){
                    //We are safe to deleted order sub item.
                    orderSubItemDAO.remove(oldSubItem);
                }
                else{
                    //Order sub item may be used by warehouse worker's tasks. So, we are able to hide.
                    oldSubItem.setDeleted(true);
                    orderSubItemDAO.save(oldSubItem);
                }
            }
        }
    }

    /**
     * Makes orderId ready for complecting.
     * @param orderId - identifier of the order to be sent to the complecting.
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException
     */
    public void makeOrderReadyForComplecting(long orderId) throws BusinessException {
        Order order = ordersDAO.get(orderId);

        //1. Verify, if order is ready to became ready for complecting.
        VerificationResult verificationResult = Verifications.performVerification(order, new BeforeMakeOrderReadyForCollectionVerification());
        if (verificationResult.isFailed()){
            throw new BusinessException(verificationResult.getFailReason());
        }

        //2. Make order waiting for collection.
        //2.1. New state of the orderId.
        OrderState oldState = order.getState();
        order.setState(OrderState.READY_FOR_COLLECTION);
        ordersDAO.save(order);

        //2.2. Notify about changing order state.
        fireOrderStateChanged(order, oldState);
    }

    /**
     * Moves orderId to construction state.
     * @param orderId - identifier of the order to be returned to construction.
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException
     */
    public void returnOrderToConstruction(long orderId) throws BusinessException {
        Order order = ordersDAO.get(orderId);

        //1. Verify, if order is valid to be returned to construction state.
        Verifications.ensureVerificationPasses(order, new BeforeReturnOrderToConstructionVerification());

        //2. Make orderId ready for construction.
        //2.1. Move order to construction state.
        OrderState oldState = order.getState();
        order.setState(OrderState.CONSTRUCTION);
        ordersDAO.save(order);

        //2.1. Notify about changing order state.
        fireOrderStateChanged(order, oldState);
    }

    /**
     * Refreshes state of the order sub items basing on current state of it's complecting tasks.
     * @param orderSubItem
     */
    public void recalculateOrderSubItemState(OrderSubItem orderSubItem)  throws BusinessException {
        OrderItemState newState = calculateOrderSubItemState(orderSubItem);
        OrderItemProcessingResult newResult = calculateOrderSubItemProcessingResult(orderSubItem);
        if (newState != orderSubItem.getState() || newResult != orderSubItem.getProcessingResult()){
            orderSubItem.setState(newState);
            orderSubItem.setProcessingResult(newResult);

            //To reload calculated values, such as foundCount of wares during complecting.
            orderSubItemDAO.flush();
            orderSubItemDAO.refresh(orderSubItem);

            //Order item state depends on state of it's sub items.
            recalculateOrderItemState(orderSubItem.getOrderItem());
        }
    }

    /**
     * Refreshes state of the order item basing on current states of it's sub items.
     * @param orderItem
     */
    private void recalculateOrderItemState(OrderItem orderItem) throws BusinessException {
        OrderItemState newState = calculateOrderItemState(orderItem);
        OrderItemState oldState = orderItem.getState();
        OrderItemProcessingResult newResut = calculateOrderItemProcessingResult(orderItem);
        if (newState != oldState || newResut != orderItem.getProcessingResult()) {
            orderItem.setState(newState);
            orderItem.setProcessingResult(newResut);

            //Order state depends on state of it's items.
            recalculateOrderState(orderItem.getOrder());

            fireOrderItemStateChanged(orderItem, oldState);
        }
    }

    /**
     * Refreshes state of the order basing on current states of it's items.
     * @param order
     */
    private void recalculateOrderState(Order order) throws BusinessException {
        //1. Refreshing state of the order.
        OrderState newState = calculateOrderState(order);
        OrderState oldState = order.getState();
        if (newState != oldState){
            order.setState(newState);

            //2.2. Notify about changing order state.
            fireOrderStateChanged(order, oldState);
        }

        // 2. Refreshing processing info of the order.
        long allItemsCount = calculateOrderNonDeletedItemsCount(order);
        if (allItemsCount != order.getProcessingInfo().getItemsCount()){
            order.getProcessingInfo().setItemsCount(allItemsCount);
        }

        long complectedItemsCount = calculateOrderComplectedItemsCount(order);
        if (complectedItemsCount != order.getProcessingInfo().getComplectedItemsCount()){
            order.getProcessingInfo().setComplectedItemsCount(complectedItemsCount);
        }
    }

    private OrderItemState calculateOrderSubItemState(OrderSubItem orderSubItem) {
        //1. Calculates state of order sub item by analyzing states of it's complecting tasks.
        ComplectingTaskState lastTaskState = getLastComplectingTaskState(orderSubItem);
        if (!lastTaskState.equals(ComplectingTaskState.SHIPPED)){
            return translateComplectingTaskStateToOrderSubItemState(lastTaskState);
        }

        //2. Order sub item is shipped from warehouse. We should analyze delivery note items,
        //linked with order sub item to determine appropriate state.
        DeliveryNoteState lastDeliveryNoteState = getLastDeliveryNoteState(orderSubItem);
        if (lastDeliveryNoteState != null){
            return translateDeliveryNoteStateToOrderSubItemState(lastDeliveryNoteState);
        }

        //3. If we cannot calculate order sub item yet, we should not change it's state.
        return orderSubItem.getState();
    }

    private OrderItemState translateDeliveryNoteStateToOrderSubItemState(DeliveryNoteState deliveryNoteState) {
        //Translates delivery note state into appropriate state of order sub item.
        if (deliveryNoteState.equals(DeliveryNoteState.SHIPPING_TO_CONTRACTOR)){
            return OrderItemState.SHIPPED;
        }
        else if (deliveryNoteState.equals(DeliveryNoteState.CLOSED)){
            return OrderItemState.SOLD;
        }
        throw new RuntimeException("OrderService.translateDeliveryNoteStateToOrderSubItemState: unsupported delivery note state.");
    }

    private DeliveryNoteState getLastDeliveryNoteState(OrderSubItem orderSubItem) {
        //Finds the last state of the delivery notes, linked with given order sub item.
        DeliveryNoteState lastState = DeliveryNoteState.getLastState();
        for (ComplectingTask task : orderSubItem.getComplectingTasks()){
            ChargeOffItem chargeOffItem = task.getChargeOffItem();
            if (chargeOffItem != null){
                DeliveryNote deliveryNote = chargeOffItem.getDeliveryNoteItem().getDeliveryNote();
                if (deliveryNote.getState().isBefore(lastState)){
                    lastState = deliveryNote.getState();
                }
            }
            else{
                return null;
            }
        }
        return lastState;
    }

    private ComplectingTaskState getLastComplectingTaskState(OrderSubItem orderSubItem) {
        ComplectingTaskState lastState = ComplectingTaskState.getLastState();
        for (ComplectingTask task : orderSubItem.getComplectingTasks()){
            if (task.getState() == ComplectingTaskState.PROCESSING){
                //When there are at least one complecting tasks in processing state, then the whole
                //order sub item is considered to be in processing.
                return ComplectingTaskState.PROCESSING;
            }
            else if (task.getState().isBefore(lastState)){
                //The latest state of complecting task determines state of the whole order sub item.
                lastState = task.getState();
            }
        }
        return lastState;
    }

    private OrderItemState translateComplectingTaskStateToOrderSubItemState(ComplectingTaskState taskState) {
        if (taskState.equals(ComplectingTaskState.NOT_PROCESSED)){
            return OrderItemState.NOT_PROCESSED;
        }
        else if (taskState.equals(ComplectingTaskState.PROCESSING)){
            return OrderItemState.PROCESSING;
        }
        else if (taskState.equals(ComplectingTaskState.PROCESSED)){
            return OrderItemState.PROCESSED;
        }
        else if (taskState.equals(ComplectingTaskState.READY_FOR_SHIPPING)){
            return OrderItemState.READY_FOR_SHIPPING;
        }
        throw new RuntimeException("OrderService.translateComplectingTaskStateToOrderSubItemState: unsupported complecting task state.");
    }

    private OrderItemProcessingResult calculateOrderSubItemProcessingResult(OrderSubItem orderSubItem) {
        //Calculates processing result of order sub item by analyzing states of it's complecting tasks.
        boolean allTasksDone = true;
        for (ComplectingTask task : orderSubItem.getComplectingTasks()){
            if (task.getFoundCount() != null){
                if (task.getFoundCount() < task.getNeededCount()){
                    //Even one problem task may cause problem with order sub item.
                    return OrderItemProcessingResult.PROBLEM;
                }
            }
            else{
                allTasksDone = false;
            }
        }
        if (allTasksDone){
            return OrderItemProcessingResult.COMPLECTED;
        }
        else{
            return null;
        }
    }

    private OrderItemState calculateOrderItemState(OrderItem orderItem) {
        OrderItemState lastState = OrderItemState.getLastState();
        for (OrderSubItem subItem : orderItem.getSubItems()){
            if (subItem.getState() == OrderItemState.PROCESSING){
                //Items is being processing is one of it's sub items is in processing state.
                return OrderItemState.PROCESSING;
            }
            else if (subItem.getState().isBefore(lastState)){
                //Otherwise order item's state is the same to the latest state of it's sub item.
                lastState = subItem.getState();
            }
        }
        return lastState;
    }

    private OrderItemProcessingResult calculateOrderItemProcessingResult(OrderItem orderItem) {
        boolean allSubItemsComplected = true;
        for (OrderSubItem subItem : orderItem.getSubItems()){
            if (subItem.getProcessingResult() != OrderItemProcessingResult.COMPLECTED){
                allSubItemsComplected = false;
            }
            if (subItem.getProcessingResult() == OrderItemProcessingResult.PROBLEM){
                return OrderItemProcessingResult.PROBLEM;
            }
        }
        return allSubItemsComplected ? OrderItemProcessingResult.COMPLECTED : null;
    }

    /**
     * Computing order state depending on states of it's items.
     * @param order
     * @return
     */
    private OrderState calculateOrderState(Order order) {
        if (!order.getState().isInProcessing()){
            //Only when order is in processing, it's state is calculated.
            return order.getState();
        }
        else if (calculateOrderNonDeletedItemsCount(order) == 0){
            return OrderState.CONSTRUCTION;
        }

        //State of the order is determined by the latest state of it's item.
        OrderItemState latestItemState = OrderItemState.getLastState();
        for (OrderItem item : order.getItems()){
            if (item.isDeleted()){
                //Deleted order items are ignored.
                continue;
            }
            if (item.getProcessingResult() != null && item.getProcessingResult().equals(OrderItemProcessingResult.PROBLEM)){
                //The first problem caused order to be marked as order with problem.
                return OrderState.COLLECTION_PROBLEM;
            }
            if (item.getState().isBefore(latestItemState)){
                latestItemState = item.getState();
            }
        }

        return translateOrderItemStateToOrderState(latestItemState, order);
    }

    private OrderState translateOrderItemStateToOrderState(OrderItemState latestItemState, Order order) {
        if (latestItemState.equals(OrderItemState.NOT_PROCESSED)){
            //When order collections begins, the first item, that is being collected moves order to collecting state.
            return order.hasItemsInState(OrderItemState.PROCESSING) ? OrderState.COLLECTION : OrderState.READY_FOR_COLLECTION;
        }
        else if (latestItemState.equals(OrderItemState.PROCESSING)){
            return OrderState.COLLECTION;
        }
        else if (latestItemState.equals(OrderItemState.PROCESSED)){
            return OrderState.COLLECTED;
        }
        else if (latestItemState.equals(OrderItemState.READY_FOR_SHIPPING)){
            return OrderState.READY_FOR_SHIPPING;
        }
        else if (latestItemState.equals(OrderItemState.SHIPPED)){
            return OrderState.SHIPPED;
        }
        else if (latestItemState.equals(OrderItemState.SOLD)){
            return OrderState.SOLD;
        }
        throw new RuntimeException("OrderService.translateOrderItemStateToOrderState - not supported order item state.");
    }

    private long calculateOrderNonDeletedItemsCount(Order order) {
        long count = 0;
        for (OrderItem item : order.getItems()){
            if (!item.isDeleted()){
                count++;
            }
        }
        return count;
    }

    private long calculateOrderComplectedItemsCount(Order order) {
        long count = 0;
        for (OrderItem item : order.getItems()){
            if (item.isProcessed()){
                count++;
            }
        }
        return count;
    }

    /**
     * Finds order sub items, linked with given delivery note.
     * @param deliveryNoteId
     * @return
     */
    public List<OrderSubItem> getOrderSubItemsForDeliveryNote(long deliveryNoteId){
        return orderSubItemDAO.getOrderSubItemsForDeliveryNote(deliveryNoteId);
    }
    //==================== Spring setters ============================

    public void setOrderDAO(OrderDAO ordersDAO) {
        this.ordersDAO = ordersDAO;
    }

    public void setOrderItemsDAO(OrderItemsDAO orderItemsDAO) {
        this.orderItemsDAO = orderItemsDAO;
    }

    public void setOrderSubItemDAO(OrderSubItemDAO orderSubItemsDAO) {
        this.orderSubItemDAO = orderSubItemsDAO;
    }

    public void setWarehouseBatchService(WarehouseBatchService warehouseBatchService) {
        this.warehouseBatchService = warehouseBatchService;
    }

    public void setOrderProcessingInfoDAO(OrderProcessingInfoDAO orderProcessingInfoDAO) {
        this.orderProcessingInfoDAO = orderProcessingInfoDAO;
    }
}
