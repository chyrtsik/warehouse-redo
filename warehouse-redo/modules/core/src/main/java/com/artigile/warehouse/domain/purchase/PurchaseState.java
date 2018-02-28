/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.purchase;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 01.03.2009
 */

/**
 * States of purchase life cycle.
 */
public enum PurchaseState {
    /**
     * The initial state of the purchase. In this state wares are placed into the purchase.
     */
    CONSTRUCTION,

    /**
     * Purchase if ordered from the supplier and is beeng wait to come soon.
     */
    WAITING,

    /**
     * This state means, that purchase has beed shipped from the supplier (but not processed by the
     * warehouse workers).
     */
    SHIPPED,

    /**
     * This state means, that purchase is beeing processed be the warehouse worker during the
     * positng operation.
     */
    IN_POSTING,

    /**
     * This is the end state of the purchase, means, that is hase beed completely processed.
     * In this state purchases are stored for purchase history.
     */
    POSTING_DONE;

    /**
     * Use this method to obtain name of ware need items states.
     * @return
     */
    public String getName() {
        if (this.equals(CONSTRUCTION)){
            return I18nSupport.message("purchase.state.name.construction");
        }
        else if (this.equals(WAITING)){
            return I18nSupport.message("purchase.state.name.waiting");
        }
        else if (this.equals(SHIPPED)){
            return I18nSupport.message("purchase.state.name.shipped");
        }
        else if (this.equals(IN_POSTING)){
            return I18nSupport.message("purchase.state.name.inPosting");
        }
        else if (this.equals(POSTING_DONE)){
            return I18nSupport.message("purchase.state.name.postingDone");
        }
        else {
            throw new RuntimeException("Invalid purchase state value.");
        }
    }

    public static PurchaseState getInitialState() {
        return CONSTRUCTION;
    }
}
