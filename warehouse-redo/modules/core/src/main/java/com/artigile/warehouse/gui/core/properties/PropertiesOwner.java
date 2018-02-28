/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties;

/**
 * @author Shyrik, 08.01.2009
 */

/**
 * Interface of the owner of the properties strategy.
 */
public interface PropertiesOwner {
    /**
     * Use it to close properties window with Ok result.
     */
    public void fireOK();

    /**
     * Use it to close properties window with Cancel result.
     */
    public void fireCancel();
}
