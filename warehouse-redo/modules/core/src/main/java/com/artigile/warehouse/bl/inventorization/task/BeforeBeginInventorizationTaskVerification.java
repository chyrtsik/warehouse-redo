/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.inventorization.task;

import com.artigile.warehouse.bl.common.verifications.Verification;
import com.artigile.warehouse.bl.common.verifications.VerificationResult;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTask;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTaskState;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Borisok V.V., 03.10.2009
 */
public class BeforeBeginInventorizationTaskVerification implements Verification {
    public VerificationResult verify(Object obj) {
        InventorizationTask task = (InventorizationTask)obj;
        if (task.getState() != InventorizationTaskState.NOT_PROCESSED){
            return new VerificationResult(I18nSupport.message("inventorization.task.verification.taskIsAlreadyInProcess", task.getNumber()));
        }
        return VerificationResult.SUCCEEDED;
    }
}
