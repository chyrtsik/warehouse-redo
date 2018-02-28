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
import com.artigile.warehouse.domain.MenuItem;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Ihar, Nov 30, 2008
 */
public class MenuItemDAOImpl extends GenericEntityDAO<MenuItem> implements MenuItemDAO {
    @Override
    public MenuItem getByName(String name) {
        List result = getSession()
            .createCriteria(MenuItem.class)
            .add(Restrictions.eq("name", name))
            .list();
        return result.size() == 0 ? null : (MenuItem)result.get(0); 
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MenuItem> getAllSortedByName() {
        return getSession()
                .createCriteria(MenuItem.class)
                .addOrder(Order.asc("name"))
                .list();
    }
}
