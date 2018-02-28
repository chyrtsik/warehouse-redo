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
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.orders.OrderItemTO;
import com.artigile.warehouse.utils.dto.orders.OrderTOForReport;

/**
 * Command for adding new text item to the order.
 */
class CreateTextOrderItemCommand extends CustomCommand {
    private OrderTOForReport order;

    public CreateTextOrderItemCommand(OrderTOForReport order) {
        super(new ResourceCommandNaming("order.items.list.command.createTextItem"), OrderItemsEditingStrategy.getEditAvailability());
        this.order = order;
    }

    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        //Creating new text item.
        OrderItemTO newTextOrderItem = new OrderItemTO(order);
        PropertiesForm prop = new OrderTextItemForm(newTextOrderItem, true);
        PropertiesDialog propDialog = new PropertiesDialog(prop);
        if (propDialog.run()) {
            //Saving new order item.
            try {
                SpringServiceContext.getInstance().getOrdersService().addItemToOrder(newTextOrderItem);
            } catch (BusinessException e) {
                throw new ReportCommandException(e);
            }
            return true;
        }
        return false;
    }
}
