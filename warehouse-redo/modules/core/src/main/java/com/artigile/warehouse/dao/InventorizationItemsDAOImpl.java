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
import com.artigile.warehouse.domain.inventorization.InventorizationItem;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Borisok V.V., 09.10.2009
 */
public class InventorizationItemsDAOImpl extends GenericEntityDAO<InventorizationItem> implements InventorizationItemsDAO {
    private InventorizationDAO inventorizationDAO;

    public long getNextAvailableNumber(long inventorizationId) {
        List result = getSession()
            .createCriteria(InventorizationItem.class)
            .setProjection(Projections.max("number"))
            .add(Restrictions.eq("inventorization", inventorizationDAO.get(inventorizationId)))
            .list();
        return (result.size() > 0 && result.get(0) != null) ? (Long)result.get(0) + 1 : 1;
    }

    //========================= Spring setters ==============================
    public void setInventorizationDAO(InventorizationDAO inventorizationDAO) {
        this.inventorizationDAO = inventorizationDAO;
    }
}
