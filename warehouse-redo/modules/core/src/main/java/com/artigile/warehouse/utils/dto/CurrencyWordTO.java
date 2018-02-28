/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto;

import com.artigile.warehouse.domain.Gender;

/**
 * @author Valery Barysok, 10/2/11
 */

public class CurrencyWordTO {

    private long id;

    private CurrencyTO currency;

    private String unitWord;

    private String twoUnitsWord;

    private String fiveUnitsWord;

    private Gender gender;

    private boolean fractionalPart;

    private int fractionalPrecision;

    private String fractionalUnitWord;

    private String fractionalTwoUnitsWord;

    private String fractionalFiveUnitsWord;

    private Gender fractionalGender;

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

    public String getUnitWord() {
        return unitWord;
    }

    public void setUnitWord(String unitWord) {
        this.unitWord = unitWord;
    }

    public String getTwoUnitsWord() {
        return twoUnitsWord;
    }

    public void setTwoUnitsWord(String twoUnitsWord) {
        this.twoUnitsWord = twoUnitsWord;
    }

    public String getFiveUnitsWord() {
        return fiveUnitsWord;
    }

    public void setFiveUnitsWord(String fiveUnitsWord) {
        this.fiveUnitsWord = fiveUnitsWord;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean getFractionalPart() {
        return fractionalPart;
    }

    public void setFractionalPart(boolean fractionalPart) {
        this.fractionalPart = fractionalPart;
    }

    public int getFractionalPrecision() {
        return fractionalPrecision;
    }

    public void setFractionalPrecision(int fractionalPrecision) {
        this.fractionalPrecision = fractionalPrecision;
    }

    public String getFractionalUnitWord() {
        return fractionalUnitWord;
    }

    public void setFractionalUnitWord(String fractionalUnitWord) {
        this.fractionalUnitWord = fractionalUnitWord;
    }

    public String getFractionalTwoUnitsWord() {
        return fractionalTwoUnitsWord;
    }

    public void setFractionalTwoUnitsWord(String fractionalTwoUnitsWord) {
        this.fractionalTwoUnitsWord = fractionalTwoUnitsWord;
    }

    public String getFractionalFiveUnitsWord() {
        return fractionalFiveUnitsWord;
    }

    public void setFractionalFiveUnitsWord(String fractionalFiveUnitsWord) {
        this.fractionalFiveUnitsWord = fractionalFiveUnitsWord;
    }

    public Gender getFractionalGender() {
        return fractionalGender;
    }

    public void setFractionalGender(Gender fractionalGender) {
        this.fractionalGender = fractionalGender;
    }
}
