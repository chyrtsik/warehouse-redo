/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi;

import java.util.Map;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public abstract class DataSaverAdapter implements DataSaver {

    @Override
    public void setSourceDataRow(Map<String, String> sourceDataRow) {
        // Can be reimplemented, if need
    }

    @Override
    public abstract void saveDataRow(Map<String, String> dataRow); // Should be reimplemented in a child class
}
