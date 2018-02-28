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

/**
 * @author Shyrik, 08.12.2009
 */
public class VerificationFailedException extends BusinessException {
    public VerificationFailedException(String failReason) {
        super(failReason);
    }
}
