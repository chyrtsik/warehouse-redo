/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.controller;

/**
 * @author Shyrik, 17.02.2009
 */

/**
 * Interface of cell editing strategy.
 */
public interface CellEditingStrategy {
    /**
     * Must return true, if cell is available for direct editing.
     * @return
     */
    public boolean isEditable(Object reportItem);

    /**
     * If cell editable, may validate and store of not store new value.
     * @param newValue
     */
    public void saveValue(Object reportItem, Object newValue);
}
