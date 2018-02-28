/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.history;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.TableFramePlugin;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;

/**
 * Command for displaying history of detail batch specified.
 *
 * @author Aliaksandr.Chyrtsik, 08.12.11
 */
public class DetailBatchHistoryCommand extends CustomCommand {
    public DetailBatchHistoryCommand() {
        super(new ResourceCommandNaming("detail.batches.command.movementHistory"),
              new PermissionCommandAvailability(PermissionType.VIEW_DETAIL_BATCH_HISTORY));
    }

    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        DetailBatchTO detailBatchTO = (DetailBatchTO)context.getCurrentReportItem();
        WareHouse.runPlugin(new TableFramePlugin(new DetailBatchHistoryList(detailBatchTO)));
        return true;
    }
}
