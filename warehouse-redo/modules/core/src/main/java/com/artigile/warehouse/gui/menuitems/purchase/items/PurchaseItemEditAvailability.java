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

import com.artigile.warehouse.bl.admin.UserService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.purchase.PurchaseState;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.purchase.PurchaseItemTO;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTOForReport;

/**
 * @author Shyrik, 04.03.2009
 */

/**
 * Availability of editing purchase item.
 */
public class PurchaseItemEditAvailability implements AvailabilityStrategy {
    private PurchaseTOForReport purchase;

    public PurchaseItemEditAvailability(PurchaseTOForReport purchase) {
        this.purchase = purchase;
    }

    public PurchaseItemEditAvailability() {
    }

    @Override
    public boolean isAvailable(ReportCommandContext context) {
        return isAvailable((PurchaseItemTO) context.getCurrentReportItem());
    }

    public boolean isAvailable(PurchaseItemTO purchaseItem) {
        //Purchase item editing availability based both on permissions and current state of the posting.
        UserService userService = SpringServiceContext.getInstance().getUserService();
        if (!userService.checkPermission(WareHouse.getUserSession().getUser().getId(), PermissionType.EDIT_PURCHASE_ITEMS)){
            return false;
        }

        if (purchase == null){
            return purchaseItem.getPurchase().getState() == PurchaseState.CONSTRUCTION;
        }
        else{
            return purchase.getState() == PurchaseState.CONSTRUCTION; 
        }
    }
}
