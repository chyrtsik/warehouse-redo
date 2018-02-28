/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.warehouseBatch;

import com.artigile.warehouse.bl.common.verifications.Verification;
import com.artigile.warehouse.bl.common.verifications.VerificationResult;
import com.artigile.warehouse.domain.warehouse.WarehouseBatch;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 15.04.2009
 */

/**
 * Verification, that is done to ensure, that given amount of ware can be reserved.
 */
public class BeforeReserveVerification implements Verification {
    private long countToReserve;

    public BeforeReserveVerification(long countToReserve) {
        this.countToReserve = countToReserve;
    }

    @Override
    public VerificationResult verify(Object obj) {
        WarehouseBatch warehouseBatch = (WarehouseBatch)obj;
        if (warehouseBatch == null){
            return new VerificationResult(I18nSupport.message("warehousebatch.verification.invalidWarehouseBatch"));
        }
        else if (warehouseBatch.getAmount() < warehouseBatch.getReservedCount() + countToReserve){
            return new VerificationResult(I18nSupport.message("warehousebatch.verification.notEnoughWares"));
        }
        return VerificationResult.SUCCEEDED;
    }
}
