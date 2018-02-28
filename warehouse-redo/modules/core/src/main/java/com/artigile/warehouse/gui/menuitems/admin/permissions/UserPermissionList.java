/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.admin.permissions;

import com.artigile.warehouse.bl.admin.UserService;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserPermissionTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 07.12.2008
 */
public class UserPermissionList extends ReportDataSourceBase {
    @Override
    public String getReportTitle() {
        return I18nSupport.message("user.rights.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(UserPermissionTO.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("table.column.name"), "name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("table.column.description"), "description"));
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new UserPermissionEditingStrategy();
    }

    @Override
    public List getReportData() {
        UserService userService = SpringServiceContext.getInstance().getUserService();
        return userService.getAllPermissions();
    }
}
