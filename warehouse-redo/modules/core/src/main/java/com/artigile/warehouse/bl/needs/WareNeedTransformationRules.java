/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.needs;

import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.domain.needs.WareNeed;
import com.artigile.warehouse.domain.needs.WareNeedItem;
import com.artigile.warehouse.utils.dto.needs.WareNeedItemTO;
import com.artigile.warehouse.utils.dto.needs.WareNeedTO;
import com.artigile.warehouse.utils.transofmers.WareNeedTransformer;

/**
 * @author Shyrik, 01.04.2010
 */

/**
 * Rules of transformation for WareNeed-related classes.
 */
public class WareNeedTransformationRules {
    public WareNeedTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getWareNeedToWareNeedTORule());
        notifier.registerTransformRule(getWareNeedItemToWareNeedItemTORule());
    }

    private EntityTransformRule getWareNeedToWareNeedTORule() {
        //Rule for transformation from WareNeedItem entity to WareNeedItemTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(WareNeed.class);
        rule.setTargetClass(WareNeedTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return WareNeedTransformer.transformWareNeed((WareNeed)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getWareNeedItemToWareNeedItemTORule() {
        //Rule for transformation from WareNeedItem entity to WareNeedItemTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(WareNeedItem.class);
        rule.setTargetClass(WareNeedItemTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return WareNeedTransformer.transformItem((WareNeedItem)entity);
            }
        });
        return rule;
    }
}

