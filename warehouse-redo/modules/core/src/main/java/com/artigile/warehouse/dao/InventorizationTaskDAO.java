/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTask;
import com.artigile.warehouse.domain.warehouse.Warehouse;

import java.util.List;

/**
 * @author Borisok V.V., 02.10.2009
 */
public interface InventorizationTaskDAO extends EntityDAO<InventorizationTask> {
    /**
     * Returns next number, available for inventorization task.
     * @return
     */
    long getNextAvailableNumber();

    /**
     * Returns a list of actual (non closed) inventorization tasks for given warehouse.
     * @param warehouse
     * @return
     */
    List<InventorizationTask> getActualListForWarehouse(Warehouse warehouse);

    /**
     * Returns a list of actual (non closed) inventorization tasks.
     * @return
     */
    List<InventorizationTask> getAllActualTasks();
}
