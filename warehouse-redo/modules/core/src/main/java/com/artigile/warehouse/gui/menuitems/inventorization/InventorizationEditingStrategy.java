/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.inventorization;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.inventorization.inventorization.InventorizationService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.plugin.Plugin;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.inventorization.items.InventorizationItemsEditor;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.inventorization.InventorizationTOForReport;

/**
 * @author Shyrik, 29.09.2009
 */
public class InventorizationEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateInventorizationCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        boolean singleItemSelected = (context.getCurrentReportItems().size() == 1);

        //Edit inventorization content command.
        if (singleItemSelected){
            ReportCommand editInventorizationContentCommand = new EditInventorizationContentCommand();
            commands.setDefaultCommandForRow(editInventorizationContentCommand);
            commands.add(editInventorizationContentCommand);
        }

        //Create inventorization command.
        commands.add(new CreateInventorizationCommand());

        //Delete inventorization command.
        boolean canDeleteInventorization = true;
        for (Object itemObj : context.getCurrentReportItems()){
            InventorizationTOForReport inventorization = (InventorizationTOForReport)itemObj;
            if (!getInventorizationService().canDeleteInventorization(inventorization.getId())){
                canDeleteInventorization = false;
                break;
            }
        }
        if (canDeleteInventorization){
            commands.add(new DeleteInventorizationCommand());
        }

        //Inventorization properties command.
        if (singleItemSelected){
            commands.add(new InventorizationPropertiesCommand());
        }
    }

    //==================================== Commands ===============================================
    private class InventorizationPropertiesCommand extends PropertiesCommandBase {
        protected InventorizationPropertiesCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //Edit inventorization properties.
            InventorizationTOForReport inventorization = (InventorizationTOForReport) editingItem;
            PropertiesForm prop = new InventorizationForm(inventorization, false);
            if (Dialogs.runProperties(prop)) {
                getInventorizationService().save(inventorization);
                return true;
            }
            return false;
        }
    }

    private class DeleteInventorizationCommand extends DeleteCommand {
        protected DeleteInventorizationCommand() {
            super(new PermissionCommandAvailability(PermissionType.EDIT_CREATE_DELETE_INVENTORIZATION));
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            //Deleting selected inventorization.
            InventorizationTOForReport inventorizationToDelete = (InventorizationTOForReport)deletingItem;
            try {
                getInventorizationService().deleteInventorization(inventorizationToDelete.getId());
            } catch (BusinessException e) {
                throw new ReportCommandException(e);
            }
            return true;
        }
    }

    private class EditInventorizationContentCommand extends ReportCommandBase {
        protected EditInventorizationContentCommand() {
            super(new ResourceCommandNaming("inventorization.items.edit.command"), new PermissionCommandAvailability(PermissionType.VIEW_INVENTORIZATION_CONTENT));
        }

        @Override
        public ReportCommandType getType() {
            return ReportCommandType.CUSTOM;
        }

        @Override
        public boolean execute(ReportCommandContext context) throws ReportCommandException {
            //Opening items of the selected inventorization.
            InventorizationTOForReport inventorizationTO = (InventorizationTOForReport)context.getCurrentReportItem();
            Plugin inventorizationContentEditor = new InventorizationItemsEditor(inventorizationTO.getId());
            WareHouse.runPlugin(inventorizationContentEditor);
            return true;
        }
    }

    private class CreateInventorizationCommand extends CreateCommand {
        protected CreateInventorizationCommand() {
            super(new PermissionCommandAvailability(PermissionType.EDIT_CREATE_DELETE_INVENTORIZATION));
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            InventorizationTOForReport newInventorization = new InventorizationTOForReport();
            newInventorization.init();
            InventorizationForm prop = new InventorizationForm(newInventorization, true);
            if (Dialogs.runProperties(prop)){
                //Creating new inventorization.
                InventorizationService service = getInventorizationService();
                service.createInventorization(newInventorization);

                //Running inventorization content editor.
                Plugin inventorizationContentEditor = new InventorizationItemsEditor(newInventorization.getId());
                WareHouse.runPlugin(inventorizationContentEditor);  

                return newInventorization;
            }
            return null;
        }
    }

    private InventorizationService getInventorizationService() {
        return SpringServiceContext.getInstance().getInventorizationService();
    }
}
