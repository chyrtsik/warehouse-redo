/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.purchase;

/**
 * @author Shyrik, 02.03.2009
 */
public interface PurchaseItemsChangeListener {
    /**
     * Called, when new item have been added to the purchase.
     * @param newItem added item.
     */
    public void itemAdded(PurchaseItemTO newItem);

    /**
     * Called, when item have been removel from the purchase.
     * @param removedItem removed item.
     */
    public void itemRemoved(PurchaseItemTO removedItem);

    /**
     * Called, when purchase item have been changed.
     * @param changedItem
     */
    public void itemChanged(PurchaseItemTO changedItem);
}
