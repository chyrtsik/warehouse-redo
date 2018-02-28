/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.command.naming;

import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;

/**
 * @author Shyrik, 20.12.2008
 */

/**
 * Interface of the strategy for naming report command.
 */
public interface NamingStrategy {
    /**
     * Implementation must return name of the command (may use context information for customization the name).
     * @param context - current report state.
     * @return
     */
    String getName(ReportCommandContext context);
}
