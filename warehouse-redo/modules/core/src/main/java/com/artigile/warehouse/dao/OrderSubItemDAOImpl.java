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
import com.artigile.warehouse.domain.orders.OrderSubItem;
import org.hibernate.SQLQuery;

import java.util.List;

/**
 * @author Shyrik, 06.04.2009
 */
public class OrderSubItemDAOImpl extends GenericEntityDAO<OrderSubItem> implements OrderSubItemDAO {
    @SuppressWarnings("unchecked")
    @Override
    public List<OrderSubItem> getOrderSubItemsForDeliveryNote(long deliveryNoteId) {
        String queryString = new StringBuilder()
            .append(" select {osi.*} from OrderSubItem osi, ComplectingTask ct, ChargeOffItem chi, DeliveryNoteItem dni ")
            .append(" where osi.deleted = 0 ")
            .append(" and osi.id = ct.ordersubitem_id ")
            .append(" and ct.chargeoffitem_id = chi.id ")
            .append(" and chi.deliverynoteitem_id = dni.id ")
            .append(" and dni.deliverynote_id = :note_id ")
            .toString();

        SQLQuery query = getSession().createSQLQuery(queryString);
        query.addEntity("osi", OrderSubItem.class);
        query.setParameter("note_id", deliveryNoteId);

        return query.list();
    }
}
