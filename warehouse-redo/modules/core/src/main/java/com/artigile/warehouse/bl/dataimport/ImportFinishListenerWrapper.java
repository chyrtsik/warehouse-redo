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
import com.artigile.warehouse.utils.logging.LoggingFacade;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Wrapper used to generate import finish events.
 * This event is generated only once after import has been finished.
 *
 * @author Aliaksandr.Chyrtsik, 07.11.11
 */
public class ImportFinishListenerWrapper extends DataImportListenerAdapter {

    private Map<Long, List<DataImportFinishListener>> listeners = new HashMap<Long, List<DataImportFinishListener>>();

    public void addListener(long dataImportId, DataImportFinishListener importFinishListener) {
        List<DataImportFinishListener> sameListeners = listeners.get(dataImportId);
        if (sameListeners == null){
            sameListeners = new LinkedList<DataImportFinishListener>();
            listeners.put(dataImportId, sameListeners);
        }
        sameListeners.add(importFinishListener);
    }

    @Override
    public void onAfterCompleteImport(long dataImportId, DataImportContext importContext) throws BusinessException {
        List<DataImportFinishListener> targetListeners = listeners.get(dataImportId);
        if (targetListeners != null){
            //Remove this instance from the listeners list to prevent memory leaks (event is generated only once).
            listeners.remove(dataImportId);

            //Notify about completion of the import being monitored.
            for (DataImportFinishListener listener : targetListeners){
                try{
                    listener.onImportFinished(dataImportId, importContext);
                }
                catch (Throwable th){
                    //Errors don't stop next listeners from receiving this notification.
                    LoggingFacade.logError(this, "Error calling data import finish listener.", th);
                }
            }
        }
    }
}
