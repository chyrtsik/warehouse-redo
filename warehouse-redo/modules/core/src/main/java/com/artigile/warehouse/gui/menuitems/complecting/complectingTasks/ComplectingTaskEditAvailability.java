/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.complecting.complectingTasks;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

/**
 * @author Shyrik, 08.05.2009
 */

/**
 * Strategy for checking availability of complecting from given warehouse by given user.
 * Availability both depends on user having appropriate permission and user having been entered into
 * list of users, who are allowed to perform complecting from giver warehouse.
 */
public class ComplectingTaskEditAvailability implements AvailabilityStrategy {
    private AvailabilityStrategy permissionAvailability = new PermissionCommandAvailability(PermissionType.EDIT_COMPLECTING_TASKS);

    private UserTO worker;

    private WarehouseTOForReport warehouse;

    public ComplectingTaskEditAvailability(UserTO worker, WarehouseTOForReport warehouse) {
        this.worker = worker;
        this.warehouse = warehouse;
    }

    public void setWarehouse(WarehouseTOForReport warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public boolean isAvailable(ReportCommandContext context) {
        if (worker == null || warehouse == null){
            return false;
        }
        else{
            return permissionAvailability.isAvailable(context) &&
                   SpringServiceContext.getInstance().getWarehouseService().getAllowedUsers(warehouse.getId()).contains(worker);
        }
    }
}
