/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.postings.items.command;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.postings.PostingService;
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
import com.artigile.warehouse.gui.menuitems.postings.items.PostingItemForm;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;
import com.artigile.warehouse.utils.dto.postings.PostingTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 05.03.2010
 */

/**
 * Command implementation for adding new item to the posting with filling posting item's form.
 */
public class AddItemToPostingCommand extends CustomCommand {
    private PostingTOForReport posting;

    public AddItemToPostingCommand(PostingTOForReport posting) {
        super(new ResourceCommandNaming("posting.items.editor.addToPosting.command"), new PermissionCommandAvailability(PermissionType.EDIT_POSTING_ITEMS));
        this.posting = posting;
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
        PostingService postingsService = SpringServiceContext.getInstance().getPostingsService();
        PostingItemTO newPostingItem = new PostingItemTO(posting, detailBatchToAdd);

        //First we decide what to if, is the new item will be duplicated with the same one.
        final int addNewItem = 1;
        final int changeExistentItem = 2;
        int whatToDo = addNewItem;

        PostingItemTO sameItem = postingsService.getSamePostingItem(newPostingItem.getPosting().getId(), newPostingItem.getDetailBatch().getId(), null);
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
            PropertiesForm prop = new PostingItemForm(newPostingItem, true, false);
            if (Dialogs.runProperties(prop)) {
                postingsService.addItemToPosting(newPostingItem);
                return true;
            }
            return false;
        } else if (whatToDo == changeExistentItem) {
            //Changing existent order item instead of adding new one.
            PropertiesForm prop = new PostingItemForm(sameItem, true, true);
            if (Dialogs.runProperties(prop)){
                postingsService.savePostingItem(sameItem);
            }
            return false; //Little hack to prevent report framework from adding new item.
        } else {
            throw new RuntimeException("AddItemToOrderCommand.onAddNewDetailItem: unexpected user choice of action.");
        }
    }
}