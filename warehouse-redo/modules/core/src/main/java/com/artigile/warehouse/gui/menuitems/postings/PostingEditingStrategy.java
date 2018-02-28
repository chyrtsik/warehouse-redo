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
import com.artigile.warehouse.bl.postings.PostingService;
import com.artigile.warehouse.bl.warehouse.StoragePlaceService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.postings.PostingState;
import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.plugin.Plugin;
import com.artigile.warehouse.gui.core.print.PrintingPreparator;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.postings.items.PostingItemsEditor;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;
import com.artigile.warehouse.utils.dto.postings.PostingTO;
import com.artigile.warehouse.utils.dto.postings.PostingTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 02.02.2009
 */
public class PostingEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreatePostingCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        boolean singleItemSelected = context.getCurrentReportItems().size() == 1;

        if (singleItemSelected){
            //Posting printing.
            commands.add(new PrintPreviewCurrentReportItemCommand(PrintTemplateType.TEMPLATE_POSTING, getPrintAvailability(), getPrintingPreparator()));
            commands.add(new PrintCurrentReportItemCommand(PrintTemplateType.TEMPLATE_POSTING, getPrintAvailability(), getPrintingPreparator()));

            //Commands for editing posting content.
            ReportCommand editPostingContentCommand = new EditPostingContentCommand();
            commands.setDefaultCommandForRow(editPostingContentCommand);
            commands.add(editPostingContentCommand);
        }

        //Command for creating new posting.
        commands.add(new CreatePostingCommand());

        //Command for deleting selected postings.
        boolean canDeletePosting = true;
        for (Object itemObj : context.getCurrentReportItems()){
            PostingTOForReport posting = (PostingTOForReport)itemObj;
            if (!posting.canBeDeleted()) {
                canDeletePosting = false;
                break;
            }
        }
        if (canDeletePosting){
            commands.add(new DeletePostingCommand());
        }

        //Command for editing posting properties.
        if (singleItemSelected){
            commands.add(new PostingPropertiesCommand());
        }
    }

    private AvailabilityStrategy getPrintAvailability() {
        return new PermissionCommandAvailability(PermissionType.VIEW_POSTING_ITEMS);
    }

    private PrintingPreparator getPrintingPreparator() {
        return new PrintingPreparator() {
            public Object prepareForPrinting(Object objectForPrinting) {
                //Before printing full content of the posting to be printed must be loaded.
                PostingTOForReport postingForReport = (PostingTOForReport) objectForPrinting;
                return getPostingsService().getPostingFullData(postingForReport.getId());
            }
        };
    }

    //==================================== Commands ===============================================
    private class DeletePostingCommand extends DeleteCommand {
        protected DeletePostingCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            //Deleting selected posting
            PostingTOForReport postingToDelete = (PostingTOForReport) deletingItem;
            try {
                getPostingsService().deletePosting(postingToDelete);
            } catch (BusinessException e) {
                throw new ReportCommandException(e);
            }
            return true;
        }
    }

    private class EditPostingContentCommand extends CustomCommand {
        protected EditPostingContentCommand() {
            super(new ResourceCommandNaming("posting.items.edit.command"), new PermissionCommandAvailability(PermissionType.VIEW_POSTING_ITEMS));
        }

        @Override
        protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
            //Opening items of the selected posting.
            PostingTOForReport postingTO = (PostingTOForReport) context.getCurrentReportItem();
            Plugin postingContentEditor = new PostingItemsEditor(postingTO.getId());
            WareHouse.runPlugin(postingContentEditor);
            return true;
        }
    }

    private class PostingPropertiesCommand extends PropertiesCommandBase {
        protected PostingPropertiesCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //Edit posting properties.
            PostingTOForReport postingTOForReport = (PostingTOForReport) editingItem;
            WarehouseTOForReport oldWarehouse = postingTOForReport.getWarehouse();
            PropertiesForm prop = new PostingForm(postingTOForReport, canEditPosting(context));
            boolean finished;
            do {
                finished = true;
                if (Dialogs.runProperties(prop)) {
                    boolean diffWarehouses = oldWarehouse.getId() != postingTOForReport.getWarehouse().getId();
                    if (diffWarehouses) {
                        boolean confirmChangeWarehouse = MessageDialogs.showConfirm(I18nSupport.message("posting.properties.confirm.title"),
                                I18nSupport.message(
                                        "posting.properties.confirm.warehouse.changed",
                                        oldWarehouse.getName(),
                                        postingTOForReport.getWarehouse().getName()
                                )
                        );
                        if (!confirmChangeWarehouse) {
                            finished = false;
                        }
                    }

                    if (finished) {
                        PostingService postingService = getPostingsService();
                        postingService.savePosting(postingTOForReport);
                        if (diffWarehouses) {
                            PostingTO postingFull = postingService.getPostingFullData(postingTOForReport.getId());

                            SpringServiceContext serviceContext = SpringServiceContext.getInstance();
                            StoragePlaceService storagePlaceService = serviceContext.getStoragePlaceService();
                            StoragePlaceTO storagePlaceFull = storagePlaceService.get(postingTOForReport.getDefaultStoragePlace().getId());
                            List<PostingItemTO> items = postingFull.getItems();
                            for (PostingItemTO item : items) {
                                item.setStoragePlace(storagePlaceFull);
                            }
                            postingService.saveFullPosting(postingFull);
                        }
                        return true;
                    }
                }
            } while (!finished);
            return false;
        }
    }

    //================================== Helpers ========================================
    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_POSTING_LIST);
    }

    private boolean canEditPosting(ReportCommandContext context) {
        //Ability to edit posting properties depends on both user permission and state of the posting to be edited.
        if (getEditAvailability().isAvailable(context)) {
            PostingTOForReport posting = (PostingTOForReport) context.getCurrentReportItem();
            return posting.getState().equals(PostingState.CONSTRUCTION);
        }
        return false;
    }

    private PostingService getPostingsService() {
        return SpringServiceContext.getInstance().getPostingsService();
    }
}
