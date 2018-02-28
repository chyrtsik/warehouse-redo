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

/**
 * Describes possible state of the movement document.
 */
public enum MovementState {
    /**
     * Movement document is being edited by worker. Nobody at the warehouse see this document.
     */
    CONSTRUCTION(I18nSupport.message("movement.state.name.construction")),

    /**
     * Tasks from the movement has been created for warehouse workers and they may work on them.
     */
    COMPLECTING(I18nSupport.message("movement.state.name.complecting")),

    /**
     * All moving positions has been complected at the warehouse.
     */
    COMPLECTED(I18nSupport.message("movement.state.name.complected")),

    /**
     * All moving positions now is ready for shipping.
     */
    READY_FOR_SHIPPING(I18nSupport.message("movement.state.name.readyForShipping")),

    /**
     * All moving positions now is being moved from one location to another.
     */
    SHIPPING(I18nSupport.message("movement.state.name.shipping")),

    /**
     * All positions in movement operation has been shipped to target place.
     */
    SHIPPED(I18nSupport.message("movement.state.name.shipped")),

    /**
     * All positions of the movement now in posting process.
     */
    POSTING(I18nSupport.message("movement.state.name.posting")),

    /**
     * All positions of the movement has been posted to te target place. 
     */
    COMPLETED(I18nSupport.message("movement.state.name.completed"));

    //========================= Naming support =================================
    private String name;

    private MovementState(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
