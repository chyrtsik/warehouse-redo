/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.print.templates;

import com.artigile.warehouse.bl.print.PrintTemplateService;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.template.PrintTemplateInstanceTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 03.12.2008
 */

/**
 * This class implements task for editing the list of printing templates.
 */
public class TemplateList extends ReportDataSourceBase {
    PrintTemplateService templateService = SpringServiceContext.getInstance().getPrintTemplateService();

    public String getReportTitle() {
        return I18nSupport.message("printing.template.list.title");
    }

    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(PrintTemplateInstanceTO.class);
        reportInfo.setRowStyleFactory(new PrintTemplateItemStyleFactory());
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("printing.template.list.templateType"), "template.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("printing.template.list.name"), "name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("printing.template.list.description"), "description"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("printing.template.list.fileName"), "templateFile.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("printing.template.list.fileDate"), "templateFile.storeDate"));
        return reportInfo;
    }

    public List getReportData() {
        return templateService.getAllTemplateInstances();
    }

    public ReportEditingStrategy getReportEditingStrategy() {
        return new TemplateListEditingStrategy();
    }
}
