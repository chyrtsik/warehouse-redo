/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.controller;

import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;

/**
 * @author Shyrik, 06.12.2008
 */

/**
 * Interface of the report edinging strategies.
 */
public interface ReportEditingStrategy {
    /**
     * Used by report engine to get a list of command, that are applicable to the whole report.
     * For example, when user call context menu for the empty area of the report.
     *
     * @param commands - list of commands for report.
     * @param context  - current report state.
     * @return
     */
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context);

    /**
     * Used by report engine to get a list of command, that are applicable to the selected item.
     *
     * @param commands - list of commands for item.
     * @param context  - current report state.
     * @return
     */
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context);
}
