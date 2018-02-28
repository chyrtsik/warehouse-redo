/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.complecting.complectingTasks;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.complecting.ComplectingTaskTO;

import java.util.List;

/**
 * @author Shyrik, 10.05.2009
 */

/**
 * Command for beginning the process of complecting wares ordered.
 */
public class BeginComplectingCommand extends CustomCommand {
    protected BeginComplectingCommand(AvailabilityStrategy availability) {
        super(new ResourceCommandNaming("complectingTask.worker.list.command.complect"), availability);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        try {
            SpringServiceContext.getInstance().getComplectingTaskService().beginComplecting((List<ComplectingTaskTO>)context.getCurrentReportItems());
        } catch (BusinessException e) {
            throw new ReportCommandException(e); 
        }
        return true;
    }
}
