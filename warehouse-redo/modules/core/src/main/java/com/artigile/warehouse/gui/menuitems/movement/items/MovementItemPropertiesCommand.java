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
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.PropertiesCommandBase;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.movement.MovementItemTO;

/**
 * @author Shyrik, 06.12.2009
 */

/**
 * Shows properties dialog for movement item and saves changes has been made in dialog.
 */
public class MovementItemPropertiesCommand extends PropertiesCommandBase{
    AvailabilityStrategy saveDataAvailability;

    protected MovementItemPropertiesCommand(AvailabilityStrategy commandAvailability, AvailabilityStrategy saveDataAvailability) {
        super(commandAvailability);
        this.saveDataAvailability = saveDataAvailability;
    }

    @Override
    protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
        MovementItemTO movementItemTO = (MovementItemTO)editingItem;
        MovementItemForm prop = new MovementItemForm(movementItemTO, saveDataAvailability.isAvailable(context), false);
        if (Dialogs.runProperties(prop)){
            try {
                SpringServiceContext.getInstance().getMovementService().saveMovementItem(movementItemTO);
            } catch (BusinessException e) {
                throw new ReportCommandException(e);
            }
            return true;
        }
        return false;
    }
}
