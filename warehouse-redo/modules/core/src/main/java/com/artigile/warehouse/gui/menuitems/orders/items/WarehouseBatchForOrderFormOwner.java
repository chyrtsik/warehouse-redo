/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.orders.items;

/**
 * @author Shyrik, 11.04.2009
 */

/**
 * Interface, requered from owner of warehouse batch for order item form.
 * In helps in interaction between order item form and warehouse batch item form.
 */
public interface WarehouseBatchForOrderFormOwner {
    /**
     * This method should return count, that is been remaining to be placed into order item.
     * @return
     */
    long getRemainingCount();

    /**
     * This method is used for notifying owner about changing of count of details, have been
     * placed into order item from one of warehouse batches.
     * @param source - warehouse batch form, thiggered this notification.
     */
    void onWarehouseBatchCountChanged(WarehouseBatchForOrderForm source);

    /**
     * Should return true, if order item has any complecting problems.
     * @return
     */
    boolean hasComplectingProblems();
}
