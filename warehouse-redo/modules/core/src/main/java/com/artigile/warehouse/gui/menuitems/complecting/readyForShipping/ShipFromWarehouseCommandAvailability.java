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

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.utils.SpringServiceContext;

/**
 * @author Shyrik, 19.12.2009
 */

/**
 * Strategy for checking availability of complecting from given warehouse by given user.
 * Availability both depends on user having appropriate permission and user having been entered into
 * list of users, who are allowed to perform complecting from giver warehouse.
 */
public class ShipFromWarehouseCommandAvailability implements AvailabilityStrategy {
    /**
     * Permission for shipping wares from warehouse.
     */
    private AvailabilityStrategy permissionAvailability = new PermissionCommandAvailability(PermissionType.EDIT_CREATE_DELIVERY_NOTE);

    private long workerId;

    private long warehouseId;

    public ShipFromWarehouseCommandAvailability(long warehouseId, long workerId) {
        this.warehouseId = warehouseId;
        this.workerId = workerId;
    }

    @Override
    public boolean isAvailable(ReportCommandContext context) {
        return permissionAvailability.isAvailable(context) &&
               SpringServiceContext.getInstance().getWarehouseService().getAllowedUserIds(warehouseId).contains(workerId);
    }
}
