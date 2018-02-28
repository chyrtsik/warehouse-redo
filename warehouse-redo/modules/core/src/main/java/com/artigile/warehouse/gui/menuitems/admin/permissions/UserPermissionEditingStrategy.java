/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.admin.permissions;

import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.report.command.PropertiesCommandBase;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.dto.UserPermissionTO;

/**
 * @author Shyrik, 07.12.2008
 */

/**
 * Strategy for edigin user rights list.
 */
public class UserPermissionEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new OpenPermissionCommand());
        }
    }

    //====================== Commands ======================================
    private class OpenPermissionCommand extends PropertiesCommandBase {
        protected OpenPermissionCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //Show properties of transformUser permission (User permissions cannot be edited).
            UserPermissionTO permissionTO = (UserPermissionTO) editingItem;
            PropertiesForm userPermissionForm = new UserPermissionForm(permissionTO, false);
            PropertiesDialog propDialog = new PropertiesDialog(userPermissionForm);
            propDialog.run();
            return false;
        }
    }
}
