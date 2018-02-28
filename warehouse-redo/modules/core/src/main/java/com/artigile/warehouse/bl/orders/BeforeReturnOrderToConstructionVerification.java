/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.orders;

import com.artigile.warehouse.bl.common.verifications.Verification;
import com.artigile.warehouse.bl.common.verifications.VerificationResult;
import com.artigile.warehouse.domain.orders.Order;
import com.artigile.warehouse.domain.orders.OrderState;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 28.04.2009
 */

/**
 * Verification checks, if order is valid to be returned to construction.
 */
public class BeforeReturnOrderToConstructionVerification implements Verification {
    public VerificationResult verify(Object obj) {
        Order order = (Order)obj;
        if (order.getState() == OrderState.COLLECTION){
            //Order is already is beeing complected.
            return new VerificationResult(I18nSupport.message("order.verification.orderIsBeeingProcessing"));
        }
        return VerificationResult.SUCCEEDED;
    }
}
