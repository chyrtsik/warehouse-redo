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
 * @author Shyrik, 14.12.2008
 */

/**
 * This exception us used in the cases to inform caller, that requested
 * item is not exists (deleted by onother user and so on).
 */
public class ItemNotExistsException extends BusinessException {
    public ItemNotExistsException() {
        super(I18nSupport.message("exceptions.itemNotExistsException"));
    }

    public ItemNotExistsException(String message){
        super(message);
    }
}
