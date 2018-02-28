/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.deliveryNote;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.postings.PostingChangeAdapter;
import com.artigile.warehouse.bl.postings.PostingService;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteState;
import com.artigile.warehouse.domain.postings.Posting;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Shyrik, 08.11.2009
 */
/**
 * This class reacts on changes in other documents to perform appropriate
 * actions on delivery note documents.
 */
@Transactional(rollbackFor = BusinessException.class)
public class DeliveryNoteWithPostingSynchronizer extends PostingChangeAdapter {
    private DeliveryNoteService deliveryNoteService;

    private PostingService postingsService;

    public DeliveryNoteWithPostingSynchronizer(){
    }

    public void initialize(){
        postingsService.addListener(this);
    }

    //================= Synchronization with posting =====================================
    @Override
    public void onPostingCreated(Posting posting) throws BusinessException {
        if ( posting.getDeliveryNote() != null ){
            //When posting for delivery note is created, delivery note considered to be being posted.
            deliveryNoteService.changeDeliveryNoteState(posting.getDeliveryNote().getId(), DeliveryNoteState.SHIPPED, DeliveryNoteState.POSTING);
        }
    }

    @Override
    public void onPostingDeleted(Posting posting) throws BusinessException {
        if ( posting.getDeliveryNote() != null ){
            //When posting for delivery note is deleted, we should move state of delivery note back.
            deliveryNoteService.changeDeliveryNoteState(posting.getDeliveryNote().getId(), DeliveryNoteState.POSTING, DeliveryNoteState.SHIPPED);
        }
    }

    @Override
    public void onPostingCompleted(Posting posting) throws BusinessException {
        if ( posting.getDeliveryNote() != null ){
            //When posting, created for delivery note, is closed then delivery note becomes
            //fully processed (i.e. closed).
            posting.getDeliveryNote().setPosting(posting);
            deliveryNoteService.changeDeliveryNoteState(posting.getDeliveryNote().getId(), DeliveryNoteState.POSTING, DeliveryNoteState.CLOSED);
        }
    }

    //========================== Spring setters ===========================
    public void setDeliveryNoteService(DeliveryNoteService deliveryNoteService) {
        this.deliveryNoteService = deliveryNoteService;
    }

    public void setPostingsService(PostingService postingsService) {
        this.postingsService = postingsService;
    }
}
