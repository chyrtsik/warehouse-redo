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
import com.artigile.warehouse.domain.details.DetailGroup;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Shyrik, 03.01.2009
 */

public class DetailGroupDAOImpl extends GenericEntityDAO<DetailGroup> implements DetailGroupDAO {
    /**
     * @return list of root detail groups.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<DetailGroup> getRootGroups() {
        return getSession()
            .createCriteria(DetailGroup.class)
            .add(Restrictions.isNull("parentGroup"))
            .addOrder(Order.asc("sortNum"))
            .list();
    }
}
