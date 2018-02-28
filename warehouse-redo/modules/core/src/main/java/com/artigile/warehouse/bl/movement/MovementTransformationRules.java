/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.movement;

import com.artigile.warehouse.bl.common.listeners.*;
import com.artigile.warehouse.domain.movement.Movement;
import com.artigile.warehouse.domain.movement.MovementItem;
import com.artigile.warehouse.utils.dto.movement.MovementItemTO;
import com.artigile.warehouse.utils.dto.movement.MovementTO;
import com.artigile.warehouse.utils.dto.movement.MovementTOForReport;
import com.artigile.warehouse.utils.transofmers.MovementItemTransformer;
import com.artigile.warehouse.utils.transofmers.MovementTransformer;

/**
 * @author Shyrik, 01.04.2010
 */

/**
 * Rules of transformation for Movement-related classes.
 */
public class MovementTransformationRules {
    public MovementTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getMovementToMovementTOForReportRule());
        notifier.registerTransformRule(getMovementItemToMovementItemTORule());
        notifier.registerTransformRule(getMovementItemToMovementTOForReportRule());
    }

    private EntityTransformRule getMovementToMovementTOForReportRule() {
        //Rule for transformation from Movement entity to MovementTOForReport DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(Movement.class);
        rule.setTargetClass(MovementTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return MovementTransformer.transformForReport((Movement)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getMovementItemToMovementItemTORule() {
        //Rule for transformation from MovementItem entity to MovementItemTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(MovementItem.class);
        rule.setTargetClass(MovementItemTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return MovementItemTransformer.transform((MovementItem)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getMovementItemToMovementTOForReportRule() {
        //Rule for transformation from MovementItem entity to MovementTOForReport DTO.
        OperationReductionEntityTransformRule rule = new OperationReductionEntityTransformRule();
        rule.setFromClass(MovementItem.class);
        rule.setTargetClass(MovementTOForReport.class);
        rule.setFromOperations(EntityOperation.ALL);
        rule.setTargetOperation(EntityOperation.CHANGE);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                MovementItem movementItem = (MovementItem)entity;
                return MovementTransformer.transformForReport(movementItem.getMovement());
            }
        });
        return rule;
    }
}

