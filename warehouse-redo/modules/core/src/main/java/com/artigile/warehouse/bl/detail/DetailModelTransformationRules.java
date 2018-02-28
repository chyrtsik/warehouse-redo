/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.detail;

import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.domain.details.DetailModel;
import com.artigile.warehouse.utils.dto.details.DetailModelTO;
import com.artigile.warehouse.utils.transofmers.DetailModelsTransformer;

/**
 * @author Shyrik, 31.03.2010
 */

/**
 * Rules of transformation for -related classes.
 */
public class DetailModelTransformationRules {
    public DetailModelTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getDetailModelToDetailModelTORule());
    }

    private EntityTransformRule getDetailModelToDetailModelTORule() {
        //Rule for transformation from DetailModel entity to DetailModelTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(DetailModel.class);
        rule.setTargetClass(DetailModelTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return DetailModelsTransformer.transform((DetailModel)entity);
            }
        });
        return rule;
    }
}