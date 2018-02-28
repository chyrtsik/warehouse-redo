/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.inventorization.task;

import com.artigile.warehouse.domain.inventorization.task.InventorizationTaskProcessingResult;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTaskState;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTaskType;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.inventorization.InventorizationItemTO;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTOForReport;

import java.util.Date;

/**
 * @author Borisok V.V., 30.09.2009
 */
public class InventorizationTaskTO extends EqualsByIdImpl {
    private long id;

    private long number;

    private InventorizationItemTO inventorizationItem;

    private InventorizationTaskState state;

    private InventorizationTaskType inventorizationType;

    private InventorizationTaskProcessingResult processingResult;

    private DetailBatchTO detailBatch;

    private StoragePlaceTOForReport storagePlace;

    private MeasureUnitTO countMeas;

    private Long foundCount;

    private boolean printed;

    private UserTO worker;

    private Date workBegin;

    private Date workEnd;

    //========================= Special getters and setters ========================================

    public String getItemName() {
        return detailBatch.getName();
    }

    public String getItemType() {
        return detailBatch.getType();
    }

    public String getItemMisc() {
        return detailBatch.getMisc();
    }

    public String getItemNotice(){
        return detailBatch.getNotice();
    }

    public String getItemMeas(){
        return countMeas.getSign();
    }

    public String getWarehouseName() {
        return storagePlace.getWarehouse().getName();
    }

    public String getStoragePlaceSign() {
        return storagePlace.getSign();
    }

    public String getWarehouseBatchNotice() {
        return inventorizationItem.getWarehouseBatchNotice();
    }

    public Long getDeviation() {
        return foundCount != null ? foundCount-getNeededCount() : null;
    }

    public boolean isNotProcessed() {
        return getState().equals(InventorizationTaskState.NOT_PROCESSED);
    }

    public boolean isInProcess() {
        return getState().equals(InventorizationTaskState.IN_PROCESS);
    }

    public boolean isProcessed() {
        return getState().equals(InventorizationTaskState.PROCESSED);
    }

    public boolean isSucceeded() {
        return getProcessingResult() != null && getProcessingResult().equals(InventorizationTaskProcessingResult.TRUE_COUNT);
    }

    public boolean isProblem() {
        return getProcessingResult() != null && !getProcessingResult().equals(InventorizationTaskProcessingResult.TRUE_COUNT);
    }

    //======================== Getters and setters =====================================

    @Override
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

    public InventorizationItemTO getInventorizationItem() {
        return inventorizationItem;
    }

    public void setInventorizationItem(InventorizationItemTO inventorizationItem) {
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

    public DetailBatchTO getDetailBatch() {
        return detailBatch;
    }

    public void setDetailBatch(DetailBatchTO detailBatch) {
        this.detailBatch = detailBatch;
    }

    public StoragePlaceTOForReport getStoragePlace() {
        return storagePlace;
    }

    public void setStoragePlace(StoragePlaceTOForReport storagePlace) {
        this.storagePlace = storagePlace;
    }

    public MeasureUnitTO getCountMeas() {
        return countMeas;
    }

    public void setCountMeas(MeasureUnitTO countMeas) {
        this.countMeas = countMeas;
    }

    public long getNeededCount() {
        assert inventorizationItem != null : "must be always not null";
        return inventorizationItem.getNeededCount();
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

    public UserTO getWorker() {
        return worker;
    }

    public void setWorker(UserTO worker) {
        this.worker = worker;
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
}
