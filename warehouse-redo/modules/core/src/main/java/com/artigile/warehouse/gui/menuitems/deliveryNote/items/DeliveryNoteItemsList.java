/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.deliveryNote.items;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteItemTO;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteTO;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 05.11.2009
 */
/**
 * Report for editing delivery note items.
 */
public class DeliveryNoteItemsList extends ReportDataSourceBase {
    /**
     * Delivery note being edited.
     */
    private DeliveryNoteTOForReport deliveryNote;

    public DeliveryNoteItemsList(DeliveryNoteTOForReport deliveryNote) {
        this.deliveryNote = deliveryNote;
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
        DeliveryNoteTO deliveryNoteFull = SpringServiceContext.getInstance().getDeliveryNoteService().getDeliveryNoteFullData(deliveryNote.getId());
        return (deliveryNoteFull != null) ? deliveryNoteFull.getItems() : new ArrayList<DeliveryNoteItemTO>();
    }

    @Override
    public boolean itemBelongsToReport(Object reportItem) {
        DeliveryNoteItemTO deliveryNoteItem = (DeliveryNoteItemTO) reportItem;
        return deliveryNoteItem.getDeliveryNote().getId() == deliveryNote.getId();
    }

    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(DeliveryNoteItemTO.class);

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.items.list.number"), "number"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.items.list.itemType"), "itemType"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.items.list.itemName"), "itemName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.items.list.itemMisc"), "itemMisc"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.items.list.warehouseBatchNotice"), "warehouseBatchNotice"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.items.list.count"), "count",
                RightMiddleAlignStyleFactory.getInstance(),NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.items.list.countMeas"), "countMeas.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.items.list.price"), "price",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.items.list.totalPrice"), "totalPrice",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.items.list.vatRate"), "displayedVatRate",
                RightMiddleAlignStyleFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.items.list.vat"), "vat",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.items.list.totalPriceWithVat"), "totalPriceWithVat",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));

        return reportInfo;
    }
}
