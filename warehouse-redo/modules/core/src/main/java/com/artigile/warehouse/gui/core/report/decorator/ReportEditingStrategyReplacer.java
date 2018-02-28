/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.decorator;

import com.artigile.warehouse.gui.core.report.controller.ReportDataSource;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.controller.TableReport;

import java.util.List;

/**
 * @author Shyrik, 19.12.2009
 */

/**
 * This class replaces original report editing strategy with given one.
 * Used for reusing the same report with different set of commands.
 */
public class ReportEditingStrategyReplacer implements ReportDataSource {
    /**
     * Original report data source implementation.
     */
    private ReportDataSource originalReportDataSource;

    /**
     * Report editing strategy to be placed instead of original report editing strategy.
     */
    private ReportEditingStrategy newEditingStrategy;

    /**
     * @param originalReportDataSource original report data source.
     * @param newEditingStrategy new editing strategy to be placed instead of original report's editing strategy.
     */
    public ReportEditingStrategyReplacer(ReportDataSource originalReportDataSource, ReportEditingStrategy newEditingStrategy){
        this.originalReportDataSource = originalReportDataSource;
        this.newEditingStrategy = newEditingStrategy;
    }

    @Override
    public void setTableReport(TableReport report) {
        originalReportDataSource.setTableReport(report);
    }

    @Override
    public String getReportTitle() {
        return originalReportDataSource.getReportTitle();
    }

    @Override
    public ReportInfo getReportInfo() {
        return originalReportDataSource.getReportInfo();
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return newEditingStrategy;
    }

    @Override
    public List getReportData() {
        return originalReportDataSource.getReportData();
    }

    @Override
    public boolean itemBelongsToReport(Object reportItem) {
        return originalReportDataSource.itemBelongsToReport(reportItem);
    }

    @Override
    public Object preProcessItem(Object reportItem) {
        return originalReportDataSource.preProcessItem(reportItem);
    }

    @Override
    public String getReportMajor() {
        return originalReportDataSource.getReportMajor();
    }

    @Override
    public String getReportMinor() {
        return originalReportDataSource.getReportMinor();
    }

    @Override
    public void setReportMinor(String reportMinor) {
        originalReportDataSource.setReportMinor(reportMinor);
    }
}
