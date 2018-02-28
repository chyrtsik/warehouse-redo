/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.complecting.complectingTasks;

import com.artigile.warehouse.bl.complecting.ComplectingTaskFilter;
import com.artigile.warehouse.domain.complecting.ComplectingTaskState;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.gui.menuitems.complecting.printing.ComplectingTasksToPrintFilterType;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.complecting.ComplectingTaskTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 28.04.2009
 */
public class ComplectingTaskList extends ReportDataSourceBase {
    /**
     * Warehouse, tasks for which are to be shown.
     */
    private WarehouseTOForReport warehouse;

    /**
     * Worker, who now works with complecting tasks list of thw warehouse.
     */
    private UserTO worker;

    /**
     * States of complecting tasks to be loaded into list.
     */
    private ComplectingTaskState[] complectingTaskStates;

    /**
     * This report editing strategy.
     */
    private ComplectingTaskEditingStrategy editingStrategy;

    private ComplectingTaskFoundCountSaver foundCountSaver;

    public ComplectingTaskList(){
    }

    /**
     * Use this constructor for creating list of tasks for specified user and filter of tasks.
     * @param filter
     * @param worker
     */
    public ComplectingTaskList(ComplectingTaskFilter filter, UserTO worker) {
        this.complectingTaskStates = filter.getStates();
        this.warehouse = SpringServiceContext.getInstance().getWarehouseService().getWarehouseForReport(filter.getWarehouseId());
        this.worker = worker;
    }

    @Override
    public String getReportTitle() {
        return I18nSupport.message("complectingTask.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(ComplectingTaskTO.class);
        reportInfo.setRowStyleFactory(new ComplectingTaskStyleFactory());

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.stickerPrinted"), "stickerPrinted", new TaskStickerPrintedStyleFactory()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.printed"), "printed", new TaskPrintedStyleFactory()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.state"), "state.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.targetLocation"), "targetLocation"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.parcelNo"), "parcelNo"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.itemNo"), "itemNo"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.itemName"), "itemName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.itemMisc"), "itemMisc"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.itemType"), "itemType"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.remainder"), "remainder"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.neededCount"), "neededCount"));
        foundCountSaver = new ComplectingTaskFoundCountSaver(worker, warehouse);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.foundCount"), "foundCount",
                foundCountSaver,
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.processingResult"), "processingResult"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.warehouse"), "warehouse.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.storagePlace"), "storagePlace"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.fillRate"), "fillRate",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.notice"), "notice"));

        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        if (warehouse != null && worker != null){
            if (editingStrategy == null){
                editingStrategy = new ComplectingTaskEditingStrategy(warehouse, worker);
            }
            return editingStrategy;
        }
        else{
            return null;
        }
    }

    @Override
    public List getReportData() {
        ComplectingTaskFilter filter = new ComplectingTaskFilter();
        filter.setWarehouseId(warehouse == null ? null : warehouse.getId());
        filter.setStates(complectingTaskStates);
        return SpringServiceContext.getInstance().getComplectingTaskService().getListByFilter(filter);
    }

    @Override
    public boolean itemBelongsToReport(Object reportItem) {
        if (warehouse != null){
            ComplectingTaskTO task = (ComplectingTaskTO)reportItem;
            return task.getWarehouse().getId() == warehouse.getId();
        }
        return true;
    }

    //====================== Extended implementation ================================
    public WarehouseTOForReport getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseTOForReport warehouse) {
        this.warehouse = warehouse;
        if (editingStrategy != null){
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
    public List<ComplectingTaskTO> getFilteredTasksList(ComplectingTasksToPrintFilterType filterType, boolean stickers) {
        if (filterType.equals(ComplectingTasksToPrintFilterType.ALL)){
            return tableReport.getDisplayedReportItems();
        }
        else if (filterType.equals(ComplectingTasksToPrintFilterType.NOT_PRINTED)){
            List<ComplectingTaskTO> allItems = tableReport.getDisplayedReportItems();
            List<ComplectingTaskTO> filteredItems = new ArrayList<ComplectingTaskTO>();
            for (ComplectingTaskTO item : allItems){
                if ( stickers && !item.getStickerPrinted() ||
                    !stickers && !item.getPrinted())
                {
                    filteredItems.add(item);
                }
            }
            return filteredItems;
        }
        else if (filterType.equals(ComplectingTasksToPrintFilterType.PROBLEM)){
            List<ComplectingTaskTO> allItems = tableReport.getDisplayedReportItems();
            List<ComplectingTaskTO> filteredItems = new ArrayList<ComplectingTaskTO>();
            for (ComplectingTaskTO item : allItems){
                if (item.isProblem()){
                    filteredItems.add(item);
                }
            }
            return filteredItems;
        }
        else if (filterType.equals(ComplectingTasksToPrintFilterType.SELECTED)){
            List<ComplectingTaskTO> selectedItems = tableReport.getCurrentReportItems();
            return selectedItems == null ? new ArrayList<ComplectingTaskTO>() : selectedItems;
        }
        else{
            throw new RuntimeException("ComplectingTaskList.getFilteredTasksList: Unsupported filter type.");
        }
    }
}
