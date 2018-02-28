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

import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.gui.baselayout.ReportComponentAdapter;
import com.artigile.warehouse.gui.baselayout.ReportComponentListener;
import com.artigile.warehouse.gui.core.plugin.FramePlugin;
import com.artigile.warehouse.gui.core.report.model.ReportModel;
import com.artigile.warehouse.gui.core.report.model.TableReportModel;
import com.artigile.warehouse.gui.core.report.view.TableReportView;
import com.artigile.warehouse.gui.core.report.view.TableReportViewListener;
import com.artigile.warehouse.utils.SpringServiceContext;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.util.EventObject;
import java.util.List;

/**
 * General implementation for the table reports (table for editing list of items).
 *
 * @author Shyrik, 08.01.2009
 */
public class TableReport {
    /**
     * View of of report.
     */
    private TableReportView tableReportView;

    /**
     * Controller for manupulating report's data.
     */
    private ReportController reportController;

    /**
     * Data updater for report. In updates report when it's data has beed changed
     * from another place.
     */
    private ReportUpdater reportUpdater;

    /**
     * Data source for the report.
     */
    private ReportDataSource reportDataSource;

    private FramePlugin parentReport;

    private final static GlobalDataChangeNotifier dataChangeNotifier = SpringServiceContext.getInstance().getDataChangeNolifier();

    private AncestorListener ancestorListener;
    private ReportComponentListener reportComponentListener;

    /**
     * Constructor. Here we need to initialize report datasource, report core components and
     * report filter.
     *
     * @param reportDataSource report data
     */
    public TableReport(ReportDataSource reportDataSource) {
        this(reportDataSource, null);
    }

    public TableReport(ReportDataSource reportDataSource, FramePlugin parentReport) {
        this.reportDataSource = reportDataSource;
        this.parentReport = parentReport;
        initReportCoreComponents();
        TableHeaderMenuManager.installTableHeaderHandler(tableReportView, reportDataSource);
    }

    /**
     * Gets report title. It's going to be the title of the window which shows report.
     *
     * @return title of the report
     */
    public String getReportTitle() {
        return reportDataSource.getReportTitle();
    }

    /**
     * Gets report content panel. It is actually what we see after clicking on tree item.
     *
     * @return view
     */
    public JPanel getContentPanel() {
        return tableReportView.getView();
    }

    public List getCurrentReportItems() {
        return reportController.getCurrentReportItems();
    }

    public Object getCurrentReportItem() {
        return reportController.getCurrentReportItem();
    }

    public List getDisplayedReportItems(){
        return reportController.getReportView().getDisplayedItems();
    }

    public ReportModel getReportModel() {
        return reportController.getReportModel();
    }

    public TableReportView getReportTable() {
        return tableReportView;
    }

    public ReportDataSource getReportDataSource() {
        return reportDataSource;
    }

    public void setReportDataSource(ReportDataSource reportDataSource) {
        this.reportDataSource = reportDataSource;
    }

    public void refreshData() {
        tableReportView.fireRefresh();
    }

    /**
     * Initializes report core components.
     */
    private void initReportCoreComponents() {
        reportDataSource.setTableReport(this);
        TableReportModel reportModel = new TableReportModel(reportDataSource);
        ReportEditingStrategy reportEditingStrategy = reportDataSource.getReportEditingStrategy();
        reportUpdater = new ReportUpdater(reportModel);
        tableReportView = new TableReportView(reportModel);
        reportController = new ReportController(reportModel, tableReportView, reportEditingStrategy);
        tableReportView.setReportController(reportController);
        initReportTableListeners();
    }

    /**
     * Initializes listeners for report table.
     */
    private void initReportTableListeners() {
        //1. Register listener for receiving notification about report data changes (to keep report up to date).
        dataChangeNotifier.addDataChangeListener(reportDataSource.getReportInfo().getDataClass(), reportUpdater);

        //2. Register listeners to unsubscribe from events when report has been closed (to prevent memory leaks).
        if (parentReport == null) {
            ancestorListener = new TableAncestorListener();
            tableReportView.getView().addAncestorListener(ancestorListener);
        } else {
            reportComponentListener = new TableReportComponentListener();
            parentReport.addReportComponentListener(reportComponentListener);
        }

        tableReportView.addRefreshListener(new TableReportViewListener() {
            @Override
            public void refresh(EventObject eventObject) {
                reportController.getReportModel().refresh();
            }
        });
    }

    /**
     * Free all report resources and unsubscribe report from listeners lists (to break references to the list).
     */
    public void dispose() {
        removeReportTableListeners();
    }

    private void removeReportTableListeners() {
        removeDataChangeListener();


        //And remove report from other listeners lists.
        if (ancestorListener != null) {
            tableReportView.getView().removeAncestorListener(ancestorListener);
        }
        if (reportComponentListener != null) {
            parentReport.removeReportComponentListener(reportComponentListener);
        }
    }

    private void removeDataChangeListener() {
        //Remove this report from data change listeners to prevent memory leaks.
        dataChangeNotifier.removeDataChangeListener(reportDataSource.getReportInfo().getDataClass(), reportUpdater);
    }

    private class TableAncestorListener implements AncestorListener {
        public void ancestorAdded(AncestorEvent event) {
        }

        public void ancestorRemoved(AncestorEvent event) {
            //When table is opened not in Frame then we expected report to become not interested in events since
            //is is hidden (or removed from components hierarchy).
            removeDataChangeListener();
        }

        public void ancestorMoved(AncestorEvent event) {
        }
    }

    private class TableReportComponentListener extends ReportComponentAdapter {
        @Override
        public void componentClosed() {
            //When report in shown in a Frame (non modal report) that it should be closed by user.
            //Between opening and closing this table (with other Frame content) ay be hidden and shown
            //many times. So, using ancestor listener is not appropriate in this case.
            removeDataChangeListener();
        }
    }
}
