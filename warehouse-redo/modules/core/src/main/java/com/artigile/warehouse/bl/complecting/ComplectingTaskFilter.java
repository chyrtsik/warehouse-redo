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

import com.artigile.warehouse.domain.complecting.ComplectingTaskState;

/**
 * @author Shyrik, 19.11.2009
 */

/**
 * Filter for loading complecting task list.
 */
public class ComplectingTaskFilter {
    /**
     * Identifier of warehouse for complecting tasks.
     */
    private Long warehouseId;

    /**
     * States of complecting tasks to be loaded.
     */
    private ComplectingTaskState states[];

    /**
     * Idendifiers of complecting tasks to be loaded.
     */
    private Long taskIds[];

    //=============================== Getters and setters ===============================
    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public ComplectingTaskState[] getStates() {
        return states;
    }

    public void setStates(ComplectingTaskState[] states) {
        this.states = states;
    }

    public Long[] getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(Long[] taskIds) {
        this.taskIds = taskIds;
    }
}