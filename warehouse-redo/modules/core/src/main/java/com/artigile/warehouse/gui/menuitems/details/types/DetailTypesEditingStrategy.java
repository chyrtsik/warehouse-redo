/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.types;

import com.artigile.warehouse.bl.detail.DetailTypeService;
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
import com.artigile.warehouse.utils.dto.details.DetailTypeTO;
import com.artigile.warehouse.utils.dto.details.DetailTypeTOForReport;
import com.artigile.warehouse.utils.transofmers.DetailTypesTransformer;

/**
 * @author Shyrik, 14.12.2008
 */
public class DetailTypesEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateDetailTypeCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateDetailTypeCommand());
        commands.add(new DeleteDetailTypeCommand());
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new OpenDetailTypeCommand());
        }
    }

    //==================== Commands ===========================================
    private class CreateDetailTypeCommand extends CreateCommand {
        protected CreateDetailTypeCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            DetailTypeTO detailTypeFullTO = new DetailTypeTO();
            detailTypeFullTO.initPredefinedFields();

            PropertiesForm prop = new DetailTypeForm(detailTypeFullTO, true);
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                //Saving new detail type.
                getDetailTypesService().saveDetailType(detailTypeFullTO);
                //Returning changed item for the tableReport.
                return DetailTypesTransformer.transformDetailType(detailTypeFullTO);
            }
            return null;
        }
    }

    private class DeleteDetailTypeCommand extends DeleteCommand {
        protected DeleteDetailTypeCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            //Deleting choosed item.
            DetailTypeTOForReport detailType = (DetailTypeTOForReport) deletingItem;
            getDetailTypesService().deleteDetailType(detailType.getId());
            return true;
        }
    }

    private class OpenDetailTypeCommand extends PropertiesCommandBase {
        protected OpenDetailTypeCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //Editing must me applied only to the full form of the TO.
            DetailTypeTOForReport detailType = (DetailTypeTOForReport) editingItem;
            DetailTypeTO detailTypeFull = getDetailTypesService().getDetailTypeFullTO(detailType);
            PropertiesForm prop = new DetailTypeForm(detailTypeFull, getEditAvailability().isAvailable(context));
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                //Saving edited detail type.
                getDetailTypesService().saveDetailType(detailTypeFull);
                //Getting changed item for the tableReport.
                DetailTypesTransformer.updateDetailType(detailType, detailTypeFull);
                return true;
            }
            return false;
        }
    }

    //====================================== Helpers ========================================
    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_DETAIL_TYPES_LIST);
    }

    private DetailTypeService getDetailTypesService() {
        return SpringServiceContext.getInstance().getDetailTypesService();
    }
}
