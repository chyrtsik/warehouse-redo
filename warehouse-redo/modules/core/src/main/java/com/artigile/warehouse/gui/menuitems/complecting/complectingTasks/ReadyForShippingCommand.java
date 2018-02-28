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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 16.11.2009
 */
public class ReadyForShippingCommand extends CustomCommand {
    public ReadyForShippingCommand(AvailabilityStrategy availabilityStrategy) {
        super(new ResourceCommandNaming("complectingTask.worker.list.command.readyForShipping"), availabilityStrategy);
    }

    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        //Make selected complecting tasks ready for shipping.
        List<Long> taskIds = new ArrayList<Long>();
        for (Object taskObj : context.getCurrentReportItems()){
            ComplectingTaskTO taskTO = (ComplectingTaskTO)taskObj;
            taskIds.add(taskTO.getId());
        }

        try {
            SpringServiceContext.getInstance().getComplectingTaskService().makeReadyForShipping(taskIds);
        } catch (BusinessException e) {
            throw new ReportCommandException(e);
        }
        return true;
    }
}
