/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.xml.parser;

import com.artigile.warehouse.utils.xml.element.DataSource;
import com.artigile.warehouse.utils.xml.element.JdbcResourcePool;
import com.artigile.warehouse.utils.xml.element.Resources;
import com.artigile.warehouse.utils.xml.element.Server;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.Map;

/**
 * @author Valery Barysok, 27.04.2010
 */
public class ResourceContentHandler implements ContentHandler {
    private static final String NAMESPACE_URI = "http://artigile.by/xml/ns/resources";
    private static final String ELEMENT_RESOURCES = "resources";
    private static final String ELEMENT_SERVER = "server";
    private static final String ELEMENT_DATASOURCE = "datasource";
    private static final String ELEMENT_JDBC_RESOURCE_POOL = "jdbc-resource-pool";
    private static final String ELEMENT_PROPERTY = "property";
    private static final String ATTRIBUTE_DATASOURCE_NAME = "datasource-name";
    private static final String ATTRIBUTE_POOL_NAME = "pool-name";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_VALUE = "value";
    private static final String ATTRIBUTE_VERSION = "version";

    private Resources resources;
    private JdbcResourcePool jdbcResourcePool;

    public ResourceContentHandler() {
        resources = new Resources();
    }

    public Resources getResources() {
        return resources;
    }

    @Override
    public void setDocumentLocator(Locator locator) {
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (NAMESPACE_URI.equals(namespaceURI)) {
            if (ELEMENT_RESOURCES.equals(localName)) {
                resources.setVersion(atts.getValue(ATTRIBUTE_VERSION));
            } else if (ELEMENT_SERVER.equals(localName)) {
                Server server = new Server(atts.getValue(ATTRIBUTE_NAME), atts.getValue(ATTRIBUTE_DATASOURCE_NAME));
                resources.getServers().add(server);
                return;
            } else if (ELEMENT_PROPERTY.equals(localName)) {
                String name = atts.getValue(ATTRIBUTE_NAME);
                String value = atts.getValue(ATTRIBUTE_VALUE);
                if (jdbcResourcePool != null) {
                    Map<String, String> properties = jdbcResourcePool.getProperties();
                    properties.put(name, value);
                }
            } else if (ELEMENT_DATASOURCE.equals(localName)) {
                DataSource dataSource = new DataSource(atts.getValue(ATTRIBUTE_NAME), atts.getValue(ATTRIBUTE_POOL_NAME));
                resources.getDataSources().add(dataSource);
                return;
            } else if (ELEMENT_JDBC_RESOURCE_POOL.equals(localName)) {
                jdbcResourcePool = new JdbcResourcePool(atts.getValue(ATTRIBUTE_NAME)); 
                return;
            }
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (NAMESPACE_URI.equals(namespaceURI)) {
            if (ELEMENT_SERVER.equals(localName)) {
            } else if (ELEMENT_JDBC_RESOURCE_POOL.equals(localName)) {
                if (jdbcResourcePool != null){
                    resources.getJdbcResourcePools().add(jdbcResourcePool);
                    jdbcResourcePool = null;
                }
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
    }
}
