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
 * @author IoaN, Dec 14, 2008
 */

public class AccountTO extends EqualsByIdImpl {
    private long id;

    private CurrencyTO currency;

    private BigDecimal currentBalance;

    private String notice;

    public AccountTO() {
    }

    public AccountTO(long id, CurrencyTO currency, BigDecimal currentBalance) {
        this.id = id;
        this.currency = currency;
        this.currentBalance = currentBalance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @Override
    public String toString() {
        return currency.toString();
    }
}
