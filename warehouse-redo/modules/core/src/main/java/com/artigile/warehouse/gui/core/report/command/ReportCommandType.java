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

/**
 * @author Shyrik, 20.12.2008
 */

/**
 * Type of the commands, that are supported by the system.
 */
public enum ReportCommandType {
    /**
     * Command for creating new report item.
     */
    CREATE,

    /**
     * Command for creating copy of selected report item.
     */
    CREATE_COPY,

    /**
     * Command for deleting current report item.
     */
    DELETE,

    /**
     * Command for editing properties of the current report item in some editor (properties dialog or another).
     */
    PROPERTIES,

    /**
     * Custom command, that isn't directly supported by the report base classes.
     */
    CUSTOM,
}
