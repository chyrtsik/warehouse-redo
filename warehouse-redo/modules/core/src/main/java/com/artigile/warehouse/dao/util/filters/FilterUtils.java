/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao.util.filters;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public abstract class FilterUtils {

    /**
     * Applies data limitation to the given Hibernate criteria.
     *
     * @param criteria Hibernate criteria
     * @param dataLimit Results limitation
     */
    public static void applyLimitation(Criteria criteria, DataLimit dataLimit) {
        if (dataLimit != null) {
            criteria.setFirstResult(dataLimit.getFirstResult());
            if (dataLimit.getMaxResults() != null) {
                criteria.setMaxResults(dataLimit.getMaxResults());
            }
        }
    }

    /**
     * Applies data ordering to the given Hibernate criteria.
     *
     * @param criteria Hibernate criteria
     * @param dataOrder Results ordering
     */
    public static void applyOrdering(Criteria criteria, DataOrder dataOrder) {
        if (dataOrder != null) {
            for (String orderField : dataOrder.getOrderFields()) {
                if (dataOrder.getOrderType().isAscending()) {
                    criteria.addOrder(Order.asc(orderField));    
                } else if (dataOrder.getOrderType().isDescending()) {
                    criteria.addOrder(Order.desc(orderField));   
                }
            }
        }
    }
}
