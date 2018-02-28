/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.basedirectory.measureunit;

import com.artigile.warehouse.bl.directory.MeasureUnitService;
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
import com.artigile.warehouse.utils.dto.MeasureUnitTO;

public class MeasureUnitEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateMeasureUnitCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateMeasureUnitCommand());
        commands.add(new DeleteMeasureUnitCommand());
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new OpenMeasureUnitCommand());
        }
    }

    //==================== Commands ===========================================
    private class CreateMeasureUnitCommand extends CreateCommand {
        protected CreateMeasureUnitCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            MeasureUnitTO measureUnitTO = new MeasureUnitTO();
            PropertiesForm prop = new MeasureUnitForm(measureUnitTO, true);
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                getMeasureUnitService().save(measureUnitTO);
                return measureUnitTO;
            }
            return null;
        }
    }

    private class DeleteMeasureUnitCommand extends DeleteCommand {
        protected DeleteMeasureUnitCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            getMeasureUnitService().remove((MeasureUnitTO) deletingItem);
            return true;
        }
    }

    private class OpenMeasureUnitCommand extends PropertiesCommandBase {
        protected OpenMeasureUnitCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            MeasureUnitTO measureUnitTO = (MeasureUnitTO) editingItem;
            PropertiesForm prop = new MeasureUnitForm(measureUnitTO, getEditAvailability().isAvailable(context));
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                getMeasureUnitService().save(measureUnitTO);
                return true;
            }
            return false;
        }
    }

    //====================================== Helpers ========================================
    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_MEASURE_UNIT_LIST);
    }

    private MeasureUnitService getMeasureUnitService() {
        return SpringServiceContext.getInstance().getMeasureUnitService();
    }
}
