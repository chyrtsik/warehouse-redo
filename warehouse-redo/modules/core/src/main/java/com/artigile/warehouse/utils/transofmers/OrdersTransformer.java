/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.orders.Order;
import com.artigile.warehouse.domain.orders.OrderItem;
import com.artigile.warehouse.domain.orders.OrderProcessingInfo;
import com.artigile.warehouse.domain.orders.OrderSubItem;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.orders.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 06.01.2009
 */
public final class OrdersTransformer {
    private OrdersTransformer(){
    }

    /**
     * Transforms list of orders to it's lighweignt representation for GUI.
     * @param orders
     * @return
     */
    public static List<OrderTOForReport> listForReport(List<Order> orders) {
        List<OrderTOForReport> ordersTO = new ArrayList<OrderTOForReport>();
        for (Order order : orders){
            ordersTO.add(transformForReport(order));
        }
        return ordersTO;
    }

    public static OrderTOForReport transformForReport(Order order){
        OrderTOForReport orderTO = new OrderTOForReport();
        update(orderTO, order);
        return orderTO;
    }

    /**
     * Copies content of the order's TO to the entity.
     * @param order - (in, out)
     * @param orderTO - (in)
     */
    public static void update(Order order, OrderTOForReport orderTO) {
        order.setState(orderTO.getState());
        order.setNumber(orderTO.getNumber());
        order.setCreateDate(orderTO.getCreateDate());
        order.setCreatedUser(UserTransformer.transformUser(orderTO.getCreatedUser()));
        order.setContractor(SpringServiceContext.getInstance().getContractorService().getContractorById(orderTO.getContractor().getId()));
        order.setLoadPlace(ContractorTransformer.transformLoadPlace(orderTO.getLoadPlace()));
        order.setLoadDate(orderTO.getLoadDate());
        order.setCurrency(SpringServiceContext.getInstance().getCurrencyService().getCurrencyById(orderTO.getCurrency().getId()));
        order.setNotice(orderTO.getNotice());
        order.setReservingType(orderTO.getReservingType());
        order.setProcessingInfo(OrdersTransformer.transformProcessingInfo(orderTO.getProcessingInfo(), order));
        order.setVatRate(orderTO.getVatRate());
    }

    /**
     * Transforms order entity to the it's report representation.
     * @param orderTO - (in, out)
     * @param order - (in)
     */
    public static void update(OrderTOForReport orderTO, Order order){
        orderTO.setId(order.getId());
        orderTO.setState(order.getState());
        orderTO.setNumber(order.getNumber());
        orderTO.setCreateDate(order.getCreateDate());
        orderTO.setCreatedUser(UserTransformer.transformUser(order.getCreatedUser()));
        orderTO.setContractor(ContractorTransformer.transformContractor(order.getContractor()));
        orderTO.setLoadPlace(ContractorTransformer.transformLoadPlace(order.getLoadPlace()));
        orderTO.setLoadDate(order.getLoadDate());
        orderTO.setCurrency(CurrencyTransformer.transformCurrency(order.getCurrency()));
        orderTO.setNotice(order.getNotice());
        orderTO.setReservingType(order.getReservingType());
        orderTO.setProcessingInfo(OrdersTransformer.transformProcessingInfo(order.getProcessingInfo(), orderTO));
        orderTO.setTotalPrice(order.getTotalPrice());
        orderTO.setVatRate(order.getVatRate());
    }

    /**
     * Transforms order entity to the it's full representation.
     * @param order
     * @return
     */
    public static OrderTO transform(Order order) {
        OrderTO orderTO = new OrderTO();
        updateFull(orderTO, order);
        return orderTO;
    }

    /**
     * Transforms order entity to the it's full representation.
     * @param orderTO - (in, out)
     * @param order - (in)
     */
    public static void updateFull(OrderTO orderTO, Order order) {
        update(orderTO, order);
        List<OrderItemTO> itemsTO = new ArrayList<OrderItemTO>();
        for (OrderItem item : order.getItems()){
            itemsTO.add(transformItem(item, orderTO));
        }
        orderTO.setItems(itemsTO);
    }

