/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.complecting;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 12.06.2009
 */

/**
 * Possible states of uncomplecting task.
 */
public enum UncomplectingTaskState {
    /**
     * Uncomplecting task is waiting it's turn for processing by warehouse worker.
     */
    NOT_PROCESSED,

    /**
     * Task has been processed by watehouse worker.
     */
    PROCESSED,

    /**
     * Task has been closed when line of order, for which task relate to, obtain status "Ready for shipping"
     */
    CLOSED;

    /**
     * Use this method to obtain name of uncomplecting task states.
     * @return
     */
    public String getName() {
        if (this.equals(NOT_PROCESSED)){
            return I18nSupport.message("uncomplectingTask.state.name.notProcessed");
        }
        else if (this.equals(PROCESSED)){
            return I18nSupport.message("uncomplectingTask.state.name.processed");
        }
        else if (this.equals(CLOSED)) {
           return I18nSupport.message("uncomplectingTask.state.name.closed");
        }
        else {
            throw new RuntimeException("UncomplectingTaskState.getName: Invalid uncomplecting task state value.");
        }
    }

    /**
     * Use this method to get initial state of the uncomplecting (at the creation of the uncomplecting task).
     * @return
     */
    public static UncomplectingTaskState getInitialState() {
        return NOT_PROCESSED;
    }
}
