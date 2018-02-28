/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.orders.items;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.orders.OrderItemTO;
import com.artigile.warehouse.utils.dto.orders.OrderTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 08.01.2009
 */

/**
 * Report for editing order items.
 */
public class OrderItemsList extends ReportDataSourceBase {
    /**
     * Order being edited.
     */
    private OrderTOForReport order;

    public OrderItemsList(OrderTOForReport order) {
        this.order = order;
    }

    @Override
    public String getReportTitle() {
        return "<not used>"; //Not used 
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(OrderItemTO.class);
        reportInfo.setRowStyleFactory(new OrderItemStyleFactory());

        //Original order item's part of the header.
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.items.list.number"), "number"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.items.list.state"), "state.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.items.list.processingResult"), "processingResult.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.items.list.type"), "type"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.items.list.name"), "name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.items.list.misc"), "misc"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.items.list.price"), "price",
                new OrderItemPriceSaver(),
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.items.list.count"), "count",
                new OrderItemCountStyleFactory(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.items.list.totalPrice"), "totalPrice",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.items.list.vatRate"), "displayedVatRate",
                RightMiddleAlignStyleFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.items.list.vat"), "vat",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.items.list.totalPriceWithVat"), "totalPriceWithVat",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.items.list.currency"), "order.currency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("order.items.list.notice"), "notice"));        
        //Part of the header from detail batches list.
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.acceptance"), "detailBatch.acceptance"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.year"), "detailBatch.year"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.manufacturer"), "detailBatch.manufacturer.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.countMeas"), "detailBatch.countMeas.sign"));

        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new OrderItemsEditingStrategy(order);
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getOrdersService().getOrderFullData(order.getId()).getItems();
    }

    @Override
    public boolean itemBelongsToReport(Object reportItem) {
        OrderItemTO orderItem = (OrderItemTO) reportItem;
        return orderItem.getOrder().getId() == order.getId();
    }
}
