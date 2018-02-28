/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.orders.items;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.utils.dto.orders.OrderTOForReport;

/**
 * @author Shyrik, 08.01.2009
 */
public class OrderItemsEditingStrategy implements ReportEditingStrategy {
    private OrderTOForReport order;

    public OrderItemsEditingStrategy(OrderTOForReport order) {
        this.order = order;
    }

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateTextOrderItemCommand(order));
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateTextOrderItemCommand(order));
        commands.add(new DeleteOrderItemCommand());
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new EditOrderItemCommand());
        }
    }

    //=================================== Helpers =============================================
    public static AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_ORDER_ITEMS);
    }
}
