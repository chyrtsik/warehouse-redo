/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.batchesImport;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.gui.utils.dataimport.DataImportReportInfoUtils;
import com.artigile.warehouse.gui.utils.dataimport.DataImportStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchImportTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * List of detail batches imports (import of self price list).
 *
 * @author Aliaksandr.Chyrtsik, 02.11.11
 */
public class DetailBatchesImportList extends ReportDataSourceBase {
    @Override
    public String getReportTitle() {
        return I18nSupport.message("detail.batch.import.list.title");
    }

    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(DetailBatchImportTO.class);
        reportInfo.setRowStyleFactory(new DataImportStyleFactory());

        //Detail batches specific fields.
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.import.list.detailType"), "detailType.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.import.list.currency"), "currency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.import.list.measureUnit"), "measureUnit.sign"));

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.import.list.insertedItemsCount"), "insertedItemsCount",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.import.list.updatedItemsCount"), "updatedItemsCount",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.import.list.errorItemsCount"), "errorItemsCount",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batch.import.list.totalProcessedItemsCount"), "totalProcessedItemsCount",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));

        //Common fields of data import.
        DataImportReportInfoUtils.addDataImportColumns(reportInfo);

        return reportInfo;
    }


    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new DetailBatchImportEditingStrategy();
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getDetailBatchImportService().getAll();
    }
}
