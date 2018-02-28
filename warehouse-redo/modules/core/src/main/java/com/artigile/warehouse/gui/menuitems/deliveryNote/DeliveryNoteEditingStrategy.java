/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.deliveryNote;

import com.artigile.warehouse.bl.deliveryNote.DeliveryNoteService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteState;
import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.core.print.PrintingPreparator;
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteTOForReport;

/**
 * @author Shyrik, 03.11.2009
 */
public class DeliveryNoteEditingStrategy implements ReportEditingStrategy {

    //================================== Helpers =====================================
    public static AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_DELIVERY_NOTE_LIST);
    }

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        //Delivery note content command.
        if (context.getCurrentReportItems().size() == 1){
            OpenDeliveryNoteCommand openDeliveryNoteCommand = new OpenDeliveryNoteCommand();
            commands.setDefaultCommandForRow(openDeliveryNoteCommand);
            commands.add(openDeliveryNoteCommand);
            commands.add(new EditDeliveryNoteContentCommand());
        }

        //Delivery note "make shipped" and "sale" commands.
        boolean canMarkShipped = true;
        boolean canMarkSold = true;

        ContractorTO contractor = null;
        for (Object deliveryNoteObj : context.getCurrentReportItems()) {
            DeliveryNoteTOForReport deliveryNote = (DeliveryNoteTOForReport)deliveryNoteObj;
            if (!deliveryNote.getState().equals(DeliveryNoteState.SHIPPING_TO_WAREHOUSE)){
                canMarkShipped = false;
            }
            if (!deliveryNote.getState().equals(DeliveryNoteState.SHIPPING_TO_CONTRACTOR)){
                canMarkSold = false;
            }
            if (contractor == null){
                contractor = deliveryNote.getContractor();
            }
            else if (deliveryNote.getContractor() != null && !deliveryNote.getContractor().equals(contractor)) {
                canMarkSold = false;
            }
        }

        if (canMarkShipped){
            commands.add(new MarkShippedDeliveryNoteCommand());
        }
        if (canMarkSold){
            commands.add(new MakeDeliveryNoteSoldCommand());
        }

        //Create posting command for shipped delivery notes.
        if (context.getCurrentReportItems().size() == 1){
            DeliveryNoteTOForReport deliveryNote = (DeliveryNoteTOForReport)context.getCurrentReportItem();
            if (deliveryNote.getState().equals(DeliveryNoteState.SHIPPED)){
                commands.add(new CreatePostingFromDeliveryNoteCommand());
            }
        }

        commands.add(new PrintCurrentReportItemCommand(PrintTemplateType.TEMPLATE_DELIVERY_NOTE, hasPrintPermission(), preparePringReport()));
        commands.add(new PrintPreviewCurrentReportItemCommand(PrintTemplateType.TEMPLATE_DELIVERY_NOTE, hasPrintPermission(), preparePringReport()));
    }

    private PrintingPreparator preparePringReport() {

        return new PrintingPreparator() {
            @Override
            public Object prepareForPrinting(Object objectForPrinting) {
                 //Before printing full content of the order to be printed must be loaded.
                DeliveryNoteTOForReport orderForReport = (DeliveryNoteTOForReport)objectForPrinting;
                return getDeliveryNoteService().getDeliveryNoteFullData(orderForReport.getId());
            }
        };
    }

    private class OpenDeliveryNoteCommand extends PropertiesCommandBase {

        protected OpenDeliveryNoteCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            DeliveryNoteTOForReport deliveryNoteTO = (DeliveryNoteTOForReport) editingItem;
            PropertiesForm prop = new DeliveryNoteForm(deliveryNoteTO, getEditAvailability().isAvailable(context));
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                getDeliveryNoteService().save(deliveryNoteTO);
                return true;
            }
            return false;
        }
    }

    private PermissionCommandAvailability hasPrintPermission() {
        return new PermissionCommandAvailability(PermissionType.VIEW_DELIVERY_NOTE_CONTENT);
    }

    private DeliveryNoteService getDeliveryNoteService() {
        return SpringServiceContext.getInstance().getDeliveryNoteService();
    }
}
