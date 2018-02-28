/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.postings.items;

import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchService;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.DateColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 05.02.2009
 */

/**
 * Report for editing postingId items.
 */
public class PostingItemsList extends ReportDataSourceBase {
    /**
     * Editing posting's Id.
     */
    private long postingId;

    /**
     * Provider of information about last edited posting item.
     */
    private LastEditedItemProvider lastEditedItemProvider;

    private WarehouseBatchService warehouseBatchService = SpringServiceContext.getInstance().getWarehouseBatchService();

    public PostingItemsList(long postingId, LastEditedItemProvider lastEditedItemProvider) {
        this.postingId = postingId;
        this.lastEditedItemProvider = lastEditedItemProvider;
    }

    @Override
    public String getReportTitle() {
        return "<not used>"; //Not used
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(PostingItemTO.class);
        reportInfo.setRowStyleFactory(new PostingItemRowStyleFactory(lastEditedItemProvider));

        //Original postingId item's part of the header.
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.items.list.number"), "number"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.items.list.name"), "name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.items.list.oldPrice"), "detailBatch.buyPrice",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.items.list.originalPrice"), "originalPrice",
                new PostingItemPriceSaver(),
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.items.list.originalCurrency"), "originalCurrency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.items.list.saleCurrency"), "detailBatch.currency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.items.list.salePrice"), "salePrice",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.items.list.price"), "price",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.items.list.currency"), "posting.currency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.items.list.count"), "count",
                new PostingItemCountSaver(),
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.items.list.countMeas"), "detailBatch.countMeas.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.items.list.totalPrice"), "totalPrice",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.items.list.warehouse"), "warehouse.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.items.list.storagePlace"), "storagePlace.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.items.list.notice"), "notice"));
        if (warehouseBatchService.isTrackShelfLife()) {
            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("posting.items.list.shelf.life.date"), "shelfLifeDate",
                    new PostingItemStyleFactory(), DateColumnFormatFactory.getInstance()));
        }

        //Part of the header from detail batches list.
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.misc"), "detailBatch.misc"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.acceptance"), "detailBatch.acceptance"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.year"), "detailBatch.year"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.manufacturer"), "detailBatch.manufacturer.name"));

        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new PostingItemsEditingStrategy(postingId);
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getPostingsService().getPostingFullData(postingId).getItems();
    }

    @Override
    public boolean itemBelongsToReport(Object reportItem) {
        PostingItemTO postingItem = (PostingItemTO) reportItem;
        return postingItem.getPosting().getId() == postingId;
    }
}
