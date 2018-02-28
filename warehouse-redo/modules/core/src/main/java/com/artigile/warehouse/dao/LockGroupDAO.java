/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.lock.LockGroup;

/**
 * @author Valery Barysok, 16.11.2009
 */
public interface LockGroupDAO extends EntityDAO<LockGroup> {

    /**
     * Set locked state for Entity.
     * @param lockGroup lock group which includes this entity
     * @param entity which marked with Lockable annotation
     */
    public void lockEntity(LockGroup lockGroup, Object entity);

    /**
     * Clear locked state for Entity.
     * @param lockGroup lock group which includes this entity
     * @param entity which marked with Lockable annotation
     */
    public void unlockEntity(LockGroup lockGroup, Object entity);

    /**
     * Check whether entity had been locked by somebody.
     * @param entity
     * @return if entity is locked then true else false
     */
    public boolean isEntityLocked(Object entity);

    /**
     * Set owner for LockGroup.
     * @param lockGroup lock group which owner is being created.
     * @param lockOwner owner of the lock group.
     */
    public void addLockOwner(LockGroup lockGroup, Object lockOwner);

    /**
     * Remove lock from lock group owned by given lock owner.
     * @param lockOwner which is owner of this lockGroup
     */
    public void unlockLockGroupsOfOwner(Object lockOwner);
}
