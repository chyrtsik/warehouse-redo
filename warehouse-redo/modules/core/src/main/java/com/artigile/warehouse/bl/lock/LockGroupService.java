/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.lock;

import com.artigile.warehouse.bl.common.exceptions.CannotEstablishLockException;
import com.artigile.warehouse.bl.common.exceptions.CannotPerformOperationException;
import com.artigile.warehouse.dao.LockGroupDAO;
import com.artigile.warehouse.dao.generic.lock.LockType;
import com.artigile.warehouse.dao.generic.lock.Lockable;
import com.artigile.warehouse.domain.lock.LockGroup;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.UserTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Set;

/**
 * @author Valery Barysok, 11.11.2009
 */

/**
 * Service for offline lock management.
 * <p>
 * Locks made by this service may cause following SQL errors:
 * <ul>
 * <li>ERR_LOCK_001 - failed attempt of entity modifications because this entity is locked.</li>
 * <li>ERR_LOCK_002 - failed to establish exclusive lock because this entity is already locked (exclusively or not exclusively).</li>
 * <li>ERR_LOCK_003 - failed to establish non exclusive lock because this entity is already locked exclusively.</li>
 * </ul>
 */
@Transactional(rollbackFor = CannotPerformOperationException.class)
public class LockGroupService {
    private LockGroupDAO lockGroupDAO;

    /**
     * Creates new lock group object and fills it's data.
     * @param lockType type of new lock.
     * @return created instance of LockGroup
     */
    private LockGroup createLockGroup(LockType lockType) {
        LockGroup lockGroup = new LockGroup();
        lockGroup.setLockType(lockType);
        lockGroup.setCreateUser(UserTransformer.transformUser(WareHouse.getUserSession().getUser())); //TODO: eliminate this request to presentation tier.
        lockGroup.setCreateDate(Calendar.getInstance().getTime());
        return lockGroup;
    }

    /**
     * Establishes exclusive lock for the given set of entities.
     * @param lockOwner object, that is marked as lock owner.
     * @param entities which need to be locked by lock group.
     * @return instance of lock group which contains all entities.
     * @throws CannotPerformOperationException
     */
    public LockGroup exclusiveLock(Object lockOwner, Set<Object> entities) throws CannotPerformOperationException {
        return lock(LockType.EXCLUSIVE_LOCK, lockOwner, entities);
    }

    /**
     * Performs a lock of given type over given set of entities.
     * @param lockType
     * @param lockOwner
     * @param entities which need to be locked by lock group
     * @return instance of lock group which contains all entities
     * @throws CannotPerformOperationException
     */
    private LockGroup lock(LockType lockType, Object lockOwner, Set<Object> entities) throws CannotPerformOperationException {
        LockGroup lockGroup = createLockGroup(lockType);
        lockGroupDAO.save(lockGroup);
        lockGroupDAO.flush();
        lockGroupDAO.addLockOwner(lockGroup, lockOwner);

        for (Object item : entities) {
            boolean isPresent = item.getClass().isAnnotationPresent(Lockable.class);
            if (isPresent) {
                // No need flush because direct modification used.
                try{
                    lockGroupDAO.lockEntity(lockGroup, item);
                }
                catch (RuntimeException e){
                    if (Utils.isLockFailedException(e)){
                        throw new CannotEstablishLockException();
                    }
                }
            } else {
                throw new CannotPerformOperationException(I18nSupport.message("locking.error.not.supported"));
            }
        }

        return lockGroup;
    }

    /**
     * Removes all lock groups owner by goven entity.
     * @param lockOwner owner of lock group
     */
    public void unlockGroupsOfOwner(Object lockOwner) {
        lockGroupDAO.unlockLockGroupsOfOwner(lockOwner);
    }

    //==================== Spring setters ============================
    public void setLockGroupDAO(LockGroupDAO lockGroupDAO) {
        this.lockGroupDAO = lockGroupDAO;
    }
}
