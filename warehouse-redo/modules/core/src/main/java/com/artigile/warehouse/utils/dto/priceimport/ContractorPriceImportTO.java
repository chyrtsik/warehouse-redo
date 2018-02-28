/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.priceimport;

import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.dataimport.DataImportTO;

/**
 * @author Valery.Barysok
 */
public class ContractorPriceImportTO extends DataImportTO {

    private int itemCount;
    private int ignoredItemCount;
    private ContractorTO contractor;
    private CurrencyTO currency;
    private MeasureUnitTO measureUnit;
    private CurrencyTO conversionCurrency;
    private double conversionExchangeRate;

    public ContractorPriceImportTO createNewAsCopy() {
        ContractorPriceImportTO clone = new ContractorPriceImportTO();
        clone.setDescription(this.getDescription());
        clone.setAdapterConf(this.getAdapterConf());
        clone.setAdapterUid(this.getAdapterUid());
        clone.setUser(WareHouse.getUserSession().getUser()); //TODO: Eliminate this dependence of UI tier.
        clone.setContractor(this.getContractor());
        clone.setCurrency(this.getCurrency());
        clone.setMeasureUnit(this.getMeasureUnit());
        return clone;
    }

    //======================= Getters and setters ==================================

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

    public ContractorTO getContractor() {
        return contractor;
    }

    public void setContractor(ContractorTO contractor) {
        this.contractor = contractor;
    }

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
    }

    public MeasureUnitTO getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(MeasureUnitTO measureUnit) {
        this.measureUnit = measureUnit;
    }

    public CurrencyTO getConversionCurrency() {
        return conversionCurrency;
    }

    public void setConversionCurrency(CurrencyTO conversionCurrency) {
        this.conversionCurrency = conversionCurrency;
    }

    public double getConversionExchangeRate() {
        return conversionExchangeRate;
    }

    public void setConversionExchangeRate(double conversionExchangeRate) {
        this.conversionExchangeRate = conversionExchangeRate;
    }
}
