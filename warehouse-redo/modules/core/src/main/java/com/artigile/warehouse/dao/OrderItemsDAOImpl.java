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

import com.artigile.warehouse.bl.detail.DetailBatchReservesFilter;
import com.artigile.warehouse.bl.orders.OrderItemFilter;
import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.orders.OrderItem;
import com.artigile.warehouse.domain.orders.OrderSubItem;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Shyrik, 05.03.2010
 */
public class OrderItemsDAOImpl extends GenericEntityDAO<OrderItem> implements OrderItemsDAO {

    @Override
    public OrderItem findSameOrderItem(long orderId, long detailBatchId) {
        List result = getSession()
            .createCriteria(OrderItem.class)
            .add(Restrictions.eq("order.id", orderId))
            .add(Restrictions.eq("detailBatch.id", detailBatchId))
            .add(
                Restrictions.or(
                  Restrictions.not(Restrictions.eq("deleted", true)),
                  Restrictions.isNull("deleted")
                )
            )
            .list();

        return result.size() > 0 ? (OrderItem)result.get(0) : null;            
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OrderItem> findByFilter(OrderItemFilter filter) {
        Criteria criteria = getSession().createCriteria(OrderItem.class);
        if (filter.getDetailBatchID() != null) {
            criteria.add(Restrictions.eq("detailBatch.id", filter.getDetailBatchID()));
        }
        if (!filter.getStates().isEmpty()) {
            criteria.add(Restrictions.in("state", filter.getStates()));
        }
        criteria.add(
            Restrictions.or(
              Restrictions.not(Restrictions.eq("deleted", true)),
              Restrictions.isNull("deleted")
            )
        );
        return criteria.list();
    }

    @Override
    public List<OrderSubItem> findByFilter(DetailBatchReservesFilter filter) {
        Criteria criteria = getSession().createCriteria(OrderSubItem.class);
        if (filter.getStoragePlaceId() != null) {
            criteria.add(Restrictions.eq("storagePlace.id", filter.getStoragePlaceId()));
        }
        Criteria orderItem = criteria.createCriteria("orderItem");
        if (filter.getDetailBatchId() != null) {
            orderItem.add(Restrictions.eq("detailBatch.id", filter.getDetailBatchId()));
        }
        if (!filter.getMovementItemStates().isEmpty()) {
            criteria.add(Restrictions.in("state", filter.getMovementItemStates()));
        }
        orderItem.add(
                Restrictions.or(
                        Restrictions.not(Restrictions.eq("deleted", true)),
                        Restrictions.isNull("deleted")
                )
        );
        return criteria.list();
    }
}
