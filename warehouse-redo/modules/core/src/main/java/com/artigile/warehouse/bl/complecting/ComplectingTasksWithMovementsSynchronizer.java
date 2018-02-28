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
import com.artigile.warehouse.bl.movement.MovementChangeAdapter;
import com.artigile.warehouse.bl.movement.MovementService;
import com.artigile.warehouse.domain.movement.Movement;
import com.artigile.warehouse.domain.movement.MovementItem;
import com.artigile.warehouse.domain.movement.MovementState;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Shyrik, 11.12.2009
 */

/**
 * This class is used for synchronization complecting tasks with movements.
 */
@Transactional(rollbackFor = BusinessException.class)
public class ComplectingTasksWithMovementsSynchronizer extends MovementChangeAdapter {
    private MovementService movementService;

    private ComplectingTaskService complectingTaskService;

    public ComplectingTasksWithMovementsSynchronizer(){
    }

    public void initialize(){
        //This class listens for changes made in movements and complectings tasks.
        movementService.addListener(this);
        //complectingTaskService.addListener(this);
    }

    //================================ MovementChangeListener ============================================
    @Override
    public void onMovementStateChanged(Movement movement, MovementState oldState, MovementState newState) throws BusinessException {
        if (oldState.equals(MovementState.CONSTRUCTION) && newState.equals(MovementState.COMPLECTING)){
            onBeginComplectingMovement(movement);
        }
        else if (oldState.equals(MovementState.COMPLECTING) && newState.equals(MovementState.CONSTRUCTION)){
            onCancelMovement(movement);
        }
    }

    /**
     * Creates complecting tasks for movement, that has jast begun.
     * @param movement
     * @throws BusinessException 
     */
    private void onBeginComplectingMovement(Movement movement) throws BusinessException {
        for (MovementItem movementItem : movement.getItems()){
            movementItem.setComplectingTask(complectingTaskService.createComplectingTaskForMovementItem(movementItem));
        }
    }

    /**
     * Deletes complecting tasks for movement when is is cancelled.
     * @param movement
     * @throws BusinessException
     */
    private void onCancelMovement(Movement movement) throws BusinessException {
        for (MovementItem movementItem : movement.getItems()){
            complectingTaskService.deleteComplectingTask(movementItem.getComplectingTask());
            movementItem.setComplectingTask(null);
        }
    }

    //====================================== Spring setters ================================
    public void setMovementService(MovementService movementService) {
        this.movementService = movementService;
    }

    public void setComplectingTaskService(ComplectingTaskService complectingTaskService) {
        this.complectingTaskService = complectingTaskService;
    }
}
