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
import com.artigile.warehouse.domain.properties.Property;

/**
 * @author Valery Barysok, 28.12.2009
 */
public interface PropertyDAO extends EntityDAO<Property> {
    void setProperty(String key, String value);

    String getProperty(String key);
}
