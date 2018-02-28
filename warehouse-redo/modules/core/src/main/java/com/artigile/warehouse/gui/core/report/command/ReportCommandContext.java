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
 * @author Shyrik, 20.12.2008
 */

/**
 * Context for the command. Provides data, that may be helpfull for
 * the executing the command (or validating it, for example).
 */
public interface ReportCommandContext {
    /**
     * Gets current (selected) report item (or null, if there is no current item).
     *
     * @return
     */
    public Object getCurrentReportItem();

    /**
     * Gets current report items.
     *
     * @return
     */
    public List getCurrentReportItems();

    /**
     * Gets model of the report.
     *
     * @return
     */
    public ReportModel getReportModel();

    /**
     * Gets view of the report.
     *
     * @return
     */
    public ReportView getReportView();

    /**
     * Gets parameters, that may depend on concrete command.
     *
     * @return
     */
    public Object getCommandParameters();
}
