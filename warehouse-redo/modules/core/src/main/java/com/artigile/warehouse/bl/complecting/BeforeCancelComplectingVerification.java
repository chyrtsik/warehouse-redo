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
 * @author Shyrik, 08.05.2010
 */

/**
 * Verification for checking, if complecting task is valid to ve cancelled.
 */
public class BeforeCancelComplectingVerification implements Verification {
    @Override
    public VerificationResult verify(Object obj) {
        ComplectingTask task = (ComplectingTask)obj;
        if (task.getState() != ComplectingTaskState.PROCESSING){
            //Complecting task should be being processed.
            return new VerificationResult(I18nSupport.message("complectingTask.verification.taskIsNotInProcessing", task.describe()));
        }
        return VerificationResult.SUCCEEDED;
    }
}
