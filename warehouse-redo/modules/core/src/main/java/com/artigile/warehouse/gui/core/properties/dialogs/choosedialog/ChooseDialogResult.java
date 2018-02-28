/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.choosedialog;

import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.dialogs.DialogResult;

import java.util.List;

/**
 * @author Shyrik, 05.01.2009
 */

/**
 * Result of runnig choose dialogs.
 */
public class ChooseDialogResult extends DialogResult {
    /**
     * Items, that have been selected by the user.
     */
    private List<ListItem> selectedItems;

    public ChooseDialogResult(boolean dialogResult, List<ListItem> selectedItems) {
        super(dialogResult);
        this.selectedItems = selectedItems;
    }

    public List<ListItem> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<ListItem> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
