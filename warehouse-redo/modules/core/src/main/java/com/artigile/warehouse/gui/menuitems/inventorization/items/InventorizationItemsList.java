/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.inventorization.items;

import com.artigile.warehouse.bl.inventorization.inventorization.InventorizationService;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.inventorization.InventorizationItemTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 07.10.2009
 */

/**
 * Report for editing inventorization items.
 */
public class InventorizationItemsList extends ReportDataSourceBase {
    /**
     * Inventorization for editing.
     */
    private long inventorizationId;

    public InventorizationItemsList(long inventorizationId) {
        this.inventorizationId = inventorizationId;
    }

    @Override
    public String getReportTitle() {
        return "<not used>"; // NOI18N
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return null;
    }

    @Override
    public List getReportData() {
        InventorizationService inventorizationService = SpringServiceContext.getInstance().getInventorizationService();
        List<InventorizationItemTO> items = inventorizationService.getInventorizationItems(inventorizationId);
        return items == null ? new ArrayList<InventorizationItemTO>() : items;
    }

    @Override
    public boolean itemBelongsToReport(Object reportItem) {
        InventorizationItemTO inventorizationItem = (InventorizationItemTO) reportItem;
        return inventorizationItem.getInventorization().getId() == inventorizationId;
    }

    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(InventorizationItemTO.class);

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.items.list.number"), "number"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.items.list.inventorizationTaskNumber"), "taskNumber"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.items.list.itemType"), "itemType"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.items.list.itemName"), "itemName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.items.list.itemMisc"), "itemMisc"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.items.list.measureUnit"), "itemMeas"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.items.list.warehouse"), "warehouseName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.items.list.storagePlace"), "storagePlaceSign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.items.list.warehouseBatchNotice"), "warehouseBatchNotice"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.items.list.neededCount"), "neededCount",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.items.list.foundCount"), "foundCount",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.items.list.deviation"), "deviation"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.items.list.state"), "state.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("inventorization.items.list.processingResult"), "processingResult.name"));

        return reportInfo;
    }
}
