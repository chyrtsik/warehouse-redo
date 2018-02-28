/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.contractors.shipping;

import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.dto.ShippingTO;

/**
 * @author Valery Barysok, 9/13/11
 */

public class ShippingStrategy implements ReportEditingStrategy {

    private boolean canEdit;

    public ShippingStrategy(boolean canEdit) {
        this.canEdit = canEdit;
    }

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateShippingCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateShippingCommand());
        commands.add(new DeleteShippingCommand());
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new OpenShippingCommand());
        }
    }

    private class CreateShippingCommand extends CreateCommand {
        protected CreateShippingCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            ShippingTO operationShipping = new ShippingTO();
            PropertiesForm prop = new ShippingForm(operationShipping, true);
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                return operationShipping;
            }
            return null;
        }
    }

    private class DeleteShippingCommand extends DeleteCommand {
        protected DeleteShippingCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            //We dont needs any special processing of deleting. Only deleting from the tableReport.
            return true;
        }
    }

    private class OpenShippingCommand extends PropertiesCommandBase {
        protected OpenShippingCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            ShippingTO operationShipping = (ShippingTO) editingItem;
            PropertiesForm prop = new ShippingForm(operationShipping, getEditAvailability().isAvailable(context));
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            return propDialog.run();
        }
    }

    private AvailabilityStrategy getEditAvailability() {
        return new PredefinedCommandAvailability(canEdit);
    }
}
