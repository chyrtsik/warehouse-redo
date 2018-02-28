/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.common.listeners;

/**
 * @author Shyrik, 09.03.2009
 */

/**
 * Interface of source of data change events.
 * Implementations of this interface fires events when some it's data was changed.
 */
public interface DataChangeEventsSource {
    /**
     * Subscribes new listener.
     * @param listener
     */
    void addDataChangeListener(DataChangeListener listener);

    /**
     * Unsubscribes event listener.
     * @param listener
     */
    void removeDataChangeListener(DataChangeListener listener);
}
