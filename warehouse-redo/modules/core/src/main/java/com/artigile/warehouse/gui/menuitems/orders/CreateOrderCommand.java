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

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.plugin.Plugin;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.CreateCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.orders.items.OrderItemsEditor;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.orders.OrderTOForReport;

/**
 * @author Shyrik, 01.02.2009
 */

/**
 * Command for creating new order.
 */
public class CreateOrderCommand extends CreateCommand {
    public CreateOrderCommand() {
        super(new PermissionCommandAvailability(PermissionType.EDIT_ORDER_LIST));
    }

    @Override
    protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
        //Creating new order.
        OrderTOForReport order = new OrderTOForReport(true);

        Object params = context.getCommandParameters();
        if (params != null){
            if (params instanceof ContractorTO){
                order.setContractor((ContractorTO)params);                
            }
        }

        PropertiesForm prop = new OrderForm(order, true);
        if (Dialogs.runProperties(prop)) {
            //Saving new order
            SpringServiceContext.getInstance().getOrdersService().saveOrder(order);

            //Starting editor of items of the created order.
            Plugin orderContentEditor = new OrderItemsEditor(order.getId());
            WareHouse.runPlugin(orderContentEditor);
            return order;
        }
        return null;
    }
}
