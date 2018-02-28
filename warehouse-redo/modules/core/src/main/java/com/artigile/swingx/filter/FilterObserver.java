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

import org.jdesktop.swingx.decorator.Filter;

/**
 * <p>A FilterObserver instance receives notifications when the associated
 * FilterObservable instance updates the held filter.</p>
 *
 * @author Borisok V.V., 24.01.2009
 */
public interface FilterObserver {

    /**
     * <p>Notification made by the observer when the associated
     * FilterObservable instance updates the held filter.</p>
     */
    public void filterUpdated(FilterObservable obs, Filter newValue);
}
