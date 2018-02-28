/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.complecting.readyForShipping;

import com.artigile.warehouse.bl.complecting.ComplectingTaskFilter;
import com.artigile.warehouse.domain.complecting.ComplectingTaskState;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.gui.menuitems.complecting.complectingTasks.ComplectingTaskList;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.complecting.ComplectingTaskTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 11.03.2010
 */

/**
 * Report with complecting tasks, that are ready to be shipped from warehouse.
 */
public class ReadyForShippingComplectingTaskList extends ComplectingTaskList {
    private ReadyForShippingEditingStrategy editingStrategy;


    public ReadyForShippingComplectingTaskList(ComplectingTaskFilter filter, UserTO worker) {
        super(filter, worker);
        editingStrategy = new ReadyForShippingEditingStrategy(filter.getWarehouseId(), worker.getId());
    }

    @Override
    public void setWarehouse(WarehouseTOForReport warehouse) {
        editingStrategy.setWarehouseId(warehouse.getId());
        super.setWarehouse(warehouse);
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return editingStrategy;
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(ComplectingTaskTO.class);

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.state"), "state.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.targetLocation"), "targetLocation"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.parcelNo"), "parcelNo"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.itemNo"), "itemNo"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.itemName"), "itemName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.itemMisc"), "itemMisc"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.itemType"), "itemType"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.countForShipping"), "foundCount",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.warehouse"), "warehouse.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.storagePlace"), "storagePlace"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("complectingTask.list.notice"), "notice"));

        return reportInfo;
    }

    @Override
    public boolean itemBelongsToReport(Object reportItem) {
        ComplectingTaskTO task = (ComplectingTaskTO)reportItem;
        ComplectingTaskState taskState = task.getState();
        return taskState.equals(ComplectingTaskState.READY_FOR_SHIPPING) || taskState.equals(ComplectingTaskState.SHIPPED);
    }
}
