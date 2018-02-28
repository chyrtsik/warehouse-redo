/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.format;

/**
 * Interface for formatting values in the table columns.
 *
 * @see com.artigile.warehouse.gui.core.report.view.TableReportView#initTableCells()
 * @see com.artigile.warehouse.gui.core.report.view.cell.ReportCellRenderer#setValue(Object)
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public interface ColumnFormatFactory {

    /**
     * @param value Cell value in column
     * @return String representation of formatted value
     */
    String getFormattedValue(Object value);
}

