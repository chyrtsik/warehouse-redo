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

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.Map;

/**
 * @author Valery Barysok, 27.04.2010
 */
public class ServersContentHandler implements ContentHandler {
    private static final String NAMESPACE_URI = "http://artigile.by/xml/ns/servers";
    private static final String ELEMENT_SERVERS = "servers";
    private static final String ELEMENT_SERVER = "server";
    private static final String ELEMENT_PROPERTY = "property";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_VALUE = "value";
    private static final String ATTRIBUTE_VERSION = "version";

    private Servers servers;
    private Server currentServer;

    public ServersContentHandler() {
        servers = new Servers();
    }

    public Servers getServers() {
        return servers;
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
            if (ELEMENT_SERVERS.equals(localName)) {
                servers.setVersion(atts.getValue(ATTRIBUTE_VERSION));
            } else if (ELEMENT_SERVER.equals(localName)) {
                currentServer = new Server(atts.getValue(ATTRIBUTE_NAME));
            } else if (ELEMENT_PROPERTY.equals(localName)) {
                String name = atts.getValue(ATTRIBUTE_NAME);
                String value = atts.getValue(ATTRIBUTE_VALUE);
                if (currentServer != null) {
                    currentServer.getProperties().put(name, value);
                }
            }
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (NAMESPACE_URI.equals(namespaceURI)) {
            if (ELEMENT_SERVER.equals(localName)) {
                servers.getServers().add(currentServer);
                currentServer = null;
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
