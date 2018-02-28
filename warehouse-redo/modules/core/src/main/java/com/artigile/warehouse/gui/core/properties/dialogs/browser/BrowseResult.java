/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.browser;

import com.artigile.warehouse.gui.core.properties.dialogs.DialogResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 08.01.2009
 */

/**
 * Result of running browser.
 */
public class BrowseResult extends DialogResult {
    /**
     * Items, that have been selected in he browser.  
     */
    private List<Object> selectedItems = new ArrayList<Object>();

    /**
     * Use this constructor to create browse result with single selected item.
     * @param dialogResult
     * @param selectedItem
     */
    public BrowseResult(boolean dialogResult, Object selectedItem) {
        super(dialogResult);
        selectedItems.add(selectedItem);
    }

    /**
     * Use this constructor to create browse result with multiple selected items.
     * @param dialogResult
     * @param selectedItems
     */
    public BrowseResult(boolean dialogResult, List<Object> selectedItems) {
        super(dialogResult);
        this.selectedItems = selectedItems;
    }

    public List<Object> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<Object> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
