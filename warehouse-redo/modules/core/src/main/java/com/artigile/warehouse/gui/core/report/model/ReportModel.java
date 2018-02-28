/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.model;

import com.artigile.warehouse.gui.core.report.controller.ReportDataSource;

/**
 * @author Borisok V.V., 25.12.2008
 */

public interface ReportModel {

    /**
     * Adding new item to the report's model. Calling to this method fiers
     * changed event.
     *
     * @param newItem
     */
    public void addItem(Object newItem);

    /**
     * Inserts new item into the report modes into specified location.
     * @param newItem - item to be inserted.
     * @param insertIndex - new item's location.
     */
    public void insertItem(Object newItem, int insertIndex);

    /**
     * Deleting item from the report's model. Calling to this method fiers
     * changed event.
     *
     * @param item
     */
    public void deleteItem(Object item);

    /**
     * Retrieves item of the report by it's index.
     *
     * @param itemIndex
     * @return
     */
    public Object getItem(int itemIndex);

    /**
     * Implementation of this method should find the same item in report and update it from the
     * given one.
     * @param item - item with fresh data.
     */
    void setItem(Object item);

    /**
     * Retrieves count of the intems in the report.
     *
     * @return
     */
    public int getItemCount();

    /**
     * Notifies all listeners that all data values in the report
     * may have changed.
     */
    public void fireDataChanged();

    /**
     * Notifies all listeners, that data of the given report item have been changed.
     * @param item changed item.
     */
    public void fireItemDataChanged(Object item);

    /**
     * Retrieves data source for report.
     * @return
     */
    public ReportDataSource getReportDataSource();

    /**
     * Refreshes data, stored in the model.
     */
    public void refresh();
}
