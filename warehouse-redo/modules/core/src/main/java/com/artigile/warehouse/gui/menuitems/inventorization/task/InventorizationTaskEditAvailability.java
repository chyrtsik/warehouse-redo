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

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

/**
 * @author Borisok V.V., 03.10.2009
 */
public class InventorizationTaskEditAvailability implements AvailabilityStrategy {
    private AvailabilityStrategy permissionAvailability = new PermissionCommandAvailability(PermissionType.EDIT_INVENTORIZATION_TASKS);

    private UserTO worker;

    private WarehouseTOForReport warehouse;

    public InventorizationTaskEditAvailability(UserTO worker, WarehouseTOForReport warehouse) {
        this.worker = worker;
        this.warehouse = warehouse;
    }

    public void setWarehouse(WarehouseTOForReport warehouse) {
        this.warehouse = warehouse;
    }

    public boolean isAvailable(ReportCommandContext context) {
        if (worker == null || warehouse == null){
            return false;
        }
        return permissionAvailability.isAvailable(context) &&
              SpringServiceContext.getInstance().getWarehouseService().getAllowedUsers(warehouse.getId()).contains(worker);
    }
}
