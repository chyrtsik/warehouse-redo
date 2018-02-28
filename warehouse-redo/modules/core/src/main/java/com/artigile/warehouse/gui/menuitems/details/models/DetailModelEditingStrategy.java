/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.models;

import com.artigile.warehouse.bl.detail.DetailModelService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailModelTO;

/**
 * @author Shyrik, 22.12.2008
 */
public class DetailModelEditingStrategy implements ReportEditingStrategy {

    private final static DetailModelService detailModelService=SpringServiceContext.getInstance().getDetailModelsService();

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateDetailModelCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        boolean singleItemSelected = (context.getCurrentReportItems().size() == 1);
        if (singleItemSelected){
            commands.add(new CreateDetailModelAsCopyCommand());
        }
        commands.add(new CreateDetailModelCommand());
        commands.add(new DeleteDetailModelCommand());
        if (singleItemSelected){
            commands.add(new OpenDetailModelCommand());
        }
    }

    //==================== Commands ===========================================
    private class CreateDetailModelCommand extends CreateCommand {
        protected CreateDetailModelCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            DetailModelTO detailModel = new DetailModelTO();
            PropertiesForm prop = new DetailModelForm(detailModel, true, DetailModelForm.PropertiesType.CreateNew);
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                //Saving new detail model.
                detailModelService.saveDetailModel(detailModel);
                return detailModel;
            }
            return null;
        }
    }

    private class CreateDetailModelAsCopyCommand extends CreateCopyCommand {
        protected CreateDetailModelAsCopyCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            DetailModelTO detailModel = new DetailModelTO();
            detailModel.copyFrom((DetailModelTO)context.getCurrentReportItem());
            detailModel.setId(0);
            
            PropertiesForm prop = new DetailModelForm(detailModel, true, DetailModelForm.PropertiesType.CreateAsCopy);
            if (Dialogs.runProperties(prop)) {
                //Saving new detail model.
                detailModelService.saveDetailModel(detailModel);
                return detailModel;
            }
            return null;
        }
    }

    private class DeleteDetailModelCommand extends DeleteCommand {
        protected DeleteDetailModelCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            //Deleting choosed detail model.
            DetailModelTO detailModel = (DetailModelTO) deletingItem;
            detailModelService.deleteDetailModel(detailModel.getId());
            return true;
        }
    }

    private class OpenDetailModelCommand extends PropertiesCommandBase {
        protected OpenDetailModelCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //Editing must me applied only to the full form of the TO.
            DetailModelTO detailModel = (DetailModelTO) editingItem;
            PropertiesForm prop = new DetailModelForm(detailModel, getEditAvailability().isAvailable(context), DetailModelForm.PropertiesType.Properties);
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                //Saving edited detail model.
                detailModelService.saveDetailModel(detailModel);
                return true;
            }
            return false;
        }
    }

    //====================================== Helpers ========================================
    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_DETAIL_MODELS_LIST);
    }

}
