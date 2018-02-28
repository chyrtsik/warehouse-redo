/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.postings;

import com.artigile.warehouse.bl.common.verifications.Verification;
import com.artigile.warehouse.bl.common.verifications.VerificationResult;
import com.artigile.warehouse.domain.postings.Posting;
import com.artigile.warehouse.domain.postings.PostingItem;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 15.02.2009
 */

/**
 * This verification checks, if posting is ready to become completed.
 */
public class BeforePostingCompleteVerification implements Verification {
    @Override
    public VerificationResult verify(Object obj) {
        Posting posting = (Posting)obj;
        if (posting.getItems().size() == 0){
            //Posting should not be empty to be performed.
            return new VerificationResult(I18nSupport.message("posting.verification.postingShouldNotBeEmptyForBegin"));
        }
        for (PostingItem item : posting.getItems()){
            if (item.getDetailBatch() == null){
                //All posting items should be classified. All not recognized bar codes should be removed or assigned
                //to appropriate detail batch.
                return new VerificationResult(I18nSupport.message("posting.verification.postingItemNotInCatalog", item.getNumber()));
            }
            else if (item.getAmount() == null || item.getAmount() == 0){
                //Count of all items must be set.
                return new VerificationResult(I18nSupport.message("posting.verification.postingItemCountNotSet", item.getNumber())); 
            }
            else if (item.getStoragePlace() == null){
                //Storage place of all items must be set.
                return new VerificationResult(I18nSupport.message("posting.verification.postingItemStoragePlaceNotSet", item.getNumber()));
            }
        }
        return VerificationResult.SUCCEEDED;
    }
}
