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

import com.artigile.warehouse.bl.common.listeners.DataChangeAdapter;
import com.artigile.warehouse.gui.core.report.model.ReportModel;

/**
 * @author Shyrik, 10.03.2009
 */

/**
 * This class listens changes of data, show in report, made from onother places.
 * Is there any changes detected, it updates report model to reflect changes.
 */
public class ReportUpdater extends DataChangeAdapter {
    /**
     * Model of report to be updated, when changes are detected.
     */
    private ReportModel model;

    public ReportUpdater(ReportModel reportModel) {
        this.model = reportModel;
    }

    @Override
    public void afterDelete(Object deletedData) {
        if (model.getReportDataSource().itemBelongsToReport(deletedData)) {
            model.deleteItem(deletedData);
        }
    }

    @Override
    public void afterCreate(Object createdData) {
        ReportDataSource dataSource = model.getReportDataSource();
        if (dataSource.itemBelongsToReport(createdData)) {
            model.addItem(dataSource.preProcessItem(createdData));
        }
    }

    @Override
    public void afterChange(Object changedData) {
        ReportDataSource dataSource = model.getReportDataSource();
        if (dataSource.itemBelongsToReport(changedData)) {
            model.setItem(dataSource.preProcessItem(changedData));
        }
    }
}
