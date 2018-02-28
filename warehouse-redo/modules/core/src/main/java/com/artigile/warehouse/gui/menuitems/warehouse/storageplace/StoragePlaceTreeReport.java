/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.warehouse.storageplace;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.controller.TreeReportDataSource;
import com.artigile.warehouse.gui.core.report.model.TreeReportModel;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 30.12.2008
 */
public class StoragePlaceTreeReport implements TreeReportDataSource {
    /**
     * Warehouse, which contens is being displaying in the tree.
     */
    private WarehouseTO warehouseTO;

    public StoragePlaceTreeReport(long warehouseId) {
        this.warehouseTO = SpringServiceContext.getInstance().getWarehouseService().getWarehouseFull(warehouseId);
    }

    public StoragePlaceTreeReport(WarehouseTO warehouseTO) {
        this.warehouseTO = warehouseTO;
    }

    @Override
    public ReportInfo getReportInfo() {
        ReportInfo reportInfo = new ReportInfo(StoragePlaceTO.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("storageplace.list.sign"), "sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("storageplace.list.fillingDegree"), "fillingDegree"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("storageplace.list.notice"), "notice"));
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new StoragePlaceEditingStrategy(warehouseTO);
    }

    @Override
    public TreeReportModel getTreeReportModel() {
        return new StoragePlaceTreeModel(warehouseTO);
    }
}
