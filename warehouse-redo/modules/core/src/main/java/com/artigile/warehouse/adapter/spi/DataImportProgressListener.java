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

/**
 * Interface of data import progress listener (used to track progress of import process).
 *
 * @author Aliaksandr.Chyrtsik, 14.07.11
 */
public interface DataImportProgressListener {
    /**
     * Called to notify listener that total rows to be imported count is changed.
     * @param newTotalRowCount new total count of rows.
     */
    public void onTotalRowCountChanged(int newTotalRowCount);

    /**
     * Called to notify listener that imported count of rows is changed.
     * @param newImportedRowCount new count of imported rows.
     */
    public void onImportedRowCountChanged(int newImportedRowCount);
}
