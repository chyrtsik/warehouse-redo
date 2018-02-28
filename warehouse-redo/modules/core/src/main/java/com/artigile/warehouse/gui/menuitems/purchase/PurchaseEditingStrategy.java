/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.purchase;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.purchase.PurchaseService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.domain.purchase.PurchaseState;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.plugin.Plugin;
import com.artigile.warehouse.gui.core.print.PrintingPreparator;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.postings.CreatePostingCommand;
import com.artigile.warehouse.gui.menuitems.purchase.items.PurchaseItemsEditor;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTOForReport;

/**
 * @author Shyrik, 01.03.2009
 */
public class PurchaseEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreatePurchaseCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        boolean singleItemSelected = context.getCurrentReportItems().size() == 1;

        //Purchase printing commands.
        if (singleItemSelected){
            commands.add(new PrintPreviewCurrentReportItemCommand(PrintTemplateType.TEMPLATE_PURCHASE, getPrintAvailability(), getPrintingPreparator()));
            commands.add(new PrintCurrentReportItemCommand(PrintTemplateType.TEMPLATE_PURCHASE, getPrintAvailability(), getPrintingPreparator()));
        }

        //Command for deleting purchase and changing it's state.
        boolean showShipPurchaseCommand = true;
        boolean showCreatePostingCommand = true;
        boolean showDeleteCommand = true;

        for (Object itemObj : context.getCurrentReportItems()){
            PurchaseTOForReport purchase = (PurchaseTOForReport) context.getCurrentReportItem();
            if (!purchase.getState().equals(PurchaseState.WAITING)){
                showShipPurchaseCommand = false;
            }
            if (!purchase.getState().equals(PurchaseState.SHIPPED)){
                showCreatePostingCommand = false;
            }
            if (!purchase.canBeDeleted()){
                showDeleteCommand = false;
            }
        }
        if (showShipPurchaseCommand){
            commands.add(new ShipPurchaseCommand());
        }
        if (showCreatePostingCommand){
            commands.add(new CreatePostingFromPurchaseCommand());
        }

        //Command for editing purchase content.
        ReportCommand editPurchaseContentCommand = new EditPurchaseContentCommand();
        commands.setDefaultCommandForRow(editPurchaseContentCommand);
        commands.add(editPurchaseContentCommand);

        //Command for creating new purchase.
        commands.add(new CreatePurchaseCommand());

        //Command for deleting selected purchases.
        if (showDeleteCommand){
            commands.add(new DeletePurchaseCommand());
        }

        //Command for editing purchase properties.
        if (singleItemSelected){
            commands.add(new PurchasePropertiesCommand());
        }
    }

    private AvailabilityStrategy getPrintAvailability() {
        return new PermissionCommandAvailability(PermissionType.VIEW_PURCHASE_ITEMS);
    }

    private PrintingPreparator getPrintingPreparator() {
        return new PrintingPreparator() {
            public Object prepareForPrinting(Object objectForPrinting) {
                //Before printing full content of the purchase to be printed must be loaded.
                PurchaseTOForReport purchaseForReport = (PurchaseTOForReport) objectForPrinting;
                return getPurchaseService().getPurchaseFullData(purchaseForReport.getId());
            }
        };
    }

    //==================================== Commands ===============================================
    private class DeletePurchaseCommand extends DeleteCommand {
        protected DeletePurchaseCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            //Deleting selected purchase
            PurchaseTOForReport purchaseToDelete = (PurchaseTOForReport) deletingItem;
            getPurchaseService().deletePurchase(purchaseToDelete.getId());
            return true;
        }
    }

    /**
     * Command for making purchase shipped.
     */
    public class ShipPurchaseCommand extends CustomCommand {
        protected ShipPurchaseCommand() {
            super(new ResourceCommandNaming("purchase.command.shipped"), new AvailabilityStrategy(){
                AvailabilityStrategy editAvailability = getEditAvailability();
                @Override
                public boolean isAvailable(ReportCommandContext context) {
                    return editAvailability.isAvailable(context) && 
                        ((PurchaseTOForReport)context.getCurrentReportItem()).getState() == PurchaseState.WAITING;
                }
            });
        }

        @Override
        protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
            PurchaseTOForReport purchase = (PurchaseTOForReport)context.getCurrentReportItem();
            try {
                SpringServiceContext.getInstance().getPurchaseService().shipPurchase(purchase);
            } catch (BusinessException e) {
                throw new ReportCommandException(e);
            }
            return true;
        }
    }

    /**
     * Command for creating posting from purchase.
     */
    private class CreatePostingFromPurchaseCommand extends CustomCommand {
        private CreatePostingCommand createPostingCommand = new CreatePostingCommand();

        protected CreatePostingFromPurchaseCommand() {
            super(new ResourceCommandNaming("purchase.command.createPorsing"), new AvailabilityStrategy(){
                AvailabilityStrategy editAvailability = getEditAvailability();
                @Override
                public boolean isAvailable(ReportCommandContext context) {
                    return editAvailability.isAvailable(context) &&
                           ((PurchaseTOForReport)context.getCurrentReportItem()).getState() == PurchaseState.SHIPPED;
                }
            });
        }

        @Override
        protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
            if (createPostingCommand.execute(new ReportCommandParametersContext(context.getCurrentReportItem()))){
                context.getReportModel().fireItemDataChanged(context.getCurrentReportItem());
                return true;
            }
            return false;
        }
    }

    private class EditPurchaseContentCommand extends CustomCommand {
        protected EditPurchaseContentCommand() {
            super(new ResourceCommandNaming("purchase.items.edit.command"), new PermissionCommandAvailability(PermissionType.VIEW_PURCHASE_ITEMS));
        }

        @Override
        protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
            //Opening items of the selected purchase.
            PurchaseTOForReport purchaseTO = (PurchaseTOForReport) context.getCurrentReportItem();
            Plugin purchaseContentEditor = new PurchaseItemsEditor(purchaseTO.getId());
            WareHouse.runPlugin(purchaseContentEditor);
            return true;
        }
    }

    private class PurchasePropertiesCommand extends PropertiesCommandBase {
        protected PurchasePropertiesCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //Edit purchase properties.
            PurchaseTOForReport purchaseTOForReport = (PurchaseTOForReport) editingItem;
            PropertiesForm prop = new PurchaseForm(purchaseTOForReport, purchaseTOForReport.canBeEdited());
            if (Dialogs.runProperties(prop)) {
                getPurchaseService().savePurchase(purchaseTOForReport);
                return true;
            }
            return false;
        }
    }

    //================================== Helpers ========================================
    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_PURCHASE_LIST);
    }

    private PurchaseService getPurchaseService() {
        return SpringServiceContext.getInstance().getPurchaseService();
    }
}

