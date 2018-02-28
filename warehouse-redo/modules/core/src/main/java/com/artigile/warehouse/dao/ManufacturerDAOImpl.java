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
import com.artigile.warehouse.domain.directory.Manufacturer;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ManufacturerDAOImpl extends GenericEntityDAO<Manufacturer> implements ManufacturerDAO {
    @Override
    public Manufacturer getManufacturerByName(String name) {
        List manufacturers = getSession()
                .createCriteria(Manufacturer.class)
                .add(Restrictions.eq("name", name)).list();

        if (manufacturers.size() == 0) {
            return null;
        } else {
            return (Manufacturer) manufacturers.get(0);
        }
    }
}
