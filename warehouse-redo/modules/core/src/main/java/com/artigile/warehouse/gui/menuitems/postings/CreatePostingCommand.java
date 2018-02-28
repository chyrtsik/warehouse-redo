/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.postings;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.plugin.Plugin;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.CreateCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.postings.items.PostingItemsEditor;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteTOForReport;
import com.artigile.warehouse.utils.dto.postings.PostingTOForReport;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTOForReport;

/**
 * @author Shyrik, 02.02.2009
 */
/**
 * Command for creating new posting.
 */
public class CreatePostingCommand extends CreateCommand {
    public CreatePostingCommand() {
        super(new PermissionCommandAvailability(PermissionType.EDIT_POSTING_LIST));
    }

    @Override
    protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
        //Creating new posting.
        PostingTOForReport posting = new PostingTOForReport(true);

        Object params = context.getCommandParameters();
        if (params != null){
            if (params instanceof PurchaseTOForReport){
                //Creating posting from purchase.
                posting.initForPurchase((PurchaseTOForReport)params);
            }
            if (params instanceof DeliveryNoteTOForReport){
                //Creating posting from purchase.
                posting.initForDeliveryNote((DeliveryNoteTOForReport)params);
            }
        }

        PropertiesForm prop = new PostingForm(posting, true);
        if (Dialogs.runProperties(prop)) {
            if (posting.getPurchase() != null){
                //Create posting from purchase.
                try {
                    SpringServiceContext.getInstance().getPostingsService().createPostingFromPurchase(posting);
                } catch (BusinessException e) {
                    throw new ReportCommandException(e);
                }
            }
            else if (posting.getDeliveryNote() != null){
                //Create posting from delivery note.
                try {
                    SpringServiceContext.getInstance().getPostingsService().createPostingFromDeliveryNote(posting);
                } catch (BusinessException e) {
                    throw new ReportCommandException(e);
                }
            }
            else{
                //Saving new empty posting.
                SpringServiceContext.getInstance().getPostingsService().savePosting(posting);
            }

            //Starting editor of items of the created posting.
            Plugin orderContentEditor = new PostingItemsEditor(posting.getId());
            WareHouse.runPlugin(orderContentEditor);
            return posting;
        }
        return null;
    }
}
