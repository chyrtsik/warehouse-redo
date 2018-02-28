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

import com.artigile.warehouse.gui.core.report.command.ReportCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSource;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.controller.TableReport;

import java.util.List;

/**
 * @author Shyrik, 08.01.2009
 */

/**
 * Decorator, that added to to the given data source additional commands.
 */
public class ReportCommandsDecorator implements ReportDataSource {

    private TableReport tableReport;
    private ReportDataSource decoratedDataSource;
    private ReportCommand newDefaultCommand;
    private ReportCommandList newCommands;

    /**
     * Constructs report data source, decorated with single default command.
     *
     * @param dataSource        - report data source to be decorated.
     * @param newDefaultCommand - new default command of the report.
     */
    public ReportCommandsDecorator(ReportDataSource dataSource, ReportCommand newDefaultCommand) {
        this.decoratedDataSource = dataSource;
        this.newDefaultCommand = newDefaultCommand;
    }

    /**
     * Constructs report data source, decorated with given list of commands command.
     *
     * @param dataSource  - report data source to be decorated.
     * @param newCommands - list of commands to be added to the report.
     */
    public ReportCommandsDecorator(ReportDataSource dataSource, ReportCommandList newCommands) {
        this.decoratedDataSource = dataSource;
        this.newCommands = newCommands;
    }

    @Override
    public void setTableReport(TableReport report) {
        this.tableReport = report;
        decoratedDataSource.setTableReport(report);
    }

    @Override
    public String getReportTitle() {
        return decoratedDataSource.getReportTitle();
    }

    @Override
    public ReportInfo getReportInfo() {
        return decoratedDataSource.getReportInfo();
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        if (newDefaultCommand != null) {
            return new EditingStrategyCommandsDecorator(decoratedDataSource.getReportEditingStrategy(), newDefaultCommand);
        } else {
            return new EditingStrategyCommandsDecorator(decoratedDataSource.getReportEditingStrategy(), newCommands);
        }
    }

    @Override
    public List getReportData() {
        return decoratedDataSource.getReportData();
    }

    @Override
    public boolean itemBelongsToReport(Object reportItem) {
        return decoratedDataSource.itemBelongsToReport(reportItem);
    }

    @Override
    public Object preProcessItem(Object reportItem) {
        return decoratedDataSource.preProcessItem(reportItem);
    }

    @Override
    public String getReportMajor() {
        return decoratedDataSource.getReportMajor();
    }

    @Override
    public String getReportMinor() {
        return decoratedDataSource.getReportMinor();
    }

    @Override
    public void setReportMinor(String reportMinor) {
        decoratedDataSource.setReportMinor(reportMinor);
    }

    //=========================== Helpers ===================================
    public Object getSelectedItem() {
        return tableReport.getCurrentReportItem();
    }
}
