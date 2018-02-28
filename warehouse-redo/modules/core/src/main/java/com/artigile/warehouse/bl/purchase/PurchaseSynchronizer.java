/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.purchase;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.postings.PostingChangeAdapter;
import com.artigile.warehouse.bl.postings.PostingService;
import com.artigile.warehouse.domain.postings.Posting;
import com.artigile.warehouse.domain.purchase.Purchase;
import com.artigile.warehouse.domain.purchase.PurchaseState;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Shyrik, 08.11.2009
 */

/**
 * This class reacts on changes in other documents to perform appropriate
 * actions on purchase documents.
 */
@Transactional(rollbackFor = BusinessException.class)
public class PurchaseSynchronizer extends PostingChangeAdapter {

    /**
     * For using methods of purchase service.
     */
    private PurchaseService purchaseService;

    /**
     * For using methods of posting service.
     */
    private PostingService postingsService;

    public PurchaseSynchronizer() {/* Default constructor*/ }

    public void initialize(){
        postingsService.addListener(this);
    }

    //================= Synchronization with posting =====================================
    @Override
    public void onPostingCreated(Posting posting) throws BusinessException {
        Purchase purchase = posting.getPurchase();
        if (purchase != null){
            //Change purchase type then posting from purchase is performed.
            purchase.setState(PurchaseState.IN_POSTING);
            purchaseService.savePurchase(purchase);
        }
    }

    @Override
    public void onPostingDeleted(Posting posting) throws BusinessException {
        Purchase purchase = posting.getPurchase();
        if (purchase != null){
            purchase.setState(PurchaseState.SHIPPED);
            purchaseService.savePurchase(purchase);
        }
    }

    @Override
    public void onPostingCompleted(Posting posting) throws BusinessException {
        Purchase purchase = posting.getPurchase();
        if (purchase != null){
            purchase.setState(PurchaseState.POSTING_DONE);
            purchaseService.savePurchase(purchase);

            // Notify that purchase changed state.
            purchaseService.firePurchaseStateChanged(purchase);
        }
    }

    //========================== Spring setters ===========================
    public void setPurchaseService(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    public void setPostingsService(PostingService postingsService) {
        this.postingsService = postingsService;
    }
}
