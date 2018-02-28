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

import com.artigile.warehouse.adapter.spi.*;
import com.artigile.warehouse.adapter.spi.impl.configuration.ExcelDataAdapterConfiguration;

import java.util.List;

/**
 * @author Valery Barysok, 6/7/11
 */

public class ExcelDataAdapterFactoryImpl implements DataAdapterFactory {

    @Override
    public DataAdapterInfo getDataAdapterInfo() {
        return ExcelDataAdapter.getDataAdapterInfo();
    }

    @Override
    public DataAdapter createDataAdapter() {
        return new ExcelDataAdapter();
    }

    @Override
    public DataAdapterUI createDataAdapterUI(List<DomainColumn> domainColumns) {
        return new ExcelDataAdapterUI(domainColumns);
    }

    @Override
    public DataAdapterConfiguration createDataAdapterConfiguration(String configurationString) {
        return new ExcelDataAdapterConfiguration(configurationString);
    }
}