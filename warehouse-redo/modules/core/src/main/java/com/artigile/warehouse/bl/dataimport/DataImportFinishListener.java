/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.dataimport;

import com.artigile.warehouse.adapter.spi.DataImportContext;

/**
 * Interface of listener to receive import finish event. Called only once after import has been finished.
 *
 * @author Aliaksandr.Chyrtsik, 07.11.11
 */
public interface DataImportFinishListener {
    /**
     * Called after import has been finished (all imported data have been saved).
     * @param dataImportId id of the import.
     * @param importContext import context (to access import details).
     */
    void onImportFinished(long dataImportId, DataImportContext importContext);
}
