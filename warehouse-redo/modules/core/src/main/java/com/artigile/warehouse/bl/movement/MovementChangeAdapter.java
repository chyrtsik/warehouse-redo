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
import com.artigile.warehouse.domain.movement.Movement;
import com.artigile.warehouse.domain.movement.MovementState;

/**
 * @author Shyrik, 12.12.2009
 */

public class MovementChangeAdapter implements MovementChangeListener{
    @Override
    public void onMovementStateChanged(Movement movement, MovementState oldState, MovementState newState) throws BusinessException {
    }
}
