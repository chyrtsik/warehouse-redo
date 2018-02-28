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

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.gui.core.report.command.DeleteCommand;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.movement.MovementItemTO;

/**
 * @author Shyrik, 06.12.2009
 */

/**
 * Command deletes selected item from movement.
 */
public class DeleteItemsFromMovementCommand extends DeleteCommand {
    protected DeleteItemsFromMovementCommand(AvailabilityStrategy availabilityStrategy) {
        super(availabilityStrategy);
    }

    @Override
    protected boolean doDelete(Object deletingItem) throws ReportCommandException {
        MovementItemTO itemToDelete = (MovementItemTO)deletingItem;
        try {
            SpringServiceContext.getInstance().getMovementService().removeItemFromMovement(itemToDelete.getId());
            return true;
        } catch (BusinessException e) {
            throw new ReportCommandException(e);
        }
    }
}
