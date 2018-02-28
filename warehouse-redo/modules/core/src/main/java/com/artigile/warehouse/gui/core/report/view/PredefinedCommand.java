/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.view;

/**
 * @author Valery Barysok, 05.03.2010
 */

/**
 * Enum of predefined buttons of reports such as refresh, sort and filter buttons.
 * if you add/remove some predefined buttons then you must go to
 * {@link com.artigile.warehouse.gui.core.report.view.TableReportView}
 * where make some appropriate changes for map between command and button
 */
public enum PredefinedCommand {
    REFRESH,
    SORT,
    FILTER,
    PRINT,
    PRINT_PREVIEW,
}
