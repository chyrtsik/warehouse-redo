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
 * @author Shyrik, 22.11.2009
 */

/**
 * This class implements AND constraint for command availability.
 * It holds a list of another availability strategies. Is <STRONG>every availability strategy</STRONG>
 * says, that command is available, that command considered to be available.
 * If at least one strategy says "no", then command is not available.
 */
public class MultipleAndCriteriaCommandAvailability implements AvailabilityStrategy {
    private AvailabilityStrategy[] criteria;

    public MultipleAndCriteriaCommandAvailability(AvailabilityStrategy ...criteria){
        this.criteria = criteria;
    }

    @Override
    public boolean isAvailable(ReportCommandContext context) {
        for (AvailabilityStrategy criterion : criteria){
            if (!criterion.isAvailable(context)){
                return false;
            }
        }
        return true;
    }
}
