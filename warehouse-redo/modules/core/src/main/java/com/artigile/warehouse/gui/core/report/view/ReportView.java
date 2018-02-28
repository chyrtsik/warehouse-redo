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

import java.awt.*;
import java.util.List;

/**
 * @author Shyrik, 28.12.2008
 */

/**
 * Base abstract class of the report view.
 */
public interface ReportView {
    /**
     * Shall return current item of the report, or null, is there no selected items.
     * @return
     */
    public List getSelectedItems();

    /**
     * Shall return model index of current report column (if any).
     * @return index of column or -1.
     */
    public int getSelectedColumn();

    /**
     * Shall report list of displayed (after filtering) items.
     * @return
     */
    public List getDisplayedItems();

    /**
     * Shall return view component of the report.
     * @return
     */                                                                                                                                                          
    public Component getViewComponent();

    /**
     * Should return panel enclosing view component.
     * @return
     */
    public Component getContentPanel();

    /**
     * Add new report selection listener.
     * @param listener new listener.
     */
    public void addSelectionListener(ReportSelectionListener listener);

    /**
     * Remove report selection listener.
     * @param listener listener to be removed from a list.
     */
    public void removeSelectionListener(ReportSelectionListener listener);
}
