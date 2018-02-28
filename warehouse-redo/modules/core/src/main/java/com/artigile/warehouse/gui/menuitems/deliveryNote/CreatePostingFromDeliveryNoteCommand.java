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

import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteState;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandParametersContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.postings.CreatePostingCommand;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteTOForReport;

/**
 * @author Shyrik, 11.03.2010
 */

/**
 * Command for creating posting from delivery note.
 */
public class CreatePostingFromDeliveryNoteCommand extends CustomCommand {
    private CreatePostingCommand createPostingCommand = new CreatePostingCommand();

    protected CreatePostingFromDeliveryNoteCommand() {
        super(new ResourceCommandNaming("deliveryNote.command.createPosting"), new AvailabilityStrategy(){
            AvailabilityStrategy editAvailability = DeliveryNoteEditingStrategy.getEditAvailability();
            @Override
            public boolean isAvailable(ReportCommandContext context) {
                return editAvailability.isAvailable(context) &&
                       ((DeliveryNoteTOForReport)context.getCurrentReportItem()).getState() == DeliveryNoteState.SHIPPED;
            }
        });
    }

    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        if (createPostingCommand.execute(new ReportCommandParametersContext(context.getCurrentReportItem()))){
            context.getReportModel().fireItemDataChanged(context.getCurrentReportItem());
            return true;
        }
        return false;
    }
}
