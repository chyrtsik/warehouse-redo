/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao.interceptor;

import com.artigile.warehouse.bl.common.listeners.DataChangeListener;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Shyrik, 09.03.2009
 */

/**
 * This intercceptor is used to notify another parts of application, abou changes, made in domain model objects
 * of applicaton. Using this interceptor allows permanent notification about changes withot writing a lot of
 * code in bsuness tier services.
 */
public class DataChangesInterceptor extends EmptyInterceptor {
    /**
     * Temporary sets of changed objects.
     */
    private Set<Object> deletedObjects = new HashSet<Object>();
    private Set<Object> changedObjects = new HashSet<Object>();
    private Set<Object> createdObjects = new HashSet<Object>();

    /**
     * Listener of data changes.
     */
    private DataChangeListener listener;

    public DataChangesInterceptor() {
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        deletedObjects.add(entity);
    }

    @Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        changedObjects.add(entity);
		return false;
	}

    @Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
	    createdObjects.add(entity);
        return false;
	}

    @Override
    public void beforeTransactionCompletion(Transaction tx) {
        if (!tx.wasRolledBack()){
            //Notify data changes listener about changes, that are to be made with domain objects.
            //...Deleted as useless code.
        }
    }

    @Override
	public void afterTransactionCompletion(Transaction tx) {
        if (!tx.wasRolledBack()){
            //Notify data changes listener about changes, have been made with domain objects.
            for (Object obj : deletedObjects){
                executeIgnoreExceptions(obj, new ListenerExecutor(){
                    public void execute(Object obj) {
                        listener.afterDelete(obj);
                    }
                });
            }
            for (Object obj : changedObjects){
                executeIgnoreExceptions(obj, new ListenerExecutor(){
                    public void execute(Object obj) {
                        listener.afterChange(obj);
                    }
                });
            }
            for (Object obj : createdObjects){
                executeIgnoreExceptions(obj, new ListenerExecutor(){
                    public void execute(Object obj) {
                        listener.afterCreate(obj);
                    }
                });
            }
        }
        deletedObjects.clear();
        changedObjects.clear();
        createdObjects.clear();
    }

    /**
     * Performs executions of given listenes with ignoring possible runtime exceptions.
     * @param obj
     * @param listenerExecutor
     */
    private void executeIgnoreExceptions(Object obj, ListenerExecutor listenerExecutor) {
        try{
            listenerExecutor.execute(obj);
        }
        catch (RuntimeException ex){
            LoggingFacade.logError(this, ex);
        }
    }

    private interface ListenerExecutor {
        void execute(Object obj);
    }

    //==================================== Spring setters ======================================
    public void setListener(DataChangeListener listener) {
        this.listener = listener;
    }
}
