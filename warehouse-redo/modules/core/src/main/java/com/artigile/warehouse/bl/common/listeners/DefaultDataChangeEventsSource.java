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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 09.03.2009
 */

/**
 * Default implementation of data change events source.
 * Implements listeners management and events firing.
 */
public class DefaultDataChangeEventsSource implements DataChangeEventsSource {
    /**
     * All listeners, attached to this events source.
     */
    private List<DataChangeListener> listeners = new ArrayList<DataChangeListener>();

    public boolean hasListeners(){
        return listeners.size() > 0;
    }

    //============================ DataChangeEventsSource implementation ==============================
    @Override
    public void addDataChangeListener(DataChangeListener listener) {
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    @Override
    public void removeDataChangeListener(DataChangeListener listener) {
        listeners.remove(listener);
    }

    //=============================== Events firing implementation ====================================
    public void fireAfterDelete(Object deletedData){
        for (DataChangeListener listener : listeners){
            listener.afterDelete(deletedData);
        }
    }

    public void fireAfterCreate(Object createdData){
        for (DataChangeListener listener : listeners){
            listener.afterCreate(createdData);
        }
    }

    public void fireAfterChange(Object changedData){
        for (DataChangeListener listener : listeners){
            listener.afterChange(changedData);
        }
    }
}
