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
 * @author Shyrik, 09.03.2009
 */

/**
 * Interface of listener of changes in data, made durung some operation via business logic.
 */
public interface DataChangeListener {
    /**
     * Called after some data has been deleted.
     * @param deletedData deleted data.
     */
    void afterDelete(Object deletedData);

    /**
     * Called after new data was saved.
     * @param createdData created data.
     */
    void afterCreate(Object createdData);

    /**
     * Called after some data has beed changed and saved.
     * @param changedData changed data.
     */
    void afterChange(Object changedData);
}
