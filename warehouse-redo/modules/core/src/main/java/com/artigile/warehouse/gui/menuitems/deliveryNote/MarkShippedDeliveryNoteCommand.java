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
 * Marks delivery notes as have been shipped.
 */
public class MarkShippedDeliveryNoteCommand extends CustomCommand {
    protected MarkShippedDeliveryNoteCommand() {
        super(new ResourceCommandNaming("deliveryNote.command.makeShipped"), DeliveryNoteEditingStrategy.getEditAvailability());
    }

    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        List<Long> deliveryNoteIds = new ArrayList<Long>();
        for (Object deliveryNoteObj : context.getCurrentReportItems()) {
            DeliveryNoteTOForReport deliveryNote = (DeliveryNoteTOForReport)deliveryNoteObj;
            deliveryNoteIds.add(deliveryNote.getId());
        }
        try {
            SpringServiceContext.getInstance().getDeliveryNoteService().markDeliveryNotesListAsShipped(deliveryNoteIds);
        } catch (BusinessException e) {
            throw new ReportCommandException(e);
        }
        return false;
    }
}
