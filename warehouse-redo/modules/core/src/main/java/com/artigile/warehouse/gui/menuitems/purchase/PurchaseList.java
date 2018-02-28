/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.purchase;

import com.artigile.warehouse.domain.purchase.PurchaseState;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.DateColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 01.03.2009
 */
public class PurchaseList extends ReportDataSourceBase {
    /**
     * Array of stated to be shown in list.
     */
    private PurchaseState [] statesFilter;

    public PurchaseList(PurchaseState[] statesToBeShown) {
        this.statesFilter = statesToBeShown;
    }

    public PurchaseList(){
    }

    @Override
    public String getReportTitle() {
        return I18nSupport.message("purchase.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(PurchaseTOForReport.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.list.number"), "number"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.list.state"), "state.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.list.createDate"), "createDate",
                DateColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.list.createdUser"), "createdUser.displayName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.list.contractor"), "contractor.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.list.loadPlace"), "loadPlace.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.list.currency"), "currency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.list.totalPrice"), "totalPrice",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.list.notice"), "notice"));
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new PurchaseEditingStrategy();
    }

    @Override
    public List getReportData() {
        if (statesFilter == null){
            return SpringServiceContext.getInstance().getPurchaseService().getAllPurchases();
        }
        else{
            return SpringServiceContext.getInstance().getPurchaseService().getAllPurchasesByStates(statesFilter);
        }
    }
}
