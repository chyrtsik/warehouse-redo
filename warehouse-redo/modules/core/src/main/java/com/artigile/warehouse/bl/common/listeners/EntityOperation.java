/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.common.listeners;

/**
 * @author Shyrik, 22.03.2010
 */

/**
 * A set of operations, supported by entity change notification mechanism.
 */
public enum EntityOperation {
    /**
     * Create operation (sql insert statement execution is assumed).
     */
    CREATE,

    /**
     * Change of entity (sql update statement is assumed).
     */
    CHANGE,

    /**
     * Deletion of the entity (sql delete statement is assumed).
     */
    DELETE;

    /**
     * Use this constant to substitute filling full list of operations.
     */
    public static final EntityOperation[] ALL = new EntityOperation[]{CREATE, CHANGE, DELETE};
}
