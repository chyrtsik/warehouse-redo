/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.admin.users;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author IoaN, 03.12.2008
 */

/**
 * Report for editing list of users.
 */
public class UsersList extends ReportDataSourceBase {

    @Override
    public String getReportTitle() {
        return I18nSupport.message("user.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(UserTO.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("user.list.login"), "login"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("user.list.first.name"), "firstName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("user.list.last.name"), "lastName"));
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new UserEditingStrategy();
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getUserService().getAllUsers();
    }
}
