/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.data.validation;

/**
 * @author Shyrik, 21.05.2010
 */

/**
 * This exception indicates silent fail of data validation.
 * 'Silent' means, that no error message should be shown.
 */
public class SilentDataValidationException extends DataValidationException {
    public SilentDataValidationException() {
        super(null);
    }
}
