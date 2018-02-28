/*
 * Copyright (c) 2007-2013 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.complecting.uncomplectingTasks;

import com.artigile.warehouse.bl.complecting.UncomplectingTaskFilter;
import com.artigile.warehouse.domain.complecting.UncomplectingTaskState;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.complecting.UncomplectingTaskTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 12.06.2009
 */
public class UncomplectingTaskList extends ReportDataSourceBase {
    /**
     * Warehouse, tasks for which are to be shown.
     */
    private WarehouseTOForReport warehouse;

    /**
     * Worker, who now works with uncomplecting tasks list of the warehouse.
     */
    private UserTO worker;

    /**
     * States of uncomplecting tasks to be loaded into list.
     */
    private UncomplectingTaskState[] uncomplectingTaskStates;

    /**
     * Use this constructor for creating list of tasks for specified user and warehouse.
     * @param filter
     * @param worker
     */
    public UncomplectingTaskList(UncomplectingTaskFilter filter, UserTO worker) {
        this.uncomplectingTaskStates = filter.getStates();
        this.warehouse = SpringServiceContext.getInstance().getWarehouseService().getWarehouseForReport(filter.getWarehouseId());
        this.worker = worker;
    }

    @Override
    public String getReportTitle() {
        return I18nSupport.message("uncomplectingTask.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(UncomplectingTaskTO.class);
        reportInfo.setRowStyleFactory(new UncomplectingTaskStyleFactory());

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("uncomplectingTask.list.state"), "state.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("uncomplectingTask.list.action"), "action"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("uncomplectingTask.list.parcelNo"), "parcelNo"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("uncomplectingTask.list.itemNo"), "itemNo"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("uncomplectingTask.list.itemName"), "itemName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("uncomplectingTask.list.itemMisc"), "itemMisc"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("uncomplectingTask.list.itemType"), "itemType"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("uncomplectingTask.list.warehouse"), "warehouse.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("uncomplectingTask.list.storagePlace"), "storagePlace"));

        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        if (warehouse != null && worker != null) {
            return new UncomplectingTaskEditingStrategy(worker, warehouse);
        }
        else {
            return null;
        }
    }

    @Override
    public List getReportData() {
        UncomplectingTaskFilter filter = new UncomplectingTaskFilter();
        filter.setWarehouseId(warehouse == null ? null : warehouse.getId());
        filter.setStates(uncomplectingTaskStates);
        return SpringServiceContext.getInstance().getUncomplectingTaskService().getListByFilter(filter);
    }

    @Override
    public boolean itemBelongsToReport(Object reportItem) {
        if (warehouse != null){
            UncomplectingTaskTO task = (UncomplectingTaskTO) reportItem;
            WarehouseTOForReport warehouseTOForReport = task.getOrderSubItem().getStoragePlace().getWarehouse();
            return warehouseTOForReport.getId() == warehouse.getId();
        }
        return true;
    }

    public WarehouseTOForReport getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseTOForReport warehouse) {
        this.warehouse = warehouse;
    }
}
