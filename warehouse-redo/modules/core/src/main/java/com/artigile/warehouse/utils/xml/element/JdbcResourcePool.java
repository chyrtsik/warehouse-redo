/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.xml.element;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Valery Barysok, 27.04.2010
 */
public class JdbcResourcePool {
    private String name;
    private Map<String, String> properties;

    public JdbcResourcePool(String name) {
        this.name = name;
        this.properties = new HashMap<String, String>();
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
