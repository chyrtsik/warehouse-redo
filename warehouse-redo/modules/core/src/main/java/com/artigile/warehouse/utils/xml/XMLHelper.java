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

import com.artigile.warehouse.utils.xml.element.Resources;
import com.artigile.warehouse.utils.xml.parser.ResourceContentHandler;
import com.artigile.warehouse.utils.xml.parser.XMLException;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class used for handling element inspection.
 *
 * @author Valery Barysok, 25.04.2010
 */
public class XMLHelper {

    public static final String RESOURCES_XML = "config/resources.xml";
    public static final String DEFAULT_RESOURCES_XML = "config/resources.xml";

    /**
     *  Search the classpath for resources.xml. A resources.xml is defined as any
     *  part of the class path that contains a "config" directory with a resources.xml file in it. 
     *  Return a list of {@link URL}s of those files.
     *  @param loader the class loader to get the class path from
     */
    public static Set<URL> findResources(ClassLoader loader) {
        Set<URL> pars = new HashSet<URL>();
        try {
            Enumeration<URL> resources = loader.getResources(RESOURCES_XML);
            while (resources.hasMoreElements()) {
                URL rxmlURL = resources.nextElement();
                pars.add(rxmlURL);
            }
        } catch (java.io.IOException exc){
            throw new RuntimeException(exc);
        }
        return pars;
    }

    public static Resources processResourcesXML(URL baseURL, ClassLoader loader) {
        try {
            InputStream rxmlStream = baseURL.openStream();
            return processResourcesXML(baseURL, rxmlStream, loader);
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    /**
     * Build a resources.xml file into a Resources object
     */
    private static Resources processResourcesXML(URL baseURL, InputStream input, ClassLoader loader) {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(true);

        SAXParser sp;

        // create a SAX parser
        try {
            sp = spf.newSAXParser();
	        sp.setProperty(XMLConstants.SCHEMA_LANGUAGE, XMLConstants.XML_SCHEMA);
	    } catch (javax.xml.parsers.ParserConfigurationException exc){
            throw new RuntimeException(exc);
	    } catch (org.xml.sax.SAXException exc){
            throw new RuntimeException(exc);
	    }

        XMLReader xmlReader;
        XMLExceptionHandler xmlErrorHandler = new XMLExceptionHandler();

	    // create an XMLReader
	    try {
            xmlReader = sp.getXMLReader();
	        xmlReader.setErrorHandler(xmlErrorHandler);
        } catch (org.xml.sax.SAXException exc){
            throw new RuntimeException(exc);
        }

        // attempt to load the schema from the classpath
        URL schemaURL = loader.getResource(XMLConstants.RESOURCES_SCHEMA_NAME);
        if (schemaURL != null) {
            try {
            	sp.setProperty(XMLConstants.JAXP_SCHEMA_SOURCE, schemaURL.toString());
            } catch (org.xml.sax.SAXException exc){
            	throw new RuntimeException(exc);
            }
        }

        ResourceContentHandler myContentHandler = new ResourceContentHandler();
        xmlReader.setContentHandler(myContentHandler);

        InputSource inputSource = new InputSource(input);
        try{
            xmlReader.parse(inputSource);
        } catch (IOException exc){
            throw new RuntimeException(exc);
        } catch (org.xml.sax.SAXException exc){
        	// XMLErrorHandler will handle SAX exceptions
        }

        // handle any parse exceptions
        XMLException xmlError = xmlErrorHandler.getXMLException();
        if (xmlError != null) {
            throw new RuntimeException(xmlError);
        }

        return myContentHandler.getResources();
    }
}
