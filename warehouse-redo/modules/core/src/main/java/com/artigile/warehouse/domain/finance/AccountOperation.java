/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.finance;

import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.utils.ModelFieldsLengths;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Shyrik, 02.12.2008
 */

/**
 * Holds information about operation have been performed to account.
 */
@Entity
public class AccountOperation {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Account, for which this operation has been made.
     */
    @ManyToOne(optional = false)
    private Account account;

    /**
     * Initial balance value.
     */
    @Column(precision = ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION,
            scale = ModelFieldsLengths.MAX_LENGTH_DOUBLE_SCALE,
            nullable = false)
    private BigDecimal initialBalance;

    /**
     * New balance value.
     */
    @Column(precision = ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION,
            scale = ModelFieldsLengths.MAX_LENGTH_DOUBLE_SCALE,
            nullable = false)
    private BigDecimal newBalance;

    /**
     * Value of changing the balance.
     */
    @Column(precision = ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION,
            scale = ModelFieldsLengths.MAX_LENGTH_DOUBLE_SCALE,
            nullable = false)
    private BigDecimal changeOfBalance;

    /**
     * Date and time of operation.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationDateTime;

    /**
     * User that performed this operation.
     */
    @ManyToOne(optional = false)
    private User performedUser;

    private String operation;

    /**
     * Additional information about operation.
     */
    @Column(nullable = false)
    private String notice;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //======================= Gettters and setters =====================//
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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

    public Date getOperationDateTime() {
        return operationDateTime;
    }

    public void setOperationDateTime(Date operationDateTime) {
        this.operationDateTime = operationDateTime;
    }

    public User getPerformedUser() {
        return performedUser;
    }

    public void setPerformedUser(User performedUser) {
        this.performedUser = performedUser;
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

    public long getVersion() {
        return version;
    }
}