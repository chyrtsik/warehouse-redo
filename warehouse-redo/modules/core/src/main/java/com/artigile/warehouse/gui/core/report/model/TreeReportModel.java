/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.model;

import java.util.List;

/**
 * @author Shyrik, 29.12.2008
 */

/**
 * Interface of the tree model.
 */
public interface TreeReportModel extends ReportModel {
    /**
     * Must return root object of the tree.
     * @return
     */
    public abstract Object getRoot();

    /**
     * Must return children of the givel tree item.
     * @param parent
     * @return - list of children or null, if no children found.
     */
    public abstract List getChildren(Object parent);
}
