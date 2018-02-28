/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.priceimport;

import com.artigile.warehouse.domain.contractors.Contractor;
import com.artigile.warehouse.domain.dataimport.DataImport;
import com.artigile.warehouse.domain.directory.MeasureUnit;
import com.artigile.warehouse.domain.finance.Currency;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * Data specific for contractor product data import.
 *
 * @author Valery Barysok, 29.05.2011
 */
@Entity
@PrimaryKeyJoinColumn(name="dataImport_id")
public class ContractorPriceImport extends DataImport {

    /**
     * Total count of records imported.
     */
    private int itemCount;

    /**
     * Total count of ignored records imported.
     */
    private int ignoredItemCount;

    /**
     * Contractor which price-list has been imported.
     */
    @ManyToOne(optional = false)
    private Contractor contractor;

    /**
     * Currency of price list.
     */
    @ManyToOne(optional = false)
    private Currency currency;

    /**
     * Measure unit of items in a price list.
     */
    @ManyToOne(optional = false)
    private MeasureUnit measureUnit;

    /**
     * Currency for later price list conversion
     */
    @ManyToOne(optional = false)
    private Currency conversionCurrency;

    /**
     * Exchange rate between the currency of a price list and the currency of conversion
     */
    private double conversionExchangeRate;

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getIgnoredItemCount() {
        return ignoredItemCount;
    }

    public void setIgnoredItemCount(int ignoredItemCount) {
        this.ignoredItemCount = ignoredItemCount;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public MeasureUnit getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(MeasureUnit measureUnit) {
        this.measureUnit = measureUnit;
    }

    public Currency getConversionCurrency() {
        return conversionCurrency;
    }

    public void setConversionCurrency(Currency conversionCurrency) {
        this.conversionCurrency = conversionCurrency;
    }

    public double getConversionExchangeRate() {
        return conversionExchangeRate;
    }

    public void setConversionExchangeRate(double conversionExchangeRate) {
        this.conversionExchangeRate = conversionExchangeRate;
    }
}
