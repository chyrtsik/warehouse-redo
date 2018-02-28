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
import javax.validation.constraints.NotNull;

/**
 * @author Shyrik, 01.12.2008
 */

/**
 * Class represents currency.
 */
@Entity
public class Currency {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Unique identification number of currency in global directory.
     */
    @Column(unique = true, updatable = false, length = ModelFieldsLengths.UID_LENGTH)
    private String uidCurrency;

    /**
     * Currency short name (sign).
     */
    @Column(nullable = false, unique = true, length = 100)
    private String sign;

    /**
     * Currency full name.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Is true, then this currency is a default currency in the system.
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean defaultCurrency;

    @NotNull
    @OneToOne(mappedBy = "currency", cascade = CascadeType.ALL)
    private CurrencyWord currencyWord = new CurrencyWord();

    /**
     * RGB color, associated with this currency.
     */
    private int associatedColor;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //============ Constructor ===============================
    public Currency() {
    }

    public Currency(String sign, String name){
        this.sign = sign;
        this.name = name;
    }

    //=============== Getters and setters =====================

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

    public boolean isDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(boolean defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public CurrencyWord getCurrencyWord() {
        return currencyWord;
    }

    public void setCurrencyWord(CurrencyWord currencyWord) {
        this.currencyWord = currencyWord;
    }

    public int getAssociatedColor() {
        return associatedColor;
    }

    public void setAssociatedColor(int associatedColor) {
        this.associatedColor = associatedColor;
    }

    public long getVersion() {
        return version;
    }
}
