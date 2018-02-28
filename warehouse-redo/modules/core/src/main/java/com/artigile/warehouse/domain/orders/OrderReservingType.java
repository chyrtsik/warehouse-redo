/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.orders;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 13.04.2009
 */

/**
 * All kinds (strategies) of reserving order items.
 */
public enum OrderReservingType {
    /**
     * Means, that appropriate count of warehouse batches shuold be reserved only by user request.
     * This kind of reservation allows user to reserve and unreserve details for order.
     */
    // MANUAL,

    /**
     * Means, that appropriate count of warehouse batch items should be reserved
     * immediately after item has been added into order.
     */
    IMMEDIATELY;

    /**
     * Means, that appropriate count of warehouse batches should be reserved only when
     * order is send to the warehouse worker for complecting.
     */
    //BEFORE_COMPLECTING,

    /**
     * Means, that items should be reserved until given date and time.
     * TODO: it's not crear now, what to do with item, when the expected date has come.  
     */
    //UNTIL_DATE_TIME;

    /**
     * Use this method to obtain name of order reserving type.
     * @return
     */
    public String getName() {
        if (this.equals(IMMEDIATELY)){
            return I18nSupport.message("order.reserving.type.immediately");
        }
        /*if (this.equals(BEFORE_COMPLECTING)){
            return I18nSupport.message("order.reserving.type.beforeComplecting");
        }
        if (this.equals(UNTIL_DATE_TIME)){
            return I18nSupport.message("order.reserving.type.untilDateTime");
        }
        if (this.equals(MANUAL)){
            return I18nSupport.message("order.reserving.type.manual");
        }*/
        else {
            throw new AssertionError("Invalid order reserving type value.");
        }
    }
}
