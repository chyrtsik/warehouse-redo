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

import com.artigile.warehouse.bl.common.exceptions.VerificationFailedException;

/**
 * @author Shyrik, 15.02.2009
 */

/**
 * Business logic utility class, that provides running given verifications.
 */
final public class Verifications {
    private Verifications(){
    }

    /**
     * Performs given verifications for given usiness object.
     * @param obj - object to be verified.
     * @param verifications - array of verifications to be performed.
     * @return - succeeded resuls, if all verifications passed, or else result of first failed verification.  
     */
    public static VerificationResult performVerifications(Object obj, Verification []verifications){
        VerificationResult result;
        for (Verification verification : verifications){
            result = performVerification(obj, verification);
            if (!result.isSucceeded()){
                //Ooops, verification failed. Let caller to know this.
                return result;
            }

        }
        return VerificationResult.SUCCEEDED;
    }

    /**
     * Performs given verification for given usiness object.
     * @param obj - object to be verified.
     * @param verification - verification to be performed.
     * @return - succeeded resuls, if verification passed, or else result of first failed verification.  
     */
    public static VerificationResult performVerification(Object obj, Verification verification){
        return verification.verify(obj);
    }

    /**
     * Ensures, that specific verification passes.
     * @param obj object to be passed into verification.
     * @param verification verification to be performed.
     * @throws VerificationFailedException is verification failes.
     */
    public static void ensureVerificationPasses(Object obj, Verification verification) throws VerificationFailedException {
        VerificationResult verificationResult = Verifications.performVerification(obj, verification);
        if (verificationResult.isFailed()){
            throw new VerificationFailedException(verificationResult.getFailReason());
        }
    }
}
