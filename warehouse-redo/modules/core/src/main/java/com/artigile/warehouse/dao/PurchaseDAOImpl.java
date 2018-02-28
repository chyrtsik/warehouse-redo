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
import com.artigile.warehouse.domain.purchase.Purchase;
import com.artigile.warehouse.domain.purchase.PurchaseState;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Shyrik, 01.03.2009
 */
public class PurchaseDAOImpl extends GenericEntityDAO<Purchase> implements PurchaseDAO {
    @Override
    public long getNextAvailablePurchaseNumber() {
        List result = getSession()
            .createCriteria(Purchase.class)
            .setProjection(Projections.max("number"))
            .list();
        return (result.size() > 0 && result.get(0) != null) ? (Long)result.get(0) + 1 : 1;
    }

    @Override
    public Purchase getPurchaseByNumber(long number) {
        List result = getSession()
            .createCriteria(Purchase.class)
            .add(Restrictions.eq("number", number))
            .list();

        if (result.size() > 0 ){
            return (Purchase)result.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Purchase> getPurchasesByStates(PurchaseState [] states) {
        Criteria criteria = getSession().createCriteria(Purchase.class);
        for (PurchaseState state : states){
            criteria.add(Restrictions.eq("state", state));
        }
        return criteria.list();
    }
}
