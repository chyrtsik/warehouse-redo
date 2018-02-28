/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.finance.exchangeRate;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.ExchangeRateTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 09.12.2008
 */

/**
 * Report for editing currency exhange rates.
 */
public class ExchangeRatesList extends ReportDataSourceBase {
    @Override
    public String getReportTitle() {
        return I18nSupport.message("currency.exchange.rate.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(ExchangeRateTO.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("currency.exchange.rate.list.from"), "fromCurrency.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("currency.exchange.rate.list.to"), "toCurrency.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("currency.exchange.rate.list.rate"), "rate",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new ExchangeRateEditingStrategy();
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getExchangeService().getAllRates();
    }
}
