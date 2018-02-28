/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.movement.Movement;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.movement.MovementTO;
import com.artigile.warehouse.utils.dto.movement.MovementTOForReport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 21.11.2009
 */
public final class MovementTransformer {
    private MovementTransformer(){
    }


    public static MovementTOForReport transformForReport(Movement movement) {
        if (movement == null){
            return null;
        }
        MovementTOForReport movementTO = new MovementTOForReport();
        update(movementTO, movement);
        return movementTO;
    }

    public static MovementTO transform(Movement movement) {
        if (movement == null){
            return null;
        }
        MovementTO movementTO = new MovementTO();
        update(movementTO, movement);
        movementTO.setItems(MovementItemTransformer.transformList(movement.getItems(), movementTO));
        return movementTO;
    }

    public static List<MovementTOForReport> transformListForReport(List<Movement> movements) {
        List<MovementTOForReport> movementsTO = new ArrayList<MovementTOForReport>();
        for (Movement movement : movements){
            movementsTO.add(transformForReport(movement));
        }
        return movementsTO;
    }

    public static Movement transformFromReport(MovementTOForReport movementTO) {
        if (movementTO == null){
            return null;
        }
        Movement movement = SpringServiceContext.getInstance().getMovementService().getMovementById(movementTO.getId());
        if (movement == null){
            movement = new Movement();
        }
        return movement;
    }

    /**
     * @param movementTO (in, out) -- DTO to be synchronized with entity.
     * @param movement (in) -- entity with fresh data.
     */
    public static void update(MovementTOForReport movementTO, Movement movement) {
        movementTO.setId(movement.getId());
        movementTO.setNumber(movement.getNumber());
        movementTO.setState(movement.getState());
        movementTO.setFromWarehouse(WarehouseTransformer.transformForReport(movement.getFromWarehouse()));
        movementTO.setToWarehouse(WarehouseTransformer.transformForReport(movement.getToWarehouse()));
        movementTO.setResult(movement.getResult());
        movementTO.setCreateUser(UserTransformer.transformUser(movement.getCreateUser()));
        movementTO.setCreateDate(movement.getCreateDate());
        movementTO.setBeginDate(movement.getBeginDate());
        movementTO.setEndDate(movement.getEndDate());
        movementTO.setNotice(movement.getNotice());
    }

    /**
     * @param movement (in, out) -- entity to be synchronized with DTO.
     * @param movementTO (in) -- DTO with fresh data.
     */
    public static void update(Movement movement, MovementTOForReport movementTO) {
        movement.setNumber(movementTO.getNumber());
        movement.setNotice(movementTO.getNotice());
    }
}
