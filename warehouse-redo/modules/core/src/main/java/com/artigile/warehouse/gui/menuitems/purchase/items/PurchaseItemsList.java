/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.purchase.items;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.purchase.PurchaseItemTO;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTO;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.Collections;
import java.util.List;

/**
 * @author Shyrik, 02.03.2009
 */
public class PurchaseItemsList extends ReportDataSourceBase {

    /**
     * Editing purchase.
     */
    private PurchaseTOForReport purchase;

    public PurchaseItemsList(PurchaseTOForReport purchase) {
        this.purchase = purchase;
    }

    @Override
    public String getReportTitle() {
        return "<not used>"; //Not used
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(PurchaseItemTO.class);

        //Original purchase item's part of the header.
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.items.list.number"), "number"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.items.list.type"), "itemType"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.items.list.name"), "itemName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.items.list.misc"), "itemMisc"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.items.list.price"), "price",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.items.list.count"), "count",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.items.list.countMeas"), "countMeas.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.items.list.totalPrice"), "totalPrice",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("purchase.items.list.notice"), "notice"));
        reportInfo.setRowStyleFactory(new PurchaseItemStyleFactory());
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new PurchaseItemsEditingStrategy(purchase);
    }

    @Override
    public List getReportData() {
        PurchaseTO purchaseFullData = SpringServiceContext.getInstance().getPurchaseService().getPurchaseFullData(purchase.getId());
        return purchaseFullData != null ? purchaseFullData.getItems() : Collections.emptyList();
    }

    @Override
    public boolean itemBelongsToReport(Object reportItem) {
        PurchaseItemTO purchaseItem = (PurchaseItemTO) reportItem;
        return purchaseItem.getPurchase().getId() == purchase.getId();
    }
}