/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.inventorization.inventorization;

import com.artigile.warehouse.bl.common.verifications.Verification;
import com.artigile.warehouse.bl.common.verifications.VerificationResult;
import com.artigile.warehouse.domain.inventorization.Inventorization;
import com.artigile.warehouse.domain.inventorization.InventorizationState;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Borisok V.V., 09.10.2009
 */

/**
 * This verification checks, if inventorization is ready to become completed.
 */
public class BeforeInventorizationCompleteVerification implements Verification {
    public VerificationResult verify(Object obj) {
        Inventorization inventorization = (Inventorization)obj;
        if (!inventorization.getState().equals(InventorizationState.PROCESSED)){
            return new VerificationResult(I18nSupport.message("inventorization.error.inventorization.shouldBeProcessed"));
        }
        return VerificationResult.SUCCEEDED;
    }
}
