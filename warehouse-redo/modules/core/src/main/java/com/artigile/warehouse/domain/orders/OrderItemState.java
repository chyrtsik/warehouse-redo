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
 * All possible states of the order item in terms of processing it by warehouse worker.
 */
public enum OrderItemState {
    /**
     * Order item is waiting it's turn for processing by warehouse worker.
     */
    NOT_PROCESSED(1, I18nSupport.message("order.item.state.name.notProcessed")),

    /**
     * Order item is been processing by warehouse worker. He noticed this item but not
     * entered quantity of goods, really found at the warehouse.
     */
    PROCESSING(2, I18nSupport.message("order.item.state.name.processing")),

    /**
     * Item has been processed by warehouse worker.
     */
    PROCESSED(3, I18nSupport.message("order.item.state.name.processed")),

    /**
     * Item is ready for shipping from warehouse.
     */
    READY_FOR_SHIPPING(4, I18nSupport.message("order.item.state.name.readyForShipping")),

    /**
     * Item has been shipped from warehouse.
     */
    SHIPPED(5, I18nSupport.message("order.item.state.name.shipped")),

    /**
     * Items has been successfully shipped to the customer and closed.
     */
    SOLD(6, I18nSupport.message("order.item.state.name.sold"));

    //============================= Construction and name support ===============================
    /**
     * Display name of the state.
     */
    private String name;

    /**
     * Ordinal number of the order item state. State with greater order
     * is considered to be applied later during order items's lifecycle.
     */
    private int stateNumber;

    private OrderItemState(int stateNumber, String name){
        this.stateNumber = stateNumber;
        this.name = name;
    }

    /**
     * Use this method to obtain name of order item states.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Use this method to get initial state of the order item (at the creation of the order item).
     * @return
     */
    public static OrderItemState getInitialState() {
        return NOT_PROCESSED;
    }

    /**
     * Use this method to get the last possible state of the order item.
     * @return
     */
    public static OrderItemState getLastState(){
        return SOLD;
    }

    /**
     * Returns true, is this state is applied earlier, than the second state.
     * @param secondState
     * @return
     */
    public boolean isBefore(OrderItemState secondState){
        return stateNumber < secondState.stateNumber;
    }
}
