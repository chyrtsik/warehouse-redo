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

import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;

/**
 * @author Shyrik, 20.12.2008
 */

/**
 * Interface of the command, provided for the report.
 */
public interface ReportCommand {
    /**
     * Type (identifier) of the command.
     * @return
     */
    ReportCommandType getType();

    /**
     * Executes the command.
     * @param context - current state of the report.
     * @return - true, if command have done all it's work. false, if nothing has been happened (but it's not a error),
     * for example, of user cancelled performing of the command.
     * @throws com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException
     */
    boolean execute(ReportCommandContext context) throws ReportCommandException;

    /**
     * Computes a name string of the command (usially for UI).
     * @param context - current state of the report.
     * @return - name of the command
     */
    String getName(ReportCommandContext context);

    /**
     * Used usially for user interface to check, how must be command control displaied. If command is not
     * available, its also diplayed, but as inactive control. And this lets user to know, that command
     * exists, but it's unavailable now.
     * @param context - current state of the report.
     * @return
     */
    boolean isAvailable(ReportCommandContext context);
}
