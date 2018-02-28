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
 * Interface of transformation rule from the entity class to the objects of other class.
 * Used for routing of notifications of changes in entities. 
 */
public interface EntityTransformRule {
    /**
     * Returns true, is this rule can be applied to the given entity and operation.
     * @param entity entity that has been operated with.
     * @param operation operation that has been performed with the entity.
     * @return
     */
    boolean isApplicable(Object entity, EntityOperation operation);

    /**
     * Returns operation, to which operation on initial entity will be changed.
     * @param operation operation, performed with initial entity.
     * @return
     */
    EntityOperation transformOperation(EntityOperation operation);

    /**
     * Performs transformation (conversion) of the entity from initial entity to th appropriate
     * entity of target type.
     * Notice, that transformed object may be an array of entities of target type.
     * @param entity original (changed) entity.
     * @return
     */
    Object transformEntity(Object entity);
}
