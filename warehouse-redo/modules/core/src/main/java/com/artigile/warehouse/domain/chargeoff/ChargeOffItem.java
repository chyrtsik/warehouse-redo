/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.chargeoff;

import com.artigile.warehouse.domain.complecting.ComplectingTask;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteItem;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.details.DetailBatchOperation;
import com.artigile.warehouse.domain.directory.MeasureUnit;
import com.artigile.warehouse.domain.warehouse.StoragePlace;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Shyrik, 09.10.2009
 */

/**
 * Represents ware, has been affected by charge-off operation.
 */
@Entity
public class ChargeOffItem {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Number of the item in the charge off.
     */
    private long number;

    /**
     * Charge off document, to which this item belongs to.
     */
    @ManyToOne(optional = false)
    private ChargeOff chargeOff;

    /**
     * Ware, that has been charged off.
     */
    @ManyToOne(optional = false)
    private DetailBatch detailBatch;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "shelf_life_date")
    private Date shelfLifeDate;

    /**
     * Warehouse notice about item, that has been chanrged off (has same meaning, than
     * notice field of the warehouse batch entity).
     */
    private String warehouseNotice;

    /**
     * Storage place, from which ware has been charged off.
     */
    @ManyToOne(optional = false)
    private StoragePlace storagePlace;

    /**
     * Count of ware, that has been charged off.
     */
    @Column(nullable = false)
    private long amount;

    /**
     * Measure unit for count, has been changed off.
     */
    @ManyToOne(optional = false)
    private MeasureUnit countMeas;

    /**
     * Notice about position, that has been charged off.
     */
    private String notice;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * Delivery note item, if charge off has been performed into delivery note.
     */
    @OneToOne
    private DeliveryNoteItem deliveryNoteItem;

    /**
     * Complecting task resulted this charge off item to be created.
     */
    @OneToOne(mappedBy = "chargeOffItem")
    private ComplectingTask complectingTask;

    /**
     * Operations performed with warehouse when this charge off items was created.
     */
    @OneToOne(mappedBy = "chargeOffItem")
    private DetailBatchOperation chargeOffOperation;

    //================================ Constructors ==================================
    public ChargeOffItem(){
    }

    //============================== Getters and setters ============================
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

    public ChargeOff getChargeOff() {
        return chargeOff;
    }

    public void setChargeOff(ChargeOff chargeOff) {
        this.chargeOff = chargeOff;
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

    public StoragePlace getStoragePlace() {
        return storagePlace;
    }

    public void setStoragePlace(StoragePlace storagePlace) {
        this.storagePlace = storagePlace;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setCountMeas(MeasureUnit countMeas) {
        this.countMeas = countMeas;
    }

    public MeasureUnit getCountMeas() {
        return countMeas;
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

    public DeliveryNoteItem getDeliveryNoteItem() {
        return deliveryNoteItem;
    }

    public void setDeliveryNoteItem(DeliveryNoteItem deliveryNoteItem) {
        this.deliveryNoteItem = deliveryNoteItem;
    }

    public ComplectingTask getComplectingTask() {
        return complectingTask;
    }

    public void setComplectingTask(ComplectingTask complectingTask) {
        this.complectingTask = complectingTask;
    }

    public DetailBatchOperation getChargeOffOperation() {
        return chargeOffOperation;
    }

    public void setChargeOffOperation(DetailBatchOperation chargeOffOperation) {
        this.chargeOffOperation = chargeOffOperation;
    }
}
