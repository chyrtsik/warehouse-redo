/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto;

import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

/**
 * @author IoaN, Dec 10, 2008
 */
public class CurrencyTO extends EqualsByIdImpl {

    private long id;

    private String uidCurrency;

    private String sign;

    private String name;

    private boolean defaultCurrency;

    private CurrencyWordTO currencyWord;

    private int associatedColor;


    public CurrencyTO() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CurrencyTO that = (CurrencyTO) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }

    @Override
    public String toString() {
        return name + "(" + sign + ")";
    }


    //=============================== Getters and setters ============================
    public boolean isNew() {
        return id == 0;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUidCurrency() {
        return uidCurrency;
    }

    public void setUidCurrency(String uidCurrency) {
        this.uidCurrency = uidCurrency;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(boolean defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public CurrencyWordTO getCurrencyWord() {
        return currencyWord;
    }

    public void setCurrencyWord(CurrencyWordTO currencyWord) {
        this.currencyWord = currencyWord;
    }

    public int getAssociatedColor() {
        return associatedColor;
    }

    public void setAssociatedColor(int associatedColor) {
        this.associatedColor = associatedColor;
    }
}
