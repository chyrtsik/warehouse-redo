/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.bl.warehouse.WarehouseFilter;
import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.warehouse.Warehouse;

import java.util.List;
import java.util.Set;

/**
 * @author Borisok V.V., 21.12.2008
 */
public interface WarehouseDAO extends EntityDAO<Warehouse> {
    List<Warehouse> getAllSortedByName();

    Warehouse getWarehouseByName(String name);

    Set<Warehouse> getWarehousesByFilter(WarehouseFilter filter);
}
