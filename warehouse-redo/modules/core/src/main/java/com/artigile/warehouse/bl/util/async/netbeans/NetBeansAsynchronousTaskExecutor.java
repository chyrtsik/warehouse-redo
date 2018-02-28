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

import com.artigile.warehouse.bl.util.async.AsynchronousTaskExecutor;
import com.artigile.warehouse.bl.util.async.ProgressTrackable;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.RequestProcessor;

/**
 * This service is used to execute asynchronous tasks.
 * TODO: This code depends on netbeans IDE. Provide own implementation if needed.
 *
 * @author Aliaksandr.Chyrtsik, 13.07.11
 */
public class NetBeansAsynchronousTaskExecutor implements AsynchronousTaskExecutor {
    private final RequestProcessor RP = new RequestProcessor("WarehouseClient Asynchronous Tasks", 5, true);

    @Override
    public void executeTask(Runnable task, String taskName) {
        RequestProcessor.Task theTask = RP.create(task);
        if (task instanceof ProgressTrackable) {
            ProgressTrackable trackable = (ProgressTrackable) task;
            trackable.setProgressTracker(new NetBeansProgressTracker(ProgressHandleFactory.createHandle(taskName)));
        }
        // XXX: delay for committing done.
        theTask.schedule(200);
    }
}