    private static OrderProcessingInfo transformProcessingInfo(OrderProcessingInfoTO processingInfoTO, Order order) {
        OrderProcessingInfo processingInfo = null;
        if (processingInfoTO != null){
            processingInfo = SpringServiceContext.getInstance().getOrdersService().getOrderProcessingInfo(processingInfoTO.getId());
        }

        if (processingInfo == null){
            processingInfo = new OrderProcessingInfo();
            processingInfo.setOrder(order);
        }

        updateProcessingInfo(processingInfo, processingInfoTO, order);
        return processingInfo;
    }

    /**
     * @param processingInfo - (in, out)
     * @param processingInfoTO - (in)
     */
    private static void updateProcessingInfo(OrderProcessingInfo processingInfo, OrderProcessingInfoTO processingInfoTO, Order order) {
        if ( processingInfoTO == null ){
            return;
        }
        processingInfo.setId(processingInfoTO.getId());
        processingInfo.setOrder(order);
        processingInfo.setComplectedItemsCount(processingInfoTO.getComplectedItemsCount());
        processingInfo.setItemsCount(processingInfoTO.getItemsCount());
    }

    private static OrderProcessingInfoTO transformProcessingInfo(OrderProcessingInfo processingInfo, OrderTOForReport orderTO) {
        OrderProcessingInfoTO processingInfoTO = new OrderProcessingInfoTO();
        update(processingInfoTO, processingInfo, orderTO);
        return processingInfoTO;
    }

    /**
     * @param processingInfoTO - (in, out)
     * @param processingInfo - (out)
     */
    private static void update(OrderProcessingInfoTO processingInfoTO, OrderProcessingInfo processingInfo, OrderTOForReport orderTO) {
        if (processingInfo == null){
            return;
        }
        processingInfoTO.setId(processingInfo.getId());
        processingInfoTO.setOrder(orderTO);
        processingInfoTO.setComplectedItemsCount(processingInfo.getComplectedItemsCount());
        processingInfoTO.setItemsCount(processingInfo.getItemsCount());
    }

    /**
     * Transforms order item.
     * @param item - order item to be transformed.
     * @param orderTO - order TO, to which this item belongs to.
     * @return
     */
    private static OrderItemTO transformItem(OrderItem item, OrderTO orderTO) {
        OrderItemTO itemTO = new OrderItemTO();
        itemTO.setOrder(orderTO);
        updateItem(itemTO, item);
        return itemTO;
    }

    public static OrderItemTO transformItem(OrderItem item) {
        if (item == null){
            return null;
        }
        return transformItem(item, transform(item.getOrder()));
    }

    /**
     * Updates data of the order item TO from the entity.
     * @param itemTO (in, out)
     * @param item (in)
     */
    public static void updateItem(OrderItemTO itemTO, OrderItem item) {
        itemTO.setId(item.getId());
        itemTO.setNumber(item.getNumber());
        itemTO.setState(item.getState());
        itemTO.setProcessingResult(item.getProcessingResult());
        itemTO.setPrice(item.getPrice());
        itemTO.setCount(item.getAmount());
        itemTO.setDetailBatch(DetailBatchTransformer.batchTO(item.getDetailBatch()));
        itemTO.setText(item.getText());
        itemTO.setNotice(item.getNotice());
        itemTO.setReserved(item.getReserved());
        itemTO.setSubItems(transformSubItems(itemTO, item.getSubItems()));
    }

    /**
     * Transforms order item TO to the entity.
     * @param itemTO
     * @return
     */
    public static OrderItem transformItem(OrderItemTO itemTO) {
        if (itemTO == null){
            return null;
        }
        OrderItem item = SpringServiceContext.getInstance().getOrdersService().getOrderItem(itemTO.getId());
        if (item == null){
            item = new OrderItem();
        }
        return item;
    }

    public static void updateItem(OrderItem item, OrderItemTO itemTO) {
        item.setOrder(SpringServiceContext.getInstance().getOrdersService().getOrderById(itemTO.getOrder().getId()));
        item.setNumber(itemTO.getNumber());
        item.setState(itemTO.getState());
        item.setProcessingResult(itemTO.getProcessingResult());
        item.setPrice(itemTO.getPrice());
        item.setAmount(itemTO.getCount());
        item.setNotice(itemTO.getNotice());
        item.setReserved(itemTO.isReserved());
        item.setSubItems(transformAndUpdateSubItemsTO(item, itemTO.getSubItems()));
        if (itemTO.getDetailBatch() == null){
            //Item is a simple text, non detail.
            item.setText(itemTO.getText());
        }
        else{
            //Item is a detail.
            item.setDetailBatch(SpringServiceContext.getInstance().getDetailBatchesService().getDetailBatchById(itemTO.getDetailBatch().getId()));
        }
    }

