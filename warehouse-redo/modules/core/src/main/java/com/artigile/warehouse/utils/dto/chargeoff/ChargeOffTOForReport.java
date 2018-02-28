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

import com.artigile.warehouse.domain.chargeoff.ChargeOffReason;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

import java.util.Date;

/**
 * @author Shyrik, 09.10.2009
 */
public class ChargeOffTOForReport extends EqualsByIdImpl{
    private long id;
    private long number;
    private WarehouseTOForReport warehouse;
    private UserTO performer;
    private Date performDate;
    private String notice;
    private ChargeOffReason reason;

    public ChargeOffTOForReport() {
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

    public WarehouseTOForReport getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseTOForReport warehouse) {
        this.warehouse = warehouse;
    }

    public UserTO getPerformer() {
        return performer;
    }

    public void setPerformer(UserTO performer) {
        this.performer = performer;
    }

    public Date getPerformDate() {
        return performDate;
    }

    public void setPerformDate(Date performDate) {
        this.performDate = performDate;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public ChargeOffReason getReason() {
        return reason;
    }

    public void setReason(ChargeOffReason reason) {
        this.reason = reason;
    }
}
