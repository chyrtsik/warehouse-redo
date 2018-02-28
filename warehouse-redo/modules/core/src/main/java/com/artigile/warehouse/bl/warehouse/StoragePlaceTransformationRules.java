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
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTO;
import com.artigile.warehouse.utils.transofmers.StoragePlaceTransformer;

/**
 * @author Shyrik, 01.04.2010
 */

/**
 * Rules of transformation for Warehouse-related classes.
 */
public class StoragePlaceTransformationRules {
    public StoragePlaceTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getStoragePlaceToStoragePlaceTORule());
    }

    private EntityTransformRule getStoragePlaceToStoragePlaceTORule() {
        //Rule for transformation from StoragePlace entity to StoragePlaceTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(StoragePlace.class);
        rule.setTargetClass(StoragePlaceTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return StoragePlaceTransformer.transform((StoragePlace)entity);
            }
        });
        return rule;
    }
}
