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

import java.util.List;

/**
 * @author Borisok V.V., 29.12.2008
 */
public interface ReportCommandList extends List<ReportCommand> {

    /**
     * @return default command for entire row.
     */
    public ReportCommand getDefaultCommandForRow();

    /**
     * Sets default command for entire row (for example when enter is pressed).
     * @param defaultCommand default command for row.
     */
    public void setDefaultCommandForRow(ReportCommand defaultCommand);

    /**
     * Returns default command for given column (is any).
     * @param column column model index to be checked.
     * @return default command for column. If not set then default command for row is returned.
     */
    public ReportCommand getDefaultCommandForColumn(int column);

    /**
     * Sets default command for column. This allows to override default commands for some of report columns.
     * @param column column model index.
     * @param defaultCommand default command for column.
     */
    public void setDefaultCommandForColumn(int column, ReportCommand defaultCommand);
}
