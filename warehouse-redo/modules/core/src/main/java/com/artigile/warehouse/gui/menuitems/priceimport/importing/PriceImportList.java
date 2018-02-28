/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.priceimport.importing;

import com.artigile.warehouse.bl.priceimport.PriceImportFilter;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.menuitems.priceimport.importing.styles.PriceImportItemCountStyleFactory;
import com.artigile.warehouse.gui.menuitems.priceimport.importing.styles.PriceImportListRequestDateStyleFactory;
import com.artigile.warehouse.gui.menuitems.priceimport.importing.styles.PriceImportStyleFactory;
import com.artigile.warehouse.gui.utils.dataimport.DataImportReportInfoUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.priceimport.ContractorPriceImportTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Valery Barysok, 6/11/11
 */

public class PriceImportList extends ReportDataSourceBase {

    private static final int MAX_PRICE_IMPORTS_COUNT = 1000;

    private PriceImportFilter priceImportFilter;

    /**
     * True (default) - report data contains only last imports
     * False - report data contains all imports, but no more than 1000
     */
    private boolean onlyLastImports;


    /* Constructors
    ------------------------------------------------------------------------------------------------------------------*/
    public PriceImportList() {
        this.priceImportFilter = new PriceImportFilter();
        priceImportFilter.setMaxResultsCount(MAX_PRICE_IMPORTS_COUNT);
        this.onlyLastImports = true;
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public String getReportTitle() {
        return I18nSupport.message("price.import.list.title");
    }

    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(ContractorPriceImportTO.class);

        //Custom columns for contractor price list imports.
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("price.import.list.contractor"), "contractor.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("price.import.list.currency"), "currency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("price.import.list.measure.unit"), "measureUnit.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("price.import.list.imported.item.count"), "itemCount",
                new PriceImportItemCountStyleFactory(),
                NumericColumnFormatFactory.getInstance(),
                new PriceImportContractorRatingTooltipFactory()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("price.import.list.ignored.item.count"), "ignoredItemCount",
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("price.import.list.conversion.currency"), "conversionCurrency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("price.import.list.conversion.exchange.rate"), "conversionExchangeRate",
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("price.import.list.priceListRequest"), "contractor.priceListRequest",
                new PriceImportListRequestDateStyleFactory()));

        //Common columns of data import.
        DataImportReportInfoUtils.addDataImportColumns(reportInfo);
        reportInfo.setRowStyleFactory(new PriceImportStyleFactory());
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new PriceImportEditingStrategy();
    }

    @Override
    public List getReportData() {
        List<ContractorPriceImportTO> priceImports = SpringServiceContext.getInstance()
                .getContractorPriceImportService().getListByFilter(priceImportFilter);
        return onlyLastImports
                ? SpringServiceContext.getInstance().getContractorPriceImportService().filterLastContractorPriceImports(priceImports)
                : priceImports;
    }


    /* Setters
    ------------------------------------------------------------------------------------------------------------------*/
    public void showOnlyLastImports(boolean onlyLastImports) {
        this.onlyLastImports = onlyLastImports;
    }
}
