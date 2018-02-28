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

import java.util.List;

/**
 * @author Shyrik, 08.01.2009
 */

/**
 * Interface of the datasource for report.
 */
public interface ReportDataSource {
    /**
     * Implements connection point for attaching to the report.
     * @param report Report, that owns this report data source.
     */
    public void setTableReport(TableReport report);

    /**
     * @return title of the window.
     */
    public String getReportTitle();

    /**
     * @return information about report
     */
    public ReportInfo getReportInfo();

    /**
     * @return strategy that is used for editing of the report.
     */
    public ReportEditingStrategy getReportEditingStrategy();

    /**
     * @return list of elements of the report
     */
    public List getReportData();

    /**
     * Checks, if given item is the same, that is shown in the report.
     * @param reportItem - item to be checked for belonging to this report (usually just created item).
     * @return
     */
    public boolean itemBelongsToReport(Object reportItem);

    /**
     * Processes report item before it is passed to the report engine.
     * @param reportItem
     * @return
     */
    public Object preProcessItem(Object reportItem);

    //============================== Identification of reports (for header and etc.) ===================================
    public String getReportMajor();

    public String getReportMinor();

    public void setReportMinor(String reportMinor);
}
