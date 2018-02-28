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

import com.artigile.warehouse.gui.core.report.model.TreeReportModel;

/**
 * Data source for tree reports (similar meaning as data source for table reports).
 *
 * @author Aliaksandr.Chyrtsik, 25.10.11
 */
public interface TreeReportDataSource {
    /**
     * @return information about report
     */
    public ReportInfo getReportInfo();

    /**
     * @return strategy that is used for editing of the report.
     */
    public ReportEditingStrategy getReportEditingStrategy();

    /**
     * @return tree report model of this concrete report.
     */
    public TreeReportModel getTreeReportModel();
}
