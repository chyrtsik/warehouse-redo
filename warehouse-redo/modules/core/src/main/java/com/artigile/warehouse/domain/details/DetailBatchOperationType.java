/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.details;

/**
 * All possible types of detail batch operations.
 *
 * @author Aliaksandr.Chyrtsik, 02.10.11
 */
public enum DetailBatchOperationType {
    /**
     * Posting operation (quantity has been increased).
     */
    POSTING,

    /**
     * Charge off operation (quantity has been decreased).
     */
    CHARGE_OFF,

    /**
     * In warehouse movement. Total quantity wasn't changed, but detail has been
     * moved from one storage place to another.
     */
    MOVEMENT,

    /**
     * Manual correction of count by warehouse worker. Used when count in warehouse system differs from
     * real count of ware in the warehouse. this is an alternative way to inventory operation.
     */
    MANUAL_CORRECTION,
}
