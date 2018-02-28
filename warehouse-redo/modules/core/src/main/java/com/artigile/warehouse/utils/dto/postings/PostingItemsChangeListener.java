/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.postings;

/**
 * @author Shyrik, 10.01.2009
 */

public interface PostingItemsChangeListener {
    /**
     * Called, when new item have been added to the posting.
     * @param newItem added item.
     */
    public void itemAdded(PostingItemTO newItem);

    /**
     * Called, when item have been removel from the posting.
     * @param removedItem removed item.
     */
    public void itemRemoved(PostingItemTO removedItem);

    /**
     * Called, when posting item have been changed.
     * @param changedItem
     */
    public void itemChanged(PostingItemTO changedItem);
}