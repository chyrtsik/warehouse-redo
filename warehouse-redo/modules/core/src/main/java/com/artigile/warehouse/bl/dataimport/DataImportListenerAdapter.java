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
 * @author Aliaksandr.Chyrtsik, 07.11.11
 */
public class DataImportListenerAdapter implements DataImportListener {
    @Override
    public void onBeforeCompleteImport(long dataImportId, DataImportContext importContext) throws BusinessException {
    }

    @Override
    public void onAfterCompleteImport(long dataImportId, DataImportContext importContext) throws BusinessException {
    }
}
