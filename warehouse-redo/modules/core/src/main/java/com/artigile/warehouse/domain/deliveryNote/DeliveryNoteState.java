/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.deliveryNote;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 01.11.2009
 */

/**
 * All possible states of delivery note document.
 */
public enum DeliveryNoteState {
    /**
     * Delivery note document has been composed and is ready to be processed.
     */
    COMPOSED(1, I18nSupport.message("deliveryNote.state.composed")),

    /**
     * Delivery note document is being shipped to warehouse.
     */
    SHIPPING_TO_WAREHOUSE(2, I18nSupport.message("deliveryNote.state.shippingToWarehouse")),

    /**
     * Delivery note document is being shipped to contractor (consumer).
     */
    SHIPPING_TO_CONTRACTOR(2, I18nSupport.message("deliveryNote.state.shippingToContractor")),

    /**
     * Delivery note has been shipped to the destination warehouse or customer.
     */
    SHIPPED(3, I18nSupport.message("deliveryNote.state.shipped")),

    /**
     * Delivery note is being posted at the destination warehouse.
     */
    POSTING(4, I18nSupport.message("deliveryNote.state.posting")),

    /**
     * Delivery note has been fully processed and closed.
     */
    CLOSED(5, I18nSupport.message("deliveryNote.state.closed"));

    //===================== Naming impementation =================================
    private int stateNumber;
    private String name;

    DeliveryNoteState(int stateNumber, String name) {
        this.stateNumber = stateNumber;
        this.name = name;
    }

    public String getName(){
        return name;
    }

    /**
     * Returns the last state in the delivery note lifecycle.
     * @return
     */
    public static DeliveryNoteState getLastState(){
        return CLOSED;
    }

    /**
     * Checks, of this state is used in delivery note's lifecycle before, that second state.
     * @param secondState
     * @return
     */
    public boolean isBefore(DeliveryNoteState secondState){
        return stateNumber < secondState.stateNumber;
    }
}
