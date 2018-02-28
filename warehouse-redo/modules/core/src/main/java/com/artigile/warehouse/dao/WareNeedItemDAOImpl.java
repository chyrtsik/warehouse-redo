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
import com.artigile.warehouse.domain.needs.WareNeedItem;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Shyrik, 25.02.2009
 */
public class WareNeedItemDAOImpl extends GenericEntityDAO<WareNeedItem> implements WareNeedItemDAO {
    private WareNeedDAO wareNeedDAO;

    @Override
    public Long getNextAvailableNumber(long wareNeedId) {
        List result = getSession()
            .createCriteria(WareNeedItem.class)
            .setProjection(Projections.max("number"))
            .add(Restrictions.eq("wareNeed", wareNeedDAO.get(wareNeedId)))
            .list();
        return (result.size() > 0 && result.get(0) != null) ? (Long)result.get(0) + 1 : 1;
    }

    @Override
    public Long getNextAvailableSubNumber(long wareNeedId, long number) {
        List result = getSession()
            .createCriteria(WareNeedItem.class)
            .setProjection(Projections.max("subNumber"))
            .add(Restrictions.eq("wareNeed", wareNeedDAO.get(wareNeedId)))
            .add(Restrictions.eq("number", number))
            .list();
        return (result.size() > 0 && result.get(0) != null) ? (Long)result.get(0) + 1 : 1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<WareNeedItem> findSameWareNeedItems(WareNeedItem needItem) {
        return getSession()
            .createCriteria(WareNeedItem.class)
            .add(Restrictions.eq("wareNeed", needItem.getWareNeed()))
            .add(Restrictions.eq("number", needItem.getNumber()))
            .add(Restrictions.not(Restrictions.eq("id", needItem.getId())))
            .add(Restrictions.eq("autoCreated", needItem.getAutoCreated()))
            .list();
    }

    //======================== Spring setters ==========================
    public void setWareNeedDAO(WareNeedDAO wareNeedDAO) {
        this.wareNeedDAO = wareNeedDAO;
    }
}
