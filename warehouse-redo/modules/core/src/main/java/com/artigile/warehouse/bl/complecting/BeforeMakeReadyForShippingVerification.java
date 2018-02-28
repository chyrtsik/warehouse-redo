/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.complecting;

import com.artigile.warehouse.bl.common.verifications.Verification;
import com.artigile.warehouse.bl.common.verifications.VerificationResult;
import com.artigile.warehouse.domain.complecting.ComplectingTask;
import com.artigile.warehouse.domain.complecting.ComplectingTaskState;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 19.11.2009
 */

/**
 * Verification for checking, if complecting task is ready to become ready for shipping.
 */
public class BeforeMakeReadyForShippingVerification implements Verification {
    @Override
    public VerificationResult verify(Object obj) {
        ComplectingTask task = (ComplectingTask)obj;
        ComplectingTaskState state = task.getState();
        if (state == ComplectingTaskState.READY_FOR_SHIPPING) {
            return new VerificationResult(I18nSupport.message("complectingTask.verification.taskAlreadyPreparedForShipping", task.describe()));
        }
        else if (state == ComplectingTaskState.SHIPPED) {
            return new VerificationResult(I18nSupport.message("complectingTask.verification.taskAlreadyShipped", task.describe()));
        }
        else if (state != ComplectingTaskState.PROCESSED) {
            //Compelcting task should be processed before made ready for complecting.
            return new VerificationResult(I18nSupport.message("complectingTask.verification.taskShouldBeProcessed", task.describe()));
        }
        return VerificationResult.SUCCEEDED;
    }
}
