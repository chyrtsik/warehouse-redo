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
import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.domain.warehouse.Warehouse;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Borisok V.V., 21.12.2008
 */
public class WarehouseDAOImpl extends GenericEntityDAO<Warehouse> implements WarehouseDAO {
    @Override
    @SuppressWarnings("unchecked")
    public List<Warehouse> getAllSortedByName() {
        return getSession()
                .createCriteria(Warehouse.class)
                .addOrder(Order.asc("name"))
                .list();
    }

    @Override
    public Warehouse getWarehouseByName(String name) {
        List warehouses = getSession()
                .createCriteria(Warehouse.class)
                .add(Restrictions.eq("name", name)).list();

        if (warehouses.isEmpty()) {
            return null;
        } else {
            return (Warehouse) warehouses.get(0);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Warehouse> getWarehousesByFilter(WarehouseFilter filter) {
        Criteria criteria = getSession().createCriteria(Warehouse.class, "wh");

        if (filter.isOnlyAvailableForPosting()){
            //Filter by having storage places, that are available for posting.
            DetachedCriteria storagePlaceCritetia = DetachedCriteria.forClass(StoragePlace.class, "sp");
                storagePlaceCritetia.add(Restrictions.eqProperty("sp.warehouse.id", "wh.id"))
                .add(Restrictions.eq("sp.availableForPosting", true))
                .setProjection(Projections.property("sp.id"));
            criteria.add(Subqueries.exists(storagePlaceCritetia));
        }

        if (filter.getExcludedWarehouseIds() != null){
            //Filter, that excludes from result given warehouse identifiers.
            criteria.add(Restrictions.not(Restrictions.in("wh.id", filter.getExcludedWarehouseIds())));
        }

        return new HashSet<Warehouse>(criteria.list());
    }
}
