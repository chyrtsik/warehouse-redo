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

import com.artigile.warehouse.domain.Gender;

import javax.persistence.*;

/**
 * @author Valery Barysok, 10/2/11
 */

@Entity
public class CurrencyWord {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne(optional = false)
    private Currency currency;

    private String unitWord;

    private String twoUnitsWord;

    private String fiveUnitsWord;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean fractionalPart;

    private int fractionalPrecision;

    private String fractionalUnitWord;

    private String fractionalTwoUnitsWord;

    private String fractionalFiveUnitsWord;

    @Enumerated(EnumType.STRING)
    private Gender fractionalGender;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

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

    public boolean isFractionalPart() {
        return fractionalPart;
    }
}
