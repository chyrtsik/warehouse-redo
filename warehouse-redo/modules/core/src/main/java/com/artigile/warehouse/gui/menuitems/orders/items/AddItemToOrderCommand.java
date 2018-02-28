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
import com.artigile.warehouse.bl.orders.OrderService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.chooseonealternative.ChooseOneAlternativeForm;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.orders.OrderItemTO;
import com.artigile.warehouse.utils.dto.orders.OrderTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 05.03.2010
 */

/**
 * Command implementation for adding new item to the order.
 */
public class AddItemToOrderCommand extends CustomCommand {
    private OrderTOForReport order;

    public AddItemToOrderCommand(OrderTOForReport order) {
        super(new ResourceCommandNaming("order.items.editor.allToOrder.command"), new PermissionCommandAvailability(PermissionType.EDIT_ORDER_ITEMS));
        this.order = order;
    }

    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        DetailBatchTO detailBatchToAdd = (DetailBatchTO) context.getCurrentReportItem();
        try {
            return onAddNewDetailItem(detailBatchToAdd);
        } catch (BusinessException e) {
            throw new ReportCommandException(e);
        }
    }

    private boolean onAddNewDetailItem(DetailBatchTO detailBatchToAdd) throws BusinessException {
        //Try to add new detail item to the order.
        OrderItemTO newOrderItem = new OrderItemTO(order, detailBatchToAdd);

        //First we decide what to if, is the new item will be duplicated with the same one.
        final int addNewItem = 1;
        final int changeExistentItem = 2;
        int whatToDo = addNewItem;

        OrderService ordersService = SpringServiceContext.getInstance().getOrdersService();
        OrderItemTO sameItem = ordersService.findSameOrderItem(newOrderItem);
        if (sameItem != null) {
            //Let user choose, what does he want to do with new item (because it will be duplicated).
            ChooseOneAlternativeForm chooseForm = new ChooseOneAlternativeForm();
            chooseForm.setTitle(I18nSupport.message("order.items.editor.itemAlreadyExists.title"));
            chooseForm.setMessage(I18nSupport.message("order.items.editor.itemAlreadyExists.message"));
            chooseForm.addChoice(new ListItem(I18nSupport.message("order.items.editor.itemAlreadyExists.addNewItem"), addNewItem));
            chooseForm.addChoice(new ListItem(I18nSupport.message("order.items.editor.itemAlreadyExists.changeExistentItem"), changeExistentItem));
            if (!Dialogs.runProperties(chooseForm)) {
                return false;
            }
            whatToDo = (Integer) chooseForm.getChoice().getValue();
        }

        if (whatToDo == addNewItem) {
            //Adding of new order item.
            PropertiesForm prop = new OrderDetailItemForm(newOrderItem, true, false);
            if (Dialogs.runProperties(prop)) {
                ordersService.addItemToOrder(newOrderItem);
                return true;
            }
            return false;
        } else if (whatToDo == changeExistentItem) {
            //Changing existent order item instead of adding new one.
            PropertiesForm prop = new OrderDetailItemForm(sameItem, true, true);
            if (Dialogs.runProperties(prop)){
                ordersService.saveOrderItem(sameItem);
            }
            return false; //Little hack to prevent report framework from adding new item.
        } else {
            throw new RuntimeException("AddItemToOrderCommand.onAddNewDetailItem: unexpected user choice of action.");
        }
    }
}
