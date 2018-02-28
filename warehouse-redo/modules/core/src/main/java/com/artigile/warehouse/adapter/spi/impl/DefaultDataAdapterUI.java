/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl;

import com.artigile.warehouse.adapter.spi.DataAdapterConfigView;
import com.artigile.warehouse.adapter.spi.DataAdapterUI;
import com.artigile.warehouse.adapter.spi.impl.configuration.DefaultDataAdapterConfigView;

/**
 * @author Valery Barysok, 6/12/11
 */

public class DefaultDataAdapterUI implements DataAdapterUI {

    @Override
    public DataAdapterConfigView getConfigView() {
        return new DefaultDataAdapterConfigView();
    }
}
