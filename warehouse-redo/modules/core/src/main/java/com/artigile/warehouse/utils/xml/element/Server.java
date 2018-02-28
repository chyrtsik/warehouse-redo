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

/**
 * @author Valery Barysok, 27.04.2010
 */
public class Server {
    private String name;
    private String dataSourceName;

    public Server(String name, String dataSourceName) {
        this.name = name;
        this.dataSourceName = dataSourceName;
    }

    public String getName() {
        return name;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }
}
