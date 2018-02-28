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

import com.artigile.warehouse.utils.custom.types.VariantPrice;
import com.artigile.warehouse.utils.custom.types.VariantQuantity;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

import java.util.Date;

/**
 *
 * @author Valery.Barysok
 */
public class ContractorProductTOForReport extends EqualsByIdImpl {

    private long id;
    private String name;
    private String description;
    private String year;
    private VariantQuantity quantity;

    /**
     * Wholesale prices: initial, with discount and with extra charge
     */
    private VariantPrice wholesalePrice;
    private VariantPrice originalWholesalePrice;
    private VariantPrice wholesaleDiscountPrice;
    private VariantPrice wholesalePriceWithExtraCharge;

    /**
     * Retails prices: initial, with discount and with extra charge
     */
    private VariantPrice retailPrice;
    private VariantPrice originalRetailPrice;
    private VariantPrice retailDiscountPrice;
    private VariantPrice retailPriceWithExtraCharge;

    private VariantQuantity pack;
    private String productType;
    private Date postingDate;
    private CurrencyTO currency;
    private CurrencyTO originalCurrency;
    private MeasureUnitTO measureUnit;
    private String sourceData;
    private boolean selected;
    private ContractorPriceImportTO priceImport;


    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public VariantQuantity getQuantity() {
        return quantity;
    }

    public void setQuantity(VariantQuantity quantity) {
        this.quantity = quantity;
    }

    public VariantPrice getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(VariantPrice wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public VariantPrice getOriginalWholesalePrice() {
        return originalWholesalePrice;
    }

    public void setOriginalWholesalePrice(VariantPrice originalWholesalePrice) {
        this.originalWholesalePrice = originalWholesalePrice;
    }

    public VariantPrice getWholesaleDiscountPrice() {
        return wholesaleDiscountPrice;
    }

    public void setWholesaleDiscountPrice(VariantPrice wholesaleDiscountPrice) {
        this.wholesaleDiscountPrice = wholesaleDiscountPrice;
    }

    public VariantPrice getWholesalePriceWithExtraCharge() {
        return wholesalePriceWithExtraCharge;
    }

    public void setWholesalePriceWithExtraCharge(VariantPrice wholesalePriceWithExtraCharge) {
        this.wholesalePriceWithExtraCharge = wholesalePriceWithExtraCharge;
    }

    public VariantPrice getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(VariantPrice retailPrice) {
        this.retailPrice = retailPrice;
    }

    public VariantPrice getOriginalRetailPrice() {
        return originalRetailPrice;
    }

    public void setOriginalRetailPrice(VariantPrice originalRetailPrice) {
        this.originalRetailPrice = originalRetailPrice;
    }

    public VariantPrice getRetailDiscountPrice() {
        return retailDiscountPrice;
    }

    public void setRetailDiscountPrice(VariantPrice retailDiscountPrice) {
        this.retailDiscountPrice = retailDiscountPrice;
    }

    public VariantPrice getRetailPriceWithExtraCharge() {
        return retailPriceWithExtraCharge;
    }

    public void setRetailPriceWithExtraCharge(VariantPrice retailPriceWithExtraCharge) {
        this.retailPriceWithExtraCharge = retailPriceWithExtraCharge;
    }

    public VariantQuantity getPack() {
        return pack;
    }

    public void setPack(VariantQuantity pack) {
        this.pack = pack;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Date getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Date postingDate) {
        this.postingDate = postingDate;
    }

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
    }

    public CurrencyTO getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(CurrencyTO originalCurrency) {
        this.originalCurrency = originalCurrency;
    }

    public MeasureUnitTO getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(MeasureUnitTO measureUnit) {
        this.measureUnit = measureUnit;
    }

    public String getSourceData() {
        return sourceData;
    }

    public void setSourceData(String sourceData) {
        this.sourceData = sourceData;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ContractorPriceImportTO getPriceImport() {
        return priceImport;
    }

    public void setPriceImport(ContractorPriceImportTO priceImport) {
        this.priceImport = priceImport;
    }
}
