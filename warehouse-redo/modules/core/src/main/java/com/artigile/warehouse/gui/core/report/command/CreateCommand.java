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
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.NoCreatePermissionException;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;

/**
 * @author Shyrik, 20.12.2008
 */

/**
 * Base implementation of the command for creating new report item.
 */
public abstract class CreateCommand extends ReportCommandBase {
    protected CreateCommand(AvailabilityStrategy availabilityStrategy) {
        super(new ResourceCommandNaming("report.command.create"), availabilityStrategy);
    }

    protected CreateCommand(NamingStrategy namingStrategy, AvailabilityStrategy availabilityStrategy) {
        super(namingStrategy, availabilityStrategy);
    }

    @Override
    public ReportCommandType getType() {
        return ReportCommandType.CREATE;
    }

    @Override
    public boolean execute(ReportCommandContext context) throws ReportCommandException {
        if (!isAvailable(context)) {
            throw new NoCreatePermissionException();
        }

        //Performing create item operation
        Object newItem = doCreate(context);
        if (newItem != null && context.getReportModel() != null) {
            context.getReportModel().addItem(newItem);
            return true;
        }

        return false;
    }

    /**
     * This method must return new report item or null, if, for example, user has been cancelled creation.
     *
     * @param context
     * @return
     */
    protected abstract Object doCreate(ReportCommandContext context) throws ReportCommandException;
}
