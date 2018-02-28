/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Borisok V.V., 29.12.2008
 */
public class ReportCommandListImpl extends ArrayList<ReportCommand> implements ReportCommandList {

    private ReportCommand defaultRowCommand;

    private Map<Integer, ReportCommand> defaultColumnCommands;

    @Override
    public ReportCommand getDefaultCommandForRow() {
        return defaultRowCommand;
    }

    @Override
    public void setDefaultCommandForRow(ReportCommand defaultCommand) {
        this.defaultRowCommand = defaultCommand;
    }

    @Override
    public ReportCommand getDefaultCommandForColumn(int column) {
        ReportCommand columnCommand = null;
        if (defaultColumnCommands != null){
            columnCommand = defaultColumnCommands.get(column);
        }
        return columnCommand != null ? columnCommand : defaultRowCommand;
    }

    @Override
    public void setDefaultCommandForColumn(int column, ReportCommand defaultCommand) {
        if (defaultColumnCommands == null){
            defaultColumnCommands = new HashMap<Integer, ReportCommand>();
        }
        defaultColumnCommands.put(column, defaultCommand);
    }
}
