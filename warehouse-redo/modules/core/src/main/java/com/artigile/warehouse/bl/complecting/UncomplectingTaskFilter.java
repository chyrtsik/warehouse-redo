/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.complecting;

import com.artigile.warehouse.domain.complecting.UncomplectingTaskState;

/**
 * @author Valery Barysok, 05.03.2010
 */

/**
 * Filter for loading uncomplecting task list.
 */
public class UncomplectingTaskFilter {

    /**
     * Identifier of warehouse for uncomplecting tasks.
     */
    private Long warehouseId;

    /**
     * States of uncomplecting tasks to be loaded.
     */
    private UncomplectingTaskState states[];

    /**
     * Idendifiers of uncomplecting tasks to be loaded.
     */
    private Long taskIds[];

    //=============================== Getters and setters ===============================
    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public UncomplectingTaskState[] getStates() {
        return states;
    }

    public void setStates(UncomplectingTaskState[] states) {
        this.states = states;
    }

    public Long[] getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(Long[] taskIds) {
        this.taskIds = taskIds;
    }
}
