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

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.domain.complecting.ComplectingTask;
import com.artigile.warehouse.domain.complecting.ComplectingTaskState;

import java.util.List;

/**
 * @author Shyrik, 12.12.2009
 */

/**
 * This listener is implemented for reaction on changes, made in complecting tasks.
 */
public interface ComplectingTaskChangeListener {
    
    /**
     * Called when state of a list of complecting tasks is changed during single transaction.
     * @param tasks
     * @param oldState
     * @param newState
     */
    void onComplectingTasksStateChanged(List<ComplectingTask> tasks, ComplectingTaskState oldState, ComplectingTaskState newState) throws BusinessException;
}
