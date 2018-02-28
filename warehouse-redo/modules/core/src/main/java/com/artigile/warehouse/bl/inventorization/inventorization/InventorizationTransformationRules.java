/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.inventorization.inventorization;

import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.domain.inventorization.Inventorization;
import com.artigile.warehouse.domain.inventorization.InventorizationItem;
import com.artigile.warehouse.utils.dto.inventorization.InventorizationItemTO;
import com.artigile.warehouse.utils.dto.inventorization.InventorizationTO;
import com.artigile.warehouse.utils.transofmers.InventorizationItemTransformer;
import com.artigile.warehouse.utils.transofmers.InventorizationTransformer;

/**
 * @author Shyrik, 01.04.2010
 */

/**
 * Rules of transformation for Inventorization-related classes.
 */
public class InventorizationTransformationRules {
    public InventorizationTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getInventorizationToInventorizationTORule());
        notifier.registerTransformRule(getInventorizationItemToInventorizationItemTORule());
    }

    private EntityTransformRule getInventorizationToInventorizationTORule() {
        //Rule for transformation from Inventorization entity to InventorizationTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(Inventorization.class);
        rule.setTargetClass(InventorizationTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return InventorizationTransformer.transformForReport((Inventorization)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getInventorizationItemToInventorizationItemTORule() {
        //Rule for transformation from InventorizationItem entity to InventorizationItemTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(InventorizationItem.class);
        rule.setTargetClass(InventorizationItemTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return InventorizationItemTransformer.transform((InventorizationItem)entity);
            }
        });
        return rule;
    }
}
