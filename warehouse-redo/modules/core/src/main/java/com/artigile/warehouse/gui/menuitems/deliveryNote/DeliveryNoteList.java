/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.deliveryNote;

import com.artigile.warehouse.bl.deliveryNote.DeliveryNoteFilter;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 01.11.2009
 */
public class DeliveryNoteList  extends ReportDataSourceBase {
    /**
     * Is set, than means a filter for loading of delivery notes.
     */
    DeliveryNoteFilter filter;

    /**
     * keep this constructor in order the reflection to be ab le to initialize instance of this object.
     */
    public DeliveryNoteList() {
    }

    public DeliveryNoteList(DeliveryNoteFilter filter) {
        this.filter = filter;
    }

    @Override
    public String getReportTitle() {
        return I18nSupport.message("deliveryNote.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(DeliveryNoteTOForReport.class);

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.number"), "number"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.state"), "state.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.shipmentDate"), "shipmentDate"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.shipmentUser"), "shipmentUser.displayName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.document"), "document"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.shipmentReason"), "shipmentReason"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.receiveDate"), "receiveDate"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.receiveUser"), "receiveUser.displayName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.fromLocation"), "fromLocation"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.toLocation"), "toLocation"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.currency"), "currency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.totalPrice"), "totalPrice",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.vatRate"), "displayedVatRate",
                RightMiddleAlignStyleFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.vat"), "vat",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.totalPriceWithVat"), "totalPriceWithVat",
                RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.notice"), "notice"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.form.number"), "blankNumber",
                RightMiddleAlignStyleFactory.getInstance(), null));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.brand"), "carBrand"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.state.number"), "carStateNumber",
                RightMiddleAlignStyleFactory.getInstance(), null));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.full.name"), "driverFullName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.owner"), "carOwner"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("deliveryNote.list.trailer"), "carTrailer"));

        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new DeliveryNoteEditingStrategy();
    }

    @Override
    public List getReportData() {
        if ( filter == null ){
            return SpringServiceContext.getInstance().getDeliveryNoteService().getAllDeliveryNotesForReport();
        }
        else{
            return SpringServiceContext.getInstance().getDeliveryNoteService().getDeliveryNotesForReportByFilter(filter); 
        }
    }
}
