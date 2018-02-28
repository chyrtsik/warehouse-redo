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
 * Interface of progress tacker used for monitoring of asynchronous task execution.
 * Implementations may use this method to add pregress tracker to the UI.
 *
 * @author Aliaksandr.Chyrtsik, 14.07.11
 */
public interface ProgressTracker {
    /**
     * Called to indicate start of a task execution.
     */
    void start();

    /**
     * Called when task is suspended (for example to wait a result of a long action).
     * @param message message to be displayed (should explain suspend reason).
     */
    void suspend(String message);

    /**
     * Called to indicate end of task execution.
     * Implementations may process this method to remove progress tracker from the UI of to mask task as finished.
     */
    void finish();

    /**
     * Sets total units of work to be performed. Progress will be tracked from 0 = 0% to given number units (100%). <br/>
     * Use <b>-1</b> value to signal that total unit of work is not determined. UI should display this case properly.
     * @param totalUnits total units of work.
     */
    void setTotalUnits(int totalUnits);

    /**
     * Sets current progress measured in units of work have done.
     * @param doneUnits units of done work.
     */
    void setDoneUnits(int doneUnits);

    /**
     * Sets current status message (explaining current work being performed).
     * @param message status message ot be shown in UI.
     */
    void setStatusMessage(String message);
}
