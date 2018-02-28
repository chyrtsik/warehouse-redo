/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.admin.license.licenses;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.license.LicenseTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * List of application licences.
 *
 * @author Aliaksandr.Chyrtsik, 17.07.11
 */
public class LicensesList extends ReportDataSourceBase{
    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(LicenseTO.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("license.list.licenseType"), "licenseType.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("license.list.beginDate"), "validFromDate"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("license.list.endDate"), "validTillDate"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("license.list.issuedFor"), "issuedFor"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("license.list.description"), "description"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("license.list.appliedByUser"), "appliedByUser"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("license.list.dateApplied"), "dateApplied"));
        return reportInfo;
    }

    @Override
    public String getReportTitle() {
        return I18nSupport.message("license.list.title");
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new LicensesEditingStrategy();
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getLicenseService().getAllLicensesForReport();
    }
}
