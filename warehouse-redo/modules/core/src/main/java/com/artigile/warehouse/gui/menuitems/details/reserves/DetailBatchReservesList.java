/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.reserves;

import com.artigile.warehouse.bl.detail.DetailBatchReservesFilter;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.DateColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchReservesTO;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 *  Provides information about orders where the given detail was reserved.
 *
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class DetailBatchReservesList extends ReportDataSourceBase {

    private DetailBatchTO detailBatch;

    private WarehouseBatchTO warehouseBatch;

    public DetailBatchReservesList(DetailBatchTO detailBatch) {
        this.detailBatch = detailBatch;
    }

    public DetailBatchReservesList(WarehouseBatchTO warehouseBatch) {
        this.warehouseBatch = warehouseBatch;
        this.detailBatch = warehouseBatch.getDetailBatch();
    }

    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(DetailBatchReservesTO.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.reserves.list.document"), "documentType.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.reserves.list.document.number"), "number",
                RightMiddleAlignStyleFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.reserves.list.document.creation.date"), "createDate", DateColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.reserves.list.contractor"), "contractorName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.reserves.list.detail.position"), "itemNumber",
                RightMiddleAlignStyleFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.reserves.list.reserved.quantity"), "amount",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.reserves.list.user"), "userName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.reserves.list.warehouse"), "warehouseName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.reserves.list.storage.place"), "storagePlaceSign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.reserves.list.storage.batchNo"), "batchNo"));

        return reportInfo;
    }

    @Override
    public String getReportTitle() {
        return warehouseBatch == null
                ? I18nSupport.message("detail.batch.reserves.title", detailBatch.getName())
                : I18nSupport.message("detail.batch.reserves.title.for.warehouse.storage.place",
                        detailBatch.getName(),
                        warehouseBatch.getStoragePlace().getSign(),
                        warehouseBatch.getWarehouse().getName());
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        // No commands for this report
        return null;
    }

    @Override
    public List getReportData() {
        DetailBatchReservesFilter filter = new DetailBatchReservesFilter(detailBatch.getId(),
                warehouseBatch == null ? null : warehouseBatch.getStoragePlace().getId());
        return SpringServiceContext.getInstance().getDetailBatchReservesService().getDetailBatchReserves(filter);
    }
}
