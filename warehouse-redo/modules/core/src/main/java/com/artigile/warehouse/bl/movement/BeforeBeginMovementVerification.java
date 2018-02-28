/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
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
import com.artigile.warehouse.domain.movement.MovementItem;
import com.artigile.warehouse.domain.movement.MovementState;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 08.12.2009
 */

/**
 * Verification, that ensures, that movement may begin.
 */
public class BeforeBeginMovementVerification implements Verification {
    @Override
    public VerificationResult verify(Object obj) {
        Movement movement = (Movement)obj;
        //1. Checks state of the movement.
        if (!movement.getState().equals(MovementState.CONSTRUCTION)){
            return new VerificationResult(I18nSupport.message("movement.verification.movementIsAlreadyProcessing"));
        }

        //2. Checks items of the movement.
        if (movement.getItems().size() == 0){
            return new VerificationResult(I18nSupport.message("movement.verification.movementShouldHaveItems"));
        }

        for (MovementItem item : movement.getItems()){
            if (item.getAmount() == null){
                return new VerificationResult(I18nSupport.message("movement.verification.movementItemCountNotSet", item.getNumber()));
            }
        }

        return VerificationResult.SUCCEEDED;
    }
}
