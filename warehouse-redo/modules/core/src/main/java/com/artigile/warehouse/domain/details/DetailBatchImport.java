/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.details;

import com.artigile.warehouse.domain.dataimport.DataImport;
import com.artigile.warehouse.domain.directory.MeasureUnit;
import com.artigile.warehouse.domain.finance.Currency;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * Holds information specific for self price list import (detail batches import
 * of a company which uses this program).
 *
 * @author Aliaksandr.Chyrtsik, 02.11.11
 */
@Entity
@PrimaryKeyJoinColumn(name="dataImport_id")
public class DetailBatchImport extends DataImport {

    /**
     * Type of details which was imported during this import.
     * Only single detail type may be imported during each import.
     */
    @ManyToOne(optional = false)
    private DetailType detailType;

    /**
     * Currency for imported items price.
     */
    @ManyToOne(optional = false)
    private Currency currency;

    /**
     * Measure init for imported items count.
     */
    @ManyToOne(optional = false)
    private MeasureUnit measureUnit;

    /**
     * Count of new detail batches.
     */
    private Integer insertedItemsCount;

    /**
     * Count of updated detail batches.
     */
    private Integer updatedItemsCount;

    /**
     * Count of positions skipped due to errors.
     */
    private Integer errorItemsCount;

    public DetailType getDetailType() {
        return detailType;
    }

    public void setDetailType(DetailType detailType) {
        this.detailType = detailType;
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

    public Integer getInsertedItemsCount() {
        return insertedItemsCount;
    }

    public void setInsertedItemsCount(Integer insertedItemsCount) {
        this.insertedItemsCount = insertedItemsCount;
    }

    public Integer getUpdatedItemsCount() {
        return updatedItemsCount;
    }

    public void setUpdatedItemsCount(Integer updatedItemsCount) {
        this.updatedItemsCount = updatedItemsCount;
    }

    public Integer getErrorItemsCount() {
        return errorItemsCount;
    }

    public void setErrorItemsCount(Integer errorItemsCount) {
        this.errorItemsCount = errorItemsCount;
    }
}
