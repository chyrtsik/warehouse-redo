/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.purchase.items;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.DeleteCommand;
import com.artigile.warehouse.gui.core.report.command.PropertiesCommandBase;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.needs.WareNeedItemTO;
import com.artigile.warehouse.utils.dto.purchase.PurchaseItemTO;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTOForReport;

/**
 * @author Shyrik, 02.03.2009
 */
public class PurchaseItemsEditingStrategy implements ReportEditingStrategy {

    private PurchaseTOForReport purchase;

    public PurchaseItemsEditingStrategy(PurchaseTOForReport purchase) {
        this.purchase = purchase;
    }

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
//        commands.add(new CreateTextPurchaseItemCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
//        commands.add(new CreateTextPurchaseItemCommand());

        boolean canDeleteItem = true;
        for (Object itemObj : context.getCurrentReportItems()) {
            PurchaseItemTO purchaseItem = (PurchaseItemTO)itemObj;
            if (!purchaseItem.canBeDeleted()){
                canDeleteItem = false;
                break;
            }
        }
        if (canDeleteItem) {
            commands.add(new DeletePurchaseItemCommand());
        }

        if (context.getCurrentReportItems().size() == 1) {
            commands.add(new EditPurchaseItemCommand());
        }
    }

//    private class CreateTextPurchaseItemCommand extends CustomCommand {
//
//        protected CreateTextPurchaseItemCommand() {
//            super(new ResourceCommandNaming("purchase.items.editor.addText.command"), getCreateAvailability());
//        }
//
//        @Override
//        protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
//            //Creating new text item.
//            PurchaseItemTO newTextItem = new PurchaseItemTO(purchase);
//            PropertiesForm prop = new PurchaseItemForm(newTextItem, true);
//            if (Dialogs.runProperties(prop)) {
//                //Saving new ware need item.
//                try {
//                    SpringServiceContext.getInstance().getPurchaseService().addItemToPurchase(newTextItem);
//                } catch (BusinessException e) {
//                    throw new ReportCommandException(e);
//                }
//                context.getReportModel().fireDataChanged();
//                return true;
//            }
//            return false;
//        }
//    }

    private class DeletePurchaseItemCommand extends DeleteCommand {

        protected DeletePurchaseItemCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            //Deleting choosed purchase item.
            PurchaseItemTO purchaseItemTO = (PurchaseItemTO)deletingItem;
            try {
                WareNeedItemTO wareNeedItem = purchaseItemTO.getWareNeedItem();
                SpringServiceContext.getInstance().getPurchaseService().deleteItemFromPurchase(purchaseItemTO);
                if (wareNeedItem.getAutoCreated()) {
                    SpringServiceContext.getInstance().getWareNeedsService().deleteItemFromWareNeed(wareNeedItem);
                }
            } catch (BusinessException e) {
                throw new ReportCommandException(e);
            }
            return true;
        }
    }

    private class EditPurchaseItemCommand extends PropertiesCommandBase {

        protected EditPurchaseItemCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //Edit properties of the purc-
            //  hase item.
            PurchaseItemTO purchaseItem = (PurchaseItemTO)editingItem;
            PropertiesForm prop = new PurchaseItemForm(purchaseItem, getEditAvailability().isAvailable(context));
            if (Dialogs.runProperties(prop)) {
                //Saving edited purchase item.
                SpringServiceContext.getInstance().getPurchaseService().savePurchaseItem(purchaseItem);
                return true;
            }
            return false;
        }
    }

    //=========================== Helpers ==================================================
    private AvailabilityStrategy getEditAvailability() {
        return new PurchaseItemEditAvailability();
    }

    private AvailabilityStrategy getCreateAvailability() {
        return new PurchaseItemEditAvailability(purchase);
    }
}
