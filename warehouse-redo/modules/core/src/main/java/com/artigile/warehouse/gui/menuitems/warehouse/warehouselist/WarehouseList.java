/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.warehouse.warehouselist;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Borisok V.V., 24.12.2008
 */
public class WarehouseList extends ReportDataSourceBase {
    @Override
    public String getReportTitle() {
        return I18nSupport.message("warehouse.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(WarehouseTOForReport.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehouse.list.name"), "name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehouse.list.address"), "address"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehouse.list.owner"), "owner.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehouse.list.responsible"), "responsible.fullName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehouse.list.notice"), "notice"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehouse.list.usualPrinter"), "usualPrinter"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehouse.list.stickerPrinter"), "stickerPrinter"));
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new WarehouseEditingStrategy();
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getWarehouseService().getAllForReport();
    }
}
