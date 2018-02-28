/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.choose.choosedialog.sort;

import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;

import javax.swing.*;

/**
 * @author Borisok V.V., 17.01.2009
 */
public class SortItem extends ListItem {

    private SortOrder sortOrder;

    public SortItem(String title, Object value) {
        super(title, value);
        setSortOrder(SortOrder.ASCENDING);
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }
}
