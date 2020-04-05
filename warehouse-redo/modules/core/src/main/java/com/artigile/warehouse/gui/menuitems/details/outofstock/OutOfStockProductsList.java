/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.outofstock;

import com.artigile.warehouse.bl.detail.DetailBatchHistoryFilter;
import com.artigile.warehouse.gui.core.report.controller.*;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchOperationTO;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.details.outofstock.OutOfStockProductTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * Report with products, which are going to be out of stock soon.
 */
public class OutOfStockProductsList extends ReportDataSourceBase{

    public OutOfStockProductsList(String reportMinor) {
        super(reportMinor);
    }

    @Override
    public String getReportTitle() {
        return I18nSupport.message("detail.batch.outofstock.title");
    }

    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(OutOfStockProductTO.class);

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.outofstock.list.name"), "productName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.outofstock.list.available"), "available",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        //TODO - color items depending on how soon items are going to be depleted
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.outofstock.list.enoughForMonths"), "enoughForMonths",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.outofstock.list.countToOrder"), "countToOrder",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.outofstock.list.orderedCount"), "orderedCount",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));

        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        //Report is not editable now.
        return new ReportEditingStrategyAdapter();
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getOutOfStockProductsService().getProductsToBeOutOfStockSoon();
    }
}
