/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.workarounds;

import java.util.EventListener;

/**
 * @author Borisok V.V., 02.02.2009
 */
public interface EnclosingScrollPaneListener extends EventListener {
    /**
     * invoke it after configureEnclosingScrollPane has been invoked
     */
    public void configure(EnclosingScrollPaneEvent event);

    /**
     * invoke it before unconfigureEnclosingScrollPane will be invoked
     */
    public void unconfigure(EnclosingScrollPaneEvent event);
}
