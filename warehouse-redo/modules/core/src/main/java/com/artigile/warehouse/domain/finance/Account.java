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

import com.artigile.warehouse.domain.contractors.Contractor;
import com.artigile.warehouse.utils.ModelFieldsLengths;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Shyrik, 02.12.2008
 */

/**
 * Class represents an account of a update. Account may have positive
 * or negative balance and only one currency.
 */

@Entity
public class Account {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Currency of account.
     */
    @ManyToOne(optional = false)
    private Currency currency;

    /**
     * Contractor, to whon this account belongs to.
     */
    @ManyToOne(optional = false)
    private Contractor contractor;

    /**
     * Current balance of the account.
     */
    @Column(precision = ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION,
            scale = ModelFieldsLengths.MAX_LENGTH_DOUBLE_SCALE,
            nullable = false)
    private BigDecimal currentBalance;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public Account() {
    }

    public Account(Currency currency, Contractor contractor, BigDecimal currentBalance) {
        this.currency = currency;
        this.contractor = contractor;
        this.currentBalance = currentBalance;
    }

    //======================== Getters abd setters ==========================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public long getVersion() {
        return version;
    }
}
