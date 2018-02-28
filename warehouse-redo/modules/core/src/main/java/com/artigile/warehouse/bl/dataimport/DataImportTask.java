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

import com.artigile.warehouse.adapter.spi.*;
import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.util.async.ProgressTrackable;
import com.artigile.warehouse.bl.util.async.ProgressTracker;
import com.artigile.warehouse.domain.dataimport.DataImport;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;

/**
 * Task for performing data import. Ready to be used in background or foreground.
 *
 * @author Aliaksandr.Chyrtsik, 13.07.11
 */
public class DataImportTask implements ProgressTrackable, Runnable {
    /**
     * Component used to monitor import progress.
     */
    private ProgressTracker progressTracker = new DefaultProgressTracker();

    /**
     * Data of import being performed.
     */
    private DataImport dataImport;

    /**
     * Saver to be used for data storing.
     */
    private DataSaver dataSaver;

    /**
     * If set then this import will be used as source of configuration data for a new import.
     */
    private Long oldDataImportId;

    /**
     * Use this constructor to perform price import from a new import configuration.
     * @param dataImport import to be processed.
     * @param dataSaver saver to be used for storing of data being imported.
     */
    public DataImportTask(DataImport dataImport, DataSaver dataSaver) {
        this(dataImport, dataSaver, null);
    }

    /**
     * Use this constructor to perform price import from a stored configuration.
     * @param dataImport price import to be processed.
     * @param dataSaver saver to be used for storing of data being imported.
     * @param oldPriceImportId price import to be used as data source for new price import.
     */
    public DataImportTask(DataImport dataImport, DataSaver dataSaver, Long oldPriceImportId) {
        this.dataImport = dataImport;
        this.dataSaver = dataSaver;
        this.oldDataImportId = oldPriceImportId;
    }

    @Override
    public void setProgressTracker(ProgressTracker progressTracker) {
        this.progressTracker = progressTracker;
    }

    @Override
    public void run() {
        DataImportService dataImportService = SpringServiceContext.getInstance().getDataImportService();
        progressTracker.start();

        DataAdapterFactory factory = dataImportService.getDataAdapterFactoryByAdapterUid(dataImport.getAdapterUid());
        if (factory == null){
            throw new RuntimeException("Cannot load factory object for data adapter with uid=" + dataImport.getAdapterUid());
        }

        try{
            //1. Persisting data import configuration.
            progressTracker.setStatusMessage(I18nSupport.message("data.import.stage.configuration.storing"));
            DataAdapterConfiguration configuration = factory.createDataAdapterConfiguration(dataImport.getAdapterConf());
            if (oldDataImportId != null){
                //Import loading import configuration previously stored in persistent storage.
                configuration.load(new DefaultDataImportConfigurationPersister(oldDataImportId));
            }
            configuration.save(new DefaultDataImportConfigurationPersister(dataImport.getId()));

            //2. Initializing import context.
            DataImportProgressListener progressListener = new DataImportProgressListener() {
                @Override
                public void onTotalRowCountChanged(int newTotalRowCount) {
                    progressTracker.setTotalUnits(newTotalRowCount);
                }

                @Override
                public void onImportedRowCountChanged(int newImportedRowCount) {
                    progressTracker.setDoneUnits(newImportedRowCount);
                }
            };
            DataImportContext importContext = new DefaultDataImportContext(configuration, progressListener, dataSaver);

            //3. Performing data items import.
            progressTracker.setStatusMessage("");
            DataAdapter dataAdapter = factory.createDataAdapter();
            dataAdapter.importData(importContext);

            //4. Marking import as done.
            progressTracker.setTotalUnits(-1);
            progressTracker.setStatusMessage(I18nSupport.message("data.import.stage.completion"));
            dataImportService.completeImport(dataImport.getId(), importContext);
        }
        catch(RuntimeException e){
            //Import has been terminated with error.
            LoggingFacade.logError(this, e);
        } catch (BusinessException e) {
            //Import has been terminated with business logic error.
            LoggingFacade.logError(this, e);
        } finally {
            progressTracker.finish();
        }
    }
}
