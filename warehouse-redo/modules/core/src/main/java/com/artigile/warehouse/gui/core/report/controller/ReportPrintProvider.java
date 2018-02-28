/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.controller;

import com.artigile.warehouse.domain.printing.PrintTemplateType;

/**
 * To enable report printing just implement this interface when implementing report data source
 * and pass interface implementation into report options.
 *
 * @author Aliaksandr.Chyrtsik, 30.09.11
 */
public interface ReportPrintProvider {
    /**
     * @return print template types to be used for report printing.
     */
    public PrintTemplateType[] getReportPrintTemplates();

    /**
     * @return printable version of report data.
     */
    public Object getReportDataForPrinting();
}
