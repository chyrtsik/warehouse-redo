/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.purchase;

import com.artigile.warehouse.bl.common.verifications.Verification;
import com.artigile.warehouse.bl.common.verifications.VerificationResult;
import com.artigile.warehouse.domain.purchase.Purchase;
import com.artigile.warehouse.domain.purchase.PurchaseItem;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 04.03.2009
 */

/**
 * This verification checks, if purchase is ready to start waiting for it.
 */
public class BeforeWaitPurchaseVerification implements Verification {
    @Override
    public VerificationResult verify(Object obj) {
        Purchase purchase = (Purchase) obj;
        if (purchase == null || purchase.getItems().size() == 0) {
            //Purchase should not be empty.
            return new VerificationResult(I18nSupport.message("purchase.verification.purchaseIsEmpty"));
        }
        for (PurchaseItem item : purchase.getItems()){
            if (item.getAmount() == null || item.getAmount() == 0){
                //Count of all items must be set.
                return new VerificationResult(I18nSupport.message("purchase.verification.purchaseItemCountNotSet", item.getNumber()));
            }
            else if (item.getPrice() == null){
                //Price of all items must be set.
                return new VerificationResult(I18nSupport.message("purchase.verification.purchaseItemPriceNotSet", item.getNumber()));
            }
        }
        return VerificationResult.SUCCEEDED;
    }
}
