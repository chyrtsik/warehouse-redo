/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.movement.items;

import com.artigile.warehouse.bl.movement.MovementService;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchService;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.DateColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.movement.MovementItemTO;
import com.artigile.warehouse.utils.dto.movement.MovementTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 22.11.2009
 */

/**
 * Report for editing movement items.
 */
public class MovementItemsList extends ReportDataSourceBase {
    /**
     * Identifier of movement being edited.
     */
    private long movementId;

    private WarehouseBatchService warehouseBatchService = SpringServiceContext.getInstance().getWarehouseBatchService();

    public MovementItemsList(long movementId) {
        this.movementId = movementId;
    }

    @Override
    public String getReportTitle() {
        return "<not used>"; // NOI18N
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new MovementItemsEditingStrategy(getMovementService().getMovement(movementId));
    }

    @Override
    public List getReportData() {
        MovementTO movement = getMovementService().getMovementFullData(movementId);
        return movement.getItems();
    }

    @Override
    public boolean itemBelongsToReport(Object reportItem) {
        MovementItemTO movementItem = (MovementItemTO) reportItem;
        return movementItem.getMovement().getId() == movementId;
    }

    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(MovementItemTO.class);

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.number"), "number"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.state"), "state.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.processingResult"), "processingResult.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.itemType"), "itemType"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.itemName"), "itemName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.itemMisc"), "itemMisc"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.itemNotice"), "itemNotice"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.warehouseNotice"), "warehouseNotice"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.availableCount"), "availableCount"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.countToMove"), "count",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.countMeas"), "countMeas.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.fromStoragePlace"), "fromStoragePlace.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.shippedUser"), "shippedUser.displayName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.shippedDate"), "shippedDate"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.postedUser"), "postedUser.displayName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.postedDate"), "postedDate"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.buyPrice"), "price"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.buyPriceTotal"), "totalPrice"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.buyCurrency"), "detailBatch.currency.sign"));

        if (warehouseBatchService.isTrackPostingItem()){
            //Information about receipt date of warehouse batch.
            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.batchNo"), "batchNo"));
            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.receiptDate"), "receiptDate",
                    DateColumnFormatFactory.getInstance()));
        }

        if (warehouseBatchService.isTrackShelfLife()) {
            reportInfo.addColumn(new ColumnInfo(I18nSupport.message("movement.items.list.shelf.life.date"), "shelfLifeDate",
                    new MovementItemStyleFactory(), DateColumnFormatFactory.getInstance()));
        }

        return reportInfo;
    }

    private MovementService getMovementService() {
        return SpringServiceContext.getInstance().getMovementService();
    }
}

