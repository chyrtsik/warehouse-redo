/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.reserves;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.TableFramePlugin;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class WarehouseBatchReservesCommand extends CustomCommand {

    public WarehouseBatchReservesCommand() {
        super(new ResourceCommandNaming("warehousebatch.command.reserveList"),
                new PermissionCommandAvailability(PermissionType.VIEW_DETAIL_BATCH_RESERVES));
    }

    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        WareHouse.runPlugin(new TableFramePlugin(new DetailBatchReservesList((WarehouseBatchTO) context.getCurrentReportItem())));
        return true;
    }
}
