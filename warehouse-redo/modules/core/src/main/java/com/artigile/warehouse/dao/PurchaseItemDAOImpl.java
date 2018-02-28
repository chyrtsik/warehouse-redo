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
import com.artigile.warehouse.domain.purchase.PurchaseItem;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Shyrik, 01.03.2009
 */
public class PurchaseItemDAOImpl extends GenericEntityDAO<PurchaseItem> implements PurchaseItemDAO {
    private PurchaseDAO purchaseDAO;

    @Override
    public long getNextAvailableNumber(long purchaseId) {
        List result = getSession()
            .createCriteria(PurchaseItem.class)
            .setProjection(Projections.max("number"))
            .add(Restrictions.eq("purchase", purchaseDAO.get(purchaseId)))
            .list();
        return (result.size() > 0 && result.get(0) != null) ? (Long)result.get(0) + 1 : 1;
    }

    //========================= Spring setters ==============================
    public void setPurchaseDAO(PurchaseDAO purchaseDAO) {
        this.purchaseDAO = purchaseDAO;
    }
}
