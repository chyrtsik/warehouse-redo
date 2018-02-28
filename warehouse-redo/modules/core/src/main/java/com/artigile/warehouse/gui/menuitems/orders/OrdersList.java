/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.orders;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.DateColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.orders.OrderTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 06.01.2009
 */
public class OrdersList extends ReportDataSourceBase {
    @Override
    public String getReportTitle() {
        return I18nSupport.message("order.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(OrderTOForReport.class);
        reportInfo.setRowStyleFactory(new OrderStyleFactory());

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.list.number"), "number"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.list.state"), "state.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.list.processingInfo.complectedItems"), "processingInfo.complected"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.list.createDate"), "createDate",
                DateColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.list.createdUser"), "createdUser.displayName"));        
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.list.contractor"), "contractor.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.list.loadPlace"), "loadPlace"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.list.loadDate"), "loadDate",
                DateColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.list.currency"), "currency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.list.totalPrice"), "totalPrice",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.list.vatRate"), "displayedVatRate",
                RightMiddleAlignStyleFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.list.vat"), "vat",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.list.totalPriceWithVat"), "totalPriceWithVat",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.list.notice"), "notice"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.list.reservingType"), "reservingType.name"));

        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new OrdersEditingStrategy();
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getOrdersService().getOrdersWithoutDeleted();
    }
}
