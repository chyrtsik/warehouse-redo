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

import com.artigile.warehouse.domain.complecting.UncomplectingTaskState;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.menuitems.complecting.complectingTasks.ComplectingTaskEditAvailability;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.complecting.UncomplectingTaskTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

import java.util.List;

/**
 * @author Shyrik, 13.06.2009
 */
public class UncomplectingTaskEditingStrategy implements ReportEditingStrategy {
    WarehouseTOForReport warehouse;
    UserTO worker;

    public UncomplectingTaskEditingStrategy(UserTO worker, WarehouseTOForReport warehouse) {
        this.worker = worker;
        this.warehouse = warehouse;
    }

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        //No commands are supported.
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        List<UncomplectingTaskTO> tasks = (List<UncomplectingTaskTO>)context.getCurrentReportItems();

        //Find out, what commands to show.
        boolean showDoneCommand = true;
        boolean showRevertCommand = true;

        for (UncomplectingTaskTO task : tasks){
            if(task.getState() != UncomplectingTaskState.NOT_PROCESSED){
                showDoneCommand = false;
            }
            if (task.getState() != UncomplectingTaskState.PROCESSED){
                showRevertCommand = false;
            }
        }

        if (showDoneCommand){
            commands.add(new ChangeUncomplectingTaskStateCommand(
                new ResourceCommandNaming("uncomplectingTask.list.command.done"),
                new ComplectingTaskEditAvailability(worker, warehouse),
                UncomplectingTaskState.PROCESSED, worker)
            );
        }
        if (showRevertCommand){
            commands.add(new ChangeUncomplectingTaskStateCommand(
                new ResourceCommandNaming("uncomplectingTask.list.command.revertDone"),
                new ComplectingTaskEditAvailability(worker, warehouse),
                UncomplectingTaskState.NOT_PROCESSED, worker)
            );
        }
    }
}
