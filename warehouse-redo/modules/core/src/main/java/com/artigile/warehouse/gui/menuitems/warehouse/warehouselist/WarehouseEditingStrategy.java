/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.warehouse.warehouselist;

import com.artigile.warehouse.bl.warehouse.WarehouseService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

/**
 * @author Borisok V.V., 24.12.2008
 */
public class WarehouseEditingStrategy implements ReportEditingStrategy {

    private static WarehouseService warehouseService = SpringServiceContext.getInstance().getWarehouseService();

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateWarehouseCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateWarehouseCommand());
        commands.add(new DeleteWarehouseCommand());
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new OpenWarehouseCommand());
        }
    }

    //==================================== Commands ===============================================
    private class CreateWarehouseCommand extends CreateCommand {

        private CreateWarehouseCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            WarehouseTO warehouseTO = new WarehouseTO();
            WarehouseForm warehouseForm = new WarehouseForm(warehouseTO, null, true,
                    getUserListEditAvailability().isAvailable(context),
                    getOtherEditAvailability().isAvailable(context));
            PropertiesDialog propDialog = new PropertiesDialog(warehouseForm);
            if (propDialog.run()) {
                warehouseService.create(warehouseTO);
                warehouseService.saveAllowedUsers(warehouseTO.getId(), warehouseForm.getAllowedUsers());
                return warehouseTO;
            }
            return null;
        }
    }

    private class DeleteWarehouseCommand extends DeleteCommand {
        protected DeleteWarehouseCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object item) throws ReportCommandException {
            if (item instanceof WarehouseTOForReport) {
                warehouseService.remove((WarehouseTOForReport) item);
                return true;
            }
            return false;
        }
    }

    private class OpenWarehouseCommand extends PropertiesCommandBase {
        protected OpenWarehouseCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            WarehouseTO warehouseTO = warehouseService.getWarehouseFull(((WarehouseTOForReport) editingItem).getId());
            WarehouseForm prop = new WarehouseForm(warehouseTO,
                    warehouseService.getAllowedUsers(warehouseTO.getId()),
                    getEditAvailability().isAvailable(context),
                    getUserListEditAvailability().isAvailable(context),
                    getOtherEditAvailability().isAvailable(context));
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                warehouseService.update(warehouseTO);
                warehouseService.saveAllowedUsers(warehouseTO.getId(), prop.getAllowedUsers());
                return true;
            }
            return false;
        }
    }

    //================================== Helpers ========================================
    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_WAREHOUSE_LIST);
    }

    private AvailabilityStrategy getUserListEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_WAREHOUSE_USER_LIST);
    }

    private AvailabilityStrategy getOtherEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_WAREHOUSE_OTHER);
    }
}
