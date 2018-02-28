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
 * Every asynchronous task which supports progress tracking should provide this interface.
 *
 * @author Aliaksandr.Chyrtsik, 14.07.11
 */
public interface ProgressTrackable {
    /**
     * Registers progress tracker for this task.
     * @param progressTracker object responsible for tracking of task execution progress.
     */
    void setProgressTracker(ProgressTracker progressTracker);
}
