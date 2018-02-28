/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.batches;

import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.core.report.view.TableReportView;

import javax.swing.*;

/**
 * @author Shyrik, 05.09.2009
 */
public class SortDetailBachesListCommand extends CustomCommand {
    /**
     * Name of the field with worn numbers of detail batch items.
     */
    private String sortNumField;

    public SortDetailBachesListCommand(String sortNumField) {
        super(new ResourceCommandNaming("detail.batches.command.sortAsPrice"), new PredefinedCommandAvailability(true));
        this.sortNumField = sortNumField;
    }

    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        TableReportView tableReportView = (TableReportView)context.getReportView();
        tableReportView.sortByReportField(sortNumField, SortOrder.ASCENDING);
        return true;
    }
}
