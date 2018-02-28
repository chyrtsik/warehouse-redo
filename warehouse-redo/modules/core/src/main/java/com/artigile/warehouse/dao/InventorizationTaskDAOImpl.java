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

import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTask;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTaskState;
import com.artigile.warehouse.domain.warehouse.Warehouse;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Borisok V.V., 02.10.2009
 */
public class InventorizationTaskDAOImpl extends GenericEntityDAO<InventorizationTask> implements InventorizationTaskDAO {

    @Override
    public long getNextAvailableNumber() {
        List result = getSession()
            .createCriteria(InventorizationTask.class)
            .setProjection(Projections.max("number"))
            //.add(Restrictions.ne("state", InventorizationTaskState.PROCESSED))
            .list();
        return (result.size() > 0 && result.get(0) != null) ? (Long)result.get(0) + 1 : 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<InventorizationTask> getActualListForWarehouse(Warehouse warehouse) {
        return getSession()
            .createCriteria(InventorizationTask.class)
            .createAlias("storagePlace", "sp")
            .add(Restrictions.eq("sp.warehouse", warehouse))
            .add(Restrictions.not(Restrictions.eq("state", InventorizationTaskState.CLOSED)))
            .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<InventorizationTask> getAllActualTasks() {
        return getSession()
            .createCriteria(InventorizationTask.class)
            .add(Restrictions.not(Restrictions.eq("state", InventorizationTaskState.CLOSED)))
            .list();
    }
}
