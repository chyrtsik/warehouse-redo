/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.movement.items;

import com.artigile.warehouse.domain.movement.MovementState;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.utils.dto.movement.MovementTOForReport;

/**
 * @author Shyrik, 22.11.2009
 */

/**
 * Implementation of check, if editing of movement items is available.
 */
public class AddMovementItemsAvailability implements AvailabilityStrategy {
    private MovementTOForReport movement;

    public AddMovementItemsAvailability(MovementTOForReport movement) {
        this.movement = movement;
    }

    @Override
    public boolean isAvailable(ReportCommandContext context) {
        return movement.getState().equals(MovementState.CONSTRUCTION);
    }
}
