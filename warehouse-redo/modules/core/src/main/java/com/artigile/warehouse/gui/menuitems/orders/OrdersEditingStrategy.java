/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.orders;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.orders.OrderService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.plugin.Plugin;
import com.artigile.warehouse.gui.core.print.PrintingPreparator;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.MultipleAndCriteriaCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.orders.items.OrderItemsEditor;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.orders.OrderTOForReport;

/**
 * @author Shyrik, 06.01.2009
 */
public class OrdersEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateOrderCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        boolean singleItemSelected = (context.getCurrentReportItems().size() == 1);

        if (singleItemSelected){
            //Order printing.
            commands.add(new PrintPreviewCurrentReportItemCommand(PrintTemplateType.TEMPLATE_ORDER, getPrintAvailability(), getPrintingPreparator()));
            commands.add(new PrintCurrentReportItemCommand(PrintTemplateType.TEMPLATE_ORDER, getPrintAvailability(), getPrintingPreparator()));

            //Command for editing of order content.
            ReportCommand editOderContentCommand = new EditOrderContentCommand();
            commands.setDefaultCommandForRow(editOderContentCommand);
            commands.add(editOderContentCommand);
        }

        //Command for adding new order.
        commands.add(new CreateOrderCommand());
        if (singleItemSelected){
            commands.add(new CopyOrderCommand());
        }

        //Command for deleting selected orders.
        boolean canDeleteOrder = true;
        for (Object itemObj : context.getCurrentReportItems()){
            OrderTOForReport order = (OrderTOForReport)itemObj;
            if (order == null || !order.isEditableState()) {
                canDeleteOrder = false;
                break;
            }
        }
        if (canDeleteOrder){
            commands.add(new DeleteOrderCommand());
        }

        //Command for editing of order properties.
        if (singleItemSelected){
            commands.add(new OrderPropertiesCommand());
        }
    }

    private AvailabilityStrategy getPrintAvailability() {
        return new PermissionCommandAvailability(PermissionType.VIEW_ORDER_ITEMS);
    }

    private PrintingPreparator getPrintingPreparator() {
        return new PrintingPreparator(){
            public Object prepareForPrinting(Object objectForPrinting) {
                //Before printing full content of the order to be printed must be loaded.
                OrderTOForReport orderForReport = (OrderTOForReport)objectForPrinting;
                return getOrdersService().getOrderFullData(orderForReport.getId());
            }
        };
    }

    //==================================== Commands ===============================================
    private class DeleteOrderCommand extends DeleteCommand {
        protected DeleteOrderCommand() {
            super(new PermissionCommandAvailability(PermissionType.EDIT_ORDER_LIST));
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            //Deleting selected order.
            boolean isDeleted = false;
            OrderTOForReport orderToDelete = (OrderTOForReport)deletingItem;
            try {
                if (getOrdersService().deleteOrder(orderToDelete.getId())) {
                    isDeleted = true;
                }
            } catch (BusinessException e) {
                throw new ReportCommandException(e);
            }
            return isDeleted;
        }
    }

    private class EditOrderContentCommand extends ReportCommandBase {
        protected EditOrderContentCommand() {
            super(new ResourceCommandNaming("order.items.edit.command"), new PermissionCommandAvailability(PermissionType.VIEW_ORDER_ITEMS));
        }

        @Override
        public ReportCommandType getType() {
            return ReportCommandType.CUSTOM;
        }

        @Override
        public boolean execute(ReportCommandContext context) throws ReportCommandException {
            //Opening items of the selected order.
            OrderTOForReport orderTO = (OrderTOForReport)context.getCurrentReportItem();
            Plugin orderContentEditor = new OrderItemsEditor(orderTO.getId());
            WareHouse.runPlugin(orderContentEditor);
            return true;
        }
    }

    private class OrderPropertiesCommand extends PropertiesCommandBase {
        protected OrderPropertiesCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //Edit order properties.
            OrderTOForReport order = (OrderTOForReport) editingItem;
            PropertiesForm prop = new OrderForm(order, getEditAvailability().isAvailable(context));
            if (Dialogs.runProperties(prop)) {
                getOrdersService().saveOrder(order);
                return true;
            }
            return false;
        }
    }

    //================================== Helpers ========================================
    private AvailabilityStrategy getEditAvailability() {
        return new MultipleAndCriteriaCommandAvailability(
                new PermissionCommandAvailability(PermissionType.EDIT_ORDER_LIST),
                new AvailabilityStrategy() {
                    @Override
                    public boolean isAvailable(ReportCommandContext context) {
                        OrderTOForReport order = (OrderTOForReport)context.getCurrentReportItem();
                        return order.isEditableState();
                    }
                });
    }

    private OrderService getOrdersService() {
        return SpringServiceContext.getInstance().getOrdersService();
    }
}
