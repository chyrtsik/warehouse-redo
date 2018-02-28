/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.chargeoff;

import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.domain.chargeoff.ChargeOff;
import com.artigile.warehouse.domain.chargeoff.ChargeOffItem;
import com.artigile.warehouse.utils.dto.chargeoff.ChargeOffItemTO;
import com.artigile.warehouse.utils.dto.chargeoff.ChargeOffTO;
import com.artigile.warehouse.utils.dto.chargeoff.ChargeOffTOForReport;
import com.artigile.warehouse.utils.transofmers.ChargeOffTransformer;

/**
 * @author Shyrik, 31.03.2010
 */

/**
 * Rules of transformation for ChargeOff-related classes.
 */
public class ChargeOffTransformationRules {
    public ChargeOffTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getChargeOffToChangeOffTORule());
        notifier.registerTransformRule(getChargeOffToChangeOffTOForReportRule());
        notifier.registerTransformRule(getChargeItemToChangeOffItemTOForReportRule());
    }

    private EntityTransformRule getChargeOffToChangeOffTORule() {
        //Rule for transformation from ChargeOff entity to ChargeOffTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(ChargeOff.class);
        rule.setTargetClass(ChargeOffTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return ChargeOffTransformer.transform((ChargeOff)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getChargeOffToChangeOffTOForReportRule() {
        //Rule for transformation from ChargeOff entity to ChargeOffTOForReport DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(ChargeOff.class);
        rule.setTargetClass(ChargeOffTOForReport.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return ChargeOffTransformer.transformForReport((ChargeOff)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getChargeItemToChangeOffItemTOForReportRule() {
        //Rule for transformation from ChangeOffItem entity to ChargeOffItemTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(ChargeOffItem.class);
        rule.setTargetClass(ChargeOffItemTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return ChargeOffTransformer.transformItem((ChargeOffItem)entity);
            }
        });
        return rule;
    }
}
