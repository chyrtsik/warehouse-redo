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

import com.artigile.warehouse.adapter.spi.AdapterConfigurationLoader;
import com.artigile.warehouse.adapter.spi.AdapterConfigurationSaver;
import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.domain.dataimport.StoredFile;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import java.io.InputStream;

/**
 * Detaulf helper class for saving and loading data import configuration.
 *
 * @author Aliaksandr.Chyrtsik, 14.07.11
 */
public class DefaultDataImportConfigurationPersister implements AdapterConfigurationLoader, AdapterConfigurationSaver{
    /**
     * Data import which configuration is saved or loaded.
     */
    private long dataImportId;

    public DefaultDataImportConfigurationPersister(long dataImportId) {
        this.dataImportId = dataImportId;
    }

    @Override
    public InputStream loadDataInputStream(String streamId) {
        if (!streamId.matches("^\\{StoredFile:\\d+\\}$")){
            throw new IllegalArgumentException("StreamId=" + streamId + " is invalid");
        }
        long fileId = Long.valueOf(streamId.substring(12, streamId.length()-1));
        try {
            return getDataImportService().loadAttachedFile(dataImportId, fileId);
        } catch (BusinessException e) {
            LoggingFacade.logError(this, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String saveDataInputStream(InputStream inputStream) {
        StoredFile storedFile;
        try {
            storedFile = getDataImportService().attachFileToImport(dataImportId, "DataImport data stream", inputStream);
        } catch (BusinessException e) {
            LoggingFacade.logError(this, e);
            throw new RuntimeException(e);
        }
        return "{StoredFile:" + storedFile.getId() + "}";
    }

    @Override
    public void updateConfigurationString(String configurationString){
        try {
            getDataImportService().updateImportConfiguration(dataImportId, configurationString);
        } catch (BusinessException e) {
            LoggingFacade.logError(this, e);
            throw new RuntimeException(e);
        }
    }

    private DataImportService getDataImportService() {
        return SpringServiceContext.getInstance().getDataImportService();
    }
}
