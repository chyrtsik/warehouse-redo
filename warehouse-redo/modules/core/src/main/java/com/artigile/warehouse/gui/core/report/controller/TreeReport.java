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
import com.artigile.warehouse.gui.core.report.model.TreeTableReportModel;
import com.artigile.warehouse.gui.core.report.view.ReportView;
import com.artigile.warehouse.gui.core.report.view.TreeReportViewImpl;
import com.artigile.warehouse.gui.core.report.view.TreeTableReportView;
import com.artigile.warehouse.gui.core.report.view.TreeTableReportViewImpl;

import java.awt.*;
import java.util.List;

/**
 * Tree report implementation. Each report is parametrized with report data source and view to be used.
 *
 * @author Shyrik, 30.12.2008
 */
public class TreeReport {
    /**
     * Controller of the tree report.
     */
    private ReportController reportController;

    /**
     * Report editing strategy proxy (used, when there is need for changing and decorating report commands).
     */
    private ReportEditingStrategy editingStrategyProxy;

    /**
     * Data source of this report.
     */
    private TreeReportDataSource dataSource;

    /**
     * View type of this report.
     */
    private TreeReportViewType viewType;

    public TreeReport(TreeReportDataSource dataSource, TreeReportViewType viewType){
        this.dataSource = dataSource;
        this.viewType = viewType;
    }

    //============================== Common getters and setters ====================================

    public Component getReportComponent() {
        return getReportView().getContentPanel();
    }

    public TreeTableReportView getReportView() {
        return (TreeTableReportView) getReportController().getReportView();
    }

    public TreeReportModel getTreeModel() {
        return ((TreeTableReportModel) getReportController().getReportModel()).getTreeModel();
    }

    public void setReportEditingStrategyProxy(ReportEditingStrategy editingStrategyProxy){
        this.editingStrategyProxy = editingStrategyProxy;
    }

    public List getSelectedItems() {
        ReportView reportView = getReportController().getReportView();
        return reportView.getSelectedItems();
    }

    //=============================== Implementation ===================================================
    private ReportController getReportController() {
        if (reportController == null) {
            TreeTableReportModel treeTableReportModel = new TreeTableReportModel(dataSource.getReportInfo(), dataSource.getTreeReportModel());
            TreeTableReportView treeTableReportView = createReportView(treeTableReportModel);
            treeTableReportModel.setTreeReportTable(treeTableReportView);
            ReportEditingStrategy editingStrategy = doGetReportEditingStrategy();
            reportController = new ReportController(treeTableReportModel, treeTableReportView, editingStrategy);
        }
        return reportController;
    }

    private TreeTableReportView createReportView(TreeTableReportModel treeTableReportModel) {
        if (viewType.equals(TreeReportViewType.TREE)){
            //By default use first column as items name.
            return new TreeReportViewImpl(treeTableReportModel, 0);
        }
        else if (viewType.equals(TreeReportViewType.TREE_TABLE)){
            return new TreeTableReportViewImpl(treeTableReportModel);
        }
        else{
            throw new AssertionError("Invalid tree report view type specified.");
        }
    }

    private ReportEditingStrategy doGetReportEditingStrategy() {
        return editingStrategyProxy != null ? editingStrategyProxy : dataSource.getReportEditingStrategy();
    }
}
