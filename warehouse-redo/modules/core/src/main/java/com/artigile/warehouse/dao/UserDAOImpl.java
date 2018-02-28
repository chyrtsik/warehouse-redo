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
import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.warehouse.Warehouse;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ihar, Nov 29, 2008
 */
public class UserDAOImpl extends GenericEntityDAO<User> implements UserDAO {

    public User getUserByLogin(String login) {
        return (User) getSession().createCriteria(User.class).add(Restrictions.eq("login", login)).uniqueResult();
    }

    @Override
    public User getUserByNameOnProduct(String nameOnProduct) {
        return (User) getSession().createCriteria(User.class).add(Restrictions.eq("nameOnProduct", nameOnProduct)).uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getUsersForWarehouse(Warehouse warehouse) {
        //TODO: learn, how can we aviod such stupid coding and make filtering only with the help of SQL.
        List<User> result = new ArrayList<User>();
        List<User> allUsers = getAll();
        for (User user : allUsers) {
            if (user.getWarehouses().contains(warehouse)) {
                result.add(user);
            }
        }
        return result;
    }
}
