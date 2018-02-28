/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.inventorization.task;

import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.directory.MeasureUnit;
import com.artigile.warehouse.domain.inventorization.InventorizationItem;
import com.artigile.warehouse.domain.warehouse.StoragePlace;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Borisok V.V., 30.09.2009
 */

/**
 * Represents inventorization task, given to warehouse worker.
 */
@Entity
public class InventorizationTask {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Number of the task
     */
    @Column(nullable = false)
    private long number;

    /**
     * Inventorization item to which this item connected.
     */
    @OneToOne(optional = false)
    private InventorizationItem inventorizationItem;

    /**
     * Current state of the inventorization.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private InventorizationTaskState state;

    /**
     * Inventorization task type
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private InventorizationTaskType inventorizationType;

    /**
     * Processing result of inventorization
     */
    @Enumerated(value = EnumType.STRING)
    private InventorizationTaskProcessingResult processingResult;

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
     * Measure unit of the details in the batch. Make duplication because it can be changed
     */
    @ManyToOne(optional = false)
    private MeasureUnit countMeas;

    /**
     * Found count
     */
    private Long foundCount;

    /**
     * True, if task has been printed by worker, and False, is not.
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean printed;

    /**
     * Worker, who began doing this task.
     */
    @ManyToOne
    private User worker;

    /**
     * Date and time of work create.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date workCreate;

    /**
     * Date and time of work begin.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date workBegin;

    /**
     * Date and time of work end.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date workEnd;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //================================ Manipulators =========================================
    public void setFoundCountAndRefresh(Long newCount) {
        setFoundCount(newCount);
        setProcessingResult(calculateProcessingResult());
        if (newCount == null){
            //Inventorization task is considered to be reopened by warehouse worker.
            setWorkEnd(null);
        }
        else{
            //Inventorization task was finished by warehouse worker.
            setWorkEnd(Calendar.getInstance().getTime());
        }
    }

    private InventorizationTaskProcessingResult calculateProcessingResult() {
        if (foundCount == null){
            return null;
        }
        else if (foundCount > getInventorizationItem().getNeededCount()){
            return InventorizationTaskProcessingResult.SURPLUS_COUNT;
        }
        else if (foundCount < getInventorizationItem().getNeededCount()){
            return InventorizationTaskProcessingResult.LACK_COUNT;
        }
        else{
            return InventorizationTaskProcessingResult.TRUE_COUNT;
        }
    }

    //============================ Getters and setters ======================================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public InventorizationItem getInventorizationItem() {
        return inventorizationItem;
    }

    public void setInventorizationItem(InventorizationItem inventorizationItem) {
        this.inventorizationItem = inventorizationItem;
    }

    public InventorizationTaskState getState() {
        return state;
    }

    public void setState(InventorizationTaskState state) {
        this.state = state;
    }

    public InventorizationTaskType getInventorizationType() {
        return inventorizationType;
    }

    public void setInventorizationType(InventorizationTaskType inventorizationType) {
        this.inventorizationType = inventorizationType;
    }

    public InventorizationTaskProcessingResult getProcessingResult() {
        return processingResult;
    }

    public void setProcessingResult(InventorizationTaskProcessingResult processingResult) {
        this.processingResult = processingResult;
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

    public MeasureUnit getCountMeas() {
        return countMeas;
    }

    public void setCountMeas(MeasureUnit countMeas) {
        this.countMeas = countMeas;
    }

    public Long getFoundCount() {
        return foundCount;
    }

    public void setFoundCount(Long foundCount) {
        this.foundCount = foundCount;
    }

    public boolean getPrinted() {
        return printed;
    }

    public void setPrinted(boolean printed) {
        this.printed = printed;
    }

    public User getWorker() {
        return worker;
    }

    public void setWorker(User worker) {
        this.worker = worker;
    }

    public Date getWorkCreate() {
        return workCreate;
    }

    public void setWorkCreate(Date workCreate) {
        this.workCreate = workCreate;
    }

    public Date getWorkBegin() {
        return workBegin;
    }

    public void setWorkBegin(Date workBegin) {
        this.workBegin = workBegin;
    }

    public Date getWorkEnd() {
        return workEnd;
    }

    public void setWorkEnd(Date workEnd) {
        this.workEnd = workEnd;
    }

    public long getVersion() {
        return version;
    }
}
