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
import com.artigile.warehouse.domain.needs.WareNeed;

/**
 * @author Shyrik, 25.02.2009
 */
public class WareNeedDAOImpl extends GenericEntityDAO<WareNeed> implements WareNeedDAO {

    @Override
    public WareNeed getWareNeedForEditing(long wareNeedId) {
        getSession().enableFilter("wareNeedItemsAvailableForEditing");
        return get(wareNeedId);
    }
}
