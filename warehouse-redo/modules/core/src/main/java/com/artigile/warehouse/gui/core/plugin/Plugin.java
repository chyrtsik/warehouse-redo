/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.plugin;

/**
 * @author IoaN, 02.12.2008
 */
public interface Plugin {
    /**
     * This method is called, when it's time to perform actions, provided by plugin.
     *
     * @param params - parameters, that may be used by plugin.
     */
    void run(PluginParams params);
}
