/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.details;

import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.dataimport.DataImportTO;

/**
 * @author Aliaksandr.Chyrtsik, 06.11.11
 */
public class DetailBatchImportTO extends DataImportTO {
    private DetailTypeTOForReport detailType;
    private CurrencyTO currency;
    private MeasureUnitTO measureUnit;

    private Integer insertedItemsCount;
    private Integer updatedItemsCount;
    private Integer errorItemsCount;

    //============================== Calculated getters ==============================
    public Integer getTotalProcessedItemsCount(){
        int totalCount = (insertedItemsCount == null ? 0 : insertedItemsCount) +
                         (updatedItemsCount == null ? 0 : updatedItemsCount) +
                         (errorItemsCount == null ? 0 : errorItemsCount);
        return totalCount > 0 ? totalCount : null;
    }

    //=========================== Getters and setters ================================
    public DetailTypeTOForReport getDetailType() {
        return detailType;
    }

    public void setDetailType(DetailTypeTOForReport detailType) {
        this.detailType = detailType;
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
