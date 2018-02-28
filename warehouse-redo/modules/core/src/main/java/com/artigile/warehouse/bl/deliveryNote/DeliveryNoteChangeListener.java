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
 * @author Shyrik, 07.11.2009
 */
public interface DeliveryNoteChangeListener {
    /**
     * Called, when state of delivery note has been changed.
     * @param deliveryNote changed delivery note.
     * @param oldState old delivery note state.
     * @param newState new delivery note state.
     */
    void onDeliveryNoteStateChanged(DeliveryNote deliveryNote, DeliveryNoteState oldState, DeliveryNoteState newState) throws BusinessException;

    /**
     * Called immediately after delivery note has been created.
     * @param deliveryNote new delivery note.
     */
    void onDeliveryNoteCreated(DeliveryNote deliveryNote) throws BusinessException;
}
