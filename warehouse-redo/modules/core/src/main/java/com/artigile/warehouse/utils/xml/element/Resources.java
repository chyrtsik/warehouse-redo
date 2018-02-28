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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Valery Barysok, 27.04.2010
 */
public class Resources {
    private List<Server> servers;
    private List<DataSource> dataSources;
    private List<JdbcResourcePool> jdbcResourcePools;
    private String version;

    public Resources() {
        servers = new ArrayList<Server>();
        dataSources = new ArrayList<DataSource>();
        jdbcResourcePools = new ArrayList<JdbcResourcePool>();
    }

    public List<Server> getServers() {
        return servers;
    }

    public List<DataSource> getDataSources() {
        return dataSources;
    }

    public List<JdbcResourcePool> getJdbcResourcePools() {
        return jdbcResourcePools;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
