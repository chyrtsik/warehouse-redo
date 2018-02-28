/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.basedirectory.measureunit;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

public class MeasureUnitList extends ReportDataSourceBase {
    @Override
    public String getReportTitle() {
        return I18nSupport.message("basedirectory.measureunit.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(MeasureUnitTO.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("basedirectory.measureunit.list.sign"), "sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("basedirectory.measureunit.list.name"), "name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("basedirectory.measureunit.list.code"), "code"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("basedirectory.measureunit.list.notice"), "notice"));
        reportInfo.setRowStyleFactory(new MeasureUnitStyleFactory());
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new MeasureUnitEditingStrategy();
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getMeasureUnitService().getAll();
    }
}
