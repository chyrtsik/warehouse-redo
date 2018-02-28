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
import com.artigile.warehouse.bl.common.exceptions.BusinessException;

/**
 * Listener of data import changes. Used for synchronizing data import changes
 * with other parts of a system.
 *
 * @author Aliaksandr.Chyrtsik, 04.11.11
 */
public interface DataImportListener {
    /**
     * Called to perform related operations before import will be completed.
     *
     * @param dataImportId id of import which is about to be completed.
     * @param importContext import context (full runtime information about import).
     * @throws BusinessException if cannot perform related operations (this will prevent import from completion).
     */
    void onBeforeCompleteImport(long dataImportId, DataImportContext importContext) throws BusinessException;

    /**
     * Called to performed related operations after import has been completed.
     *
     * @param dataImportId id of import has been completed.
     * @param importContext import context (full runtime information about import).
     * @throws BusinessException if cannot perform related operations (this will rollback import completion).
     */
    void onAfterCompleteImport(long dataImportId, DataImportContext importContext) throws BusinessException;
}
