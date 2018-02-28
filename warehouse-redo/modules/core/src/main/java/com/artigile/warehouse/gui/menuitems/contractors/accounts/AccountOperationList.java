/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.contractors.accounts;

import com.artigile.warehouse.bl.finance.AccountService;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.AccountOperationTO;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 18.04.2010
 */

/**
 * List of operations with contractor's account.
 */
public class AccountOperationList extends ReportDataSourceBase {
    /**
     * Contractor, whose account operations will be shown.
     */
    private ContractorTO contractor;

    public AccountOperationList(ContractorTO contractor) {
        this.contractor = contractor;
    }

    //====================== ReportDataSourceBase implementation ========================
    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(AccountOperationTO.class);

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("accountOperation.list.operationId"), "id"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("accountOperation.list.contractor"), "contractorName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("accountOperation.list.currency"), "currencySign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("accountOperation.list.operationDateTime"), "operationDateTime"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("accountOperation.list.performedUser"), "performedUserFullName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("accountOperation.list.initialBalance"), "initialBalance"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("accountOperation.list.newBalance"), "newBalance"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("accountOperation.list.changeOfBalance"), "changeOfBalance"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("accountOperation.list.operation"), "operation"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("accountOperation.list.notice"), "notice"));

        return reportInfo;
    }

    @Override
    public String getReportTitle() {
        return I18nSupport.message("accountOperation.list.title", contractor.getName());
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return null;
    }

    @Override
    public List getReportData() {
        AccountService accountService = SpringServiceContext.getInstance().getAccountService();
        return accountService.getContractorAccountOperations(contractor.getId());
    }
}
