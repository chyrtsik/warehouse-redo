/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.util.async;

/**
 * This service is used to execute asynchronous tasks.
 *
 * @author Aliaksandr.Chyrtsik, 14.07.11
 */
public interface AsynchronousTaskExecutor {
    /**
     * Schedule execution of given runnable task.
     * @param task task to be executed.
     * @param taskName name of the task (to be displayed in UI).
     */
    void executeTask(Runnable task, String taskName);
}
