/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.bl.complecting.ComplectingTaskFilter;
import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.complecting.ComplectingTask;
import com.artigile.warehouse.domain.orders.OrderSubItem;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Shyrik, 28.04.2009
 */
public class ComplectingTaskDAOImpl extends GenericEntityDAO<ComplectingTask> implements ComplectingTaskDAO {
    @Override
    public ComplectingTask getTaskForOrderSubItem(OrderSubItem subItem) {
        List result = getSession()
            .createCriteria(ComplectingTask.class)
            .add(Restrictions.eq("orderSubItem", subItem))
            .list();
        return (ComplectingTask)(result.size() > 0  ? result.get(0) : null);  
    }

    /**
     * Loading list of complecting tasks includen given filter.
     * @param filter
     * @return
     */
    @Override
    public List<ComplectingTask> getListByFilter(ComplectingTaskFilter filter) {
        //1. Complecting tasks for orders.
        List<ComplectingTask> tasksList = getListOfOrderTasks(filter);

        //2. Complecting tasks for movements.
        tasksList.addAll(getListOfMovementTasks(filter));

        return tasksList;
    }

    /**
     * Retrieves list of order complecting tasks including filter specified.
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<ComplectingTask> getListOfOrderTasks(ComplectingTaskFilter filter){
        Criteria criteria = getSession()
            .createCriteria(ComplectingTask.class)
            .createAlias("orderSubItem", "osi")
            .createAlias("osi.warehouseBatch", "wb")
            .createAlias("wb.storagePlace", "sp")
            .createAlias("sp.warehouse", "wh");

        if ( filter.getWarehouseId() != null ){
            //Filter by warehouse.
            criteria.add(Restrictions.eq("wh.id", filter.getWarehouseId()));
        }
        if ( filter.getStates() != null ){
            //Filter by complecting task states.
            criteria.add(Restrictions.in("state", filter.getStates()));
        }
        if ( filter.getTaskIds() != null ){
            //Filter by task id's.
            criteria.add(Restrictions.in("id", filter.getTaskIds()));
        }
        return criteria.list();
    }

    /**
     * Retrieves list of movement complecting tasks including filter specified.
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<ComplectingTask> getListOfMovementTasks(ComplectingTaskFilter filter) {
        Criteria criteria = getSession()
            .createCriteria(ComplectingTask.class)
            .createAlias("movementItem", "mi")
            .createAlias("mi.movement", "m")
            .createAlias("m.fromWarehouse", "wh");

        if ( filter.getWarehouseId() != null ){
            //Filter by warehouse.
            criteria.add(Restrictions.eq("wh.id", filter.getWarehouseId()));
        }
        if ( filter.getStates() != null ){
            //Filter by complecting task states.
            criteria.add(Restrictions.in("state", filter.getStates()));
        }
        if ( filter.getTaskIds() != null ){
            //Filter by task id's.
            criteria.add(Restrictions.in("id", filter.getTaskIds()));
        }
        return criteria.list();
    }
}
