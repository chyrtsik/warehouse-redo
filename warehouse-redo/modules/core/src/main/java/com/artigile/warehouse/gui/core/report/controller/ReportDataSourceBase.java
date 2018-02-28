/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.controller;

import com.artigile.warehouse.gui.core.report.model.ReportModel;

/**
 * Base implementation for report data source (table for editing list of items).
 *
 * @author Shyrik, 07.12.2008
 */
public abstract class ReportDataSourceBase implements ReportDataSource {
    /**
     * Owner of the data source (used as helper).
     */
    protected TableReport tableReport;

    /**
     * Minor identifier of the report. Use this identifier, when you want an ability
     * to have more than one report saved headers (for example, when report is used in different
     * places of the application, and in these plases there should be diferent headers).
     */
    private String reportMinor;

    /**
     * Cashed report info.
     */
    private ReportInfo reportInfo;

    public ReportDataSourceBase() {
        this("0");
    }

    protected ReportDataSourceBase(String reportMinor) {
        this.reportMinor = reportMinor;
    }

    @Override
    public ReportInfo getReportInfo() {
        if (reportInfo == null){
            reportInfo = doGetReportInfo();
        }
        return reportInfo;
    }

    /**
     * This method should be implemented by subclasses.
     * @return - ReportInfo of this report.
     */
    protected abstract ReportInfo doGetReportInfo();

    @Override
    public void setTableReport(TableReport tableReport) {
        this.tableReport = tableReport;
    }

    @Override
    public boolean itemBelongsToReport(Object reportItem) {
        //Default implementation considers, that every item of matching type belongs to tableReport.
        //If you need any extra check for making this decision, the should override this method.
        return true;
    }

    @Override
    public Object preProcessItem(Object reportItem) {
        //By default there is no additional processing.
        return reportItem;
    }

    public void refreshData() {
        tableReport.refreshData();
    }

    //======================== Helpers for derived classes ====================================
    protected Object getCurrentReportItem() {
        return tableReport.getCurrentReportItem();
    }

    protected ReportModel getReportModel() {
        return tableReport.getReportModel();
    }

    @Override
    public String getReportMajor() {
        return getClass().getCanonicalName();
    }

    @Override
    public String getReportMinor() {
        return reportMinor;
    }

    @Override
    public void setReportMinor(String reportMinor) {
        this.reportMinor = reportMinor;
    }
}
