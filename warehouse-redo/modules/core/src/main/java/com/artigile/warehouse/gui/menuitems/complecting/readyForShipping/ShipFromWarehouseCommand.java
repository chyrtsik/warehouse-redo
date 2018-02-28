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
 * @author Shyrik, 19.12.2009
 */

/**
 * Command, that performes shipping from warehouse.
 */
public class ShipFromWarehouseCommand extends CustomCommand {
    public ShipFromWarehouseCommand(AvailabilityStrategy availability) {
        super(new ResourceCommandNaming("readyForShippingFromWarehouse.command.shipFromWarehouse"), availability);
    }

    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        //Prepairs list of tasks to be shipped.
        List<Long> taskIds = new ArrayList<Long>();
        for (Object itemObj : context.getCurrentReportItems()){
            ComplectingTaskTO task = (ComplectingTaskTO)itemObj;
            taskIds.add(task.getId());
        }

        //Try to ship wares from warehouse.
        try {
            SpringServiceContext.getInstance().getComplectingTaskService().makeTasksShippedFromWarehouse(taskIds);
            return true;
        } catch (BusinessException e) {
            throw new ReportCommandException(e);
        }
    }
}
