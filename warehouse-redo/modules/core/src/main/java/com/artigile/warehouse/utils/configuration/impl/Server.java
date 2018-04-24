/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.configuration.impl;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Valery Barysok, 27.04.2010
 */
public class Server {
    private String name;
    private Map<String, String> properties = new TreeMap<>();

    public Server(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

}
