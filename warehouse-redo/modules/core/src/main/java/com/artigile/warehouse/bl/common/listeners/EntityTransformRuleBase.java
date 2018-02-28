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
 * @author Shyrik, 27.03.2010
 */

/**
 * Super class for entity transform rules. Holds data and method, similar to all
 * transform rules.
 */
public abstract class EntityTransformRuleBase implements EntityTransformRule {
    /**
     * Source class, that is to be transformed.
     */
    protected Class fromClass;

    /**
     * Class, to which source entity would be transformed using this rule.
     */
    protected Class targetClass;

    /**
     * Transformer, that performs entity transformation from source to target class.
     */
    private EntityTransformer entityTransformer;

    //========================= Initialization ===================================
    public void setFromClass(Class fromClass) {
        this.fromClass = fromClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public void setEntityTransformer(EntityTransformer entityTransformer) {
        this.entityTransformer = entityTransformer;
    }

    //======================= EntityTransformRule implementation ================
    @Override
    public Object transformEntity(Object entity){
        return entityTransformer.transform(entity);
    }
}
