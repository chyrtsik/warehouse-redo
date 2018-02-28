/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.warehouseBatch;

import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.domain.warehouse.WarehouseBatch;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;
import com.artigile.warehouse.utils.transofmers.WarehouseBatchTransformer;

/**
 * @author Shyrik, 01.04.2010
 */

/**
 * Rules of transformation for WarehouseBatch-related classes.
 */
public class WarehouseBatchTransformationRules {
    public WarehouseBatchTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getWarehouseBatchToWarehouseBatchTORule());
    }

    private EntityTransformRule getWarehouseBatchToWarehouseBatchTORule() {
        //Rule for transformation from WarehouseBatch entity to WarehouseBatchTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(WarehouseBatch.class);
        rule.setTargetClass(WarehouseBatchTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return WarehouseBatchTransformer.transform((WarehouseBatch)entity);
            }
        });
        return rule;
    }
}

