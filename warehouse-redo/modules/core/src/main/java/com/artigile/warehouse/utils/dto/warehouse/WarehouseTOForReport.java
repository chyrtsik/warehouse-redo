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

import com.artigile.warehouse.utils.dto.ContactTO;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

/**
 * @author Shyrik, 17.01.2009
 */

/**
 * Short representation of the warehouse.
 */
public class WarehouseTOForReport extends EqualsByIdImpl {
    private long id;
    private String name;
    private String address;
    private ContractorTO owner;
    private ContactTO responsible;
    private String notice;
    private String usualPrinter;
    private String stickerPrinter;

    public WarehouseTOForReport() {}

    public WarehouseTOForReport(long id) {
        this.id = id;
    }

    //================================= Operations ==========================================
    public void copyFrom(WarehouseTOForReport src) {
        setId(src.getId());
        setName(src.getName());
        setAddress(src.getAddress());
        setOwner(src.getOwner());
        setResponsible(src.getResponsible());
        setNotice(src.getNotice());
        setUsualPrinter(src.getUsualPrinter());
        setStickerPrinter(src.getStickerPrinter());
    }

    public boolean equals(Object obj){
        if (obj instanceof WarehouseTOForReport){
            WarehouseTOForReport warehouseObj = (WarehouseTOForReport)obj;
            return getId() == warehouseObj.getId();
        }
        return super.equals(obj);
    }

    //=============================== Gettters and setters ==================================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ContractorTO getOwner() {
        return owner;
    }

    public void setOwner(ContractorTO owner) {
        this.owner = owner;
    }

    public ContactTO getResponsible() {
        return responsible;
    }

    public void setResponsible(ContactTO responsible) {
        this.responsible = responsible;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getUsualPrinter() {
        return usualPrinter;
    }

    public void setUsualPrinter(String usualPrinter) {
        this.usualPrinter = usualPrinter;
    }

    public String getStickerPrinter() {
        return stickerPrinter;
    }

    public void setStickerPrinter(String stickerPrinter) {
        this.stickerPrinter = stickerPrinter;
    }
}
