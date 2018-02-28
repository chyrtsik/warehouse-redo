/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.inventorization;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.inventorization.InventorizationTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 29.09.2009
 */

/**
 * List of all inventorizations performed.
 */
public class InventorizationList extends ReportDataSourceBase {
    @Override
    public String getReportTitle() {
        return I18nSupport.message("inventorization.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(InventorizationTOForReport.class);

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.list.number"), "number"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.list.type"), "type.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.list.state"), "state.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.list.processed.state"), "processedState"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.list.result"), "result.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.list.createDate"), "createDate"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.list.createUser"), "createUser.displayName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.list.closeDate"), "closeDate"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.list.closeUser"), "closeUser.displayName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.list.warehouse"), "warehouse.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.list.notice"), "notice"));

        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new InventorizationEditingStrategy();
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getInventorizationService().getAllInventorizations();
    }
}
