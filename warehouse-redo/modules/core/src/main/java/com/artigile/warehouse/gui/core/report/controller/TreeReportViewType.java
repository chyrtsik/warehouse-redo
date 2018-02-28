/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.controller;

/**
 * Enumeration of all supported tree report view types.
 *
 * @author Aliaksandr.Chyrtsik, 25.10.11
 */
public enum TreeReportViewType {
    /**
     * Report view is a simple tree (only one column of data is used).
     */
    TREE,

    /**
     * Report view is a tree table (with any set of columns).
     */
    TREE_TABLE,
}
