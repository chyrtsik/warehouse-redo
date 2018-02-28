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

/**
 * @author Shyrik, 20.12.2008
 */

/**
 * Base implementation for the most of command objects. Provide different strategigies of naming
 * and checking the availability of the command.
 */
public abstract class ReportCommandBase implements ReportCommand {
    
    private NamingStrategy namingStrategy;
    private AvailabilityStrategy availabilityStrategy;

    protected ReportCommandBase(NamingStrategy namingStrategy, AvailabilityStrategy availabilityStrategy) {
        this.namingStrategy = namingStrategy;
        this.availabilityStrategy = availabilityStrategy;
    }

    @Override
    public String getName(ReportCommandContext context) {
        return namingStrategy.getName(context);
    }

    @Override
    public boolean isAvailable(ReportCommandContext context) {
        return availabilityStrategy.isAvailable(context);
    }
}
