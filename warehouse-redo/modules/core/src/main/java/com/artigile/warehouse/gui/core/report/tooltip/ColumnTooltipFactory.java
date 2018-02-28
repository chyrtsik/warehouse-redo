/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.tooltip;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * It used to get tooltip text for the table columns.
 *
 * @see <code>WareHouse.configureSystemTooltips</code> - tooltips configuration.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public interface ColumnTooltipFactory {

    /**
     * @param cellRenderer Renderer of the current cell
     * @return Tooltip text for the current cell
     */
    String getTooltipText(DefaultTableCellRenderer cellRenderer);
}
