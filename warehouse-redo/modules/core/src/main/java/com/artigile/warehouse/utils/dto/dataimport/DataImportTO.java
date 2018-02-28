/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.dataimport;

import com.artigile.warehouse.domain.dataimport.ImportStatus;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

import java.util.Date;

/**
 * @author Aliaksandr.Chyrtsik, 03.11.11
 */
public class DataImportTO extends EqualsByIdImpl {
    private long id;
    private Date importDate;
    protected String description;
    protected String adapterUid;
    protected String adapterConf;
    private UserTO user;
    private ImportStatus importStatus;

    private String adapterName;

    //=========================== Calculated getters ===============================

    public String getAdapterName() {
        if (adapterName == null){
            adapterName = SpringServiceContext.getInstance().getDataImportService().getDataAdapterNameByAdapterUid(adapterUid);
        }
        return adapterName;
    }

    //=============================== Getters and setters ===============================
    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdapterUid() {
        return adapterUid;
    }

    public void setAdapterUid(String adapterUid) {
        this.adapterUid = adapterUid;
    }

    public String getAdapterConf() {
        return adapterConf;
    }

    public void setAdapterConf(String adapterConf) {
        this.adapterConf = adapterConf;
    }

    public UserTO getUser() {
        return user;
    }

    public void setUser(UserTO user) {
        this.user = user;
    }

    public ImportStatus getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(ImportStatus importStatus) {
        this.importStatus = importStatus;
    }
}
