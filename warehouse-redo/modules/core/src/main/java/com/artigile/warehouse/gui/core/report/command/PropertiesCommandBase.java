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
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.NoPropertiesPermissionException;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.core.report.model.ReportModel;

/**
 * @author Shyrik, 20.12.2008
 */
public abstract class PropertiesCommandBase extends ReportCommandBase {
    protected PropertiesCommandBase(AvailabilityStrategy availabilityStrategy) {
        super(new ResourceCommandNaming("report.command.properties"), availabilityStrategy);
    }

    @Override
    public ReportCommandType getType() {
        return ReportCommandType.PROPERTIES;
    }

    @Override
    public boolean execute(ReportCommandContext context) throws ReportCommandException {
        if (!isAvailable(context)) {
            throw new NoPropertiesPermissionException();
        }

        //Performing viewing or editing item
        Object currentItem = context.getCurrentReportItem();
        if (currentItem == null) {
            return false;
        }

        if (doProperties(currentItem, context)) {
            ReportModel reportModel = context.getReportModel();
            if (reportModel != null) {
                reportModel.fireItemDataChanged(currentItem);
            }
            return true;
        }

        return false;
    }

    /**
     * Must open item's properties and return result of the editing.
     *
     * @param editingItem
     * @param context     - current state of the report.
     * @return
     */
    protected abstract boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException;
}
