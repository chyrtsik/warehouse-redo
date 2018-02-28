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
import com.artigile.warehouse.domain.properties.Property;
import org.hibernate.FlushMode;
import org.hibernate.criterion.Restrictions;

/**
 * @author Valery Barysok, 28.12.2009
 */
public class PropertyDAOImpl extends GenericEntityDAO<Property> implements PropertyDAO {
    @Override
    public void setProperty(String key, String value) {
        Property property = (Property) getSession()
            .createCriteria(Property.class)
            .add(Restrictions.eq("key", key))
            .uniqueResult();
        if (property == null) {
            save(new Property(key, value));
        } else {
            property.setValue(value);
            update(property);
        }
    }

    @Override
    public String getProperty(String key) {
        Property property = (Property) getSession()
            .createCriteria(Property.class)
            .setFlushMode(FlushMode.MANUAL) //Property queries are not dependent on any other data so flush is not required.
            .add(Restrictions.eq("key", key))
            .uniqueResult();
        return property != null ? property.getValue() : null;
    }
}
