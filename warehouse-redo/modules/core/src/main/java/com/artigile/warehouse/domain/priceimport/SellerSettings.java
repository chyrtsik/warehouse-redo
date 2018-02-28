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

import com.artigile.warehouse.domain.admin.User;

import javax.persistence.*;

/**
 * @author Valery Barysok, 9/19/11
 */

@Entity
public class SellerSettings {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    private User user;

    private long contractorId;

    private long currencyId;

    private long measureUnitId;

    @Column(nullable = false)
    private String importAdapterUid;

    @Lob
    @Column(nullable = false)
    private String adapterConfig;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public SellerSettings() {
    }

    public SellerSettings(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public long getVersion() {
        return version;
    }
}
