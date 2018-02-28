/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.controller;

import com.artigile.warehouse.gui.core.plugin.FramePlugin;

import javax.swing.*;

/**
 * @author Shyrik, 08.01.2009
 */

/**
 * Table report plugin launcher (used for editing table of items).
 */
public class TableFramePlugin extends FramePlugin {

    private TableReport tableReport;

    public TableFramePlugin(ReportDataSource dataSource){
        tableReport = new TableReport(dataSource, this);
    }

    @Override
    public String getTitle() {
        return tableReport.getReportTitle();
    }

    @Override
    public JPanel getContentPanel() {
        return tableReport.getContentPanel();
    }

    @Override
    public String getFrameId() {
        return tableReport.getReportDataSource().getReportMajor();
    }
}
