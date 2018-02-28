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
 * Transformation rule, that not changes operation during transformation.
 */
public class OperationSavingEntityTransformRule extends EntityTransformRuleBase {
    @Override
    public boolean isApplicable(Object entity, EntityOperation operation) {
        return entity.getClass().equals(fromClass);
    }

    @Override
    public EntityOperation transformOperation(EntityOperation operation) {
        return operation;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        else if ( !(obj instanceof OperationSavingEntityTransformRule) ){
            return false;
        }
        OperationSavingEntityTransformRule second = (OperationSavingEntityTransformRule)obj;
        return fromClass.equals(second.fromClass) && targetClass.equals(second.targetClass);
    }
}
