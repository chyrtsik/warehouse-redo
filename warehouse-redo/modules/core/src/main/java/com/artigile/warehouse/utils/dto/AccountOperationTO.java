/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Shyrik, 18.04.2010
 */

/**
 * Represents data of account operation, that goes to UI layer.
 */
public class AccountOperationTO {
    long id;
    private String contractorName;
    private String currencySign;
    private Date operationDateTime;
    private String performedUserFullName;
    private BigDecimal initialBalance;
    private BigDecimal newBalance;
    private BigDecimal changeOfBalance;
    private String operation;
    private String notice;

    //================================= Getters and setters ================================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public String getCurrencySign() {
        return currencySign;
    }

    public void setCurrencySign(String currencySign) {
        this.currencySign = currencySign;
    }

    public Date getOperationDateTime() {
        return operationDateTime;
    }

    public void setOperationDateTime(Date operationDateTime) {
        this.operationDateTime = operationDateTime;
    }

    public String getPerformedUserFullName() {
        return performedUserFullName;
    }

    public void setPerformedUserFullName(String performedUserFullName) {
        this.performedUserFullName = performedUserFullName;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
    }

    public BigDecimal getChangeOfBalance() {
        return changeOfBalance;
    }

    public void setChangeOfBalance(BigDecimal changeOfBalance) {
        this.changeOfBalance = changeOfBalance;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
