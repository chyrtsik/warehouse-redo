/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.postings.items;

import com.artigile.warehouse.gui.core.report.controller.CellEditingStrategy;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;

/**
 * @author Shyrik, 17.02.2009
 */

/**
 * Base class of posting items fields editing strategy.
 */
public abstract class PostingItemCellEditingStrategy implements CellEditingStrategy {
    private PostingItemEditingAvailability availability = new PostingItemEditingAvailability();

    @Override
    public boolean isEditable(Object reportItem) {
        return availability.isAvailable((PostingItemTO)reportItem);
    }

    @Override
    public void saveValue(Object reportItem, Object newValue) {
        PostingItemTO postingItem = (PostingItemTO)reportItem;
        doSave(postingItem, newValue);
    }

    /**
     * Implement this method to perform correct saving of posting item's field value.
     * @param postingItem
     * @param newValue
     */
    protected abstract void doSave(PostingItemTO postingItem, Object newValue);
}
