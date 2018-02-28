/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.inventorization.task;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;

/**
 * @author Shyrik, 18.10.2009
 */
/**
 * Command for completing process of inventorization.
 */
public class CompleteInventorizationTaskCommand extends CustomCommand {
    protected CompleteInventorizationTaskCommand(AvailabilityStrategy availabilty) {
        super(new ResourceCommandNaming("inventorization.task.worker.list.command.inventorization.completed"), availabilty);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        try {
            SpringServiceContext.getInstance().getInventorizationTaskService().completeInventorizationTasks(context.getCurrentReportItems());
        } catch (BusinessException e) {
            throw new ReportCommandException(e);
        }
        return true;
    }
}
