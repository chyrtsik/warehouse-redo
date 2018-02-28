/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.print.images;

import com.artigile.warehouse.domain.printing.PrintTemplateImage;
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.List;

/**
 * @author Shyrik, 24.01.2009
 */

/**
 * List of images, used for printing.
 */
public class ImagesList extends ReportDataSourceBase {
    @Override
    public String getReportTitle() {
        return I18nSupport.message("printing.image.list.title");
    }

    @Override
    public ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(PrintTemplateImage.class);
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("printing.image.list.name"), "name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("printing.image.list.notice"), "notice"));
        return reportInfo;
    }

    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return new ImagesListEditingStrategy();
    }

    @Override
    public List getReportData() {
        return SpringServiceContext.getInstance().getPrintTemplateService().getAllTemplateImages();
    }
}
