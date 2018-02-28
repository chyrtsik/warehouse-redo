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

import com.artigile.warehouse.bl.complecting.UncomplectingTaskFilter;
import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.complecting.UncomplectingTask;
import com.artigile.warehouse.domain.warehouse.Warehouse;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Shyrik, 13.06.2009
 */
@SuppressWarnings("unchecked")
public class UncomplectingTaskDAOImpl extends GenericEntityDAO<UncomplectingTask> implements UncomplectingTaskDAO {
    @Override
    public List<UncomplectingTask> getListForWarehouse(Warehouse warehouse) {
        return getSession()
            .createCriteria(UncomplectingTask.class)
            .createAlias("orderSubItem", "osi")
            .createAlias("osi.warehouseBatch", "wb")
            .createAlias("wb.storagePlace", "sp")
            .add(Restrictions.eq("sp.warehouse", warehouse))
            .list();
    }

    /**
     * Loading list of uncomplecting tasks include given filter.
     * @param filter
     * @return
     */
    @Override
    public List<UncomplectingTask> getListByFilter(UncomplectingTaskFilter filter) {
        //1. Uncomplecting tasks for orders.
        return getListOfOrderTasks(filter);
    }

    /**
     * Retrieves list of order uncomplecting tasks including filter specified.
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<UncomplectingTask> getListOfOrderTasks(UncomplectingTaskFilter filter){
        Criteria criteria = getSession()
            .createCriteria(UncomplectingTask.class)
            .createAlias("orderSubItem", "osi")
            .createAlias("osi.storagePlace", "sp")
            .createAlias("sp.warehouse", "wh");

        if ( filter.getWarehouseId() != null ){
            //Filter by warehouse.
            criteria.add(Restrictions.eq("wh.id", filter.getWarehouseId()));
        }
        if ( filter.getStates() != null ){
            //Filter by uncomplecting task states.
            criteria.add(Restrictions.in("state", filter.getStates()));
        }
        if ( filter.getTaskIds() != null ){
            //Filter by task id's.
            criteria.add(Restrictions.in("id", filter.getTaskIds()));
        }
        return criteria.list();
    }
}
