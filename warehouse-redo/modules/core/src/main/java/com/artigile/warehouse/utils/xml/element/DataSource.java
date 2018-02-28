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
public class DataSource {
    private String name;
    private String poolName;

    public DataSource(String name, String poolName) {
        this.name = name;
        this.poolName = poolName;
    }

    public String getName() {
        return name;
    }

    public String getPoolName() {
        return poolName;
    }
}
