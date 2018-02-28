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

import com.artigile.warehouse.domain.directory.MeasureUnit;
import com.artigile.warehouse.domain.finance.Currency;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Valery Barysok, 29.05.2011
 */
@Entity
public class ContractorProduct {

    public static final String postingDateF = "postingDate";

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String simplifiedName;

    private String description;

    @Column(length = 50)
    private String year;

    @Column(nullable = false, length = 100)
    private String quantity;

    @Column(nullable = false, length = 100)
    private String wholesalePrice;

    @Column(nullable = false, length = 100)
    private String retailPrice;

    @Column(nullable = false, length = 100)
    private String pack;

    @Column(nullable = false)
    private String productType;

    @Column(nullable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date postingDate;

    @ManyToOne(optional = false)
    private ContractorPriceImport priceImport;

    @ManyToOne(optional = false)
    private Currency currency;

    @ManyToOne(optional = false)
    private MeasureUnit measureUnit;

    @Column(columnDefinition = "TEXT")
    private String sourceData;
    /**
     * Indicates that this product selected by user in a separate list
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean selected;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /* Constructors
    ------------------------------------------------------------------------------------------------------------------*/
    public ContractorProduct() {
    }

    public ContractorProduct(long id) {
        this.id = id;
    }

    public ContractorProduct(long id, String name, String quantity, String wholesalePrice, String retailPrice, Date postingDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.wholesalePrice = wholesalePrice;
        this.retailPrice = retailPrice;
        this.postingDate = postingDate;
    }

    public ContractorProduct(long id, String name, String simplifiedName, String quantity, String wholesalePrice, String retailPrice, Date postingDate) {
        this.id = id;
        this.name = name;
        this.simplifiedName = simplifiedName;
        this.quantity = quantity;
        this.wholesalePrice = wholesalePrice;
        this.retailPrice = retailPrice;
        this.postingDate = postingDate;
    }


    /* Getters and setters
    ------------------------------------------------------------------------------------------------------------------*/
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

    public String getSimplifiedName() {
        return simplifiedName;
    }

    public void setSimplifiedName(String simplifiedName) {
        this.simplifiedName = simplifiedName;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(String wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
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

    public ContractorPriceImport getPriceImport() {
        return priceImport;
    }

    public void setPriceImport(ContractorPriceImport priceImport) {
        this.priceImport = priceImport;
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

    public long getVersion() {
        return version;
    }
}
