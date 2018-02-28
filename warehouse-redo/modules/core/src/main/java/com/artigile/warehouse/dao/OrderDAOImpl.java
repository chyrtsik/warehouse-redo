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
import com.artigile.warehouse.domain.orders.Order;
import com.artigile.warehouse.domain.orders.OrderItem;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;

/**
 * @author Shyrik, 06.01.2009
 */
public class OrderDAOImpl extends GenericEntityDAO<Order> implements OrderDAO {

    @Override
    public List<Order> getOrdersWithoutDeleted() {
        List orders = getSession()
            .createCriteria(Order.class)
            .add(Restrictions.eq("deleted", false))
            .list();
        
        return orders;
    }

    @Override
    public Order get(Serializable primaryKey) {
        getSession().enableFilter("nonDeletedOrderItems");
        getSession().enableFilter("nonDeletedOrderSubItems");
        return super.get(primaryKey);
    }

    @Override
    public Order getOrderByNumber(long number){
        List orders = getSession()
            .createCriteria(Order.class)
            .add(Restrictions.eq("number", number))
            .add(Restrictions.eq("deleted", false))
            .list();

        if (orders.size() > 0 ){
            return (Order)orders.get(0);
        }
        return null;
    }

    @Override
    public long getNextAvailableOrderNumber(){
        List result = getSession()
            .createCriteria(Order.class)
            .add(Restrictions.eq("deleted", false))
            .setProjection(Projections.max("number"))
            .list();
        return (result.size() > 0 && result.get(0) != null) ? (Long)result.get(0) + 1 : 1;
    }

    @Override
    public long getNextAvailableOrderItemNumber(long orderId) {
        List result = getSession()
            .createCriteria(OrderItem.class)
            .setProjection(Projections.max("number"))
            .add(Restrictions.eq("order", get(orderId)))
            .add(Restrictions.eq("deleted", false))
            .list();
        return (result.size() > 0 && result.get(0) != null) ? (Long)result.get(0) + 1 : 1;
    }

    @Override
    public OrderItem getOrderItem(long orderItemId) {
        getSession().enableFilter("nonDeletedOrderSubItems");
        return (OrderItem) getSession().get(OrderItem.class, orderItemId);
    }

    @Override
    public List<OrderItem> getOrderItems(Order order) {
        List orderItems = getSession()
            .createCriteria(OrderItem.class)
            .add(Restrictions.eq("order", order))
            .list();
        return orderItems;
    }

    @Override
    public void saveOrderItem(OrderItem orderItem) {
        getSession().save(orderItem);
    }

    @Override
    public void deleteOrderItem(OrderItem orderItem) {
        //1. Deleting order item.
        getSession().delete(orderItem);

        //2. Update order items' numbers.
        List itemsToUpdate = getSession()
            .createCriteria(OrderItem.class)
            .add(Restrictions.and(
                Restrictions.eq("order", orderItem.getOrder()),
                Restrictions.gt("number", orderItem.getNumber()))
            )
            .list();

        for (Object item : itemsToUpdate){
            OrderItem updatingOrderItem = (OrderItem)item;
            updatingOrderItem.setNumber(updatingOrderItem.getNumber() - 1);
            saveOrderItem(updatingOrderItem);
        }
    }
}
