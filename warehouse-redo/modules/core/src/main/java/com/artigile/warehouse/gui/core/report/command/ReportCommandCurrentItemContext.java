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

import com.artigile.warehouse.gui.core.report.model.ReportModel;
import com.artigile.warehouse.gui.core.report.view.ReportView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

/**
 * Report command context implementation for custom current item.
 * This context object usually is created manually to execute commands not from report.
 *
 * @author Aliaksandr.Chyrtsik, 01.09.11
 */
public class ReportCommandCurrentItemContext implements ReportCommandContext {
    /**
     * Current "current" items for the command.
     */
    private List<Object> currentItems = new ArrayList<Object>();

    public ReportCommandCurrentItemContext(Object currentItem) {
        this.currentItems.add(currentItem);
    }

    @Override
    public Object getCurrentReportItem() {
        return currentItems.get(0);
    }

    @Override
    public List getCurrentReportItems() {
        return currentItems;
    }

    @Override
    public ReportModel getReportModel() {
        return null;
    }

    @Override
    public ReportView getReportView() {
        return null;
    }

    @Override
    public Object getCommandParameters() {
        return null;
    }
}
