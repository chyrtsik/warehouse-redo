/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.chargeoff.items;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.utils.dto.chargeoff.ChargeOffItemTO;
import com.artigile.warehouse.utils.dto.chargeoff.ChargeOffTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 17.10.2009
 */
public class ChargeOffItemsList extends ReportDataSourceBase {
    /**
     * Charge off is being edited.
     */
    private ChargeOffTO chargeOff;


    public ChargeOffItemsList(ChargeOffTO chargeOff) {
        this.chargeOff = chargeOff;
    }

    @Override
    public String getReportTitle() {
        return null; //Not used now.
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return null; //Items are not able for editing now.
    }

    @Override
    public List getReportData() {
        return chargeOff.getItems();
    }

    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(ChargeOffItemTO.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("chargeOff.editor.items.list.number"), "number",
                RightMiddleAlignStyleFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("chargeOff.editor.items.list.itemName"), "itemName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("chargeOff.editor.items.list.itemMisc"), "itemMisc"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("chargeOff.editor.items.list.itemNotice"), "itemNotice"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("chargeOff.editor.items.list.warehouseName"), "warehouseName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("chargeOff.editor.items.list.storagePlaceSign"), "storagePlaceSign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("chargeOff.editor.items.list.count"), "count",
                RightMiddleAlignStyleFactory.getInstance(),
                NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("chargeOff.editor.items.list.countMeas"), "countMeas"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("chargeOff.editor.items.list.notice"), "notice"));
        return reportInfo;
    }
}
