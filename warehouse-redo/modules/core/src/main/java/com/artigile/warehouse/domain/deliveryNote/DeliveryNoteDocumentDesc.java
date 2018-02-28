/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.deliveryNote;

import com.artigile.warehouse.domain.chargeoff.ChargeOff;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import java.util.Date;

/**
 * Helper class to describe document delivery note.
 * @author Aliaksandr Chyrtsik
 * @since 13.05.13
 */
public abstract class DeliveryNoteDocumentDesc {
    public abstract Long getNumber(DeliveryNote deliveryNote);
    public abstract String getName(DeliveryNote deliveryNote);
    public abstract Date getDate(DeliveryNote deliveryNote);

    private final static OrderDocumentDesc ORDER_DOCUMENT_DESC = new OrderDocumentDesc();
    private final static MovementDocumentDesc MOVEMENT_DOCUMENT_DESC = new MovementDocumentDesc();

    /**
     * Factory method returning implementation suitable for given detail batch operation.
     * @param deliveryNote delivery note to find document implementation.
     * @return document implementation for this delivery note.
     */
    public static DeliveryNoteDocumentDesc getInstance(DeliveryNote deliveryNote) {
        ChargeOff chargeOff = deliveryNote.getChargeOff();
        if (chargeOff.getOrder() != null){
            //Delivery note for order.
            return ORDER_DOCUMENT_DESC;
        }
        else if (chargeOff.getMovement() != null){
            //Delivery note for movement between warehouses.
            return MOVEMENT_DOCUMENT_DESC;
        }
        else {
            LoggingFacade.logError("DeliveryNoteDocumentDesc.getInstance: Not supported delivery note type. Id = " + deliveryNote.getId());
            throw new AssertionError("Not supported delivery note type. Id = " + deliveryNote.getId());
        }
    }

    private static class OrderDocumentDesc extends DeliveryNoteDocumentDesc {
        @Override
        public Long getNumber(DeliveryNote deliveryNote) {
            return deliveryNote.getChargeOff().getOrder().getNumber();
        }

        @Override
        public String getName(DeliveryNote deliveryNote) {
            return I18nSupport.message("detail.batch.history.document.order");
        }

        @Override
        public Date getDate(DeliveryNote deliveryNote) {
            return deliveryNote.getChargeOff().getOrder().getCreateDate();
        }
    }

    private static class MovementDocumentDesc extends DeliveryNoteDocumentDesc {
        @Override
        public Long getNumber(DeliveryNote deliveryNote) {
            return deliveryNote.getChargeOff().getMovement().getNumber();
        }

        @Override
        public String getName(DeliveryNote deliveryNote) {
            return I18nSupport.message("detail.batch.history.document.movement");
        }

        @Override
        public Date getDate(DeliveryNote deliveryNote) {
            return deliveryNote.getChargeOff().getMovement().getCreateDate();
        }
    }
}
