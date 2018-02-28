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
 * @author Shyrik, 06.01.2009
 */

/**
 * Possible states of the order.
 */
public enum OrderState {
    /**
     * The initial state of each order. In this state list of order items is
     * being constructed by the sales manager.
     */
    CONSTRUCTION(1, I18nSupport.message("order.state.name.construction")),

    /**
     * Order has been constructed and is wating for beginning of collection.
     */
    READY_FOR_COLLECTION(2, I18nSupport.message("order.state.name.readyForCollection")),

    /**
     * In this state position in the order are being collected by the warehouse worker.
     */
    COLLECTION(3, I18nSupport.message("order.state.name.collection")),

    /**
     * Problem with one or more positions of the order (not enough wares).
     */
    COLLECTION_PROBLEM(4, I18nSupport.message("order.state.name.collectionProblem")),

    /**
     * In this state all positions in the order have been collected (or marked as missing).
     */
    COLLECTED(5, I18nSupport.message("order.state.name.collected")),

    /**
     * In this state order is ready to be shipped.
     */
    READY_FOR_SHIPPING(6, I18nSupport.message("order.state.name.readyForShipping")),

    /**
     * In this state order has been taken by the delivery service. But we don't know, if the
     * order will be shipped successfully.
     */
    SHIPPED(7, I18nSupport.message("order.state.name.shipped")),

    /**
     * Order has been sold to the customer.
     */
    SOLD(8, I18nSupport.message("order.state.name.sold")),

    /**
     * Order content marked as reserved.
     */
    RESERVED(9, I18nSupport.message("order.state.name.reserved"));

    //================================= Operations =============================================
    private int stateNumber;

    private String name;

    private OrderState(int stateNumber, String name){
        this.stateNumber = stateNumber;
        this.name = name;
    }

    /**
     * Returns true, if order state is one of the state, which order may have during real processing
     * by workers (not by manager only).
     * @return
     */
    public boolean isInProcessing() {
        return !this.equals(CONSTRUCTION);
    }

    /**
     * Use this method to obtain name of order states.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Returns true, if second state is applied before this state.
     * @param second
     * @return
     */
    public boolean isBefore(OrderState second){
        return stateNumber < second.stateNumber;
    }
}
