/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.xml;

import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.artigile.warehouse.utils.xml.element.DataSource;
import com.artigile.warehouse.utils.xml.element.JdbcResourcePool;
import com.artigile.warehouse.utils.xml.element.Resources;
import com.artigile.warehouse.utils.xml.element.Server;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Valery Barysok, 27.04.2010
 */
public final class ResourcesInitializer {
    
    private static class ResourcesInitializerHolder {
        private static ResourcesInitializer instance;
        static {
            instance = new ResourcesInitializer();
            instance.initialize();
        }
    }

    private ClassLoader sessionClassLoader = null;
    private List<Resources> resourcesList = null;
    private Map<String, DataSource> serverToDataSource = null;
    private Map<String, JdbcResourcePool> dataSourceToPool = null;

    private ResourcesInitializer() {
        sessionClassLoader = WareHouse.getMainLoader();
        resourcesList = new ArrayList<Resources>();
        serverToDataSource = new HashMap<String, DataSource>();
        dataSourceToPool = new HashMap<String, JdbcResourcePool>();
    }

    public static ResourcesInitializer getInstance() {
        return ResourcesInitializerHolder.instance;
    }

    public void initialize() {
        try {
            URL url = new File(XMLHelper.RESOURCES_XML).toURI().toURL();
            initializeResources(url);
        } catch (MalformedURLException e) {
            LoggingFacade.logError(this, "Error while initializing database configuration from XML config file.", e);
        } catch (RuntimeException e) {
            URL url = sessionClassLoader.getResource(XMLHelper.DEFAULT_RESOURCES_XML);
            initializeResources(url);
        }
        buildServerToDataSource();
        buildDataSourceToPool();
    }

    private void buildDataSourceToPool() {
        Map<String, DataSource> nameToDataSource = new HashMap<String, DataSource>();
        for (Resources resources : getResourcesList()) {
            for (DataSource dataSource : resources.getDataSources()) {
                nameToDataSource.put(dataSource.getName(), dataSource);
            }
        }

        for (Resources resources : getResourcesList()) {
            for (Server server : resources.getServers()) {
                serverToDataSource.put(server.getName(), nameToDataSource.get(server.getDataSourceName()));
            }
        }
    }

    private void buildServerToDataSource() {
        Map<String, JdbcResourcePool> nameToPool = new HashMap<String, JdbcResourcePool>();

        for (Resources resources : getResourcesList()) {
            for (JdbcResourcePool jdbcResourcePool : resources.getJdbcResourcePools()) {
                nameToPool.put(jdbcResourcePool.getName(), jdbcResourcePool);
            }
        }

        for (Resources resources : getResourcesList()) {
            for (DataSource dataSource : resources.getDataSources()) {
                dataSourceToPool.put(dataSource.getName(), nameToPool.get(dataSource.getPoolName()));
            }
        }
    }

    private void initializeResources(URL url) {
        resourcesList.add(XMLHelper.processResourcesXML(url, sessionClassLoader));
    }

    public List<Resources> getResourcesList() {
        return resourcesList;
    }

    public List<String> getServers() {
        List<String> servers = new ArrayList<String>();
        for (Resources resources : getResourcesList()) {
            for (Server server : resources.getServers()) {
                servers.add(server.getName());
            }
        }
        return servers;
    }

    public JdbcResourcePool getJdbcResourcePoolByServer(String serverName) {
        DataSource dataSource = serverToDataSource.get(serverName);
        return dataSource != null ? dataSourceToPool.get(dataSource.getName()) : null;
    }
}
