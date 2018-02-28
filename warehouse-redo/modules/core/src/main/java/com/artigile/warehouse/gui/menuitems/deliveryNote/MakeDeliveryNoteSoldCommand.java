/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.deliveryNote;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.finance.PaymentService;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteTOForReport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 11.03.2010
 */

/**
 * Command for marking delivery note items as sold to the contractor.
 */
public class MakeDeliveryNoteSoldCommand extends CustomCommand {
    protected MakeDeliveryNoteSoldCommand() {
        super(new ResourceCommandNaming("deliveryNote.command.makeSold"), DeliveryNoteEditingStrategy.getEditAvailability());
    }

    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        List<DeliveryNoteTOForReport> deliveryNotes = new ArrayList<DeliveryNoteTOForReport>();
        List<Long> deliveryNoteIds = new ArrayList<Long>();
        for (Object deliveryNoteObj : context.getCurrentReportItems()) {
            DeliveryNoteTOForReport deliveryNote = (DeliveryNoteTOForReport)deliveryNoteObj;
            deliveryNotes.add(deliveryNote);
            deliveryNoteIds.add(deliveryNote.getId());
        }
        MakeDeliveryNotesSoldForm prop = new MakeDeliveryNotesSoldForm(deliveryNotes);
        if (Dialogs.runProperties(prop)){
            //Make payment for chosen delivery notes.
            PaymentService paymentService = SpringServiceContext.getInstance().getPaymentService();
            try {
                paymentService.acceptIncomePayment(
                    prop.getMadePayment(), prop.getPaymentCurrency().getId(),
                    prop.getContractor().getId(), deliveryNoteIds, prop.getNotice()
                );
            } catch (BusinessException e) {
                throw new ReportCommandException(e);
            }
        }
        return false;
    }
}
