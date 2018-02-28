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

import com.artigile.warehouse.bl.deliveryNote.DeliveryNoteFilter;
import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNote;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Shyrik, 01.11.2009
 */
public class DeliveryNoteDAOImpl extends GenericEntityDAO<DeliveryNote> implements DeliveryNoteDAO {
    @SuppressWarnings("unchecked")
    @Override
    public List<DeliveryNote> getDeliveryNotesByFilter(DeliveryNoteFilter filter) {
        Criteria criteria = getSession().createCriteria(DeliveryNote.class);
        if ( filter != null ){
            if (filter.getDestinationWarehouseId() != null){
                //Filtering by delibary note destination warehouse.
                criteria.createAlias("destinationWarehouse", "w")
                        .add(Restrictions.eq("w.id", filter.getDestinationWarehouseId()));
            }
            if (filter.getStates() != null){
                //Filtering by delivery note states.
                criteria.add(Restrictions.in("state", filter.getStates()));
            }
        }
        return criteria.list();
    }

    @Override
    public long getNextDeliveryNoteNumber() {
        List result = getSession()
            .createCriteria(DeliveryNote.class)
            .setProjection(Projections.max("number"))
            .list();
        return (result.size() > 0 && result.get(0) != null) ? (Long)result.get(0) + 1 : 1;
    }
}
