/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.chargeoff;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 01.11.2009
 */

/**
 * All possible reasons of performing charge offs.
 */
public enum ChargeOffReason {
    /**
     * Charge off has been performed during moving wares operation.
     */
    MOVING_WARES(I18nSupport.message("chargeOff.reason.movingWares")),

    /**
     * Charge off has been performed before shipping to the custimer.
     */
    SHIPPING_TO_CUSTOMER(I18nSupport.message("chargeOff.reason.shippingToCustomer")),

    /**
     * Charge off has been perfirmed as a result of inventorization.
     */
    INVENTORIZATION(I18nSupport.message("chargeOff.reason.inventorization"));

    //===================== Naming impementation =================================
    private String name;

    ChargeOffReason(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
