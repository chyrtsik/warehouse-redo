/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.orders;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 05.04.2009
 */

/**
 * All posible results of processing order item by warehouse worker.
 */
public enum OrderItemProcessingResult {
    /**
     * Item goods has been found and their quantity is enough to satisfy order item.
     */
    COMPLECTED,

    /**
     * There is some sort of problem with this item (not found enough ware, for example).
     */
    PROBLEM;

    /**
     * Use this method to obtain name of order item processing results.
     * @return
     */
    public String getName() {
        if (this.equals(COMPLECTED)){
            return I18nSupport.message("order.item.processing.result.complected");
        }
        else if (this.equals(PROBLEM)){
            return I18nSupport.message("order.item.processing.result.problem");
        }
        else {
            throw new RuntimeException("Invalid order item processing result value.");
        }
    }
}
