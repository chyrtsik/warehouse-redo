/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.movement;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.complecting.ComplectingTaskChangeAdapter;
import com.artigile.warehouse.bl.complecting.ComplectingTaskService;
import com.artigile.warehouse.domain.complecting.ComplectingTask;
import com.artigile.warehouse.domain.complecting.ComplectingTaskState;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Shyrik, 12.12.2009
 */

/**
 * Class for synchronization of movements with changed, made in complecting tasks.
 */
@Transactional(rollbackFor = BusinessException.class)
public class MovementWithComplectingTasksSynchronizer extends ComplectingTaskChangeAdapter {
    private ComplectingTaskService complectingTaskService;
    private MovementService movementService;

    //=========================== Construction and initialization =============================
    public MovementWithComplectingTasksSynchronizer(){
    }

    public void initialize(){
        complectingTaskService.addListener(this);
    }

    //============================== ComplectingTaskChangeAdapter =============================
    @Override
    public void onComplectingTasksStateChanged(List<ComplectingTask> tasks, ComplectingTaskState oldState, ComplectingTaskState newState) throws BusinessException {
        for (ComplectingTask task : tasks){
            if (task.getMovementItem() != null){
                //React on changes, made with complecting tasks, linked with movements.
                movementService.recalculateMovementItemState(task.getMovementItem());
            }
        }
    }

    //===================================== Spring setters ====================================
    public void setComplectingTaskService(ComplectingTaskService complectingTaskService) {
        this.complectingTaskService = complectingTaskService;
    }

    public void setMovementService(MovementService movementService) {
        this.movementService = movementService;
    }
}
