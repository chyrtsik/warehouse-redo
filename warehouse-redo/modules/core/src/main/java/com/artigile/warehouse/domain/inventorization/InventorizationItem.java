/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.inventorization;

import com.artigile.warehouse.domain.chargeoff.ChargeOffItem;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.directory.MeasureUnit;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTask;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTaskProcessingResult;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTaskState;
import com.artigile.warehouse.domain.postings.PostingItem;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.domain.warehouse.WarehouseBatch;

import javax.persistence.*;

/**
 * @author Borisok V.V., 07.10.2009
 */

/**
 * One item of a inventorization. Represents detail (or other good, that located at the warehouse).
 */
@Entity
public class InventorizationItem {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Inventorization, to which this items belongs to.
     */
    @ManyToOne(optional = false)
    private Inventorization inventorization;

    /**
     * Number of the item in the inventorization.
     */
    private long number;

    /**
     * Inventorization task to which this item connected.
     */
    @OneToOne(mappedBy = "inventorizationItem")
    private InventorizationTask inventorizationTask;

    /**
     * Contains type, name and miscellaneous information about detail batch
     */
    @ManyToOne(optional = false)
    private DetailBatch detailBatch;

    /**
     * Storage place, where batch is placed. Make duplication because it can be changed
     */
    @ManyToOne(optional = false)
    private StoragePlace storagePlace;

    /**
     * Warehouse batch for exact identification of ware, that should be checked.
     * Not null only during inventprization processing process. 
     */
    @ManyToOne
    @JoinColumn
    private WarehouseBatch warehouseBatch;

    /**
     * Measure unit of the details in the batch. Make duplication because it can be changed
     */
    @ManyToOne(optional = false)
    private MeasureUnit countMeas;

    /**
     * Needed count
     */
    private long neededCount;

    /**
     * Processing result of inventorization item
     */
    @Enumerated(value = EnumType.STRING)
    private InventorizationItemProcessingResult processingResult;

    /**
     * State of inventorization item
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private InventorizationItemState state;

    /**
     * Posting item for this inventorization item (if posting for this item was needed).
     */
    @OneToOne
    private PostingItem postingItem;

    /**
     * Charge off item for this inventorization item (if charge off was needed).
     */
    @OneToOne
    private ChargeOffItem chargeOffItem;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //======================================= Operations ===========================================
    public void refreshProcessingState() {
        if (inventorizationTask.getState().equals(InventorizationTaskState.IN_PROCESS)){
            //Warehouse workers has begun processing the task.
            setState(InventorizationItemState.IN_PROCESS);
            setProcessingResult(null);
        }
        else if (inventorizationTask.getState().equals(InventorizationTaskState.NOT_PROCESSED)){
            //Warehouse worker has cancelled processing of the task.
            setState(InventorizationItemState.NOT_PROCESSED);
            setProcessingResult(null);
        }
        else if (inventorizationTask.getState().equals(InventorizationTaskState.PROCESSED)){
            //Warehouse worker has completed the task.
            setState(InventorizationItemState.PROCESSED);
            setProcessingResult(translateTaskProcessingResult(inventorizationTask.getProcessingResult()));
        }
    }

    private InventorizationItemProcessingResult translateTaskProcessingResult(InventorizationTaskProcessingResult taskProcessingResult) {
        if (taskProcessingResult.equals(InventorizationTaskProcessingResult.TRUE_COUNT)){
            return InventorizationItemProcessingResult.TRUE_COUNT;
        }
        else if (taskProcessingResult.equals(InventorizationTaskProcessingResult.LACK_COUNT)){
            return InventorizationItemProcessingResult.LACK_COUNT;
        }
        else if (taskProcessingResult.equals(InventorizationTaskProcessingResult.SURPLUS_COUNT)){
            return InventorizationItemProcessingResult.SURPLUS_COUNT;
        }
        else{
            throw new RuntimeException("InventorizationItem.translateTaskProcessingResult: unsupported task procesing result.");
        }
    }

    //=================================== Calculated getters ===============================================
    public boolean isProblem() {
        return getProcessingResult() != null && !getProcessingResult().equals(InventorizationItemProcessingResult.TRUE_COUNT);
    }

    public long getCountDifference() {
        return Math.abs(getNeededCount() - inventorizationTask.getFoundCount());
    }

    //==================================== Getters and setters =====================================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Inventorization getInventorization() {
        return inventorization;
    }

    public void setInventorization(Inventorization inventorization) {
        this.inventorization = inventorization;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public InventorizationTask getInventorizationTask() {
        return inventorizationTask;
    }

    public void setInventorizationTask(InventorizationTask inventorizationTask) {
        this.inventorizationTask = inventorizationTask;
    }

    public DetailBatch getDetailBatch() {
        return detailBatch;
    }

    public void setDetailBatch(DetailBatch detailBatch) {
        this.detailBatch = detailBatch;
    }

    public StoragePlace getStoragePlace() {
        return storagePlace;
    }

    public void setStoragePlace(StoragePlace storagePlace) {
        this.storagePlace = storagePlace;
    }

    public WarehouseBatch getWarehouseBatch() {
        return warehouseBatch;
    }

    public void setWarehouseBatch(WarehouseBatch warehouseBatch) {
        this.warehouseBatch = warehouseBatch;
    }

    public MeasureUnit getCountMeas() {
        return countMeas;
    }

    public void setCountMeas(MeasureUnit countMeas) {
        this.countMeas = countMeas;
    }

    public long getNeededCount() {
        return neededCount;
    }

    public void setNeededCount(long neededCount) {
        this.neededCount = neededCount;
    }

    public InventorizationItemProcessingResult getProcessingResult() {
        return processingResult;
    }

    public void setProcessingResult(InventorizationItemProcessingResult processingResult) {
        this.processingResult = processingResult;
    }

    public InventorizationItemState getState() {
        return state;
    }

    public void setState(InventorizationItemState state) {
        this.state = state;
    }

    public PostingItem getPostingItem() {
        return postingItem;
    }

    public void setPostingItem(PostingItem postingItem) {
        this.postingItem = postingItem;
    }

    public ChargeOffItem getChargeOffItem() {
        return chargeOffItem;
    }

    public void setChargeOffItem(ChargeOffItem chargeOffItem) {
        this.chargeOffItem = chargeOffItem;
    }

    public long getVersion() {
        return version;
    }
}
