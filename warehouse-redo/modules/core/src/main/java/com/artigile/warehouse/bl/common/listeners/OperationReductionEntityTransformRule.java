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
import java.util.Collections;
import java.util.List;

/**
 * @author Shyrik, 27.03.2010
 */

/**
 * Rule, that performs transformation from a list of source entity operations to a
 * single target entity operation.
 */
public class OperationReductionEntityTransformRule extends EntityTransformRuleBase {

    /**
     * Operations on the source entity, that are supported by this transform rule.
     */
    private List<EntityOperation> fromOperations;

    /**
     * Operation on target class, to which source operation will be transformed.
     */
    private EntityOperation targetOperation;

    //============================ Construction and initialization ==============================
    public OperationReductionEntityTransformRule() {
    }

    public void setFromOperations(EntityOperation ... fromOperations) {
        this.fromOperations = new ArrayList<EntityOperation>();
        Collections.addAll(this.fromOperations, fromOperations);
    }

    public void setTargetOperation(EntityOperation targetOperation) {
        this.targetOperation = targetOperation;
    }

    //==================== TransformRule implementation =======================================
    @Override
    public boolean isApplicable(Object entity, EntityOperation operation){
        return entity.getClass().equals(fromClass) && fromOperations.contains(operation);
    }

    @Override
    public EntityOperation transformOperation(EntityOperation operation) {
        return targetOperation;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        else if ( !(obj instanceof OperationReductionEntityTransformRule) ){
            return false;
        }
        OperationReductionEntityTransformRule second = (OperationReductionEntityTransformRule)obj;
        return fromClass.equals(second.fromClass) && fromOperations.equals(second.fromOperations) &&
               targetClass.equals(second.targetClass) && targetOperation.equals(second.targetOperation);
    }
}
