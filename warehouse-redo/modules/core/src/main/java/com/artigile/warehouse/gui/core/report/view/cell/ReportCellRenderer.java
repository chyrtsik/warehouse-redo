/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.view.cell;

import com.artigile.warehouse.gui.core.report.format.ColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.tooltip.ColumnTooltipFactory;
import com.artigile.warehouse.utils.StringUtils;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author IoaN, Feb 10, 2009
 */

public class ReportCellRenderer extends DefaultTableCellRenderer {

    /**
     * Format of value in the cell
     */
    private ColumnFormatFactory cellFormatFactory;

    /**
     * Tooltip for the cell
     */
    private ColumnTooltipFactory cellTooltipFactory;


    /* Constructors
    ------------------------------------------------------------------------------------------------------------------*/
    public ReportCellRenderer() {
        super();
    }

    public ReportCellRenderer(ColumnFormatFactory cellFormatFactory, ColumnTooltipFactory cellTooltipFactory) {
        super();
        this.cellFormatFactory = cellFormatFactory;
        this.cellTooltipFactory = cellTooltipFactory;
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void setValue(Object value) {
        String text = cellFormatFactory != null
                ? cellFormatFactory.getFormattedValue(value)
                : StringUtils.toString(value);
        if (text != null) {
            setText(text);
        } else {
            super.setValue(value);
        }
    }

    /**
     * @return Custom text of a tooltip.
     */
    @Override
    public String getToolTipText() {
        if (cellTooltipFactory != null) {
            return cellTooltipFactory.getTooltipText(this);
        }
        return null;
    }
}
