/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.filter;

/**
 * <p>Interface to be implemented by any instance holding a filter than can be
 * updated dynamically.</p>
 *
 * <p>When the observable instance detects a change on the held filter,
 * it will report the update to the observers, in no prefixed order.</p>
 *
 * @author Borisok V.V., 24.01.2009
 */
public interface FilterObservable {

    /**
     * Adds an observer to receive filter change notifications.
     */
    public void addFilterObserver(FilterObserver observer);

    /**
     * Unregisters an observer, that will not receive no further
     * filter update notifications.
     */
    public void removeFilterObserver(FilterObserver observer);
}
