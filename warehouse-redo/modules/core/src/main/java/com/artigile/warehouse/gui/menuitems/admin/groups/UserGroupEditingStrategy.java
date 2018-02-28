/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.admin.groups;

import com.artigile.warehouse.bl.admin.UserGroupService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserGroupTO;

/**
 * Strategy of editing user groups.
 *
 * @author Shyrik, 07.12.2008
 */
public class UserGroupEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateUserGroupCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateUserGroupCommand());
        commands.add(new DeleteUserGroupCommand());
        if (context.getCurrentReportItems().size() == 1){
          commands.add(new OpenUserGroupCommand());
        }
    }

    private void refreshTree() {
        WareHouse.refreshMenuTree();
    }

    private class CreateUserGroupCommand extends CreateCommand {
        protected CreateUserGroupCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            //Open properties editor for the new group.
            UserGroupTO group = new UserGroupTO();
            UserGroupForm prop = new UserGroupForm(group, true);
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                //Saving new group.
                group.setId(getUserGroupService().saveOrUpdateGroup(group, prop.getPermissions()));
                refreshTree();
                return group;
            }

            return null;
        }
    }

    private class DeleteUserGroupCommand extends DeleteCommand {
        protected DeleteUserGroupCommand() {
            super(getDeleteAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            if (deletingItem instanceof UserGroupTO) {
                UserGroupTO userGroupTO = (UserGroupTO) deletingItem;
                if (!userGroupTO.isPredefined()) {
                    getUserGroupService().deleteGroup(userGroupTO);
                    refreshTree();
                    return true;
                }
            }
            return false;
        }
    }

    private class OpenUserGroupCommand extends PropertiesCommandBase {
        protected OpenUserGroupCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //Open transformUser group in property editor.
            UserGroupTO group = (UserGroupTO) editingItem;
            PropertiesForm prop = new UserGroupForm(group, getEditAvailability().isAvailable(context));
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                //Saving edited group.
                getUserGroupService().saveOrUpdateGroup(group, ((UserGroupForm) prop).getPermissions());
                refreshTree();
                return true;
            }
            return false;
        }
    }

    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_GROUPS);
    }

    /**
     * Decorator for availability strategy, that forbids deleting predefined user groups.
     */
    class DeleteCommandAvailabilityDecorator implements AvailabilityStrategy {
        private AvailabilityStrategy decoratedStrategy;

        DeleteCommandAvailabilityDecorator(AvailabilityStrategy decoratedStrategy) {
            this.decoratedStrategy = decoratedStrategy;
        }

        @Override
        public boolean isAvailable(ReportCommandContext context) {
            boolean isAvailable = decoratedStrategy.isAvailable(context);
            if (isAvailable) {
                Object obj = context.getCurrentReportItem();
                if (obj instanceof UserGroupTO) {
                    UserGroupTO userGroupTO = (UserGroupTO) obj;
                    isAvailable = !userGroupTO.isPredefined();
                }
            }
            return isAvailable;
        }
    }

    private AvailabilityStrategy getDeleteAvailability() {
        return new DeleteCommandAvailabilityDecorator(new PermissionCommandAvailability(PermissionType.EDIT_GROUPS));
    }

    private UserGroupService getUserGroupService() {
        return SpringServiceContext.getInstance().getUserGroupService();
    }
}
