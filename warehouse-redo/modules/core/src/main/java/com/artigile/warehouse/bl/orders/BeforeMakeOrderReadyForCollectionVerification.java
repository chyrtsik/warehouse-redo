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
import com.artigile.warehouse.domain.orders.OrderItem;
import com.artigile.warehouse.domain.orders.OrderState;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 28.04.2009
 */

/**
 * Verification, that ensures, that order is ready to be marked as "ready for collection".
 */
public class BeforeMakeOrderReadyForCollectionVerification implements Verification {
    public VerificationResult verify(Object obj) {
        Order order = (Order)obj;

        //Checking of order.
        if (order.getState() != OrderState.CONSTRUCTION){
            return new VerificationResult(I18nSupport.message("order.verification.orderNotInState", OrderState.CONSTRUCTION.getName()));
        }
        else if (order.getItems().size() == 0){
           return new VerificationResult(I18nSupport.message("order.verification.orderIsEmpty"));            
        }

        //Checking if order items.
        for (OrderItem item : order.getItems()){
            if (item.isDetail() && !item.getReserved()){
                return new VerificationResult(I18nSupport.message("order.verification.itemNotReserved", item.getNumber()));
            }
            else if (item.getAmount() == 0){
                return new VerificationResult(I18nSupport.message("order.verification.itemCountNotSet", item.getNumber()));                
            }
        }

        return VerificationResult.SUCCEEDED;
    }
}
