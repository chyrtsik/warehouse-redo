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

import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.warehouse.Warehouse;

import java.util.List;

/**
 * @author Ihar, Nov 29, 2008
 */
public interface UserDAO extends EntityDAO<User> {

    User getUserByLogin(String login);

    User getUserByNameOnProduct(String nameOnProduct);

    List<User> getUsersForWarehouse(Warehouse warehouse);
}
