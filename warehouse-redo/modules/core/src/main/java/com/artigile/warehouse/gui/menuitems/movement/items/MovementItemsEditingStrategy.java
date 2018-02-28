/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.movement.items;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.movement.MovementState;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.MultipleAndCriteriaCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.utils.dto.movement.MovementTOForReport;

/**
 * @author Shyrik, 22.11.2009
 */
public class MovementItemsEditingStrategy implements ReportEditingStrategy{
    /**
     * Movement is been editing.
     */
    private MovementTOForReport movement;

    public MovementItemsEditingStrategy(MovementTOForReport movement){
        this.movement = movement;
    }

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        if ( movement.getState() == MovementState.CONSTRUCTION ){
            commands.add(new DeleteItemsFromMovementCommand(getDeleteItemAvailability()));
        }
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new MovementItemPropertiesCommand(new PredefinedCommandAvailability(true), getEditItemAvailability(movement)));
        }
    }

    private AvailabilityStrategy getDeleteItemAvailability() {
        return getEditItemAvailability(movement);
    }

    private static AvailabilityStrategy getEditItemAvailability(MovementTOForReport movement) {
        return new MultipleAndCriteriaCommandAvailability(
            new AvailabilityStrategy[]{
                new PermissionCommandAvailability(PermissionType.EDIT_MOVEMENT_CONTENT),
                new AddMovementItemsAvailability(movement)
            });
    }

    /**
     * Returns strategy for calulating availability of "add item to movement" command.
     * @param movement
     * @return
     */
    public static AvailabilityStrategy getAddItemAvailability(MovementTOForReport movement) {
        return getEditItemAvailability(movement);
    }
}



