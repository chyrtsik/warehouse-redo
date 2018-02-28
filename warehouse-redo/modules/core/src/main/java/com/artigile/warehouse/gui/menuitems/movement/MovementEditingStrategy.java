/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.movement;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.movement.MovementService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.movement.MovementState;
import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.plugin.Plugin;
import com.artigile.warehouse.gui.core.print.PrintingPreparator;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.movement.items.MovementItemsEditor;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.movement.MovementTOForReport;

/**
 * @author Shyrik, 21.11.2009
 */
public class MovementEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateMovementCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        boolean singleItemSelected = (context.getCurrentReportItems().size() == 1);

        //Edit movement content command.
        if (singleItemSelected){
            ReportCommand editMovementContentCommand = new EditMovementContentCommand();
            commands.setDefaultCommandForRow(editMovementContentCommand);
            commands.add(editMovementContentCommand);
        }

        //Command for creating new movement.
        commands.add(new CreateMovementCommand());

        //Command for deleting selected movements.
        boolean canDeleteMovement = true;
        for (Object itemObj : context.getCurrentReportItems()){
            MovementTOForReport movement = (MovementTOForReport)itemObj;
            if (!getMovementService().canDeleteMovement(movement.getId())){
                canDeleteMovement = false;
                break;
            }
        }
        if (canDeleteMovement){
            commands.add(new DeleteMovementCommand());
        }

        //Command for editing movement properties.
        if (singleItemSelected){
            commands.add(new MovementPropertiesCommand());
            commands.add(new PrintCurrentReportItemCommand(PrintTemplateType.TEMPLATE_MOVEMENT, hasPrintPermission(), getPrintPreparator()));
            commands.add(new PrintPreviewCurrentReportItemCommand(PrintTemplateType.TEMPLATE_MOVEMENT, hasPrintPermission(), getPrintPreparator()));
        }
    }

    private AvailabilityStrategy hasPrintPermission() {
        return new PermissionCommandAvailability(PermissionType.VIEW_MOVEMENT_CONTENT);
    }

    private PrintingPreparator getPrintPreparator() {
        return new PrintingPreparator() {
            @Override
            public Object prepareForPrinting(Object objectForPrinting) {
                //Before printing full content of the movement to be printed must be loaded.
                MovementTOForReport movementForReport = (MovementTOForReport)objectForPrinting;
                return getMovementService().getMovementFullData(movementForReport.getId());
            }
        };
    }

    //==================================== Commands ===============================================
    private class MovementPropertiesCommand extends PropertiesCommandBase {
        protected MovementPropertiesCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //Edit movement properties.
            MovementTOForReport movement = (MovementTOForReport) editingItem;
            PropertiesForm prop = new MovementForm(movement, canEditMovement(context));
            if (Dialogs.runProperties(prop)) {
                getMovementService().save(movement);
                return true;
            }
            return false;
        }
    }

    private class DeleteMovementCommand extends DeleteCommand {
        protected DeleteMovementCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            //Deleting selected movement.
            MovementTOForReport movementToDelete = (MovementTOForReport)deletingItem;
            try {
                getMovementService().deleteMovement(movementToDelete.getId());
            } catch (BusinessException e) {
                throw new ReportCommandException(e);
            }
            return true;
        }
    }

    private class EditMovementContentCommand extends ReportCommandBase {
        protected EditMovementContentCommand() {
            super(new ResourceCommandNaming("movement.list.editItemsCommand"), new PermissionCommandAvailability(PermissionType.VIEW_MOVEMENT_CONTENT));
        }

        @Override
        public ReportCommandType getType() {
            return ReportCommandType.CUSTOM;
        }

        @Override
        public boolean execute(ReportCommandContext context) throws ReportCommandException {
            //Opening items of the selected movement.
            MovementTOForReport movementTO = (MovementTOForReport)context.getCurrentReportItem();
            Plugin movementContentEditor = new MovementItemsEditor(movementTO.getId());
            WareHouse.runPlugin(movementContentEditor);
            return true;
        }
    }

    private class CreateMovementCommand extends CreateCommand {
        protected CreateMovementCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            MovementTOForReport newMovement = new MovementTOForReport();
            newMovement.init();
            PropertiesForm prop = new MovementForm(newMovement, true);
            if (Dialogs.runProperties(prop)){
                //Creating new inventorization.
                newMovement = getMovementService().create(newMovement);

                //Running movement content editor.
                Plugin movementContentEditor = new MovementItemsEditor(newMovement.getId());
                WareHouse.runPlugin(movementContentEditor);

                return newMovement;
            }
            return null;
        }
    }

    //==================================== Utility methods ============================
    public static AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_MOVEMENT_LIST);
    }

    private boolean canEditMovement(ReportCommandContext context) {
        //Ability to edit movement properties depends on both user permission and state of the movement to be edited.
        if (getEditAvailability().isAvailable(context)) {
            MovementTOForReport movement = (MovementTOForReport) context.getCurrentReportItem();
            return movement.getState().equals(MovementState.CONSTRUCTION);
        }
        return false;
    }

    //======================================== Helpers ================================
    private MovementService getMovementService() {
        return SpringServiceContext.getInstance().getMovementService();
    }
}
