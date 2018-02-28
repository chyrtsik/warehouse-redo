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

import com.artigile.warehouse.dao.generic.lock.LockOwner;
import com.artigile.warehouse.dao.generic.lock.Lockable;
import com.artigile.warehouse.utils.SpringServiceContext;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Map;

/**
 * @author Valery Barysok, 03.12.2009
 */

/**
 * Update locking support by entities at RDBMS level  
 */
@Transactional
public class LockingManagerService {

    /**
     * prepare DB to support lock logic
     */
    public void prepareLockSupport() {
        initLockGroupItems();
        initLockGroupOwners();
    }

    /**
     * register all lockable entities in the DB
     */
    private void initLockGroupItems() {
        SessionFactory factory = SpringServiceContext.getInstance().getSessionFactory();
        Map metadata = factory.getAllClassMetadata();
        for(Object key : metadata.keySet()) {
            ClassMetadata classMetadata = (ClassMetadata) metadata.get(key);
            Class clazz = classMetadata.getMappedClass();
            if (clazz != null && clazz.isAnnotationPresent(Lockable.class)) {
                registerLockable(clazz);
            }
        }
        updateLockable();
    }

    /**
     * register all lock owner entities in the DB
     */
    private void initLockGroupOwners() {
        SessionFactory factory = SpringServiceContext.getInstance().getSessionFactory();
        Map metadata = factory.getAllClassMetadata();
        for(Object key : metadata.keySet()) {
            ClassMetadata classMetadata = (ClassMetadata) metadata.get(key);
            Class clazz = classMetadata.getMappedClass();
            if (clazz != null && clazz.isAnnotationPresent(LockOwner.class)) {
                registerLockOwner(clazz);
            }
        }
        updateLockOwner();
    }

    /**
     * register lockable entity
     */
    private void registerLockable(Class entity) {
        execute("CALL register_lockable('"+ getEntityName(entity) + "');");
    }

    /**
     * create join tables for registered entities and
     * delete join tables for no longer lockable entities
     */
    private void updateLockable() {
        execute("CALL update_lockable();");
        createLockableTriggers();
    }

    /**
     * register lock owner entity
     */
    private void registerLockOwner(Class entity) {
        execute("CALL registerLockOwner('"+ getEntityName(entity) + "');");
    }

    /**
     * create lock owner join tables for registered entities and
     * delete lock owner join tables for no longer lock owner entities
     */
    private void updateLockOwner() {
        execute("CALL updateLockOwner();");
    }

    private void execute(String sql) {
        getSession().createSQLQuery(sql).executeUpdate();
    }

    private String getEntityName(Class entity) {
        String name = entity.getName();
        int i = name.lastIndexOf('.');
        return name.substring(i+1);
    }

    private SessionFactory getSessionFactory() {
        return SpringServiceContext.getInstance().getSessionFactory();
    }

    private Session getSession() {
        return getSessionFactory().getCurrentSession();
    }

    private enum TRIGGER_EVENT {
        UPDATE(0),
        DELETE(1);

        private int id;

        private TRIGGER_EVENT(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    private void nativeExecute(final String sql) {
        try {
            getSession().doWork(new Work() {

                @Override
                public void execute(Connection connection) throws SQLException {
                    Statement statement = connection.createStatement();
                    statement.execute(sql);
                }
            });
        } catch (HibernateException e) {
            throw new RuntimeException("Cannot execute native query: " + sql, e);
        }
    }

    private void createLockableTriggers() {
        Map metadata = getSessionFactory().getAllClassMetadata();
        for(Object key : metadata.keySet()) {
            ClassMetadata classMetadata = (ClassMetadata) metadata.get(key);
            Class clazz = classMetadata.getMappedClass();
            if (clazz != null && clazz.isAnnotationPresent(Lockable.class)) {
                String entityName = getEntityName(clazz);

                //1. Creating triggers for monitoring changes in lockable objects.
                createLockableTrigger(entityName, TRIGGER_EVENT.UPDATE);
                createLockableTrigger(entityName, TRIGGER_EVENT.DELETE);

                //2. Creating trigger for checking lock allowance.
                createLockAllowanceTrigger(entityName);
            }
        }
    }

    private void createLockableTrigger(String entityName, TRIGGER_EVENT event) {
        //1. Drop old trigger.
        Object obj = getSession()
                .createSQLQuery("SELECT CONCAT('DROP TRIGGER IF EXISTS ', " +
                            " get_lockable_trigger_name('" + entityName + "', " + event.getId() + "));")
                .uniqueResult();
        nativeExecute(obj.toString());

        //2. Create new trigger.
        obj = getSession()
                .createSQLQuery("SELECT get_lockable_trigger_sql('" + entityName + "', " + event.getId() + ");")
                .uniqueResult();
        nativeExecute(obj.toString());
    }

    private void createLockAllowanceTrigger(String entityName) {
        //1. Drop old trigger.
        String dropTriggerSQL = MessageFormat.format(
            "SELECT CONCAT(''DROP TRIGGER IF EXISTS '', get_lock_allowance_trigger_name(''{0}''));",
            entityName
        );
        Object obj = getSession().createSQLQuery(dropTriggerSQL).uniqueResult();
        nativeExecute(obj.toString());

        //2. Create new trigger.
        String createTriggerSQL = MessageFormat.format("SELECT get_lock_allowance_trigger_sql(''{0}'');", entityName);
        obj = getSession().createSQLQuery(createTriggerSQL).uniqueResult();
        nativeExecute(obj.toString());        
    }
}
