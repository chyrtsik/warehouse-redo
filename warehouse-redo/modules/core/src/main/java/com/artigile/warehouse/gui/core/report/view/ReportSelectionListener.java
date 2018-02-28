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
 * Interface of report selection listener.
 *
 * @author Aliaksandr.Chyrtsik, 25.10.11
 */
public interface ReportSelectionListener {
    /**
     * Called when selection has been changed. Use view interface to get selected items list.
     */
    void onSelectionChanged();
}
