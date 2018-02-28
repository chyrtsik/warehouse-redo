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

import com.artigile.warehouse.bl.util.async.ProgressTracker;

/**
 * Default progress tracker (used when no other tracker are apecified).
 *
 * @author Aliaksandr.Chyrtsik, 04.11.11
 */
public class DefaultProgressTracker implements ProgressTracker {
    @Override
    public void start() {
    }

    @Override
    public void suspend(String message) {
    }

    @Override
    public void finish() {
    }

    @Override
    public void setTotalUnits(int totalUnits) {
    }

    @Override
    public void setDoneUnits(int doneUnits) {
    }

    @Override
    public void setStatusMessage(String message) {
    }
}
