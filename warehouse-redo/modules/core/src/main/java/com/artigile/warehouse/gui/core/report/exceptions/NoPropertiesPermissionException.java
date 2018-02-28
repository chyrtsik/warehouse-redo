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
 * @author Shyrik, 20.12.2008
 */
public class NoPropertiesPermissionException extends NoPermissionException {
    public NoPropertiesPermissionException() {
        super(I18nSupport.message("security.no.right.properties"));
    }
}
