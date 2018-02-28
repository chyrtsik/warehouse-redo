/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.contractors.contacts;

import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.dto.ContactTO;

/**
 * @author IoaN, Dec 11, 2008
 */

public class ContactsStrategy implements ReportEditingStrategy {
    private boolean canEdit;

    public ContactsStrategy(boolean canEdit) {
        this.canEdit = canEdit;
    }

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateContactCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateContactCommand());
        commands.add(new DeleteContactCommand());
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new OpenContactCommand());
        }
    }

    //==================== Commands ===========================================
    private class CreateContactCommand extends CreateCommand {
        protected CreateContactCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            ContactTO operationContact = new ContactTO();
            PropertiesForm prop = new ContactForm(operationContact, true);
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                return operationContact;
            }
            return null;
        }
    }

    private class DeleteContactCommand extends DeleteCommand {
        protected DeleteContactCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            //We dont needs any apesial processing of deleting. Only deleting from the tableReport.
            return true;
        }
    }

    private class OpenContactCommand extends PropertiesCommandBase {
        protected OpenContactCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            ContactTO operationContact = (ContactTO) editingItem;
            PropertiesForm prop = new ContactForm(operationContact, getEditAvailability().isAvailable(context));
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            return propDialog.run();
        }
    }

    //====================================== Helpers ========================================
    private AvailabilityStrategy getEditAvailability() {
        return new PredefinedCommandAvailability(canEdit);
    }
}
