/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.warehouse;

import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Borisok V.V., 30.12.2008
 */
public class WarehouseBatchTO extends EqualsByIdImpl {
    private long id;

    private DetailBatchTO detailBatch;

    private StoragePlaceTOForReport storagePlace;

    private long count;

    private long reservedCount;

    private boolean needRecalculate;

    private String notice;

    private PostingItemTO postingItem;

    private Date shelfLifeDate;

    public WarehouseBatchTO() {
    }

    //============================= Calculated getters =============================
    public Date getReceiptDate(){
        return postingItem == null ? null : postingItem.getPosting().getCreateDate();
    }

    public BigDecimal getBuyPrice(){
        return postingItem == null ? null : postingItem.getOriginalPrice();
    }

    public CurrencyTO getBuyCurrency(){
        return postingItem == null ? null : postingItem.getOriginalCurrency();
    }

    //============================= Getters and setters ============================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getAvailableCount(){
        return getCount() - getReservedCount();
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
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

    public WarehouseTOForReport getWarehouse() {
        return storagePlace.getWarehouse();
    }

    public String getNeedRecalculateText() {
        return I18nSupport.message(needRecalculate ? "warehousebatch.properties.recalculate.required" : "warehousebatch.properties.recalculate.not.required");
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public PostingItemTO getPostingItem() {
        return postingItem;
    }

    public void setPostingItem(PostingItemTO postingItem) {
        this.postingItem = postingItem;
    }

    public Date getShelfLifeDate() {
        return shelfLifeDate;
    }

    public void setShelfLifeDate(Date shelfLifeDate) {
        this.shelfLifeDate = shelfLifeDate;
    }
}
