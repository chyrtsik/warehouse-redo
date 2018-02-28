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

import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteState;

/**
 * @author Shyrik, 07.11.2009
 */

/**
 * Filter for loading delivery notes list.
 */
public class DeliveryNoteFilter {
    /**
     * Identifier of destination warehouse for delivery note.
     */
    private Long destinationWarehouseId;

    /**
     * States of delivery notes to be loaded.
     */
    private DeliveryNoteState states[];

    //=============================== Getters and setters ===============================
    public Long getDestinationWarehouseId() {
        return destinationWarehouseId;
    }

    public void setDestinationWarehouseId(Long destinationWarehouseId) {
        this.destinationWarehouseId = destinationWarehouseId;
    }

    public DeliveryNoteState[] getStates() {
        return states;
    }

    public void setStates(DeliveryNoteState[] states) {
        this.states = states;
    }
}
