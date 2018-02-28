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
import com.artigile.warehouse.bl.finance.PaymentEventAdapter;
import com.artigile.warehouse.bl.finance.PaymentService;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNote;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteState;
import com.artigile.warehouse.domain.finance.Payment;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Shyrik, 13.03.2010
 */

/**
 * Synchronizes delivery notes with payment events performed.
 */
@Transactional(rollbackFor = BusinessException.class)
public class DeliveryNoteWithPaymentSynchronizer extends PaymentEventAdapter {

    DeliveryNoteService deliveryNoteService;
    PaymentService paymentService;

    //========================= Construction and initialization =======================
    public DeliveryNoteWithPaymentSynchronizer(){
    }

    public void initialize(){
        paymentService.addListener(this);
    }

    //===================== PaymentEventsListener implementation ======================

    @Override
    public void onPaymentPerformed(Payment payment) throws BusinessException {
        //Close all delivery notes, that are in new payment.
        for (DeliveryNote deliveryNote : payment.getDeliveryNotes()){
            deliveryNoteService.changeDeliveryNoteState(
                    deliveryNote.getId(),
                    DeliveryNoteState.SHIPPING_TO_CONTRACTOR,
                    DeliveryNoteState.CLOSED,
                    payment.getNotice()
            );
        }
    }

    //================================ Spring setters ==================================

    public void setDeliveryNoteService(DeliveryNoteService deliveryNoteService) {
        this.deliveryNoteService = deliveryNoteService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
