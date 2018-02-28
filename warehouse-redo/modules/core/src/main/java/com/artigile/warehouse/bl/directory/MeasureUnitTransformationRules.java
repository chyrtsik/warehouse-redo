/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.directory;

import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.domain.directory.MeasureUnit;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.transofmers.MeasureUnitTransformer;

/**
 * @author Shyrik, 31.03.2010
 */

/**
 * Rules of transformation for -related classes.
 */
public class MeasureUnitTransformationRules {
    public MeasureUnitTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getMeasureUnitToMeasureUnitTOForReportRule());
    }

    private EntityTransformRule getMeasureUnitToMeasureUnitTOForReportRule() {
        //Rule for transformation from MeasureUnit entity to MeasureUnitTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(MeasureUnit.class);
        rule.setTargetClass(MeasureUnitTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return MeasureUnitTransformer.transform((MeasureUnit)entity);
            }
        });
        return rule;
    }
}