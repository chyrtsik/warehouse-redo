/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.orders;

import com.artigile.warehouse.domain.complecting.ComplectingTask;
import com.artigile.warehouse.domain.complecting.UncomplectingTask;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.domain.warehouse.WarehouseBatch;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 05.04.2009
 */

/**
 * Order sub item means the least part of the order. It holds information about concrete details
 * with their storage place. These details will be (in normal case) put into the parcell during
 * processing the order.
 */
@Entity
public class OrderSubItem {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Order item, to which this sub item belongs to.
     */
    @ManyToOne(optional = false)
    private OrderItem orderItem;

    /**
     * Unique number of this subitem in in the order item.
     */
    @Column(nullable = false)
    private long number;

    /**
     * State of this subitem.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderItemState state;

    /**
     * Result of processing this sub item. Will be null until any processing has not beed performed. 
     */
    @Enumerated(value = EnumType.STRING)
    private OrderItemProcessingResult processingResult;

    /**
     * True, if this order sub item was marked as deleted.
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean deleted;

    /**
     * Warehouse batch, which will be put into order as this order sub item.
     * This field is set to null when warehouse batch is charged off from warehouse.
     */
    @ManyToOne
    private WarehouseBatch warehouseBatch;

    /**
     * Storage place, from where warehouse batch is placed into order.
     * Remains filled forever for calculation of a history of warehouse operations.
     */
    @ManyToOne(optional = false)
    private StoragePlace storagePlace;

    /**
     * Notice about ware, taken from warehouse batch (it's important, because notice
     * used to check uniqueness of warehouse batches).
     */
    private String warehouseNotice;

    /**
     * Count of details to be placed into this subitem from the warehouse.
     */
    @Column(nullable = false)
    private long amount;

    /**
     * Complecting tasks for warehouse worker, that are related to this order sub item.
     */
    @OneToMany(mappedBy = "orderSubItem")
    private List<ComplectingTask> complectingTasks = new ArrayList<ComplectingTask>();


    /**
     * Uncomplecting tasks for warehouse worker, that are related to this order sub item.
     */
    @OneToMany(mappedBy = "orderSubItem")
    private List<UncomplectingTask> uncomplectingTasks = new ArrayList<UncomplectingTask>();

    /**
     * Only user defined notice for this subitem.
     */
    private String notice;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    @Formula("(select sum(t.foundCount) from ComplectingTask t where t.orderSubItem_id=id and t.foundCount is not null)")
    private Long foundCount;

    @Formula("(select sum(t.neededCount) from ComplectingTask t where t.orderSubItem_id=id)")
    private Long neededCount;

    //==================================== Calculated getters ===============================================
    /**
     * @return - calculated summary count of found wares for this order sub item.
     */
    public Long getFoundCount() {
        return foundCount;
    }

    /**
     * @return - calculated summary count of wares in all complecting tasks, related to this order sub item.
     */
    public long getNeededCount(){
        return neededCount;
    }

    //==================================== Gettres and setters ==============================================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public WarehouseBatch getWarehouseBatch() {
        return warehouseBatch;
    }

    public void setWarehouseBatch(WarehouseBatch warehouseBatch) {
        this.warehouseBatch = warehouseBatch;
    }

    public StoragePlace getStoragePlace() {
        return storagePlace;
    }

    public void setStoragePlace(StoragePlace storagePlace) {
        this.storagePlace = storagePlace;
    }

    public String getWarehouseNotice() {
        return warehouseNotice;
    }

    public void setWarehouseNotice(String warehouseNotice) {
        this.warehouseNotice = warehouseNotice;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public List<ComplectingTask> getComplectingTasks() {
        return complectingTasks;
    }

    public void setComplectingTasks(List<ComplectingTask> complectingTasks) {
        this.complectingTasks = complectingTasks;
    }

    public List<UncomplectingTask> getUncomplectingTasks() {
        return uncomplectingTasks;
    }

    public void setUncomplectingTasks(List<UncomplectingTask> uncomplectingTasks) {
        this.uncomplectingTasks = uncomplectingTasks;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public long getVersion() {
        return version;
    }
}
