/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.basedirectory.manufacturer;

import com.artigile.warehouse.bl.directory.ManufacturerService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.ManufacturerTO;

public class ManufacturerEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateManufacturerCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateManufacturerCommand());
        commands.add(new DeleteManufacturerCommand());
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new OpenManufacturerCommand());
        }
    }

    //==================== Commands ===========================================
    private class CreateManufacturerCommand extends CreateCommand {
        protected CreateManufacturerCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            ManufacturerTO manufacturerTO = new ManufacturerTO();
            PropertiesForm prop = new ManufacturerForm(manufacturerTO, true);
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                getManufacturerService().save(manufacturerTO);
                return manufacturerTO;
            }
            return null;
        }
    }

    private class DeleteManufacturerCommand extends DeleteCommand {
        protected DeleteManufacturerCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            getManufacturerService().remove((ManufacturerTO) deletingItem);
            return true;
        }
    }

    private class OpenManufacturerCommand extends PropertiesCommandBase {
        protected OpenManufacturerCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            ManufacturerTO manufacturerTO = (ManufacturerTO) editingItem;
            PropertiesForm prop = new ManufacturerForm(manufacturerTO, getEditAvailability().isAvailable(context));
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                getManufacturerService().save(manufacturerTO);
                return true;
            }
            return false;
        }
    }

    //====================================== Helpers ========================================
    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_MANUFACTURER_LIST);
    }

    private ManufacturerService getManufacturerService() {
        return SpringServiceContext.getInstance().getManufacturerService();
    }
}
