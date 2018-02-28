/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.command;

import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.NoDeletePermissionException;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.dto.orders.OrderTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 20.12.2008
 */

/**
 * Base implementation of the command for deleting report item.
 */
public abstract class DeleteCommand extends ReportCommandBase {

    protected DeleteCommand(AvailabilityStrategy availabilityStrategy) {
        super(new ResourceCommandNaming("report.command.delete"), availabilityStrategy);
    }

    @Override
    public ReportCommandType getType() {
        return ReportCommandType.DELETE;
    }

    @Override
    public boolean execute(ReportCommandContext context) throws ReportCommandException {
        if (!isAvailable(context)) {
            throw new NoDeletePermissionException();
        }

        //Performing deleting item operation
        List currentItems = context.getCurrentReportItems();
        if (currentItems == null) {
            return false;
        }

        boolean confirmDelete = MessageDialogs.showConfirm(context.getReportView().getViewComponent(), I18nSupport.message("delete.dialog.title"), getConfirmContent(currentItems.size()));
        if (confirmDelete) {
            //Checking if we can delete all items.
            for (Object item : currentItems) {
                if (!doDelete(item)) {
                    // Method of deleting order from order service haves own messages of errors.
                    if (item instanceof OrderTOForReport) {
                        return false;
                    }

                    MessageDialogs.showWarning(context.getReportView().getViewComponent(), getWarningContent(currentItems.size()));
                    return false;
                }
            }

            for (Object item : currentItems) {
                context.getReportModel().deleteItem(item);
            }
            return true;
        }
        return false;
    }

    private String getConfirmContent(int number) {
        if (number == 1) {
            return I18nSupport.message("delete.single.confirm");
        } else {
            return I18nSupport.message("delete.multi.confirm");
        }
    }

    private String getWarningContent(int number) {
        if (number == 1) {
            return I18nSupport.message("delete.single.warning");
        } else {
            return I18nSupport.message("delete.multi.warning");
        }
    }

    /**
     * Must delete the and report result of the operation (done or not done).
     *
     * @param deletingItem
     * @return
     */
    protected abstract boolean doDelete(Object deletingItem) throws ReportCommandException;
}
