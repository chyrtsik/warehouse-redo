/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.exceptions;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 07.12.2008
 */

/**
 * This exception is thrown, when user have no rights to delete the report item.
 */
public class NoDeletePermissionException extends NoPermissionException {
    public NoDeletePermissionException(){
        super(I18nSupport.message("security.no.right.delete"));
    }
}
