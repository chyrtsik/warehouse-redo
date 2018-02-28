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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Valery Barysok, 26.04.2010
 */
public class XMLException extends RuntimeException {
    private List<Exception> m_nestedExceptions;

    public XMLException() {
        super();
        m_nestedExceptions = new ArrayList<Exception>();
    }

    public void addNestedException(Exception nestedException) {
        m_nestedExceptions.add(nestedException);
    }

    public String getMessage() {
        StringBuffer buffer = new StringBuffer();
        Exception nestedException;
        for (int x=0; x<m_nestedExceptions.size(); x++) {
            nestedException = m_nestedExceptions.get(x);
            buffer.append(System.getProperty("line.separator"));
            buffer.append('(');
            buffer.append(x + 1);
            buffer.append(". ");
            buffer.append(nestedException.getMessage());
            buffer.append(')');
        }
        return buffer.toString();
    }

    public String toString() { return getMessage(); }
}
