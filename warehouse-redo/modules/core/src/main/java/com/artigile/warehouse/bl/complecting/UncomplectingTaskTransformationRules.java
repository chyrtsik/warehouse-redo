/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.complecting;

import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.domain.complecting.UncomplectingTask;
import com.artigile.warehouse.utils.dto.complecting.UncomplectingTaskTO;
import com.artigile.warehouse.utils.transofmers.UncomplectingTaskTransformer;

/**
 * @author Shyrik, 31.03.2010
 */

/**
 * Rules of transformation for UncomplectingTask-related classes.
 */
public class UncomplectingTaskTransformationRules {
    public UncomplectingTaskTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getUncomplectingTaskToUncomplectingTaskTORule());
    }

    private EntityTransformRule getUncomplectingTaskToUncomplectingTaskTORule() {
        //Rule for transformation from UncomplectingTask entity to UncomplectingTaskTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(UncomplectingTask.class);
        rule.setTargetClass(UncomplectingTaskTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return UncomplectingTaskTransformer.transform((UncomplectingTask)entity);
            }
        });
        return rule;
    }
}
