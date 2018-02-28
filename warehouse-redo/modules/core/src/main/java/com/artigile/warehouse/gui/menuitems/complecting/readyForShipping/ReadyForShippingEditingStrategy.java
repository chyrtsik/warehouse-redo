/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.complecting.readyForShipping;

import com.artigile.warehouse.domain.complecting.ComplectingTaskState;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategyAdapter;
import com.artigile.warehouse.utils.dto.complecting.ComplectingTaskTO;

import java.util.List;

/**
 * @author Shyrik, 19.12.2009
 */

/**
 * Editing strategy for list of tasks, that are ready for shipping from warehouse.
 */
public class ReadyForShippingEditingStrategy extends ReportEditingStrategyAdapter {
    /**
     * Warehouse, which items is being edited.
     */
    private long warehouseId;

    /**
     * User, who performs operations.
     */
    private long workerId;

    /**
     * @param warehouseId
     * @param workerId
     */
    public ReadyForShippingEditingStrategy(long warehouseId, long workerId) {
        this.warehouseId = warehouseId;
        this.workerId = workerId;
    }

    public void setWarehouseId(long warehouseId) {
        this.warehouseId = warehouseId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        //Determing, which commands to show.
        boolean showShipCommand = true;

        List<ComplectingTaskTO> tasks = (List<ComplectingTaskTO>)context.getCurrentReportItems();
        for (ComplectingTaskTO task : tasks){
            if(task.getState() != ComplectingTaskState.READY_FOR_SHIPPING){
                showShipCommand = false;
                break;
            }
        }

        if (showShipCommand){
            commands.add(new ShipFromWarehouseCommand(new ShipFromWarehouseCommandAvailability(warehouseId, workerId)));
        }
    }
}
