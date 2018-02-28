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

import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.lock.LockGroup;
import org.hibernate.jdbc.Work;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author Valery Barysok, 16.11.2009
 */
public class LockGroupDAOImpl extends GenericEntityDAO<LockGroup> implements LockGroupDAO {

    /**
     * Extract entity name from given class.
     * @param entity
     * @return
     */
    private String getEntityName(Class entity) {
        String name = entity.getName();
        int i = name.lastIndexOf('.');
        return name.substring(i+1);
    }

    /**
     * Using reflection fot get primary key value of given entity.
     * @param entity
     * @return
     */
    private Object getId(Object entity) {
        try {
            Method idGetter = entity.getClass().getMethod("getId");
            idGetter.setAccessible(true);
            return idGetter.invoke(entity);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Lock entity ant place it into the lock group.
     * @param lockGroup lock group which includes this entity
     * @param entity which marked with Lockable annotation
     */
    public void lockEntity(final LockGroup lockGroup, final Object entity) {
        getSession().doWork(new Work() {

            @Override
            public void execute(Connection connection) throws SQLException {
                CallableStatement cs = connection.prepareCall("{ CALL lock_entity(?, ?, ?) }");
                cs.setString(1, getEntityName(entity.getClass()));
                cs.setLong(2, (Long)getId(entity));
                cs.setLong(3, lockGroup != null ? lockGroup.getId() : 0);
                cs.executeUpdate();
                cs.close();
            }
        });
    }

    /**
     * Unlock entity ant remove it from lock group.
     * @param lockGroup lock group which includes this entity
     * @param entity which marked with Lockable annotation
     */
    public void unlockEntity(final LockGroup lockGroup, final Object entity) {
        getSession().doWork(new Work() {

            @Override
            public void execute(Connection connection) throws SQLException {
                CallableStatement cs = connection.prepareCall("{ CALL unlock_entity(?, ?, ?) }");
                cs.setString(1, getEntityName(entity.getClass()));
                cs.setLong(2, (Long)getId(entity));
                cs.setLong(3, lockGroup != null ? lockGroup.getId() : 0);
                cs.executeUpdate();
                cs.close();
            }
        });
    }

    /**
     * Check whether entity has been locked by somebody.
     * @param entity
     * @return if entity is locked then true else false
     */
    public boolean isEntityLocked(final Object entity) {
        // TODO: upgrade to doReturningWork instead of doWork
        final boolean res[] = new boolean[1];
        getSession().doWork(new Work() {

            @Override
            public void execute(Connection connection) throws SQLException {
                CallableStatement cs = connection.prepareCall("{ CALL is_lock_entity(?, ?, ?) }");
                cs.setString(1, getEntityName(entity.getClass()));
                cs.setLong(2, (Long)getId(entity));
                cs.registerOutParameter(3, Types.INTEGER);
                if (cs.execute()) {
                    Integer result = cs.getInt(3);
                    cs.close();

                    res[0] = result.equals(Integer.valueOf(0));
                }
                res[0] = false;
            }
        });
        return res[0];
    }

    /**
     * Add owner for LockGroup.
     * @param lockGroup
     * @param lockOwner which is owner of this lockGroup
     */
    public void addLockOwner(final LockGroup lockGroup, final Object lockOwner) {
        getSession().doWork(new Work() {

            @Override
            public void execute(Connection connection) throws SQLException {
                CallableStatement cs = connection.prepareCall("{ CALL addLockOwner(?, ?, ?) }");
                cs.setString(1, getEntityName(lockOwner.getClass()));
                Long id = (Long) getId(lockOwner);
                cs.setLong(2, id);
                cs.setLong(3, lockGroup.getId());
                cs.executeUpdate();
                cs.close();
            }
        });
    }

    /**
     * remove lock from lock group owned by given lock owner
     * @param lockOwner which is owner of this lockGroup
     */
    public void unlockLockGroupsOfOwner(final Object lockOwner) {
        getSession().doWork(new Work() {

            @Override
            public void execute(Connection connection) throws SQLException {
                CallableStatement cs = connection.prepareCall("{ CALL unlockLockGroupsOfOwner(?, ?) }");
                cs.setString(1, getEntityName(lockOwner.getClass()));
                cs.setLong(2, (Long)getId(lockOwner));
                cs.executeUpdate();
                cs.close();
            }
        });
    }
}
