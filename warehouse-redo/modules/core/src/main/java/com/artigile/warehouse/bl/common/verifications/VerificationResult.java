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

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 15.02.2009
 */

/**
 * Result of the verification.
 */
public class VerificationResult {
    /**
     * If true, than verification was done successfully, if false -- verification failed.
     */
    private boolean succeeded;

    /**
     * If verification failed, contains message, explaining reason of failure.
     */
    private String failReason;

    /**
     * Single instance of succeeded result.
     */
    public static VerificationResult SUCCEEDED = new VerificationResult(true, I18nSupport.message("verifications.succeeded"));

    //=============================== Constructors ===================================
    /**
     * Constructs failed vesification result with given message of failure reason.
     * @param failReason
     */
    public VerificationResult(String failReason){
        this(false, failReason);
    }

    private VerificationResult(boolean succeeded, String failReason){
        this.succeeded = succeeded;
        this.failReason = failReason;
    }

    //================================ Getters =======================================

    public boolean isSucceeded() {
        return succeeded;
    }

    public boolean isFailed() {
        return !isSucceeded();
    }

    public String getFailReason() {
        return failReason;
    }
}
