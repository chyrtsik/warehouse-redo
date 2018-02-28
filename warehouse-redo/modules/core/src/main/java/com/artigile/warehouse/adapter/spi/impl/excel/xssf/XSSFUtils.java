/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.excel.xssf;

import com.artigile.warehouse.adapter.spi.impl.excel.Dimension;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Utility class for parsing Excel 2007 files.
 *
 * @author Aliaksandr.Chyrtsik, 16.08.11
 */
public final class XSSFUtils {
    private XSSFUtils(){
    }

    /**
     * Iterates through xml document calling listener for each event.
     * @param xmlReader reader to access to document.
     * @param xmlIterationListener listener to be called on events.
     */
    public static void iterateXMLDocument(XMLStreamReader xmlReader, XmlIterationListener xmlIterationListener) {
        try {
            while (xmlReader.hasNext()) {
                int event = xmlReader.next();
                if (!xmlIterationListener.processEvent(xmlReader, event)){
                    //Listener forces us to stop iteration.
                    break;
                }
            }
        } catch (XMLStreamException e) {
            LoggingFacade.logError(e);
            throw new RuntimeException(e);
        } finally {
            try {
                xmlReader.close();
            } catch (XMLStreamException ex) {
                LoggingFacade.logError(ex);
            }
        }
    }

    public interface XmlIterationListener {
        boolean processEvent(XMLStreamReader xmlReader, int event) throws XMLStreamException;
    }

    /**
     * Parser Excel 2007 dimension string.
     * @param dimensionStr dimension string.
     * @return parsed dimension.
     */
    public static Dimension parseDimension(String dimensionStr) {
        char[] chars = dimensionStr.toCharArray();
        int i = 0;

        int left = 0;
        for (; i < chars.length && isLatin(chars[i]); ++i) {
            left = left * 26 + chars[i] - 'A' + 1;
        }

        int top = i >= chars.length ? 1 : 0;
        for (; i < chars.length && isDigit(chars[i]); ++i) {
            top = top * 10 + chars[i] - '0';
        }

        // skip delimeter
        ++i;

        int right = i >= chars.length ? left : 0;
        for (; i < chars.length && isLatin(chars[i]); ++i) {
            right = right * 26 + chars[i] - 'A' + 1;
        }

        int bottom = i >= chars.length ? top : 0;
        for (; i < chars.length && isDigit(chars[i]); ++i) {
            bottom = bottom * 10 + chars[i] - '0';
        }

        return new Dimension(top - 1, left - 1, bottom - 1, right - 1);
    }

    private static boolean isLatin(char ch) {
        return 'A' <= ch && ch <= 'Z';
    }

    private static boolean isDigit(char ch) {
        return '0' <= ch && ch <= '9';
    }
}
