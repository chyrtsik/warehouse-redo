/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.inventorization.task;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Borisok V.V., 30.09.2009
 */

/**
 * All possible states of task warehouse.
 */
public enum InventorizationTaskState {
    /**
     * In this state task warehouse is being not processed by workers.
     */
    NOT_PROCESSED(I18nSupport.message("inventorization.task.state.notProcessed")),

    /**
     * In this state task warehouse is being processed by workers.
     */
    IN_PROCESS(I18nSupport.message("inventorization.task.state.inProcess")),

    /**
     * Task has been processed by warehouse workers and is ready to be closed.
     */
    PROCESSED(I18nSupport.message("inventorization.task.state.processed")),

    /**
     * Task has been closed.
     */
    CLOSED(I18nSupport.message("inventorization.task.state.closed")),;

    //===================== Naming impementation =================================
    private String name;

    InventorizationTaskState(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    /**
     * Use this method to get initial state (at the creation of the inventorization task).
     * @return
     */
    public static InventorizationTaskState getInitialState() {
        return NOT_PROCESSED;
    }
}
