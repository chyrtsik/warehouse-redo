/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.orders;

import com.artigile.warehouse.domain.orders.OrderItemProcessingResult;
import com.artigile.warehouse.domain.orders.OrderItemState;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;

/**
 * @author Shyrik, 05.04.2009
 */
public class OrderSubItemTO {
    private long id;
    private OrderItemTO orderItem;
    private long number;
    private OrderItemState state;
    private OrderItemProcessingResult processingResult;
    private WarehouseBatchTO warehouseBatch;
    private StoragePlaceTOForReport storagePlace;
    private String warehouseNotice;
    private long count;
    private Long foundCount;
    private String notice;

    //=================================== Constructos ================================================
    
    /**
     * Use this constructor to create and initalize new order subitem.
     * @param orderItem
     * @param warehouseBatch
     */
    public OrderSubItemTO(OrderItemTO orderItem, WarehouseBatchTO warehouseBatch) {
        this.orderItem = orderItem;
        this.warehouseBatch = warehouseBatch;
        this.storagePlace = warehouseBatch.getStoragePlace();
        this.warehouseNotice = warehouseBatch.getNotice();
        this.state = OrderItemState.getInitialState();
    }

    public OrderSubItemTO(){
    }

    //================================= Getters and setters =======================================================

    public boolean isNew() {
        return id == 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrderItemTO getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItemTO orderItem) {
        this.orderItem = orderItem;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public OrderItemState getState() {
        return state;
    }

    public void setState(OrderItemState state) {
        this.state = state;
    }

    public OrderItemProcessingResult getProcessingResult() {
        return processingResult;
    }

    public void setProcessingResult(OrderItemProcessingResult processingResult) {
        this.processingResult = processingResult;
    }

    public WarehouseBatchTO getWarehouseBatch() {
        return warehouseBatch;
    }

    public void setWarehouseBatch(WarehouseBatchTO warehouseBatch) {
        this.warehouseBatch = warehouseBatch;
    }

    public StoragePlaceTOForReport getStoragePlace() {
        return storagePlace;
    }

    public void setStoragePlace(StoragePlaceTOForReport storagePlace) {
        this.storagePlace = storagePlace;
    }

    public String getWarehouseNotice() {
        return warehouseNotice;
    }

    public void setWarehouseNotice(String warehouseNotice) {
        this.warehouseNotice = warehouseNotice;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Long getFoundCount() {
        return foundCount;
    }

    public void setFoundCount(Long foundCount) {
        this.foundCount = foundCount;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
