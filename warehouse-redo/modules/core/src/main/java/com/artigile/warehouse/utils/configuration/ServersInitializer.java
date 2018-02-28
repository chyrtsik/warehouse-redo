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
import com.artigile.warehouse.utils.configuration.impl.XMLHelper;
import com.artigile.warehouse.utils.configuration.impl.Server;
import com.artigile.warehouse.utils.configuration.impl.Servers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valery Barysok, 27.04.2010
 */
public final class ServersInitializer {

    private static class ResourcesInitializerHolder {
        private static ServersInitializer instance;
        static {
            instance = new ServersInitializer();
            instance.initialize();
        }
    }

    private ClassLoader sessionClassLoader;
    private Servers servers;

    private ServersInitializer() {
        sessionClassLoader = WareHouse.getMainLoader();
    }

    public static ServersInitializer getInstance() {
        return ResourcesInitializerHolder.instance;
    }

    public void initialize() {
        try {
            URL url = new File(XMLHelper.SERVERS_XML).toURI().toURL();
            initializeServers(url);
        } catch (MalformedURLException e) {
            LoggingFacade.logError(this, "Error while initializing database configuration from XML config file.", e);
        } catch (RuntimeException e) {
            URL url = sessionClassLoader.getResource(XMLHelper.DEFAULT_SERVERS_XML);
            initializeServers(url);
        }
    }

    private void initializeServers(URL url) {
        servers = XMLHelper.processServersXML(url, sessionClassLoader);
    }

    public Server getServerByName(String serverName) {
        return servers.getServers().stream().filter(server -> server.getName().equals(serverName)).findAny().get();
    }

    public List<String> getServerNames() {
        return servers.getServers().stream().map(Server::getName).collect(Collectors.toList());
    }
}
