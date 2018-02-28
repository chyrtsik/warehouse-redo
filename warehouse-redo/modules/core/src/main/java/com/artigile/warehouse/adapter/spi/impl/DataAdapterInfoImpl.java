/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl;

import com.artigile.warehouse.adapter.spi.DataAdapterInfo;

/**
 * @author Aliaksandr.Chyrtsik, 04.11.11
 */
public class DataAdapterInfoImpl implements DataAdapterInfo {
    private final String adapterUid;
    private final String adapterName;
    private final String adapterDescription;

    public DataAdapterInfoImpl(String adapterUid, String adapterName, String adapterDescription) {
        this.adapterUid = adapterUid;
        this.adapterName = adapterName;
        this.adapterDescription = adapterDescription;
    }

    @Override
    public String getAdapterUid() {
        return adapterUid;
    }

    @Override
    public String getAdapterName() {
        return adapterName;
    }

    @Override
    public String getAdapterDescription() {
        return adapterDescription;
    }
}
