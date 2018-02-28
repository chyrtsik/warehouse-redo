/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.common.listeners;

/**
 * @author Shyrik, 17.05.2009
 */
public class DataChangeAdapter implements DataChangeListener {
    @Override
    public void afterDelete(Object deletedData) {
        //Implement this method to handle deleting of data.
    }

    @Override
    public void afterCreate(Object createdData) {
        //Implement this method to handle creating of data.
    }

    @Override
    public void afterChange(Object changedData) {
        //Implement this method to handle changing of data.
    }
}
