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
 * @author Shyrik, 08.01.2009
 */

/**
 * Enumerates types of the predefined report implementations.
 */
public enum PluginType {
    /**
     * Plugin is table report. Application uses it's standard implementation
     * class for this type of plugin.
     */
    TABLE_REPORT,

    /**
     * There are no detail implementation of the plugin (plugin fully implemented by
     * the programmer from).
     */
    CUSTOM,
}
