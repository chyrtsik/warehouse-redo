/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.movement;

import com.artigile.warehouse.bl.common.verifications.Verification;
import com.artigile.warehouse.bl.common.verifications.VerificationResult;
import com.artigile.warehouse.domain.movement.Movement;
import com.artigile.warehouse.domain.movement.MovementState;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 12.12.2009
 */

/**
 * Checks, that state of the movement is valid for adding new movement items.
 */
public class BeforeAddItemToMovementVerification implements Verification {
    @Override
    public VerificationResult verify(Object obj) {
        Movement movement = (Movement)obj;
        if (!movement.getState().equals(MovementState.CONSTRUCTION)){
            return new VerificationResult(I18nSupport.message("movement.verification.addingItemsToMovementNotAllowed"));
        }
        return VerificationResult.SUCCEEDED;
    }
}
