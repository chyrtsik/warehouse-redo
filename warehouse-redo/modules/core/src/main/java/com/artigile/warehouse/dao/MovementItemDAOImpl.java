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
import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.movement.Movement;
import com.artigile.warehouse.domain.movement.MovementItem;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Shyrik, 06.12.2009
 */
public class MovementItemDAOImpl extends GenericEntityDAO<MovementItem> implements MovementItemDAO {

    @Override
    public long getNextAvailableMovementItemNumber(Movement movement) {
        List result = getSession()
            .createCriteria(MovementItem.class)
            .setProjection(Projections.max("number"))
            .add(Restrictions.eq("movement", movement))
            .list();
        return (result.size() > 0 && result.get(0) != null) ? (Long)result.get(0) + 1 : 1;
    }

    @Override
    public MovementItem findSameMovementItem(long movementId, long warehouseBatchId) {
        String queryString = "from MovementItem where movement.id = :movementId and warehouseBatch.id = :warehouseBatchId";
        Query query = getSession().createQuery(queryString);
        query.setParameter("movementId", movementId);
        query.setParameter("warehouseBatchId", warehouseBatchId);
        List result = query.list();
        return result.size() > 0 ? (MovementItem)result.get(0) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MovementItem> getMovementItemsForDeliveryNote(long deliveryNoteId) {
        String queryString = "from MovementItem where complectingTask.chargeOffItem.deliveryNoteItem.deliveryNote.id = :deliveryNoteId";
        Query query = getSession().createQuery(queryString);
        query.setParameter("deliveryNoteId", deliveryNoteId);
        return query.list();
    }

    @Override
    public List<MovementItem> findByFilter(DetailBatchReservesFilter filter) {
        Criteria criteria = getSession().createCriteria(MovementItem.class);
        if (filter.getStoragePlaceId() != null) {
            criteria.add(Restrictions.eq("fromStoragePlace.id", filter.getStoragePlaceId()));
        }
        if (filter.getDetailBatchId() != null) {
            criteria.add(Restrictions.eq("detailBatch.id", filter.getDetailBatchId()));
        }
        if (!filter.getMovementItemStates().isEmpty()) {
            criteria.add(Restrictions.in("state", filter.getMovementItemStates()));
        }
        return criteria.list();
    }
}
