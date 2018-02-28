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

import com.artigile.warehouse.utils.ModelFieldsLengths;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Shyrik, 09.12.2008
 */

/**
 * Holds exchange rate between two currencies.
 */

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"fromCurrency_id", "toCurrency_id"})})
public class ExchangeRate {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Exchange rate value.
     */
    @Column(precision = ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION,
            scale = ModelFieldsLengths.MAX_LENGTH_DOUBLE_SCALE,
            nullable = false)
    private BigDecimal rate;

    @ManyToOne(optional = false)
    private Currency fromCurrency;

    @ManyToOne(optional = false)
    private Currency toCurrency;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //================= Cnstructors and Manipulators ===================
    public ExchangeRate() {

    }

    public ExchangeRate(Currency fromCurrency, Currency toCurrency, BigDecimal rate) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
    }

    public void copyFrom(ExchangeRate rate) {
        this.setRate(rate.getRate());
    }

    //====================== Getters and setters =======================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Currency getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(Currency fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public Currency getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(Currency toCurrency) {
        this.toCurrency = toCurrency;
    }

    public long getVersion() {
        return version;
    }
}
