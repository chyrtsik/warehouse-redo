/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.priceimport;

import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

/**
 * @author Valery Barysok, 9/19/11
 */

public class SellerSettingsTO extends EqualsByIdImpl {

    private long id;

    private UserTO user;

    private long contractorId;

    private long currencyId;

    private long measureUnitId;

    private String importAdapterUid;

    private String adapterConfig;

    public SellerSettingsTO() {
    }

    public SellerSettingsTO(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserTO getUser() {
        return user;
    }

    public void setUser(UserTO user) {
        this.user = user;
    }

    public long getContractorId() {
        return contractorId;
    }

    public void setContractorId(long contractorId) {
        this.contractorId = contractorId;
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    public long getMeasureUnitId() {
        return measureUnitId;
    }

    public void setMeasureUnitId(long measureUnitId) {
        this.measureUnitId = measureUnitId;
    }

    public String getImportAdapterUid() {
        return importAdapterUid;
    }

    public void setImportAdapterUid(String importAdapterUid) {
        this.importAdapterUid = importAdapterUid;
    }

    public String getAdapterConfig() {
        return adapterConfig;
    }

    public void setAdapterConfig(String adapterConfig) {
        this.adapterConfig = adapterConfig;
    }
}
