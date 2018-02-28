/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.types;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailTypeTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 14.12.2008
 */
public class DetailTypesList extends ReportDataSourceBase {
    @Override
    public String getReportTitle() {
        return I18nSupport.message("details.types.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(DetailTypeTOForReport.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("details.types.list.name"), "name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("details.types.list.nameTemplate"), "nameTemplate"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("details.types.list.nameInPrice"), "nameInPrice"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("details.types.list.miscInPrice"), "miscInPrice"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("details.types.list.typeInPrice"), "typeInPrice"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("details.types.list.description"), "description"));
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new DetailTypesEditingStrategy();
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getDetailTypesService().getAllDetailTypes();
    }
}
