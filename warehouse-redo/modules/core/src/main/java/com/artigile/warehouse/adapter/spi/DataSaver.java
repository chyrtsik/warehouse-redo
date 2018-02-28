/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi;

import java.util.Map;

/**
 * Data saver used for storing every imported data row.
 *
 * @author Valery Barysok, 6/7/11
 */
public interface DataSaver {

    /**
     * Transfers source data row to the saver for subsequent saving.
     *
     * @param sourceDataRow One row with source data,
     *                      where key is identifier of column,
     *                      value - source value of this column.
     */
    void setSourceDataRow(Map<String, String> sourceDataRow);

    /**
     * Called to save new imported row of data.
     * @param dataRow key-value pairs to be saved.
     */
    void saveDataRow(Map<String, String> dataRow);
}
