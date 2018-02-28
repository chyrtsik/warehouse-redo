/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.chargeoff;

import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTOForReport;

/**
 * @author Shyrik, 09.10.2009
 */
public class ChargeOffItemTO extends EqualsByIdImpl {
    private long id;
    private long number;
    private DetailBatchTO detailBatch;
    private StoragePlaceTOForReport storagePlace;
    private long count;
    private String notice;

    public ChargeOffItemTO() {
    }

    //============================== Special (unmutable) getters =================================
    public String getItemName(){
        return getDetailBatch().getName();
    }

    public String getItemMisc(){
        return getDetailBatch().getMisc();
    }

    public String getItemNotice(){
        return getDetailBatch().getNotice();
    }

    public String getWarehouseName(){
        return getStoragePlace().getWarehouse().getName();
    }

    public String getStoragePlaceSign(){
        return getStoragePlace().getSign();
    }

    public String getCountMeas(){
        return getDetailBatch().getCountMeas().getSign();
    }

    //================================ Getters and setters ==============================
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

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
