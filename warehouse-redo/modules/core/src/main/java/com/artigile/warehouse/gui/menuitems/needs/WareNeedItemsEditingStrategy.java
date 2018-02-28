/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.needs;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.needs.WareNeedItemTO;
import com.artigile.warehouse.utils.dto.needs.WareNeedTO;

/**
 * @author Shyrik, 28.02.2009
 */
public class WareNeedItemsEditingStrategy implements ReportEditingStrategy {
    WareNeedTO wareNeed;

    public WareNeedItemsEditingStrategy(WareNeedTO wareNeed) {
        this.wareNeed = wareNeed;
    }

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        //TODO: Now text commands are disabled. Fix working with text of remove text logic at all.
        //commands.add(new CreateTextWareNeedItemCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        //Command for creating new are need.
        //TODO: Now text commands are disabled. Fix working with text of remove text logic at all.
        //commands.add(new CreateTextWareNeedItemCommand());

        //Command for deleting ware needs.
        boolean canDeleteWareNeed = true;
        for (Object itemObj : context.getCurrentReportItems()){
            WareNeedItemTO wareNeedItem = (WareNeedItemTO)itemObj;
            if (!wareNeedItem.canBeDeleted()){
                canDeleteWareNeed = false;
                break;
            }
        }
        if (canDeleteWareNeed){
            commands.add(new DeleteWareNeedItemCommand());
        }

        //Command for edit ware need properties.
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new EditWareNeedItemCommand());
        }
    }

    //==================== Commands ===========================================
    private class CreateTextWareNeedItemCommand extends CustomCommand {
        protected CreateTextWareNeedItemCommand() {
            super(new ResourceCommandNaming("wareNeed.items.editor.addText.command"), getEditAvailability());
        }

        @Override
        protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
            //Creating new text item.
            WareNeedItemTO newTextItem = new WareNeedItemTO(wareNeed, true);
            PropertiesForm prop = new WareNeedItemForm(newTextItem, true);
            if (Dialogs.runProperties(prop)) {
                //Saving new ware need item.
                wareNeed.addNewItem(newTextItem);
                context.getReportModel().fireDataChanged();
                return true;
            }
            return false;
        }
    }

    private class DeleteWareNeedItemCommand extends DeleteCommand {
        protected DeleteWareNeedItemCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            //Deleting choosed ware need item.
            WareNeedItemTO wareNeedItemTO = (WareNeedItemTO)deletingItem;
            if (wareNeedItemTO.isEditableState()) {
                wareNeedItemTO.getWareNeed().deleteItem(wareNeedItemTO);
            }
            return true;
        }
    }

    private class EditWareNeedItemCommand extends PropertiesCommandBase {
        protected EditWareNeedItemCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //Edit properties of the ware need item.
            WareNeedItemTO wareNeedItem = (WareNeedItemTO)editingItem;
            PropertiesForm prop = new WareNeedItemForm(wareNeedItem, getEditAvailability().isAvailable(context)
                    && wareNeedItem.isEditableState());
            if (Dialogs.runProperties(prop)) {
                //Saving edited ware need item.
                SpringServiceContext.getInstance().getWareNeedsService().saveWareNeedItem(wareNeedItem);
                return true;
            }
            return false;
        }
    }

    //=========================== Helpers ==================================================
    private static AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_WARE_NEED_ITEMS);
    }
}
