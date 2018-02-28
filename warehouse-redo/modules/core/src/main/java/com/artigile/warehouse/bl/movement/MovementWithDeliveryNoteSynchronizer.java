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
import com.artigile.warehouse.bl.deliveryNote.DeliveryNoteChangeAdapter;
import com.artigile.warehouse.bl.deliveryNote.DeliveryNoteService;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNote;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteState;
import com.artigile.warehouse.domain.movement.MovementItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Shyrik, 20.12.2009
 */

/**
 * Reacts on changed mede in delivery notes and performs appropriate changes in movement documents.
 */
@Transactional(rollbackFor = BusinessException.class)
public class MovementWithDeliveryNoteSynchronizer extends DeliveryNoteChangeAdapter {
    private DeliveryNoteService deliveryNoteService;
    private MovementService movementService;

    public MovementWithDeliveryNoteSynchronizer(){
    }

    public void initialize(){
        deliveryNoteService.addListener(this);
    }

    @Override
    public void onDeliveryNoteStateChanged(DeliveryNote deliveryNote, DeliveryNoteState oldState, DeliveryNoteState newState) throws BusinessException {
        //Performs recalculating of state of movement items, that are linked with given delivery note.
        List<MovementItem> items = movementService.getMovementItemsForDeliveryNote(deliveryNote.getId());
        for (MovementItem item : items){
            movementService.recalculateMovementItemState(item);
        }
    }

    //============================== Spring setters ===================================
    public void setDeliveryNoteService(DeliveryNoteService deliveryNoteService) {
        this.deliveryNoteService = deliveryNoteService;
    }

    public void setMovementService(MovementService movementService) {
        this.movementService = movementService;
    }
}
