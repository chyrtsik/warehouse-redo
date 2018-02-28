/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.needs;

import com.artigile.warehouse.bl.needs.WareNeedService;
import com.artigile.warehouse.domain.needs.WareNeedItemState;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.DateColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.format.predefined.TimeColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.needs.WareNeedItemTO;
import com.artigile.warehouse.utils.dto.needs.WareNeedTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 28.02.2009
 */

/**
 * List of items of a ware need.
 */
public class WareNeedItemsList extends ReportDataSourceBase {
    /**
     * Ware need, which items are being editing in this list.
     */
    private WareNeedTO wareNeed;
    private long wareNeedId;

   private final static WareNeedService wareNeedsService = SpringServiceContext.getInstance().getWareNeedsService();

    public WareNeedItemsList(long wareNeedId, String reportMinor) {
        super(reportMinor);
        this.wareNeedId=wareNeedId;
        this.wareNeed = wareNeedsService.getWareNeedForEditing(wareNeedId);
    }

    @Override
    public String getReportTitle() {
        return "<not used>"; //Not used
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(WareNeedItemTO.class);

        //Original needs item's part of the header.
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.createDate"), "createDateTime",
                DateColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.createTime"), "createDateTime",
                TimeColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.createdUser"), "createdUser.displayName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.number"), "fullNumber"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.state"), "state.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.type"), "needType"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.name"), "needName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.misc"), "needMisc"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.count"), "count",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
//        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.minYear"), "minYear"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.maxPrice"), "maxPrice",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.currency"), "currency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.notice"), "notice"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.customer"), "customer.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.supplier"), "supplier.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.buyPrice"), "buyPrice",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("wareNeed.items.list.buyCurrency"), "buyCurrency.sign"));

        //Part of the header from detail batches list.
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("detail.batches.list.countMeas"), "detailBatch.countMeas.sign"));

        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new WareNeedItemsEditingStrategy(wareNeed);
    }

    @Override
    public List getReportData() {
        this.wareNeed = wareNeedsService.getWareNeedForEditing(wareNeedId);
        return getAllExceptClosed(wareNeed.getItems());
    }

    @Override
    public boolean itemBelongsToReport(Object reportItem) {
        WareNeedItemTO needItem = (WareNeedItemTO)reportItem;
        return needItem.getWareNeed().getId() == wareNeed.getId() && !needItem.getAutoCreated();
    }

    /**
     * Get all ware need items except where state of item is closed.
     * @param wareNeedItems  All ware need items.
     * @return  List of ware need items which don't contains items with closed state.
     */
    private List<WareNeedItemTO> getAllExceptClosed(List<WareNeedItemTO> wareNeedItems) {
        List<WareNeedItemTO> wareNeedItemsExceptClosed = new ArrayList<WareNeedItemTO>();
        for (WareNeedItemTO wareNeedItem : wareNeedItems) {
            if (!(wareNeedItem.getState().getEnumConstantName()).equals(WareNeedItemState.CLOSED.getEnumConstantName())) {
                wareNeedItemsExceptClosed.add(wareNeedItem);
            }
        }
        return wareNeedItemsExceptClosed;
    }
}
