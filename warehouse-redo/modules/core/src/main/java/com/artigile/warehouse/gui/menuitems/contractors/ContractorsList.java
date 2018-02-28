/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.contractors;

import com.artigile.warehouse.bl.contractors.ContractorService;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author IoaN, Dec 10, 2008
 */

public class ContractorsList extends ReportDataSourceBase {

    ContractorService contractorService = SpringServiceContext.getInstance().getContractorService();

    @Override
    public String getReportTitle() {
        return I18nSupport.message("contractors.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(ContractorTO.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.id"), "id"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.name"), "name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.fullName"), "fullName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.country"), "country"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.legal.address"), "legalAddress"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.postal.address"), "postalAddress"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.phone"), "phone"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.currency"), "defaultCurrency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.url"), "webSiteURL"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.email"), "email"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.discount"), "discount"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.rating"), "rating"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.notice"), "notice"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.def.ship.address"), "defaultShippingAddress.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.bank.short.data"), "bankShortData"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.bank.full.data"), "bankFullData"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.bank.account"), "bankAccount"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.mfo"), "bankCode"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.bank.address"), "bankAddress"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.unp"), "unp"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.okpo"), "okpo"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractors.list.priceListRequest"), "priceListRequest"));
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new ContractorEditingStrategy();
    }

    @Override
    public List<ContractorTO> getReportData() {
        return contractorService.getAll();
    }
}
