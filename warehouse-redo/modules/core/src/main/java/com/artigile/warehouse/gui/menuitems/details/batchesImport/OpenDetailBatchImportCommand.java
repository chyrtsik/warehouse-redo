/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.batchesImport;

import com.artigile.warehouse.gui.core.report.command.PropertiesCommandBase;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;

/**
 * Command for opening content of detail batch import.
 *
 * @author Aliaksandr.Chyrtsik, 06.11.11
 */
public class OpenDetailBatchImportCommand extends PropertiesCommandBase {
    protected OpenDetailBatchImportCommand() {
        super(new PredefinedCommandAvailability(true));
    }

    @Override
    protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
        //TODO: implement
        throw new RuntimeException("Not implemented");
    }
}
