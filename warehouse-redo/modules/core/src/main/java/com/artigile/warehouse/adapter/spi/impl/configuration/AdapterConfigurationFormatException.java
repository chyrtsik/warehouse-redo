/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.configuration;

/**
 *
 * @author Aliaksandr.Chyrtsik, 17.08.11
 */
public class AdapterConfigurationFormatException extends IllegalArgumentException {
    public AdapterConfigurationFormatException(){
        this(null, null);
    }

    public AdapterConfigurationFormatException(Throwable cause){
        this(null, cause);
    }

    public AdapterConfigurationFormatException(String message){
        this(message, null);
    }

    public AdapterConfigurationFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
