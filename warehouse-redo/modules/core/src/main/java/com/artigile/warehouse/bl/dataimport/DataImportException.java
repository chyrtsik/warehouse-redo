/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.dataimport;

/**
 * Base class of all known exceptions generated during data import.
 *
 * @author Aliaksandr.Chyrtsik, 07.11.11
 */
public class DataImportException extends Exception{
    public DataImportException(String message) {
        super(message);
    }
}
