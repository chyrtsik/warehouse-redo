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

import com.artigile.warehouse.bl.admin.UserService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.orders.OrderItemTO;

/**
 * @author: Vadim.Zverugo
 */

/**
 * Availability of editing order item.
 */
public class OrderItemEditingAvailability implements AvailabilityStrategy {
    @Override
    public boolean isAvailable(ReportCommandContext context) {
        return isAvailable((ReportCommandContext) context.getCurrentReportItem());
    }

    public boolean isAvaible(OrderItemTO orderItem) {
        UserService userService = SpringServiceContext.getInstance().getUserService();
        return userService.checkPermission(WareHouse.getUserSession().getUser().getId(), PermissionType.EDIT_POSTING_ITEMS) &&
                orderItem.isEditableState();
    }
}
