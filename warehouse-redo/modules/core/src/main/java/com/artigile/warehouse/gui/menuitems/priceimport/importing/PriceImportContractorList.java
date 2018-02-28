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
import com.artigile.warehouse.gui.menuitems.priceimport.importing.styles.PriceImportListRequestDateStyleFactory;
import com.artigile.warehouse.gui.menuitems.priceimport.importing.styles.PriceImportStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.date.DateUtils;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.priceimport.ContractorPriceImportTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class PriceImportContractorList extends ReportDataSourceBase {

    // Interval between repeated requests to each contractor (in days)
    private static final int PRICE_LIST_REPEATED_REQUESTS_INTERVAL = 2;

    private PriceImportFilter filter;

    // Parameters, required for filtering list with contractors
    private boolean importDateMoreTwoWeeks;
    private boolean importDateFromOneToTwoWeeks;
    private boolean importDateLessOneWeek;
    private boolean priceListRequestForPastTwoDays;


    /* Constructor
    ------------------------------------------------------------------------------------------------------------------*/
    public PriceImportContractorList() {
        this.filter = new PriceImportFilter();
        filter.setMaxResultsCount(1000);
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(ContractorPriceImportTO.class);

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("price.import.list.contractor"), "contractor.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("data.import.list.import.date"), "importDate"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("price.import.list.priceListRequest"), "contractor.priceListRequest", new PriceImportListRequestDateStyleFactory()));

        reportInfo.setRowStyleFactory(new PriceImportStyleFactory());
        return reportInfo;
    }

    @Override
    public String getReportTitle() {
        return null;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return null;
    }

    @Override
    public List getReportData() {
        // Get all imports
        List<ContractorPriceImportTO> priceImportList = SpringServiceContext.getInstance()
                .getContractorPriceImportService().getListByFilter(filter); 
        // Filter. Get only last imports
        priceImportList = SpringServiceContext.getInstance().getContractorPriceImportService()
                .filterLastContractorPriceImports(priceImportList);
        return filterPriceImports(priceImportList);
    }

    /**
     * @param priceImportList Imports
     * @return Filtered by the given parameters list with imports
     */
    private List<ContractorPriceImportTO> filterPriceImports(List<ContractorPriceImportTO> priceImportList) {
        List<ContractorPriceImportTO> filteredPriceImportList = new ArrayList<ContractorPriceImportTO>();
        // Prepare required dates
        Date now = DateUtils.now();
        Date twoWeeksAgo = DateUtils.moveDatetime(now, Calendar.DATE, -14);
        Date oneWeekAgo = DateUtils.moveDatetime(now, Calendar.DATE, -7);

        for (ContractorPriceImportTO priceImport : priceImportList) {
            ContractorTO currentContractor = priceImport.getContractor();
            Date priceImportDate = priceImport.getImportDate();
            // Contractor should have the email address
            if (!StringUtils.isStringNullOrEmpty(currentContractor.getEmail())) {
                boolean alreadyAdded = false;
                if (importDateMoreTwoWeeks) {
                    if (priceImportDate.before(twoWeeksAgo) && validContractorPriceListRequest(currentContractor)) {
                        filteredPriceImportList.add(priceImport);
                        alreadyAdded = true;
                    }
                }
                if (!alreadyAdded && importDateFromOneToTwoWeeks) {
                    if (DateUtils.isBetweenDates(priceImportDate, twoWeeksAgo, oneWeekAgo, true)
                            && validContractorPriceListRequest(currentContractor)) {
                        filteredPriceImportList.add(priceImport);
                        alreadyAdded = true;
                    }
                }
                if (!alreadyAdded && importDateLessOneWeek) {
                    if (priceImportDate.after(oneWeekAgo) && validContractorPriceListRequest(currentContractor)) {
                        filteredPriceImportList.add(priceImport);
                    }
                }
            }
        }

        return filteredPriceImportList;
    }

    private boolean validContractorPriceListRequest(ContractorTO contractor) {
        if (priceListRequestForPastTwoDays) {
            Date requestDeadline = DateUtils.moveDatetime(DateUtils.now(), Calendar.DATE, -PRICE_LIST_REPEATED_REQUESTS_INTERVAL);
            return contractor.getPriceListRequest() == null || contractor.getPriceListRequest().before(requestDeadline);
        } else {
            return true;
        }
    }


    /* Setters
    ------------------------------------------------------------------------------------------------------------------*/
    public void setImportDateMoreTwoWeeks(boolean importDateMoreTwoWeeks) {
        this.importDateMoreTwoWeeks = importDateMoreTwoWeeks;
    }

    public void setImportDateFromOneToTwoWeeks(boolean importDateFromOneToTwoWeeks) {
        this.importDateFromOneToTwoWeeks = importDateFromOneToTwoWeeks;
    }

    public void setImportDateLessOneWeek(boolean importDateLessOneWeek) {
        this.importDateLessOneWeek = importDateLessOneWeek;
    }

    public void setPriceListRequestForPastTwoDays(boolean priceListRequestForPastTwoDays) {
        this.priceListRequestForPastTwoDays = priceListRequestForPastTwoDays;
    }
}
