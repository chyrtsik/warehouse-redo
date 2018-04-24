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

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Valery Barysok, 26.04.2010
 */
public class XMLExceptionHandler implements ErrorHandler {
    private XMLException m_xmlException;

    public void warning(SAXParseException exception) throws SAXException {
    	this.error(exception);
    }

    public void error(SAXParseException exception) throws SAXException {
        if (m_xmlException == null) {
        	m_xmlException = new XMLException();
        }
        m_xmlException.addNestedException(exception);
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        this.error(exception);
    }

    public XMLException getXMLException() {
    	return m_xmlException;
    }
}
