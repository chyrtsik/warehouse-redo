/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.movement;

import com.artigile.warehouse.domain.movement.MovementResult;
import com.artigile.warehouse.domain.movement.MovementState;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Shyrik, 21.11.2009
 */
public class MovementTOForReport extends EqualsByIdImpl{
    private long id;
    private long number;
    private MovementState state;
    private MovementResult result;
    private UserTO createUser;
    private Date createDate;
    private Date beginDate;
    private Date endDate;
    private String notice;
    private WarehouseTOForReport fromWarehouse;
    private WarehouseTOForReport toWarehouse;

    //=============================== Operations ==================================
    public void init() {
        number = SpringServiceContext.getInstance().getMovementService().getNextAvailableMovementNumber();
        state = MovementState.CONSTRUCTION;
        createDate = Calendar.getInstance().getTime();
        createUser = WareHouse.getUserSession().getUser();
    }

    public boolean hasSameItem(MovementItemTO item) {
        return getSameItem(item) != null;
    }

    public MovementItemTO getSameItem(MovementItemTO item) {
        return SpringServiceContext.getInstance().getMovementService().findSameMovementItem(item);
    }

    public boolean canBeginMovement() {
        return getState().equals(MovementState.CONSTRUCTION);
    }

    public boolean canCancelMovement(){
        return getState().equals(MovementState.COMPLECTING);
    }

    //=========================== Calculated getters ==============================
    public boolean isNew() {
        return id == 0;
    }

    //=========================== Getters and setters =============================
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

    public MovementState getState() {
        return state;
    }

    public void setState(MovementState state) {
        this.state = state;
    }

    public MovementResult getResult() {
        return result;
    }

    public void setResult(MovementResult result) {
        this.result = result;
    }

    public UserTO getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserTO createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public WarehouseTOForReport getFromWarehouse() {
        return fromWarehouse;
    }

    public void setFromWarehouse(WarehouseTOForReport fromWarehouse) {
        this.fromWarehouse = fromWarehouse;
    }

    public WarehouseTOForReport getToWarehouse() {
        return toWarehouse;
    }

    public void setToWarehouse(WarehouseTOForReport toWarehouse) {
        this.toWarehouse = toWarehouse;
    }
}
