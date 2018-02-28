/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.printing;

/**
 * Created by IntelliJ IDEA.
 * User: Shyrik
 * Date: 29.11.2008
 * Time: 9:49:31
 */

/**
 * Types of print templates. DO NOT remove items from this enumeration, because database template
 * form id's depend on items in this enum.
 */
public enum PrintTemplateType {
    /**
     * Order document template.
     */
    TEMPLATE_ORDER,

    /**
     * Posting document template.
     */
    TEMPLATE_POSTING,

    /**
     * Purchase document template.
     */
    TEMPLATE_PURCHASE,

    /**
     * Movement document template.
     */
    TEMPLATE_MOVEMENT,

    /**
     * Complecting tasks for warehouse template list.
     */
    TEMPLATE_COMPLECTING_TASKS_FOR_WORKER,

    /**
     * Stickers for wares for order.
     */
    TEMPLATE_STICKER,

    /**
     * Inventorization tasks for warehouse template list.
     */
    TEMPLATE_INVENTORIZATION_TASKS_FOR_WORKER,

    /**
     * Template for printing detail batches list.
     */
    TEMPLATE_DETAIL_BATCHES_LIST,

    /**
     * Template for product serial numbers (this can be either simple number + bar code for warehouse or full
     * information about product with information about date of production and so on).
     */
    TEMPLATE_SERIAL_NUMBER_LIST,

    /**
     * Template for printing delivery notes.
     */
    TEMPLATE_DELIVERY_NOTE,

    /**
     * Template for printing stock report on given date.
     */
    TEMPLATE_STOCK_REPORT,

    /**
     * Template for printing report with changes of items in stock during the specified period of time.
     */
    TEMPLATE_STOCK_CHANGES_REPORT,
}
