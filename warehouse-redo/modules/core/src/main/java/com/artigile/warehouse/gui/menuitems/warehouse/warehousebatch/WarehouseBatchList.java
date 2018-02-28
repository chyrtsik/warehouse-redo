/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.warehouse.warehousebatch;

import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchFilter;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchService;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.DateColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Borisok V.V., 04.01.2009
 */
public class WarehouseBatchList extends ReportDataSourceBase {
    private WarehouseBatchFilter filter;
    private WarehouseBatchEditingStrategy warehouseBatchEditingStrategy = new WarehouseBatchEditingStrategy();
    private WarehouseBatchService warehouseBatchService = SpringServiceContext.getInstance().getWarehouseBatchService();

    public WarehouseBatchList(){
    }

    public WarehouseBatchList(String reportMinor) {
        this(null, reportMinor);
    }

    public WarehouseBatchList(WarehouseBatchFilter filter, String reportMinor) {
        super(reportMinor);
        this.filter = filter;
    }

    @Override
    public String getReportTitle() {
        return I18nSupport.message("warehousebatch.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(WarehouseBatchTO.class);

        //Detail batch header
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.type"), "detailBatch.type"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.name"), "detailBatch.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.misc"), "detailBatch.misc"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.notice"), "detailBatch.notice"));

        //Warehouse batch header
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehousebatch.list.count"), "count",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehousebatch.list.reservedCount"), "reservedCount",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.countMeas"), "detailBatch.countMeas.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehousebatch.list.needrecalculate"), "needRecalculateText"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehousebatch.list.warehouse"), "warehouse.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehousebatch.list.storageplace"), "storagePlace.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehousebatch.list.notice"), "notice"));

        if (warehouseBatchService.isTrackPostingItem()){
            //Information about receipt date and price of warehouse batch.
            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehousebatch.list.receiptDate"), "receiptDate",
                    DateColumnFormatFactory.getInstance()));
            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehousebatch.list.buyPrice"), "buyPrice",
                    RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehousebatch.list.buyCurrency"), "buyCurrency.sign"));
        }

        if (warehouseBatchService.isTrackShelfLife()) {
            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("warehousebatch.list.shelfLifeDate"), "shelfLifeDate",
                    new WarehouseBatchStyleFactory(), DateColumnFormatFactory.getInstance()));
        }

        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return warehouseBatchEditingStrategy;
    }

    @Override
    public List getReportData() {
        return warehouseBatchService.getWarehouseBatchesByFilter(filter);
    }
}

