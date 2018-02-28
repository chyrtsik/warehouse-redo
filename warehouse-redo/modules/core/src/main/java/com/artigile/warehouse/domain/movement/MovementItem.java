/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.movement;

import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.chargeoff.ChargeOff;
import com.artigile.warehouse.domain.complecting.ComplectingTask;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.details.DetailBatchOperation;
import com.artigile.warehouse.domain.directory.MeasureUnit;
import com.artigile.warehouse.domain.postings.Posting;
import com.artigile.warehouse.domain.postings.PostingItem;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.domain.warehouse.WarehouseBatch;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Shyrik, 21.11.2009
 */

/**
 * Describes item of movement document.
 */
@Entity
public class MovementItem {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    private Movement movement;

    /**
     * Number of the movement item in the movement document.
     */
    @Column(nullable = false)
    private long number;

    /**
     * Current state of the movement document.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MovementItemState state;

    /**
     * Result of movement item processing.
     */
    @Enumerated(value = EnumType.STRING)
    private MovementItemProcessingResult processingResult;

    /**
     * Warehouse batch to be moved (nol null until item is not in processing).
     */
    @ManyToOne
    private WarehouseBatch warehouseBatch;

    /**
     * Ware being moved.
     */
    @ManyToOne
    private DetailBatch detailBatch;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "shelf_life_date")
    private Date shelfLifeDate;

    /**
     * Notice about ware from warehouse.
     */
    private String warehouseNotice;

    /**
     * Count of moved wares.
     */
    private Long amount;

    /**
     * Measure unit of the ware's count (at the movement time).
     */
    @ManyToOne(optional = false)
    private MeasureUnit countMeas;

    /**
     * Storage place, from which wares are moved.
     */
    @ManyToOne(optional = false)
    private StoragePlace fromStoragePlace;

    /**
     * Complecting task, that has been created for processing of this movement item.
     */
    @OneToOne(mappedBy = "movementItem")
    private ComplectingTask complectingTask;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //================================== Special getters ===============================
    public User getShippedUser() {
        ChargeOff chargeOff = getChargeOff();
        return chargeOff == null ? null : chargeOff.getPerformer();
    }

    public Date getShippedDate() {
        ChargeOff chargeOff = getChargeOff();
        return chargeOff == null ? null : chargeOff.getPerformDate();
    }

    private ChargeOff getChargeOff(){
        if (getComplectingTask() == null){
            return null;
        }
        else if (getComplectingTask().getChargeOffItem() == null){
            return null;
        }
        return getComplectingTask().getChargeOffItem().getChargeOff();
    }

    public User getPostedUser() {
        Posting posting = getPosting();
        return posting == null ? null : posting.getCreatedUser();
    }

    public Date getPostedDate() {
        Posting posting = getPosting();
        return posting == null ? null : posting.getCreateDate();
    }

    private Posting getPosting(){
        if (getComplectingTask() == null){
            return null;
        }
        else if (getComplectingTask().getChargeOffItem() == null){
            return null;
        }
        else if (getComplectingTask().getChargeOffItem().getDeliveryNoteItem() == null){
            return null;
        }
        return getComplectingTask().getChargeOffItem().getDeliveryNoteItem().getDeliveryNote().getPosting();
    }

    public BigDecimal getItemCost(){
        if (warehouseBatch != null){
            //Movement item was not shipped from warehouse yet.
            if (warehouseBatch.getPostingItem() != null){
                //Buying price is stored.
                return warehouseBatch.getPostingItem().getPrice();
            }
            else{
                //As buy price is not stored we use current buying price of product.
                return detailBatch.getBuyPrice();
            }
        }
        else {
            //Movement item was shipped from warehouse. Operation item should be created for that case.
            DetailBatchOperation chargeOffOperation = getComplectingTask().getChargeOffItem().getChargeOffOperation();
            if (chargeOffOperation.getPostingItemOfChangedWarehouseBatch() != null){
                //Buying price is stored.
                return chargeOffOperation.getPostingItemOfChangedWarehouseBatch().getPrice();
            }
            else{
                //As buy price is not stored we use current buying price of product.
                return detailBatch.getBuyPrice();
            }
        }
    }

    public PostingItem getInitialPostingItem() {
        if (warehouseBatch != null){
            //Movement item was not shipped from warehouse yet.
            return warehouseBatch.getPostingItem();
        }
        else {
            //Movement item was shipped from warehouse. Operation item should be created for that case.
            return getComplectingTask().getChargeOffItem().getChargeOffOperation().getPostingItemOfChangedWarehouseBatch();
        }
    }

    //================================= Getters and setters ============================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public MovementItemState getState() {
        return state;
    }

    public void setState(MovementItemState state) {
        this.state = state;
    }

    public MovementItemProcessingResult getProcessingResult() {
        return processingResult;
    }

    public void setProcessingResult(MovementItemProcessingResult processingResult) {
        this.processingResult = processingResult;
    }

    public WarehouseBatch getWarehouseBatch() {
        return warehouseBatch;
    }

    public void setWarehouseBatch(WarehouseBatch warehouseBatch) {
        this.warehouseBatch = warehouseBatch;
    }

    public DetailBatch getDetailBatch() {
        return detailBatch;
    }

    public void setDetailBatch(DetailBatch detailBatch) {
        this.detailBatch = detailBatch;
    }

    public Date getShelfLifeDate() {
        return shelfLifeDate;
    }

    public void setShelfLifeDate(Date shelfLifeDate) {
        this.shelfLifeDate = shelfLifeDate;
    }

    public String getWarehouseNotice() {
        return warehouseNotice;
    }

    public void setWarehouseNotice(String warehouseNotice) {
        this.warehouseNotice = warehouseNotice;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public MeasureUnit getCountMeas() {
        return countMeas;
    }

    public void setCountMeas(MeasureUnit countMeas) {
        this.countMeas = countMeas;
    }

    public StoragePlace getFromStoragePlace() {
        return fromStoragePlace;
    }

    public void setFromStoragePlace(StoragePlace fromStoragePlace) {
        this.fromStoragePlace = fromStoragePlace;
    }

    public ComplectingTask getComplectingTask() {
        return complectingTask;
    }

    public void setComplectingTask(ComplectingTask complectingTask) {
        this.complectingTask = complectingTask;
    }

    public long getVersion() {
        return version;
    }
}
