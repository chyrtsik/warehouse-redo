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

import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

/**
 * @author Shyrik, 04.02.2009
 */
public class StoragePlaceTOForReport extends EqualsByIdImpl {
    private long id;
    private String sign;
    private Long fillingDegree;
    private String notice;
    private boolean availableForPosting;
    private WarehouseTOForReport warehouse;

    //================================= Contructors ===============================
    public StoragePlaceTOForReport(){
    }

    public StoragePlaceTOForReport(long id, String sign, Long fillingDegree, String notice,
                                   boolean availableForPosting, WarehouseTOForReport warehouse){
        this.id = id;
        this.sign = sign;
        this.fillingDegree = fillingDegree;
        this.notice = notice;
        this.availableForPosting = availableForPosting;
        this.warehouse = warehouse;
    }

    public StoragePlaceTOForReport(long id) {
        this.id = id;
    }

    public boolean equals(Object obj){
        if (obj instanceof StoragePlaceTOForReport){
            StoragePlaceTOForReport storagePlaceObj = (StoragePlaceTOForReport)obj;
            long id1 = getId();
            long id2 = storagePlaceObj.getId();
            return id1 != 0 && id2 != 0 ? id1 == id2 : super.equals(obj);
        }
        return super.equals(obj);
    }

    //============================= Getters and setters ===========================
    public boolean isNew() {
        return id == 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getFillingDegree() {
        return fillingDegree;
    }

    public void setFillingDegree(Long fillingDegree) {
        this.fillingDegree = fillingDegree;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public boolean isAvailableForPosting() {
        return availableForPosting;
    }

    public void setAvailableForPosting(boolean availableForPosting) {
        this.availableForPosting = availableForPosting;
    }

    public WarehouseTOForReport getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseTOForReport warehouse) {
        this.warehouse = warehouse;
    }
}
