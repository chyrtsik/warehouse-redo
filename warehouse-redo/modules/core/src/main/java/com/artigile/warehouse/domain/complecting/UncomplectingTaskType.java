/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.complecting;

/**
 * @author Shyrik, 12.06.2009
 */

/**
 * Type of uncomplecting task (what warehouse worker should do).
 */
public enum UncomplectingTaskType {
    /**
     * Warehouse worker should decrease amount of wares in order item.
     */
    DECREASE_AMOUNT_OF_WARES,

    /**
     * Warehouse worker should remove full item from the order.
     */
    REMOVE_ORDER_ITEM,
}
