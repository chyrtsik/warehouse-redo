/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.admin.users;

import com.artigile.warehouse.bl.admin.UserService;
import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.admin.password.PasswordResetForm;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserTO;

/**
 * @author Shyrik, 08.12.2008
 */
public class UserEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateUserCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        boolean singleItemSelected = context.getCurrentReportItems().size() == 1;

        if (singleItemSelected){
            commands.add(new SetUserPasswordCommand());
        }
        commands.add(new CreateUserCommand());
        commands.add(new DeleteUserCommand());
        if (singleItemSelected){
            commands.add(new OpenUserCommand());
        }
    }

    //=========================== Commands ============================================

    private class SetUserPasswordCommand extends ReportCommandBase {
        private SetUserPasswordCommand() {
            super(new ResourceCommandNaming("user.list.change.password"), getEditAvailability());
        }

        @Override
        public ReportCommandType getType() {
            return ReportCommandType.CUSTOM;
        }

        @Override
        public boolean execute(ReportCommandContext context) throws ReportCommandException {
            Object curItem = context.getCurrentReportItem();
            if (curItem instanceof UserTO) {
                UserTO user = (UserTO) curItem;
                PasswordResetForm passwordResetForm = new PasswordResetForm();
                if (Dialogs.runProperties(passwordResetForm)){
                    try {
                        SpringServiceContext.getInstance().getUserService().updatePassword(user.getId(), passwordResetForm.getNewPassword());
                        user.setPassword(passwordResetForm.getNewPassword());
                    } catch (BusinessException e) {
                        MessageDialogs.showWarning(e.getLocalizedMessage());
                    }
                }
                return true;
            }

            return false;
        }
    }

    private class CreateUserCommand extends CreateCommand {
        protected CreateUserCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            //Open properties editor for the new transformUser.
            UserTO user = new UserTO();
            UserForm prop = new UserForm(user, true);
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                //Saving new transformUser.
                try {
                    getUserService().saveUser(user, prop.getGroups(), prop.getWarehouses());
                } catch (BusinessException e) {
                    throw new ReportCommandException(e);
                }
                return user;
            }

            return null;
        }
    }

    private class DeleteUserCommand extends DeleteCommand {
        protected DeleteUserCommand() {
            super(getDeleteAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            try {
                getUserService().deleteUser((UserTO) deletingItem);
            } catch (BusinessException e) {
                throw new ReportCommandException(e);
            }
            return true;
        }
    }

    private class OpenUserCommand extends PropertiesCommandBase {
        protected OpenUserCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            UserTO user = (UserTO) editingItem;
            UserForm prop = new UserForm(user, getEditAvailability().isAvailable(context), false);
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                //Saving new group.
                try {
                    getUserService().saveUser(user, prop.getGroups(), prop.getWarehouses());
                } catch (BusinessException e) {
                    throw new ReportCommandException(e);
                }
                return true;
            }
            return false;
        }
    }

    //========================== Helpers =======================================
    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_USERS);
    }

    /**
     * Decorator for availability strategy, that forbids deleting predefined users.
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
                if (obj instanceof UserTO) {
                    UserTO userTO = (UserTO) obj;
                    if (userTO.isPredefined()){
                        //Predefined usr cannot be deleted.
                        isAvailable = false;
                    }
                    if (userTO.getId() == WareHouse.getUserSession().getUser().getId()){
                        //Current user cannot be deleted.
                        isAvailable = false;
                    }
                }
            }
            return isAvailable;
        }
    }

    private AvailabilityStrategy getDeleteAvailability() {
        return new DeleteCommandAvailabilityDecorator(new PermissionCommandAvailability(PermissionType.EDIT_GROUPS));
    }

    public UserService getUserService() {
        return SpringServiceContext.getInstance().getUserService();
    }
}