    private static List<OrderSubItem> transformAndUpdateSubItemsTO(OrderItem item, List<OrderSubItemTO> subItemsTO) {
        List<OrderSubItem> subItems = new ArrayList<OrderSubItem>();
        for (OrderSubItemTO subItemTO : subItemsTO){
            OrderSubItem subItem = transformSubItem(subItemTO);
            updateSubItem(subItem, subItemTO, item);
            subItems.add(subItem);
        }
        return subItems;
    }

    public static OrderSubItem transformSubItem(OrderSubItemTO subItemTO) {
        if (subItemTO == null){
            return null;
        }
        OrderSubItem subItem = SpringServiceContext.getInstance().getOrdersService().getOrderSubItem(subItemTO.getId());
        if (subItem == null){
            subItem = new OrderSubItem();
        }
        return subItem;
    }

    /**
     * Updates entity from it's DTO.
     * @param subItem - (in, out) entity to be updated.
     * @param subItemTO - (in) DTO with edited data.
     * @param item
     */
    private static void updateSubItem(OrderSubItem subItem, OrderSubItemTO subItemTO, OrderItem item) {
        subItem.setOrderItem(item);
        subItem.setNumber(subItemTO.getNumber());
        subItem.setState(subItemTO.getState());
        subItem.setProcessingResult(subItemTO.getProcessingResult());
        subItem.setWarehouseBatch(WarehouseBatchTransformer.transform(subItemTO.getWarehouseBatch()));
        subItem.setStoragePlace(StoragePlaceTransformer.transform(subItemTO.getStoragePlace()));
        subItem.setWarehouseNotice(subItemTO.getWarehouseNotice());
        subItem.setAmount(subItemTO.getCount());
        subItem.setNotice(subItemTO.getNotice());
    }

    private static List<OrderSubItemTO> transformSubItems(OrderItemTO itemTO, List<OrderSubItem> subItems) {
        List<OrderSubItemTO> subItemsTO = new ArrayList<OrderSubItemTO>();
        for (OrderSubItem subItem : subItems){
            subItemsTO.add(transformSubItem(itemTO, subItem));
        }
        return subItemsTO;
    }

    public static OrderSubItemTO transformSubItem(OrderItemTO itemTO, OrderSubItem subItem) {
        if (subItem == null){
            return null;
        }
        OrderSubItemTO subItemTO = new OrderSubItemTO();
        updateSubItem(itemTO, subItemTO, subItem);
        return subItemTO;
    }

    public static OrderSubItemTO transformSubItem(OrderSubItem subItem) {
        if (subItem == null){
            return null;
        }
        return transformSubItem(transformItem(subItem.getOrderItem()), subItem);
    }

    /**
     * Updates order subitem DTO from entity.
     * @param itemTO
     * @param subItemTO - (in, out) DTO to be updated.
     * @param subItem - (in) entity with fresh data.
     */
    private static void updateSubItem(OrderItemTO itemTO, OrderSubItemTO subItemTO, OrderSubItem subItem) {
        subItemTO.setId(subItem.getId());
        subItemTO.setOrderItem(itemTO);
        subItemTO.setNumber(subItem.getNumber());
        subItemTO.setState(subItem.getState());
        subItemTO.setProcessingResult(subItem.getProcessingResult());
        subItemTO.setWarehouseBatch(WarehouseBatchTransformer.transform(subItem.getWarehouseBatch()));
        subItemTO.setStoragePlace(StoragePlaceTransformer.transformForReport(subItem.getStoragePlace()));
        subItemTO.setWarehouseNotice(subItem.getWarehouseNotice());
        subItemTO.setCount(subItem.getAmount());
        subItemTO.setFoundCount(subItem.getFoundCount());
        subItemTO.setNotice(subItem.getNotice());
    }
}
