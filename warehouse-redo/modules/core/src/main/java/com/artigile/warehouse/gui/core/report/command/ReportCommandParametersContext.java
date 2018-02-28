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

import java.util.List;

/**
 * @author Shyrik, 01.02.2009
 */

/**
 * Report command's context implementation for passing only parameter to the command.
 * This context object usially is created manually to execute commands not from report.
 */
public class ReportCommandParametersContext implements ReportCommandContext {
    /**
     * Parameter of the command.
     */
    private Object parameter;

    public ReportCommandParametersContext(Object parameter) {
        this.parameter = parameter;
    }

    @Override
    public Object getCurrentReportItem() {
        return null;
    }

    @Override
    public List getCurrentReportItems() {
        return null;
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
        return parameter;
    }
}
