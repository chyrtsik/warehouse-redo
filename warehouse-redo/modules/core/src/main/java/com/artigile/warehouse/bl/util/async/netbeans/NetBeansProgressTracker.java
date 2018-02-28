/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.util.async.netbeans;

import com.artigile.warehouse.bl.util.async.ProgressTracker;
import org.netbeans.api.progress.ProgressHandle;

/**
 * Progress tracker used in netbeans-based UI implementation.
 *
 * @author Aliaksandr.Chyrtsik, 14.07.11
 */
public class NetBeansProgressTracker implements ProgressTracker {
    private ProgressHandle progressHandle;

    public NetBeansProgressTracker(ProgressHandle progressHandle) {
        this.progressHandle = progressHandle;
    }

    @Override
    public void start() {
        progressHandle.start();
    }

    @Override
    public void suspend(String message) {
        progressHandle.suspend(message);
    }

    @Override
    public void finish() {
        progressHandle.finish();
    }

    @Override
    public void setTotalUnits(int totalUnits) {
        if (totalUnits == -1){
            progressHandle.switchToIndeterminate();
        }
        else{
            progressHandle.switchToDeterminate(totalUnits);
        }
    }

    @Override
    public void setDoneUnits(int doneUnits) {
        progressHandle.progress(doneUnits);
    }

    @Override
    public void setStatusMessage(String message) {
        progressHandle.progress(message);
    }
}
