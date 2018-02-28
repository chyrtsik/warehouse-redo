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
import com.artigile.warehouse.domain.inventorization.Inventorization;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Shyrik, 29.09.2009
 */
public class InventorizationDAOImpl extends GenericEntityDAO<Inventorization> implements InventorizationDAO {
    @Override
    public long getNextAvailableInventorizationNumber() {
        List result = getSession()
            .createCriteria(Inventorization.class)
            .setProjection(Projections.max("number"))
            .list();
        return (result.size() > 0 && result.get(0) != null) ? (Long)result.get(0) + 1 : 1;
    }

    @Override
    public Inventorization getInventorizationByNumber(long number) {
        List inventorizations = getSession()
            .createCriteria(Inventorization.class)
            .add(Restrictions.eq("number", number))
            .list();

        if (inventorizations.size() > 0 ){
            return (Inventorization)inventorizations.get(0);
        }
        return null;
    }
}
