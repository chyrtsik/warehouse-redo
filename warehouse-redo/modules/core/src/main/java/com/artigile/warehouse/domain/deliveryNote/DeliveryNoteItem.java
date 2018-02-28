/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.deliveryNote;

import com.artigile.warehouse.domain.chargeoff.ChargeOffItem;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.directory.MeasureUnit;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.utils.ModelFieldsLengths;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Shyrik, 02.11.2009
 */

/**
 * Delivery note item: describes ware in the delivery note.
 */
@Entity
public class DeliveryNoteItem {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Number of the positing in the delivery note document.
     */
    private long number;

    /**
     * Delivery note document, to which this position belongs to.
     */
    @ManyToOne(optional = false)
    private DeliveryNote deliveryNote;

    /**
     * Ware, that is being moving with the delivery note.
     */
    @ManyToOne(optional = false)
    private DetailBatch detailBatch;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "shelf_life_date")
    private Date shelfLifeDate;

    /**
     * Notice for the position from the warehouse.
     * E.g., notice about warehouse batch.
     */
    private String warehouseBatchNotice;

    /**
     * Count of wares in delivery note item.
     */
    @Column(nullable = false)
    private long amount;

    /**
     * Measure unit for delivery note item ware.
     */
    @ManyToOne(optional = false)
    private MeasureUnit countMeas;

    /**
     * If set, represents storage place at the target warehouse
     * (when wares are moved from one warehouse to another).
     */
    @ManyToOne
    private StoragePlace storagePlace;

    /**
     * Price of one exemplar of the position, that
     * was actual for the time, when delivery note was processed.
     */
    @Column(precision = ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION,
            scale = ModelFieldsLengths.MAX_LENGTH_DOUBLE_SCALE)
    private BigDecimal price;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    @OneToOne(mappedBy = "deliveryNoteItem")
    private ChargeOffItem chargeOffItem;

    //=============================== Getters and setters ====================================

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

    public DeliveryNote getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNote = deliveryNote;
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

    public String getWarehouseBatchNotice() {
        return warehouseBatchNotice;
    }

    public void setWarehouseBatchNotice(String warehouseBatchNotice) {
        this.warehouseBatchNotice = warehouseBatchNotice;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public MeasureUnit getCountMeas() {
        return countMeas;
    }

    public void setCountMeas(MeasureUnit countMeas) {
        this.countMeas = countMeas;
    }

    public StoragePlace getStoragePlace() {
        return storagePlace;
    }

    public void setStoragePlace(StoragePlace storagePlace) {
        this.storagePlace = storagePlace;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getVersion() {
        return version;
    }

    public ChargeOffItem getChargeOffItem() {
        return chargeOffItem;
    }

    public void setChargeOffItem(ChargeOffItem chargeOffItem) {
        this.chargeOffItem = chargeOffItem;
    }
}
