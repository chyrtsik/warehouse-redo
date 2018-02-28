/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.contractors.shipping;

import com.artigile.warehouse.bl.contractors.ContractorService;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.ShippingTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Valery Barysok, 9/13/11
 */

public class ShippingList extends ReportDataSourceBase {

    ContractorService contractorService = SpringServiceContext.getInstance().getContractorService();

    private long contractorId;
    private boolean canEdit;

    private List<ShippingTO> shippingList;

    public ShippingList(long contractorId, boolean canEdit) {
        this.contractorId = contractorId;
        this.canEdit = canEdit;
    }


    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(ShippingTO.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("shippings.list.table.header.address"), "address"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("shippings.list.table.header.courier"), "courier"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("shippings.list.table.header.phone"), "phone"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("shippings.list.table.header.note"), "note"));
        return reportInfo;
    }

    @Override
    public String getReportTitle() {
        return "no Title for non framed table";
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new ShippingStrategy(canEdit);
    }

    @Override
    public List getReportData() {
        shippingList = contractorService.getShippingsByContractorId(contractorId);
        return shippingList;
    }

    public List<ShippingTO> getUpdatedShippings() {
        return shippingList;
    }
}
