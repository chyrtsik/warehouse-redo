/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.postings;

import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTO;

/**
 * Configuration required to perform posting items import.
 *
 * @author Aliaksandr.Chyrtsik, 07.11.11
 */
public class PostingItemsImportConfiguration {
    /**
     * Posting which items will be imported.
     */
    private long postingId;

    /**
     * Currency to be used for buy price.
     */
    private CurrencyTO currency;

    /**
     * Measure unit for items count.
     */
    private MeasureUnitTO measureUnit;

    /**
     * Storage place for the imported goods.
     */
    private StoragePlaceTO storagePlace;

    /**
     * Adapter to be used to parse source data.
     */
    private String dataAdapterUid;

    /**
     * Configuration to be passed into data adapted.
     */
    private String dataAdapterConfiguration;

    /**
     * Type of identity to be used to determine which product is represented by each position being imported.
     */
    private PostingItemsIdentityType itemsIdentityType;

    public long getPostingId() {
        return postingId;
    }

    public void setPostingId(long postingId) {
        this.postingId = postingId;
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

    public StoragePlaceTO getStoragePlace() {
        return storagePlace;
    }

    public void setStoragePlace(StoragePlaceTO storagePlace) {
        this.storagePlace = storagePlace;
    }

    public String getDataAdapterUid() {
        return dataAdapterUid;
    }

    public void setDataAdapterUid(String dataAdapterUid) {
        this.dataAdapterUid = dataAdapterUid;
    }

    public String getDataAdapterConfiguration() {
        return dataAdapterConfiguration;
    }

    public void setDataAdapterConfiguration(String dataAdapterConfiguration) {
        this.dataAdapterConfiguration = dataAdapterConfiguration;
    }

    public PostingItemsIdentityType getItemsIdentityType() {
        return itemsIdentityType;
    }

    public void setItemsIdentityType(PostingItemsIdentityType itemsIdentityType) {
        this.itemsIdentityType = itemsIdentityType;
    }
}
