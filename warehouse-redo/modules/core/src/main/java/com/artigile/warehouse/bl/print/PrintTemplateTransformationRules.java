/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.print;

import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.domain.printing.PrintTemplateInstance;
import com.artigile.warehouse.utils.dto.template.PrintTemplateInstanceTO;
import com.artigile.warehouse.utils.transofmers.PrintTemplateTransformer;

/**
 * @author Shyrik, 01.04.2010
 */

/**
 * Rules of transformation for PrintTemplate-related classes.
 */
public class PrintTemplateTransformationRules {
    public PrintTemplateTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getPrintTemplateToPrintTemplateTORule());
    }

    private EntityTransformRule getPrintTemplateToPrintTemplateTORule() {
        //Rule for transformation from PrintTemplate entity to PrintTemplateTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(PrintTemplateInstance.class);
        rule.setTargetClass(PrintTemplateInstanceTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return PrintTemplateTransformer.transformPrintTemplateInstance((PrintTemplateInstance) entity);
            }
        });
        return rule;
    }
}

