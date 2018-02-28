/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.inventorization.task;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.gui.menuitems.inventorization.task.printing.InventorizationTasksFilterType;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.inventorization.task.InventorizationTaskTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 03.10.2009
 */
public class InventorizationTaskList extends ReportDataSourceBase {
    /**
     * Warehouse, tasks for which are to be shown.
     */
    private WarehouseTOForReport warehouse;

    /**
     * Worker, who now works with inventorization tasks list of thw warehouse.
     */
    private UserTO worker;

    /**
     * This report editing strategy.
     */
    private InventorizationTaskEditingStrategy editingStrategy;

    private InventorizationTaskFoundCountSaver foundCountSaver;

    public InventorizationTaskList() {

    }

    /**
     * Use this constructor for creating list of tasks for specified user and warehouse.
     * @param warehouse
     * @param worker
     */
    public InventorizationTaskList(WarehouseTOForReport warehouse, UserTO worker) {
        this.warehouse = warehouse;
        this.worker = worker;
    }

    @Override
    public String getReportTitle() {
        return I18nSupport.message("inventorization.task.list.title");
    }

    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(InventorizationTaskTO.class);
        reportInfo.setRowStyleFactory(new InventorizationTaskStyleFactory());

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.task.list.number"), "number"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.task.list.state"), "state.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.task.list.taskType"), "inventorizationType.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.task.list.processingResult"), "processingResult.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.task.list.itemType"), "itemType"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.task.list.itemName"), "itemName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.task.list.itemMisc"), "itemMisc"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.task.list.warehouse"), "warehouseName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.task.list.storagePlace"), "storagePlaceSign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.task.list.warehouseBatchNotice"), "warehouseBatchNotice"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.task.list.measureUnit"), "itemMeas"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.task.list.neededCount"), "neededCount",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        foundCountSaver = new InventorizationTaskFoundCountSaver(worker, warehouse);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.task.list.foundCount"), "foundCount",
                foundCountSaver,
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.task.list.deviation"), "deviation"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.task.list.printed"), "printed", new TaskPrintedStyleFactory()));

        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        if (warehouse != null && worker != null) {
            if (editingStrategy == null) {
                editingStrategy = new InventorizationTaskEditingStrategy(warehouse, worker);
            }
            return editingStrategy;
        } else {
            return null;
        }
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getInventorizationTaskService().getListForWarehouse(warehouse);
    }

    @Override
    public boolean itemBelongsToReport(Object reportItem) {
        if (warehouse != null){
            InventorizationTaskTO task = (InventorizationTaskTO)reportItem;
            return task.getStoragePlace().getWarehouse().getId() == warehouse.getId();
        }
        return true;
    }

    //====================== Extended implementation ================================
    public WarehouseTOForReport getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseTOForReport warehouse) {
        this.warehouse = warehouse;
        if (editingStrategy != null) {
            editingStrategy.setWarehouse(warehouse);
        }
        if (foundCountSaver != null) {
            foundCountSaver.setWarehouse(warehouse);
        }
    }

    /**
     * Filters list of already displayed tasks with according to given filtration type.
     * @param filterType - type of filtering to be performed.
     * @params stickers - if true, stickers are analyzed, if false -- complecting tasks itself.
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<InventorizationTaskTO> getFilteredTasksList(InventorizationTasksFilterType filterType) {
        if (filterType.equals(InventorizationTasksFilterType.ALL)){
            return tableReport.getDisplayedReportItems();
        }
        else if (filterType.equals(InventorizationTasksFilterType.NOT_PRINTED)){
            List<InventorizationTaskTO> allItems = tableReport.getDisplayedReportItems();
            List<InventorizationTaskTO> filteredItems = new ArrayList<InventorizationTaskTO>();
            for (InventorizationTaskTO item : allItems){
                if (!item.getPrinted())
                {
                    filteredItems.add(item);
                }
            }
            return filteredItems;
        }
        else if (filterType.equals(InventorizationTasksFilterType.SELECTED)){
            List<InventorizationTaskTO> selectedItems = tableReport.getCurrentReportItems();
            return selectedItems == null ? new ArrayList<InventorizationTaskTO>() : selectedItems;
        }
        else{
            throw new RuntimeException("InventorizationTaskList.getFilteredTasksList: Unsupported filter type.");
        }
    }
}
