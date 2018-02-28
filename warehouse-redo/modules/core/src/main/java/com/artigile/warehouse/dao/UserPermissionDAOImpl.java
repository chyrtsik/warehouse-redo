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
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.admin.UserPermission;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Shyrik, 07.12.2008
 */
public class UserPermissionDAOImpl extends GenericEntityDAO<UserPermission> implements UserPermissionDAO {
    /**
     * Loads right, that is specified by te given right id.
     * @param permissionType - the right type.
     * @return - right object or null, if no such right has been found.
     */
    public UserPermission getPermissionByType(PermissionType permissionType) {
        List rights = getSession()
            .createCriteria(UserPermission.class)
            .add(Restrictions.eq("rightType", permissionType)).list();

        if (rights.size() == 0){
            return null;
        }
        else{
            return (UserPermission)rights.get(0);
        }
    }
}
