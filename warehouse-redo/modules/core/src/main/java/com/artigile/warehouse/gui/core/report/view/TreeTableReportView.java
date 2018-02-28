/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.view;

import javax.swing.tree.TreePath;

/**
 * Interface for tree report view implementations.
 *
 * @author Aliaksandr.Chyrtsik, 25.10.11
 */
public interface TreeTableReportView extends ReportView {
    /**
     * @return TreePath for selected item in the tree.
     */
    TreePath getSelectedPath();
}
