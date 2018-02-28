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
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.PropertiesCommandBase;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.orders.OrderItemTO;

/**
 * @author Shyrik, 05.03.2010
 */

/**
 * Command for editing order item properties.
 */
public class EditOrderItemCommand extends PropertiesCommandBase {
    protected EditOrderItemCommand() {
        super(new PredefinedCommandAvailability(true));
    }

    @Override
    protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
        //Edit properties of the order item.
        OrderItemTO orderItem = (OrderItemTO) editingItem;

        boolean canEdit = OrderItemsEditingStrategy.getEditAvailability().isAvailable(context) && orderItem.isEditableState();
        PropertiesForm prop = orderItem.isDetailItem()
            ? new OrderDetailItemForm(orderItem, canEdit, false)
            : new OrderTextItemForm(orderItem, canEdit);

        if (Dialogs.runProperties(prop)) {
            //Saving edited order item.
            try {
                SpringServiceContext.getInstance().getOrdersService().saveOrderItem(orderItem);
            } catch (BusinessException e) {
                throw new ReportCommandException(e);
            }
            return true;
        }
        return false;
    }
}
