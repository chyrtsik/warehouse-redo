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
import com.artigile.warehouse.domain.admin.UserGroup;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Shyrik, 07.12.2008
 */
public class UserGroupDAOImpl extends GenericEntityDAO<UserGroup> implements UserGroupDAO {
    /**
     * Finds in database group of users with given name.
     * @param groupName
     * @return
     */
    public UserGroup getGroupByName(String groupName) {
        List groups = getSession()
            .createCriteria(UserGroup.class)
            .add(Restrictions.eq("name", groupName)).list();

        if (groups.size() == 0){
            return null;
        }
        else{
            return (UserGroup)groups.get(0);
        }
    }
}
