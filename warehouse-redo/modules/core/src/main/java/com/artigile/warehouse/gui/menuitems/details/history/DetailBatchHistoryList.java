/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.history;

import com.artigile.warehouse.bl.detail.DetailBatchHistoryFilter;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.DateColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchOperationTO;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * Command for showing a list of all events related to the given detail batch.
 *
 * @author Aliaksandr.Chyrtsik, 02.10.11
 */
public class DetailBatchHistoryList extends ReportDataSourceBase{
    /**
     * Detail batch which this report is related to.
     */
    private DetailBatchTO detailBatch;

    /**
     * Warehouse batch which changes are shown by this report.
     */
    private WarehouseBatchTO warehouseBatch;

    /**
     * Use this constructor to create report for detail batch history (including all warehouses and stprage places).
     * @param detailBatch detail batch which history is to be shown.
     */
    public DetailBatchHistoryList(DetailBatchTO detailBatch){
        this.detailBatch = detailBatch;
    }

    /**
     * Use this constructor to view history of warehouse batch count changes.
     * Prices are now shown when using this constructor (warehouse worked is not permitted to know prices).
     * @param warehouseBatch warehouse batch which changes are to be loaded.
     */
    public DetailBatchHistoryList(WarehouseBatchTO warehouseBatch){
        this.warehouseBatch = warehouseBatch;
    }

    @Override
    public String getReportTitle() {
        if (warehouseBatch == null){
            return I18nSupport.message("detail.batch.history.title", getDetailBatch().getName());
        }
        else{
            return I18nSupport.message("detail.batch.history.title.for.warehouse.storage.place",
                    getDetailBatch().getName(),
                    warehouseBatch.getStoragePlace().getSign(),
                    warehouseBatch.getWarehouse().getName());
        }
    }

    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(DetailBatchOperationTO.class);

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.dateTime"), "dateTime"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.document.name"), "documentName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.document.number"), "documentNumber"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.document.date"), "documentDate",
                DateColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.document.contractor"), "documentContractorName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.initialCount"), "initialCount",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.initialCost"), "initialCost",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.postedCount"), "postedCount",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.postedCost"), "postedCost",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.chargedOffCount"), "chargedOffCount",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.chargedOffCost"), "chargedOffCost",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.finalCount"), "finalCount",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.finalCost"), "finalCost",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
//        if (isShowingPriceColumns()){
//            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.price"), "price",
//                    RightMiddleAlignStyleFactory.getInstance(),
//                    NumericColumnFormatFactory.getInstance()));
//            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.total"), "total",
//                    RightMiddleAlignStyleFactory.getInstance(),
//                    NumericColumnFormatFactory.getInstance()));
//            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.currency"), "currency"));
//        }
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.warehouse"), "storagePlace.warehouse.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.storagePlace"), "storagePlace.sign"));

        if (SpringServiceContext.getInstance().getWarehouseBatchService().isTrackPostingItem()){
            //Information about receipt date and price of warehouse batch.
            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.batchNo"), "batchNo"));
            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.receiptDate"), "receiptDate",
                    DateColumnFormatFactory.getInstance()));
            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.buyPrice"), "buyPrice",
                    RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.buyCurrency"), "buyCurrency.sign"));
        }

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.history.list.performedUser"), "performedUser"));

        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        //Report is not editable now.
        return null;
    }

    @Override
    public List getReportData() {
        DetailBatchHistoryFilter filter = new DetailBatchHistoryFilter();
        filter.setDetailBatchId(getDetailBatch().getId());
        if (warehouseBatch != null){
            filter.setStoragePlaceId(warehouseBatch.getStoragePlace().getId());
        }
        return SpringServiceContext.getInstance().getDetailBatchHistoryService()
                .getDetailBatchHistoryForReport(null, null, filter);
    }

    private DetailBatchTO getDetailBatch() {
        return warehouseBatch == null ? detailBatch : warehouseBatch.getDetailBatch();
    }

//    private boolean isShowingPriceColumns() {
//        return warehouseBatch == null;
//    }
}
