/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.complecting.uncomplectingTasks;

import com.artigile.warehouse.bl.complecting.UncomplectingTaskService;
import com.artigile.warehouse.domain.complecting.UncomplectingTaskState;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.naming.NamingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.complecting.UncomplectingTaskTO;

import java.util.List;

/**
 * @author Shyrik, 13.06.2009
 */
/**
 * Command changes state of uncomplecting task to a given one.
 */
public class ChangeUncomplectingTaskStateCommand extends CustomCommand {
    /**
     * New state, that is to be set by this command.
     */
    UncomplectingTaskState newState;

    /**
     * Worker, who executes the command.
     */
    UserTO worker;

    protected ChangeUncomplectingTaskStateCommand(NamingStrategy naming, AvailabilityStrategy availability, UncomplectingTaskState newState, UserTO worker) {
        super(naming, availability);
        this.newState = newState;
        this.worker = worker;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        UncomplectingTaskService taskService = SpringServiceContext.getInstance().getUncomplectingTaskService();
        List<UncomplectingTaskTO> tasks = context.getCurrentReportItems();
        for (UncomplectingTaskTO task : tasks){
            taskService.changeTaskState(task, worker, newState);
        }
        return true;
    }
}
