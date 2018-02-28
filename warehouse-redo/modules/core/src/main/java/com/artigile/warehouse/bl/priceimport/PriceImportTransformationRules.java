/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.priceimport;

import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.domain.priceimport.ContractorPriceImport;
import com.artigile.warehouse.utils.dto.priceimport.ContractorPriceImportTO;
import com.artigile.warehouse.utils.transofmers.ContractorPriceImportTransformer;

/**
 * @author Valery Barysok, 7/10/11
 */

public class PriceImportTransformationRules {
    public PriceImportTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getPriceImportToPriceImportTORule());
    }

    private EntityTransformRule getPriceImportToPriceImportTORule() {
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(ContractorPriceImport.class);
        rule.setTargetClass(ContractorPriceImportTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return ContractorPriceImportTransformer.transform((ContractorPriceImport) entity);
            }
        });
        return rule;
    }
}
