/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.contractors.loadplace;

import com.artigile.warehouse.bl.contractors.ContractorService;
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
import com.artigile.warehouse.utils.dto.LoadPlaceTO;

/**
 * @author IoaN, Jan 3, 2009
 */

public class LoadPlaceStrategy implements ReportEditingStrategy {
    ContractorService contractorService = SpringServiceContext.getInstance().getContractorService();


    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateLoadPlaceCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateLoadPlaceCommand());
        commands.add(new DeleteLoadPlaceCommand());
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new OpenLoadPlaceCommand());
        }
    }

    //==================== Commands ===========================================
    private class CreateLoadPlaceCommand extends CreateCommand {
        protected CreateLoadPlaceCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            LoadPlaceTO loadPlaceTO = new LoadPlaceTO();
            PropertiesForm prop = new LoadPlaceForm(loadPlaceTO, true);
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                contractorService.saveLoadPlace(loadPlaceTO);
                return loadPlaceTO;
            }
            return null;
        }
    }

    private class DeleteLoadPlaceCommand extends DeleteCommand {
        protected DeleteLoadPlaceCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            contractorService.deleteLoadPlace((LoadPlaceTO) deletingItem);
            return true;
        }
    }

    private class OpenLoadPlaceCommand extends PropertiesCommandBase {
        protected OpenLoadPlaceCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            LoadPlaceTO contractor = (LoadPlaceTO) editingItem;
            PropertiesForm prop = new LoadPlaceForm(contractor, getEditAvailability().isAvailable(context));
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                contractorService.updateLoadPlace(contractor);
                return true;
            }
            return false;
        }
    }

    //====================================== Helpers ========================================
    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_LOAD_PLACE_LIST);
    }
}
