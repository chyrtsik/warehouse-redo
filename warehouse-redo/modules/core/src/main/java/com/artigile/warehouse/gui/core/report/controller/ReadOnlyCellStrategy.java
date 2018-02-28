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
 * Read only cell editing strategy default implementation.
 */
public class ReadOnlyCellStrategy implements CellEditingStrategy {
    @Override
    public boolean isEditable(Object reportItem) {
        return false;
    }

    @Override
    public void saveValue(Object reportItem, Object newValue) {
        //Do nothing.
    }
}
