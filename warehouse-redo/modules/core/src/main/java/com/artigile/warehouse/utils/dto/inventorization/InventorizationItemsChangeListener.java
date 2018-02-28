/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.inventorization;

/**
 * @author Borisok V.V., 07.10.2009
 */
public interface InventorizationItemsChangeListener {
    /**
     * Called, when new item have been added to the inventorization.
     * @param newItem added item.
     */
    public void itemAdded(InventorizationItemTO newItem);

    /**
     * Called, when item have been removel from the inventorization.
     * @param removedItem removed item.
     */
    public void itemRemoved(InventorizationItemTO removedItem);

    /**
     * Called, when inventorization item has been changed.
     * @param changedItem
     */
    public void itemChanged(InventorizationItemTO changedItem);
}
