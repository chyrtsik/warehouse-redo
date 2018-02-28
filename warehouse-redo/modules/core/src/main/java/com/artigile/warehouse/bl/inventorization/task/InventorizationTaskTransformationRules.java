/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.inventorization.task;

import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTask;
import com.artigile.warehouse.utils.dto.inventorization.task.InventorizationTaskTO;
import com.artigile.warehouse.utils.transofmers.InventorizationTaskTransformer;

/**
 * @author Shyrik, 01.04.2010
 */

/**
 * Rules of transformation for InventorizationTask-related classes.
 */
public class InventorizationTaskTransformationRules {
    public InventorizationTaskTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getInventorizationTaskToInventorizationTaskTORule());
    }

    private EntityTransformRule getInventorizationTaskToInventorizationTaskTORule() {
        //Rule for transformation from InventorizationTask entity to InventorizationTaskTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(InventorizationTask.class);
        rule.setTargetClass(InventorizationTaskTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return InventorizationTaskTransformer.transform((InventorizationTask)entity);
            }
        });
        return rule;
    }
}

