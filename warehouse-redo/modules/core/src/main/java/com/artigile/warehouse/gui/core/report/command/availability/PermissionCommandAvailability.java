/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.command.availability;

import com.artigile.warehouse.bl.admin.UserService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.utils.SpringServiceContext;

/**
 * @author Shyrik, 20.12.2008
 */
public class PermissionCommandAvailability implements AvailabilityStrategy {
    /**
     * Permission, needed to for the command to be available.
     */
    private PermissionType permission;

    public PermissionCommandAvailability(PermissionType permission) {
        this.permission = permission;
    }

    @Override
    public boolean isAvailable(ReportCommandContext context) {
        UserService userService = SpringServiceContext.getInstance().getUserService();
        return userService.checkPermission(WareHouse.getUserSession().getUser().getId(), permission);
    }
}
