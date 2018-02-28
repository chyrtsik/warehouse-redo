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
 * @author Shyrik, 24.05.2009
 */

/**
 * All possible states of the complecting task in terms of processing it by warehouse worker.
 */
public enum ComplectingTaskState {
    /**
     * Complecting task is waiting it's turn for processing by warehouse worker.
     */
    NOT_PROCESSED(1, I18nSupport.message("complectingTask.state.name.notProcessed")),

    /**
     * Complecting task is been processing by warehouse worker. He noticed this item but not
     * entered quantity of goods, really found at the warehouse.
     */
    PROCESSING(2, I18nSupport.message("complectingTask.state.name.processing")),

    /**
     * Task has beed processed by watehouse worker.
     */
    PROCESSED(3, I18nSupport.message("complectingTask.state.name.processed")),

    /**
     * Task has been processed and now is ready for shipping from warehouse.
     */
    READY_FOR_SHIPPING(4, I18nSupport.message("complectingTask.state.name.readyForShipping")),

    /**
     * Wares, that has been complected during processing of complecting task has been shipped from warehouse.
     */
    SHIPPED(5, I18nSupport.message("complectingTask.state.name.shipped"));


    //======================== Naming support ==========================
    private int stateNumber;

    private String name;

    private ComplectingTaskState(int stateNumber, String name){
        this.stateNumber = stateNumber;
        this.name = name;
    }

    /**
     * Use this method to obtain name of complecting task states.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Use this method to get initial state of the complecting task (at the creation of the complecting task).
     * @return
     */
    public static ComplectingTaskState getInitialState() {
        return NOT_PROCESSED;
    }

    /**
     * Use this method to get the last state of the complecting task.
     * @return
     */
    public static ComplectingTaskState getLastState() {
        return SHIPPED;
    }

    /**
     * Returns true, is second state is used before, then this state.
     * @param second
     * @return
     */
    public boolean isBefore(ComplectingTaskState second) {
        return stateNumber < second.stateNumber;
    }
}
