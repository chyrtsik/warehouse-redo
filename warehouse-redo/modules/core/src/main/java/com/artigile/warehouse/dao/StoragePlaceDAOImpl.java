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

import com.artigile.warehouse.bl.warehouse.StoragePlaceFilter;
import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Borisok V.V., 21.12.2008
 */
public class StoragePlaceDAOImpl extends GenericEntityDAO<StoragePlace> implements StoragePlaceDAO {
    @Override
    @SuppressWarnings("unchecked")
    public List<StoragePlace> getListByFilter(StoragePlaceFilter filter) {
        Criteria criteria = getSession().createCriteria(StoragePlace.class);

        if (filter.getWarehouseId() != null){
            //Filter by warehouse.
            criteria.createAlias("warehouse", "wh").add(Restrictions.eq("wh.id", filter.getWarehouseId()));
        }

        if (filter.isAvailableForPosting()){
            //Filter by availability to perform posting into storage place.
            criteria.add(Restrictions.eq("availableForPosting", true));
        }

        return criteria.list();
    }
}
