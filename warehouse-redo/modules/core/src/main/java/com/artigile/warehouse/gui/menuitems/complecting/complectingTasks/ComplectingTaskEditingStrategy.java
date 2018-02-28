/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.complecting.complectingTasks;

import com.artigile.warehouse.domain.complecting.ComplectingTaskState;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.complecting.ComplectingTaskTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

import java.util.List;

/**
 * @author Shyrik, 10.05.2009
 */
public class ComplectingTaskEditingStrategy implements ReportEditingStrategy {
    private WarehouseTOForReport warehouse;
    private UserTO worker;

    public ComplectingTaskEditingStrategy(WarehouseTOForReport warehouse, UserTO worker) {
        this.warehouse = warehouse;
        this.worker = worker;
    }

    public void setWarehouse(WarehouseTOForReport warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        //No commands are supported.
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        //Find out, what commands to show.
        boolean showBeginComplecting = true;
        boolean showCancelComplecting = true;
        boolean showReadyForShippingCommand = true;

        List<ComplectingTaskTO> tasks = (List<ComplectingTaskTO>)context.getCurrentReportItems();
        for (ComplectingTaskTO task : tasks){
            if(task.getState() != ComplectingTaskState.NOT_PROCESSED){
                showBeginComplecting = false;
            }
            if (task.getState() != ComplectingTaskState.PROCESSING){
                showCancelComplecting = false;
            }
            if (!task.isReadyForShipping()){
                showReadyForShippingCommand = false;
            }
        }

        if (showBeginComplecting){
            commands.add(new BeginComplectingCommand(new ComplectingTaskEditAvailability(worker, warehouse)));
        }

        if (showCancelComplecting){
            commands.add(new CancelComplectingCommand(new ComplectingTaskEditAvailability(worker, warehouse)));
        }

        if (showReadyForShippingCommand){
            commands.add(new ReadyForShippingCommand(new ComplectingTaskEditAvailability(worker, warehouse)));
        }
    }
}
