/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.warehouse;

import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.domain.warehouse.Warehouse;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.transofmers.WarehouseTransformer;

/**
 * @author Shyrik, 01.04.2010
 */

/**
 * Rules of transformation for Warehouse-related classes.
 */
public class WarehouseTransformationRules {
    public WarehouseTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getWarehouseToWarehouseTORule());
        notifier.registerTransformRule(getWarehouseToWarehouseTOForReportRule());
    }

    private EntityTransformRule getWarehouseToWarehouseTORule() {
        //Rule for transformation from Warehouse entity to WarehouseTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(Warehouse.class);
        rule.setTargetClass(WarehouseTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return WarehouseTransformer.transform((Warehouse)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getWarehouseToWarehouseTOForReportRule() {
        //Rule for transformation from Warehouse entity to WarehouseTOForReport DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(Warehouse.class);
        rule.setTargetClass(WarehouseTOForReport.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return WarehouseTransformer.transformForReport((Warehouse)entity);
            }
        });
        return rule;
    }
}

        