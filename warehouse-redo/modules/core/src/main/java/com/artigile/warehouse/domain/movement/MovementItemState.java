/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.movement;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 21.11.2009
 */
public enum MovementItemState {
    /**
     * Item was not processed yet.
     */
    NOT_PROCESSED(1, I18nSupport.message("movement.item.state.name.notProcessed")),

    /**
     * Item is being complecting.
     */
    COMPLECTING(2, I18nSupport.message("movement.item.state.name.complecting")),

    /**
     * Item has been complected by warehouse worker.
     */
    COMPLECTED(3, I18nSupport.message("movement.item.state.name.complected")),

    /**
     * Item is ready for shipping from source warehouse.
     */
    READY_FOR_SHIPPING(4, I18nSupport.message("movement.item.state.name.readyForShipping")),

    /**
     * Item is shipping between warehouses.
     */
    SHIPPING(5, I18nSupport.message("movement.item.state.name.shipping")),

    /**
     * Item has been shipped to the target warehouse.
     */
    SHIPPED(6, I18nSupport.message("movement.item.state.name.shipped")),

    /**
     * Item is being posted at the target warehouse.
     */
    POSTING(7, I18nSupport.message("movement.item.state.name.posting")),

    /**
     * Item has been posted. Operation with this item  
     */
    POSTED(8, I18nSupport.message("movement.item.state.name.posted"));

    //========================= Naming support ========================
    /**
     * Display name of the movement item state.
     */
    private String name;

    /**
     * Ordinal number of the movement item state. State with greater order
     * is considered to be applied later during movement items's lifecycle.
     */
    private int stateNumber;

    private MovementItemState(int stateNumber, String name){
        this.stateNumber = stateNumber;
        this.name = name;
    }

    public String getName(){
        return name;
    }

    /**
     * Returns true, is this state is applied earlier, than the second state.
     * @param secondState
     * @return
     */
    public boolean isBefore(MovementItemState secondState) {
        return stateNumber < secondState.stateNumber;
    }
}
