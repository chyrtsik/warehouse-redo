/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.utils.dataimport;

import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.utils.dto.dataimport.DataImportTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.text.MessageFormat;

/**
 * @author Aliaksandr.Chyrtsik, 06.11.11
 */
public final class DataImportReportInfoUtils {
    private DataImportReportInfoUtils(){
    }

    /**
     * Append data import fields to report info specified.
     * @param reportInfo
     */
    public static void addDataImportColumns(ReportInfo reportInfo) {
        if (!DataImportTO.class.isAssignableFrom(reportInfo.getDataClass())){
            throw new AssertionError(MessageFormat.format(
                    "This is not proper use data import fields. Type {0} should be derived from {1}",
                    reportInfo.getDataClass().getCanonicalName(), DataImportTO.class.getCanonicalName()
            ));
        }
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("data.import.list.created.by.user"), "user.displayName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("data.import.list.import.date"), "importDate"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("data.import.list.description"), "description"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("data.import.list.adapter"), "adapterName"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("data.import.list.import.status"), "importStatus.name"));
    }
}
