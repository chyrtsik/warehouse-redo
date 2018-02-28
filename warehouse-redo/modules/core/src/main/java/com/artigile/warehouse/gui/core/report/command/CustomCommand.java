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

import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.naming.NamingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.NoPermissionException;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;

/**
 * @author Shyrik, 09.01.2009
 */

/**
 * Base class for custom command. It eliminates copy-pasting writing
 * code for returning type of the command and checking, if used has right to
 * execute such command.
 */
public abstract class CustomCommand extends ReportCommandBase {
    protected CustomCommand(NamingStrategy namingStrategy, AvailabilityStrategy availabilityStrategy) {
        super(namingStrategy, availabilityStrategy);
    }

    @Override
    public ReportCommandType getType() {
        return ReportCommandType.CUSTOM;
    }

    @Override
    public boolean execute(ReportCommandContext context) throws ReportCommandException {
        if (!isAvailable(context)){
            throw new NoPermissionException();
        }
        return doExecute(context);
    }

    /**
     * This is the singular method, that must be implemented by the derived classes.
     * @param context
     * @return
     * @throws ReportCommandException
     */
    protected abstract boolean doExecute(ReportCommandContext context) throws ReportCommandException;
}
