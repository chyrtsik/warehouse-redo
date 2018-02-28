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
import com.artigile.warehouse.domain.complecting.ComplectingTask;
import com.artigile.warehouse.utils.dto.complecting.ComplectingTaskTO;
import com.artigile.warehouse.utils.transofmers.ComplectingTaskTransformer;

/**
 * @author Shyrik, 31.03.2010
 */

/**
 * Rules of transformation for ComplectingTask-related classes.
 */
public class ComplectingTaskTransformationRules {
    public ComplectingTaskTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getComplectingTaskToComplectingTaskTORule());
    }

    private EntityTransformRule getComplectingTaskToComplectingTaskTORule() {
        //Rule for transformation from ComplectingTask entity to ComplectingTaskTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(ComplectingTask.class);
        rule.setTargetClass(ComplectingTaskTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return ComplectingTaskTransformer.transform((ComplectingTask)entity);
            }
        });
        return rule;
    }
}
