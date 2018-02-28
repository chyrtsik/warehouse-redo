/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.command.availability;

import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;

/**
 * @author Shyrik, 20.12.2008
 */
public class PredefinedCommandAvailability implements AvailabilityStrategy {
    
    private boolean available;

    public PredefinedCommandAvailability(boolean available){
        this.available = available;
    }

    @Override
    public boolean isAvailable(ReportCommandContext context) {
        return available;
    }
}
