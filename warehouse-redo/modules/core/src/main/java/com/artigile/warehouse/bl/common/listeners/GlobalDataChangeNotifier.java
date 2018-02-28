/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.common.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shyrik, 10.03.2009
 */

/**
 * This class implements mapping of entity changes to their's listeners.
 */
public class GlobalDataChangeNotifier implements DataChangeListener {
    /**
     * Container for mapping listeners to their classes. Classes may be DTO and any other.
     */
    private Map<Class, DefaultDataChangeEventsSource> listenersMapping = new HashMap<Class, DefaultDataChangeEventsSource>();

    /**
     * Container for rules, used for mapping entities to objects of other type (usually DTO).
     */
    private List<EntityTransformRule> transformRules = new ArrayList<EntityTransformRule>();

    //=========================== Global listener operations =======================================
    
    /**
     * Adds new listener to the data listeners.
     * @param dataClass - class, which events listener wants to receive.
     * @param listener
     */
    public void addDataChangeListener(Class dataClass, DataChangeListener listener){
        DefaultDataChangeEventsSource eventsSource = listenersMapping.get(dataClass);
        if (eventsSource == null){
            //First listener for such king of data.
            eventsSource = new DefaultDataChangeEventsSource();
            listenersMapping.put(dataClass, eventsSource);
        }
        eventsSource.addDataChangeListener(listener);
    }

    /**
     * Removes listener of given data class.
     * @param dataClass - data class, which listener is to be removed.
     * @param listener - listener to be removed.
     */
    public void removeDataChangeListener(Class dataClass, DataChangeListener listener){
        DefaultDataChangeEventsSource eventsSource = listenersMapping.get(dataClass);
        if (eventsSource != null){
            eventsSource.removeDataChangeListener(listener);
            if (!eventsSource.hasListeners()){
                //We don't need empty listeners container. 
                listenersMapping.remove(dataClass);
            }
        }
    }

    /**
     * Use this method to register transformation rules.
     * @param transformRule - rule, that transforms entity of one type to the entity of another type.
     */
    public void registerTransformRule(EntityTransformRule transformRule){
        if (!transformRules.contains(transformRule)){
            transformRules.add(transformRule);            
        }
    }

    //================================ DataChangeListener implementation ===================================

    @Override
    public void afterDelete(Object deletedData) {
        processEvent(afterDeleteEventGenerator, deletedData);
    }

    @Override
    public void afterCreate(Object createdData) {
        processEvent(afterCreateEventGenerator, createdData);
    }

    @Override
    public void afterChange(Object changedData) {
        processEvent(afterChangeEventGenerator, changedData);
    }

    //============================== Notification implementation ========================================

    private void processEvent(EventGenerationStrategy eventGenerator, Object eventEntitySource) {
        //1. Notify of all listeners of given entity about changes in this entity.
        DefaultDataChangeEventsSource entity_listener = listenersMapping.get(eventEntitySource.getClass());
        if (entity_listener != null){
            eventGenerator.fireEvent(entity_listener, eventEntitySource);
        }

        //2. Process transformation rules for given entity and notify all listeners, that are listening
        //to the changes in entities, to which initial entity may be transformed.
        for (EntityTransformRule transformRule : transformRules){
            if (transformRule.isApplicable(eventEntitySource, eventGenerator.getEntityOperation())){
                processTransformRule(eventEntitySource, eventGenerator.getEntityOperation(), transformRule);
            }
        }
    }

    private void processTransformRule(Object initialEntity, EntityOperation initialEntityOperation, EntityTransformRule transformRule) {
        EventGenerationStrategy transformedEventGenerator = getEventGenerator(transformRule.transformOperation(initialEntityOperation));
        Object transformedEntity = transformRule.transformEntity(initialEntity);
        if (transformedEntity != null) {
            if (List.class.isAssignableFrom(transformedEntity.getClass())){
                //Processing list of transformed entities.
                List transformedItems = (List)transformedEntity;
                for (Object transformedItem : transformedItems){
                    processEvent(transformedEventGenerator, transformedItem);
                }
            }
            else{
                //Processing single transformed entity.
                processEvent(transformedEventGenerator, transformedEntity);
            }
        }
    }

    private EventGenerationStrategy getEventGenerator(EntityOperation operation) {
        if (operation.equals(EntityOperation.DELETE)){
            return afterDeleteEventGenerator;
        }
        else if (operation.equals(EntityOperation.CREATE)){
            return afterCreateEventGenerator;
        }
        else if (operation.equals(EntityOperation.CHANGE)){
            return afterChangeEventGenerator;
        }
        throw new RuntimeException("GlobalDataChangeNotifier.getEventGenerator - unsupported entity operation.");
    }

    /**
     * This interface used for un directional generation of events, which allows us to eliminate code
     * duplication, whe we are to fire different events.
     */
    private interface EventGenerationStrategy {
        void fireEvent(DefaultDataChangeEventsSource listener, Object eventSource);
        EntityOperation getEntityOperation();
    }

    private EventGenerationStrategy afterDeleteEventGenerator = new EventGenerationStrategy(){
        public void fireEvent(DefaultDataChangeEventsSource listener, Object eventSource) {
            listener.fireAfterDelete(eventSource);
        }
        public EntityOperation getEntityOperation() {
            return EntityOperation.DELETE;
        }
    };

    private EventGenerationStrategy afterCreateEventGenerator = new EventGenerationStrategy(){
        public void fireEvent(DefaultDataChangeEventsSource listener, Object eventSource) {
            listener.fireAfterCreate(eventSource);
        }
        public EntityOperation getEntityOperation() {
            return EntityOperation.CREATE;
        }
    };

    private EventGenerationStrategy afterChangeEventGenerator = new EventGenerationStrategy(){
        public void fireEvent(DefaultDataChangeEventsSource listener, Object eventSource) {
            listener.fireAfterChange(eventSource);
        }
        public EntityOperation getEntityOperation() {
            return EntityOperation.CHANGE;
        }
    };
}
