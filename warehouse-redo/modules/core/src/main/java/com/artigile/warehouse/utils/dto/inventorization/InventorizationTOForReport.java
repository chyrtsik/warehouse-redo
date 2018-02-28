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

import com.artigile.warehouse.domain.inventorization.InventorizationResult;
import com.artigile.warehouse.domain.inventorization.InventorizationState;
import com.artigile.warehouse.domain.inventorization.InventorizationType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Shyrik, 29.09.2009
 */
public class InventorizationTOForReport extends EqualsByIdImpl {
    private long id;

    private long number;

    private InventorizationType type;

    private InventorizationState state;

    private InventorizationResult result;

    private Date createDate;

    private UserTO createUser;

    private Date closeDate;

    private UserTO closeUser;

    private WarehouseTOForReport warehouse;

    private String notice;

    private List<StoragePlaceTOForReport> storagePlacesToCheck = new ArrayList<StoragePlaceTOForReport>();

    private Integer completed;

    private Integer total;

    //=============================== Manipulators ===================================
    public InventorizationTOForReport() {
    }

    public void init() {
        number = SpringServiceContext.getInstance().getInventorizationService().getNextAvailableInventorizationNumber();
        type = InventorizationType.BY_WAREHOUSE_PLACES;
        state = InventorizationState.NOT_PROCESSED;
        createDate = Calendar.getInstance().getTime();
        createUser = WareHouse.getUserSession().getUser();
    }

    //============================= Getters and setters ==============================
    public boolean isNew() {
        return id == 0;
    }

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

    public InventorizationType getType() {
        return type;
    }

    public void setType(InventorizationType type) {
        this.type = type;
    }

    public InventorizationState getState() {
        return state;
    }

    public void setState(InventorizationState state) {
        this.state = state;
    }

    public InventorizationResult getResult() {
        return result;
    }

    public void setResult(InventorizationResult result) {
        this.result = result;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    private String getDateAsText(Date date) {
        return date != null ? StringUtils.getDateFormat().format(date) : "";
    }

    public String getCreateDateAsText() {
        return getDateAsText(getCreateDate());
    }

    public UserTO getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserTO createUser) {
        this.createUser = createUser;
    }

    private String getUserDisplayName(UserTO user) {
        return user != null ? user.getDisplayName() : "";
    }

    public String getCreateUserDisplayName() {
        return getUserDisplayName(getCreateUser());
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public String getCloseDateAsText() {
        return getDateAsText(getCloseDate());
    }

    public UserTO getCloseUser() {
        return closeUser;
    }

    public void setCloseUser(UserTO closeUser) {
        this.closeUser = closeUser;
    }

    public String getCloseUserDisplayName() {
        return getUserDisplayName(getCloseUser());
    }

    public WarehouseTOForReport getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseTOForReport warehouse) {
        this.warehouse = warehouse;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public List<StoragePlaceTOForReport> getStoragePlacesToCheck() {
        return storagePlacesToCheck;
    }

    public void setStoragePlacesToCheck(List<StoragePlaceTOForReport> storagePlacesToCheck) {
        this.storagePlacesToCheck = storagePlacesToCheck;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getProcessedState() {
        return completed != null && total != null ?  "" + completed + "/" + total : "";
    }
}
