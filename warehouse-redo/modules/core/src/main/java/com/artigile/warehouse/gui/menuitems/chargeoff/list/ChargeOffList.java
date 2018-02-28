/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.chargeoff.list;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.chargeoff.ChargeOffTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 09.10.2009
 */
public class ChargeOffList extends ReportDataSourceBase{
    @Override
    public String getReportTitle() {
        return I18nSupport.message("chargeOff.list.title");
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new ChargeOffEditingStrategy();
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getChargeOffService().getAllChargeOffs();
    }

    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(ChargeOffTOForReport.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("chargeOff.list.number"), "number",
                RightMiddleAlignStyleFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("chargeOff.list.warehouse"), "warehouse.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("chargeOff.list.performer"), "performer.displayName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("chargeOff.list.performDate"), "performDate"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("chargeOff.list.notice"), "notice"));
        return reportInfo;
    }
}
