/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao.generic.lock;

/**
 * @author Valery Barysok, 12.12.2009
 */

/**
 * Supported types of locks.
 */
public enum LockType {
    /**
     * Exclusive lock. When entity is locked with such a lock, no other locks are permitted to lock this
     * entity.
     */
    EXCLUSIVE_LOCK,
}
