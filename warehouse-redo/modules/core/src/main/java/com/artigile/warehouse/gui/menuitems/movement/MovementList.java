/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.movement;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.movement.MovementTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 21.11.2009
 */

/**
 * List of all ware movements.
 */
public class MovementList extends ReportDataSourceBase {
    @Override
    public String getReportTitle() {
        return I18nSupport.message("movement.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(MovementTOForReport.class);

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.list.number"), "number"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.list.fromWarehouse"), "fromWarehouse.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.list.toWarehouse"), "toWarehouse.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.list.state"), "state.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.list.result"), "result.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.list.createUser"), "createUser.displayName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.list.createDate"), "createDate"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.list.beginDate"), "beginDate"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.list.endDate"), "endDate"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.list.notice"), "notice"));
        
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new MovementEditingStrategy();
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getMovementService().getAllMovements();
    }
}
