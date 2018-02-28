/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.common.verifications;

/**
 * @author Shyrik, 15.02.2009
 */

/**
 * Verification -- some check of the object, that should be done before performing some business operation.
 * If verification verification fails, operation cannot be performed.
 */
public interface Verification {
    /**
     * Perform checks, provided by verification.
     * @param obj - business object to be checked.
     * @return
     */
    public VerificationResult verify(Object obj);
}
