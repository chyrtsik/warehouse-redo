/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.deliveryNote;

import com.artigile.warehouse.bl.common.verifications.Verification;
import com.artigile.warehouse.bl.common.verifications.VerificationResult;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNote;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteState;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 07.11.2009
 */
public class BeforeDeliveryNoteStateChangeVerification implements Verification {
    /*
     * Old state of the delivery note.
     */
    private DeliveryNoteState oldState;

    public BeforeDeliveryNoteStateChangeVerification(DeliveryNoteState oldState) {
        this.oldState = oldState;
    }


    @Override
    public VerificationResult verify(Object obj) {
        if (obj == null){
            return new VerificationResult(I18nSupport.message("deliveryNote.verification.error.deliveNoteNotExist"));
        }
        DeliveryNote deliveryNote = (DeliveryNote)obj;
        if (!deliveryNote.getState().equals(oldState)){
            return new VerificationResult(I18nSupport.message("deliveryNote.verification.error.deliveNoteStateNotMatch", oldState.getName()));
        }
        return VerificationResult.SUCCEEDED;
    }
}
