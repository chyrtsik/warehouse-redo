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

import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

import java.math.BigDecimal;

/**
 * @author Shyrik, 10.12.2008
 */
public class ExchangeRateTO extends EqualsByIdImpl {
    private long id;

    private CurrencyTO fromCurrency;

    private CurrencyTO toCurrency;

    private BigDecimal rate;

    //================= Getters and setters ==================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CurrencyTO getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(CurrencyTO fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public CurrencyTO getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(CurrencyTO toCurrency) {
        this.toCurrency = toCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
