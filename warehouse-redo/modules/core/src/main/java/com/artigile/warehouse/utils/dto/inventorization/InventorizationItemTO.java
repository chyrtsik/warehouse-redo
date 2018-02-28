/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.inventorization;

import com.artigile.warehouse.domain.inventorization.InventorizationItemProcessingResult;
import com.artigile.warehouse.domain.inventorization.InventorizationItemState;
import com.artigile.warehouse.utils.Copiable;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.inventorization.task.InventorizationTaskTO;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTO;

/**
 * @author Borisok V.V., 07.10.2009
 */
public class InventorizationItemTO extends EqualsByIdImpl implements Copiable {

    private long id;

    private InventorizationTOForReport inventorization;

    private long number;

    private InventorizationTaskTO inventorizationTask;

    private DetailBatchTO detailBatch;

    private StoragePlaceTO storagePlace;

    private String warehouseBatchNotice;

    private MeasureUnitTO countMeas;

    private long neededCount;

    private InventorizationItemProcessingResult processingResult;

    private InventorizationItemState state;

    public InventorizationItemTO(){
    }

    /**
     * Use this constructor to create detail inventorization item.
     * @param inventorization
     * @param detailBatch
     */
    public InventorizationItemTO(InventorizationTO inventorization, DetailBatchTO detailBatch){
        this.inventorization = inventorization;
        this.detailBatch = detailBatch;
    }

    /**
     * Use this constructor to create text inventorization item.
     * @param inventorization
     */
    public InventorizationItemTO(InventorizationTO inventorization){
        this.inventorization = inventorization;
    }

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

    public String getItemMeas(){
        return countMeas.getSign();
    }

    public String getWarehouseName() {
        return storagePlace.getWarehouse().getName();
    }

    public String getStoragePlaceSign() {
        return storagePlace.getSign();
    }

    public Long getTaskNumber() {
        return inventorizationTask != null ? inventorizationTask.getNumber() : null;
    }

    public Long getDeviation() {
        Long foundCount = getFoundCount();
        return foundCount != null ? foundCount-neededCount : null;
    }

    public boolean isCompleted() {
        return getFoundCount() != null;
    }

    public void copyFrom(Object source) {
        InventorizationItemTO srcItem = (InventorizationItemTO)source;
        setState(srcItem.getState());
        setProcessingResult(srcItem.getProcessingResult());
        setInventorizationTask(srcItem.getInventorizationTask());
    }

    //======================== Getters and setters =====================================

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public InventorizationTOForReport getInventorization() {
        return inventorization;
    }

    public void setInventorization(InventorizationTOForReport inventorization) {
        this.inventorization = inventorization;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public InventorizationTaskTO getInventorizationTask() {
        return inventorizationTask;
    }

    public void setInventorizationTask(InventorizationTaskTO inventorizationTask) {
        this.inventorizationTask = inventorizationTask;
    }

    public DetailBatchTO getDetailBatch() {
        return detailBatch;
    }

    public void setDetailBatch(DetailBatchTO detailBatch) {
        this.detailBatch = detailBatch;
    }

    public StoragePlaceTO getStoragePlace() {
        return storagePlace;
    }

    public void setStoragePlace(StoragePlaceTO storagePlace) {
        this.storagePlace = storagePlace;
    }

    public MeasureUnitTO getCountMeas() {
        return countMeas;
    }

    public void setCountMeas(MeasureUnitTO countMeas) {
        this.countMeas = countMeas;
    }

    public long getNeededCount() {
        return neededCount;
    }

    public void setNeededCount(long neededCount) {
        this.neededCount = neededCount;
    }

    public Long getFoundCount() {
        return inventorizationTask != null ? inventorizationTask.getFoundCount() : null;
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

    public String getWarehouseBatchNotice() {
        return warehouseBatchNotice;
    }

    public void setWarehouseBatchNotice(String warehouseBatchNotice) {
        this.warehouseBatchNotice = warehouseBatchNotice;
    }
}
