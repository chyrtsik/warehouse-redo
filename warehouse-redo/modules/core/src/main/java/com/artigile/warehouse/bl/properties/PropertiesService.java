/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.properties;

import com.artigile.warehouse.dao.PropertyDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Valery Barysok, 28.12.2009
 */
@Transactional
public class PropertiesService {
    private PropertyDAO propertyDAO;

    public PropertiesService() {
    }

    public void setProperty(String key, String value) {
        propertyDAO.setProperty(key, value);
    }

    public String getProperty(String key) {
        return propertyDAO.getProperty(key);
    }

    public void setPropertyDAO(PropertyDAO propertyDAO) {
        this.propertyDAO = propertyDAO;
    }
}
