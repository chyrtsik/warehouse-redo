/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.contractors.contacts;

import com.artigile.warehouse.bl.contractors.ContractorService;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.ContactTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author IoaN, Dec 11, 2008
 */

public class ContactsList extends ReportDataSourceBase {

    ContractorService contractorService = SpringServiceContext.getInstance().getContractorService();

    private long contractorId;

    private boolean canEdit;

    private List<ContactTO> contactsList;

    public ContactsList(long contractorId, boolean canEdit) {
        this.contractorId = contractorId;
        this.canEdit = canEdit;
    }

    @Override
    public String getReportTitle() {
        return "no Title for non framed table";
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(ContactTO.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contacts.list.table.header.full.name"), "fullName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contacts.list.table.header.appointment"), "appointment"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contacts.list.table.header.phone"), "phone"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contacts.list.table.header.email"), "email"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contacts.list.table.header.icqId"), "icqId"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contacts.list.table.header.skypeId"), "skypeId"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contacts.list.table.header.notice"), "notice"));
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new ContactsStrategy(canEdit);
    }

    @Override
    public List<ContactTO> getReportData() {
        contactsList = contractorService.getContactsByContractorId(contractorId);
        return contactsList;
    }

    public List<ContactTO> getUpdatedContacts() {
        return contactsList;
    }
}
