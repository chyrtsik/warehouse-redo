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

import com.artigile.warehouse.adapter.spi.DataAdapterConfigView;
import com.artigile.warehouse.adapter.spi.DataAdapterUI;
import com.artigile.warehouse.adapter.spi.impl.configuration.ExcelDataAdapterConfigView;

import java.util.List;

/**
 * @author Valery Barysok, 6/12/11
 */

public class ExcelDataAdapterUI implements DataAdapterUI {

    private List<DomainColumn> domainColumns;

    public ExcelDataAdapterUI(List<DomainColumn> domainColumns) {
        this.domainColumns = domainColumns;
    }

    @Override
    public DataAdapterConfigView getConfigView() {
        return new ExcelDataAdapterConfigView(domainColumns);
    }
}
