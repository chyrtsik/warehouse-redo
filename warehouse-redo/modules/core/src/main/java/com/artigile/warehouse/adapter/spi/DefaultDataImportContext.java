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
 * Default implementation of data import context.
 *
 * @author Valery Barysok, 6/26/11
 */

public class DefaultDataImportContext implements DataImportContext {

    private DataAdapterConfiguration configuration;

    private DataImportProgressListener progressListener;

    private DataSaver dataSaver;

    public DefaultDataImportContext(DataAdapterConfiguration configuration, DataImportProgressListener progressListener, DataSaver dataSaver) {
        this.configuration = configuration;
        this.progressListener = progressListener != null ? progressListener : new DefaultImportProgressListener();
        this.dataSaver = dataSaver;
    }

    @Override
    public DataAdapterConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public DataSaver getDataSaver() {
        return dataSaver;
    }

    @Override
    public DataImportProgressListener getImportProgressListener() {
        return progressListener;
    }

    /**
     * Default implementation of import progress listener.
     */
    private class DefaultImportProgressListener implements DataImportProgressListener {
        @Override
        public void onTotalRowCountChanged(int newTotalRowCount) {
            //Do nothing.
        }

        @Override
        public void onImportedRowCountChanged(int newImportedRowCount) {
            //Do nothing.
        }
    }
}