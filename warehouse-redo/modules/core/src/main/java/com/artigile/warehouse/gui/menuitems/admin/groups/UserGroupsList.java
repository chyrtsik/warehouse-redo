/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.admin.groups;

import com.artigile.warehouse.bl.admin.UserGroupService;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserGroupTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 07.12.2008
 */

/**
 * List of user groups.
 */
public class UserGroupsList extends ReportDataSourceBase {
    @Override
    public String getReportTitle() {
        return I18nSupport.message("user.groups.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(UserGroupTO.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("table.column.name"), "name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("table.column.description"), "description"));
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new UserGroupEditingStrategy();
    }

    @Override
    public List getReportData() {
        UserGroupService groupService = SpringServiceContext.getInstance().getUserGroupService();
        return new ArrayList<UserGroupTO>(groupService.getAllGroups());
    }
}
