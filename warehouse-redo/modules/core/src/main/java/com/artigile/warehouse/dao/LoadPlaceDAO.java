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

import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.contractors.LoadPlace;

import java.util.List;

/**
 * @author IoaN, Jan 3, 2009
 */

public interface LoadPlaceDAO extends EntityDAO<LoadPlace> {
    /**
     * Gets load place with the given name.
     * @param name
     * @return
     */
    LoadPlace getLoadPlaceByName(String name);

    List<LoadPlace> getAllSortedByName();
}
