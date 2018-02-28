/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.common.exceptions;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 31.05.2010
 */

/**
 * Exceptions is thrown when requested offline lock cannot be established.
 */
public class CannotEstablishLockException extends CannotPerformOperationException {
    public CannotEstablishLockException(){
        super(I18nSupport.message("locking.error.lockCannotBeEstablished"));
    }

    public CannotEstablishLockException(String message) {
        super(message);
    }
}
