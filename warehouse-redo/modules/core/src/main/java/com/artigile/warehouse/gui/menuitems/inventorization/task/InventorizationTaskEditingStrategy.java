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

import com.artigile.warehouse.domain.inventorization.task.InventorizationTaskState;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.inventorization.task.InventorizationTaskTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

import java.util.List;

/**
 * @author Borisok V.V., 03.10.2009
 */
public class InventorizationTaskEditingStrategy implements ReportEditingStrategy {
    private WarehouseTOForReport warehouse;
    private UserTO worker;

    public InventorizationTaskEditingStrategy(WarehouseTOForReport warehouse, UserTO worker) {
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
        List<InventorizationTaskTO> tasks = (List<InventorizationTaskTO>)context.getCurrentReportItems();

        //Find out, what commands to show.
        boolean showBeginInventorization = true;
        boolean showCancelInventorization = true;
        boolean showCompleteInventorization = true;

        for (InventorizationTaskTO task : tasks){
            if(!task.getState().equals(InventorizationTaskState.NOT_PROCESSED)){
                showBeginInventorization = false;
            }
            if (!task.getState().equals(InventorizationTaskState.IN_PROCESS)){
                showCancelInventorization = false;
                showCompleteInventorization = false;
            }
        }

        if (showBeginInventorization) {
            commands.add(new BeginInventorizationTaskCommand(new InventorizationTaskEditAvailability(worker, warehouse)));
        }
        if (showCancelInventorization) {
            commands.add(new CancelInventorizationTaskCommand(new InventorizationTaskEditAvailability(worker, warehouse)));
        }
        if (showCompleteInventorization){
            commands.add(new CompleteInventorizationTaskCommand(new InventorizationTaskEditAvailability(worker, warehouse)));
        }
    }
}
