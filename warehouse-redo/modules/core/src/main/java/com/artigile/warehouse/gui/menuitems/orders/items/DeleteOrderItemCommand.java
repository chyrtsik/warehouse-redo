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

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.gui.core.report.command.DeleteCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.MultipleAndCriteriaCommandAvailability;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.orders.OrderItemTO;

/**
 * @author Shyrik, 05.03.2010
 */

/**
 * Command for deleting items from the order.
 */
public class DeleteOrderItemCommand extends DeleteCommand {
    public DeleteOrderItemCommand() {
        super(new MultipleAndCriteriaCommandAvailability(
                OrderItemsEditingStrategy.getEditAvailability(),
                new AvailabilityStrategy() {
                    @Override
                    public boolean isAvailable(ReportCommandContext context) {
                        OrderItemTO orderItem = (OrderItemTO) context.getCurrentReportItem();
                        return orderItem != null && orderItem.isEditableState();
                    }
                }
                ));
    }

    @Override
    protected boolean doDelete(Object deletingItem) throws ReportCommandException {
        //Deleting choosed order item.
        boolean isDeleted = false;
        OrderItemTO orderItem = (OrderItemTO) deletingItem;
        try {
            if (SpringServiceContext.getInstance().getOrdersService().deleteItemFromOrder(orderItem.getId())) {
                isDeleted = true;
            }
        } catch (BusinessException e) {
            throw new ReportCommandException(e);
        }
        return isDeleted;
    }
}