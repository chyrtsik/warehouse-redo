/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.postings;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.DateColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.postings.PostingTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 02.02.2009
 */
public class PostingsList extends ReportDataSourceBase {
    @Override
    public String getReportTitle() {
        return I18nSupport.message("posting.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(PostingTOForReport.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.list.number"), "number"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.list.state"), "state.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.list.createDate"), "createDate",
                DateColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.list.createdUser"), "createdUser.displayName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.list.contractor"), "contractor.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.list.currency"), "currency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.list.totalPrice"), "totalPrice",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.list.defaultCurrency"), "defaultCurrency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.list.warehouse"), "warehouse.name"));        
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.list.defaultStoragePlace"), "defaultStoragePlace.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.list.notice"), "notice"));
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new PostingEditingStrategy();
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getPostingsService().getAllPostings();
    }
}
