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

import com.artigile.warehouse.bl.contractors.ContractorService;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.AccountTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author IoaN, Dec 14, 2008
 */

public class AccountsList extends ReportDataSourceBase {

    private ContractorService contractorService = SpringServiceContext.getInstance().getContractorService();

    private long countractorId;

    private ReportEditingStrategy editingStrategy;

    private List<AccountTO> accountTOList;

    public AccountsList(long countractorId, ReportEditingStrategy editingStrategy) {
        this.countractorId = countractorId;
        this.editingStrategy = editingStrategy;
    }

    @Override
    public String getReportTitle() {
        return "no Title for non framed table";
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(AccountTO.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("account.currency"), "currency"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("account.balance"), "currentBalance"));
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return editingStrategy;
    }

    @Override
    public List<AccountTO> getReportData() {
        if (accountTOList == null) {
            accountTOList = contractorService.getAccountsByContractorId(countractorId);
        }
        return accountTOList;
    }

    public AccountTO getSelectedItem() {
        return (AccountTO) getCurrentReportItem();
    }

    public void fireItemChanged(AccountTO accountTO) {
        getReportModel().fireItemDataChanged(accountTO);
    }
}