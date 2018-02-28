/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.postings.items;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.postings.PostingState;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.MultipleAndCriteriaCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.postings.items.command.CreateDetailBatchForUnclassifiedItemCommand;
import com.artigile.warehouse.gui.menuitems.postings.items.command.ImportPostingItemsCommand;
import com.artigile.warehouse.gui.menuitems.postings.items.command.SpecifyDetailBatchForUnclassifiedItemCommand;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;
import com.artigile.warehouse.utils.dto.postings.PostingType;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;

/**
 * @author Shyrik, 05.02.2009
 */
public class PostingItemsEditingStrategy implements ReportEditingStrategy {
    private long postingId;

    public PostingItemsEditingStrategy(long postingId) {
        this.postingId = postingId;
    }

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new ImportPostingItemsCommand(postingId));
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        //Command for deleting posting items.
        boolean canDeletePostingItem = true;
        for (Object itemObj : context.getCurrentReportItems()){
            PostingItemTO postingItem = (PostingItemTO)itemObj;
            if (!postingItem.getPosting().getState().equals(PostingState.CONSTRUCTION)) {
                canDeletePostingItem = false;
                break;
            }
        }
        if (canDeletePostingItem){
            commands.add(new DeletePostingItemCommand());
        }
        //Command for editing posting item's properties.
        if (context.getCurrentReportItems().size() == 1){
            PostingItemTO currentItem = (PostingItemTO)context.getCurrentReportItem();
            if (currentItem.isUnclassifiedItem()){
                commands.add(new CreateDetailBatchForUnclassifiedItemCommand(getEditAvailability()));
                commands.add(new SpecifyDetailBatchForUnclassifiedItemCommand(getEditAvailability()));
            }
            else {
                //Ordinary posting item (mapped to detail batch).
                commands.add(new EditPostingItemCommand());
                // Correct product quantity after posting completion.
                PostingItemTO postingItem = (PostingItemTO) context.getCurrentReportItem();
                if (postingItem.getPosting().getState() == PostingState.COMPLETED) {
                    commands.add(new EditCompletedPostingItemQuantityCommand());
                }
            }
        }

        commands.add(new ImportPostingItemsCommand(postingId));
    }

    //==================== Commands ===========================================
    private class DeletePostingItemCommand extends DeleteCommand {
        protected DeletePostingItemCommand() {
            super(new MultipleAndCriteriaCommandAvailability(getEditAvailability(),
                    new AvailabilityStrategy() {
                        @Override
                        public boolean isAvailable(ReportCommandContext context) {
                            PostingItemTO postingItem = (PostingItemTO) context.getCurrentReportItem();
                            return postingItem != null && !postingItem.getPosting().getPostingType().equals(PostingType.FROM_PURCHASE);
                        }
                    }));
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            //Deleting choosed posting item.
            PostingItemTO postingItem = (PostingItemTO) deletingItem;
            SpringServiceContext.getInstance().getPostingsService().deleteItemFromPosting(postingItem.getId());
            return true;
        }
    }

    private class EditPostingItemCommand extends PropertiesCommandBase {
        protected EditPostingItemCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //Edit properties of the posting item.
            PostingItemTO postingItem = (PostingItemTO) editingItem;
            boolean canEdit = getEditAvailability().isAvailable(context)
                    && !postingItem.getPosting().getPostingType().equals(PostingType.FROM_PURCHASE);
            PropertiesForm prop = new PostingItemForm(postingItem, canEdit, false);
            if (Dialogs.runProperties(prop)) {
                //Saving edited posting item.
                SpringServiceContext.getInstance().getPostingsService().savePostingItem(postingItem);
                return true;
            }
            return false;
        }
    }

    /**
     * Correct product quantity after posting completion.
     */
    private class EditCompletedPostingItemQuantityCommand extends CustomCommand {

        protected EditCompletedPostingItemQuantityCommand() {
            super(new ResourceCommandNaming("posting.items.command.change.completed.item.quantity"),
                    new PermissionCommandAvailability(PermissionType.EDIT_COMPLETED_POSTING_ITEM_QUANTITY));
        }

        @Override
        protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
            return Dialogs.runProperties(new EditCompletedPostingItemQuantityForm((PostingItemTO) context.getCurrentReportItem()));
        }
    }

    //=================================== Helpers =============================================
    static AvailabilityStrategy getEditAvailability() {
        //Availability of editing posting items depends on user permissions and state of the posting.
        return new PostingItemEditingAvailability();
    }
}
