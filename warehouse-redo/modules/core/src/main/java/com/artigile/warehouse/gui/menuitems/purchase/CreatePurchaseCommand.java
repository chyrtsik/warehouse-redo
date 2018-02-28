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

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.plugin.Plugin;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.CreateCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.purchase.items.PurchaseItemsEditor;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTOForReport;

/**
 * @author Shyrik, 02.03.2009
 */

/**
 * Command for creating new purchase.
 */
public class CreatePurchaseCommand extends CreateCommand {
    public CreatePurchaseCommand() {
        super(new PermissionCommandAvailability(PermissionType.EDIT_PURCHASE_LIST));
    }

    @Override
    protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
        //Creating new purchase.
        PurchaseTOForReport purchase = new PurchaseTOForReport(true);
        PropertiesForm prop = new PurchaseForm(purchase, true);
        if (Dialogs.runProperties(prop)) {
            //Saving new purchase.
            SpringServiceContext.getInstance().getPurchaseService().savePurchase(purchase);

            //Starting editor of items of the created purchase.
            Plugin purchaseContentEditor = new PurchaseItemsEditor(purchase.getId());
            WareHouse.runPlugin(purchaseContentEditor);
            return purchase;
        }
        return null;
    }
}
