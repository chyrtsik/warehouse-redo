/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.batches;

import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.core.report.controller.*;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.details.DetailGroupTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 26.12.2008
 */

/**
 * List of the batches of the details. Content of this list appeares in the price list.
 * This list can be also called "Price list".
 */
public class DetailBatchesList extends ReportDataSourceBase implements ReportPrintProvider {

    /**
     * Details catalog groups (for detail batches filtering).
     */
    protected DetailGroupTO catalogGroup;

    public DetailBatchesList(){
    }

    public DetailBatchesList(String reportMinor){
        this(null, reportMinor);
    }

    public DetailBatchesList(DetailGroupTO catalogGroup, String reportMinor){
        super(reportMinor);
        this.catalogGroup = catalogGroup;
    }

    public void setCatalogGroup(DetailGroupTO catalogGroup) {
        this.catalogGroup = catalogGroup;
    }

    @Override
    public String getReportTitle() {
        return I18nSupport.message("detail.batches.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(DetailBatchTO.class);

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.name"), "name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.misc"), "misc"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.type"), "type"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.acceptance"), "acceptance"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.year"), "year"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.manufacturer"), "manufacturer.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.barCode"), "barCode"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.nomenclatureArticle"), "nomenclatureArticle"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.buyPrice"), "buyPrice",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.sellPrice"), "sellPrice",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.currency"), "currency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.sellPrice2"), "sellPrice2",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.currency2"), "currency2.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.sellPrice3"), "sellPrice3",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.currency3"), "currency3.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.sellPrice4"), "sellPrice4",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.currency4"), "currency4.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.sellPrice5"), "sellPrice5",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.currency5"), "currency5.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.availCount"), "availCount", new DetailBatchCountStyleFactory()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.reservedCount"), "reservedCount"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.count"), "count",
                new DetailBatchCountStyleFactory(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.countMeas"), "countMeas.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.notice"), "notice"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.sortNum"), "sortNum"));

        reportInfo.getOptions().setCustomToolbarCommand(new SortDetailBachesListCommand("sortNum"));
        reportInfo.getOptions().setPrintProvider(this);

        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new DetailBatchesEditingStrategy();
    }

    @Override
    public List getReportData() {
        if (catalogGroup == null) {
            return PriceUpdater.updatePrices(SpringServiceContext.getInstance().getDetailBatchesService()
                    .getAllBatchesSortedByPriceNumber());
        } else {
            return PriceUpdater.updatePrices(SpringServiceContext.getInstance().getDetailBatchesService()
                    .getFilteredByCatalogGroupBatches(catalogGroup));
        }
    }

    @Override
    public PrintTemplateType[] getReportPrintTemplates() {
        return new PrintTemplateType[]{
                PrintTemplateType.TEMPLATE_DETAIL_BATCHES_LIST,
                PrintTemplateType.TEMPLATE_STOCK_CHANGES_REPORT,
                PrintTemplateType.TEMPLATE_STOCK_REPORT
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getReportDataForPrinting() {
        return new DetailBatchesPrintableObject(tableReport.getDisplayedReportItems());
    }
}
