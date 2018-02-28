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

import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.contractors.LoadPlace;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author IoaN, Jan 3, 2009
 */

public class LoadPlaceDAOImpl  extends GenericEntityDAO<LoadPlace> implements LoadPlaceDAO{
    public LoadPlace getLoadPlaceByName(String name) {
        List places = getSession()
            .createCriteria(LoadPlace.class)
            .add(Restrictions.eq("name", name))
            .list();

        if (places.size() > 0){
            return (LoadPlace)places.get(0);
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LoadPlace> getAllSortedByName() {
        return getSession()
                .createCriteria(LoadPlace.class)
                .addOrder(Order.asc("name"))
                .list();
    }
}
