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
import com.artigile.warehouse.domain.deliveryNote.DeliveryNote;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteState;

/**
 * @author Shyrik, 20.12.2009
 */
public class DeliveryNoteChangeAdapter implements DeliveryNoteChangeListener {
    @Override
    public void onDeliveryNoteStateChanged(DeliveryNote deliveryNote, DeliveryNoteState oldState, DeliveryNoteState newState) throws BusinessException {
    }

    @Override
    public void onDeliveryNoteCreated(DeliveryNote deliveryNote) throws BusinessException {
    }
}
