/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.warehouse;

import com.artigile.warehouse.dao.generic.lock.Lockable;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.postings.PostingItem;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * @author Shyrik, 26.12.2008
 */

/**
 * Batch of the details at the concrete place of the warehouse.
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"storagePlace_id", "detailBatch_id", "notice", "postingItem_id"})})
@Lockable
public class WarehouseBatch {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Detail batch, which details are in the warehouse batch.
     */
    @ManyToOne(optional = false)
    private DetailBatch detailBatch;


    /**
     * Storage place, where batch is placed.
     */
    @ManyToOne(optional = false)
    private StoragePlace storagePlace;

    /**
     * Count of available details in this warehouse batch.
     */
    @Min(value = 0)
    @Column(nullable = false)
    private long amount;

    /**
     * Count of reserved details in this warehouse batch.
     */
    @Min(value = 0)
    @Column(nullable = false)
    private long reservedCount;

    /**
     * If this flag is set, the count of the details sees to be wrong, and must be
     * updated by the warehouse manager.
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean needRecalculate;

    /**
     * Notice about warehouse batch
     */
    private String notice;

    /**
     * Posting item of this warehouse batch (if tracking of posting items for warehouse batches is enabled).
     */
    @ManyToOne
    private PostingItem postingItem;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "shelf_life_date")
    private Date shelfLifeDate;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getReservedCount() {
        return reservedCount;
    }

    public void setReservedCount(long reservedCount) {
        this.reservedCount = reservedCount;
    }

    public boolean getNeedRecalculate() {
        return needRecalculate;
    }

    public void setNeedRecalculate(boolean needRecalculate) {
        this.needRecalculate = needRecalculate;
    }

    public String getNotice() {
        if (notice == null) {
            notice = "";
        }
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public PostingItem getPostingItem() {
        return postingItem;
    }

    public void setPostingItem(PostingItem postingItem) {
        this.postingItem = postingItem;
    }

    public Date getShelfLifeDate() {
        return shelfLifeDate;
    }

    public void setShelfLifeDate(Date shelfLifeDate) {
        this.shelfLifeDate = shelfLifeDate;
    }

    public long getVersion() {
        return version;
    }
}
