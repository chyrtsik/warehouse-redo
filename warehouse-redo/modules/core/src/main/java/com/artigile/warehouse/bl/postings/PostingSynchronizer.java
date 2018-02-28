/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.postings;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.inventorization.inventorization.InventorizationChangeAdapter;
import com.artigile.warehouse.bl.inventorization.inventorization.InventorizationService;
import com.artigile.warehouse.domain.inventorization.Inventorization;
import com.artigile.warehouse.domain.inventorization.InventorizationItem;
import com.artigile.warehouse.domain.inventorization.InventorizationItemProcessingResult;
import com.artigile.warehouse.domain.inventorization.InventorizationState;
import com.artigile.warehouse.domain.postings.Posting;
import com.artigile.warehouse.domain.postings.PostingItem;
import com.artigile.warehouse.domain.postings.PostingState;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.UserTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Shyrik, 26.10.2009
 */

/**
 * This class reacts on changes in other documents to perform appropriate
 * actions on posting documents.
 */
@Transactional(rollbackFor = BusinessException.class)
public class PostingSynchronizer extends InventorizationChangeAdapter {
    private InventorizationService inventorizationService;

    /**
     * For using methods of posting service.
     */
    private PostingService postingService;

    public PostingSynchronizer() { /* Default constructor */ }

    public void initialize(){
        //This class listens for changes made in inventorizations.
        inventorizationService.addListener(this);
    }

    //======================= Synchronization with inventrizations ==========================
    @Override
    public void onInventorizationStateChanged(Inventorization inventorization, InventorizationState oldState, InventorizationState newState) throws BusinessException {
        if (newState.equals(InventorizationState.CLOSED)){
            //When inventorization has been closed, we should create posting for all positions,
            //that has additional wares at the warehouse.
            onCloseInventorization(inventorization);
        }
    }

    private void onCloseInventorization(Inventorization inventorization) throws BusinessException {
        //1. Searching for ability to create posting items.
        List<PostingItem> postingItems = new ArrayList<PostingItem>();
        long currentNumber = 1;
        for (InventorizationItem inventorizationItem : inventorization.getItems()){
            if (inventorizationItem.getProcessingResult().equals(InventorizationItemProcessingResult.SURPLUS_COUNT)){
                //Creating posting item.
                PostingItem postingItem = new PostingItem();
                postingItem.setNumber(currentNumber++);
                postingItem.setStoragePlace(inventorizationItem.getStoragePlace());
                postingItem.setDetailBatch(inventorizationItem.getDetailBatch());
                postingItem.setAmount(inventorizationItem.getCountDifference());
                postingItem.setNotice(inventorizationItem.getWarehouseBatch().getNotice());
                postingItems.add(postingItem);

                inventorizationItem.setPostingItem(postingItem);
            }
        }

        //2. Creating posting.
        if ( postingItems.size() > 0 ){
            Posting posting = new Posting();
            posting.setState(PostingState.CONSTRUCTION);
            posting.setNumber(postingService.getNextAvailablePostingNumber());
            posting.setCreateDate(Calendar.getInstance().getTime());
            posting.setCreatedUser(UserTransformer.transformUser(WareHouse.getUserSession().getUser())); //TODO: eliminate this reference to presentation tier.
            posting.setDefaultWarehouse(inventorization.getWarehouse());
            posting.setItems(postingItems);
            posting.setNotice(I18nSupport.message("posting.notice.forInventorizationWithNumber", inventorization.getNumber()));

            postingService.savePosting(posting);
            postingService.completePosting(posting);

            inventorization.setPosting(posting);
        }
    }

    //================================ Spring setters ================================
    public void setInventorizationService(InventorizationService inventorizationService) {
        this.inventorizationService = inventorizationService;
    }

    public void setPostingService(PostingService postingService) {
        this.postingService = postingService;
    }
}
