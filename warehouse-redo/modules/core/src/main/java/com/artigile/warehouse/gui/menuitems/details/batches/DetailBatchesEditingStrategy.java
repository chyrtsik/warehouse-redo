/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.batches;

import com.artigile.warehouse.bl.detail.DetailBatchService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.core.print.PrintingPreparator;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.details.batches.command.CreateDetailBatchCommand;
import com.artigile.warehouse.gui.menuitems.details.history.DetailBatchHistoryCommand;
import com.artigile.warehouse.gui.menuitems.details.reserves.DetailBatchReservesCommand;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;

import java.util.Arrays;

/**
 * @author Shyrik, 26.12.2008
 */
public class DetailBatchesEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateDetailBatchCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        boolean singleItemSelected = context.getCurrentReportItems().size() == 1;
        if (singleItemSelected){
            commands.add(new CreateDetailBatchAsCopyCommand());
        }

        commands.add(new CreateDetailBatchCommand());
        commands.add(new DeleteDetailBatchCommand());

        if (singleItemSelected){
            commands.add(new OpenDetailBatchCommand());
            commands.add(new DetailBatchHistoryCommand());
            commands.add(new DetailBatchReservesCommand());
            commands.add(new PrintPreviewCurrentReportItemCommand(PrintTemplateType.TEMPLATE_DETAIL_BATCHES_LIST, getPrintAvailability(), getPrintPreparator()));
            commands.add(new PrintCurrentReportItemCommand(PrintTemplateType.TEMPLATE_DETAIL_BATCHES_LIST, getPrintAvailability(), getPrintPreparator()));
        }
    }

    private PrintingPreparator getPrintPreparator() {
        return new PrintingPreparator() {
            @Override
            public Object prepareForPrinting(Object objectForPrinting) {
                return new DetailBatchesPrintableObject(Arrays.asList((DetailBatchTO)objectForPrinting));
            }
        };
    }

    //==================== Commands ===========================================
    protected class CreateDetailBatchAsCopyCommand extends CreateCopyCommand {
        protected CreateDetailBatchAsCopyCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            DetailBatchTO detailBatch = new DetailBatchTO();
            detailBatch.copyFrom((DetailBatchTO)context.getCurrentReportItem());
            detailBatch.setId(0);
            detailBatch.setSellPrice(null);
            detailBatch.setNeedRecalculate(false);

            PropertiesForm prop = new DetailBatchForm(detailBatch, true, DetailBatchForm.PropertiesType.CreateAsCopy);
            if (Dialogs.runProperties(prop)) {
                //Saving new detail batch.
                getDetailBatchesService().saveDetailBatch(detailBatch);
                return PriceUpdater.updatePrices(detailBatch);
            }
            return null;
        }
    }

    protected class DeleteDetailBatchCommand extends DeleteCommand {
        public DeleteDetailBatchCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            //Deleting choosen detail batch.
            DetailBatchTO detailBatch = (DetailBatchTO) deletingItem;
            getDetailBatchesService().deleteDetailBatch(detailBatch.getId());
            return true;
        }
    }

    protected class OpenDetailBatchCommand extends PropertiesCommandBase {
        public OpenDetailBatchCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //Edit properties of the details batch.
            DetailBatchTO detailBatch = (DetailBatchTO) editingItem;
            PropertiesForm prop = new DetailBatchForm(detailBatch, getEditAvailability().isAvailable(context), DetailBatchForm.PropertiesType.Properties);
            if (Dialogs.runProperties(prop)) {
                //Saving edited detail batch.
                getDetailBatchesService().saveDetailBatch(detailBatch);
                PriceUpdater.updatePrices(detailBatch);
                return true;
            }
            return false;
        }
    }

    //====================================== Helpers ========================================
    public static AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_DETAIL_BATCHES_LIST);
    }

    private AvailabilityStrategy getPrintAvailability() {
        return new PredefinedCommandAvailability(true);
    }

    protected DetailBatchService getDetailBatchesService() {
        return SpringServiceContext.getInstance().getDetailBatchesService();
    }
}
