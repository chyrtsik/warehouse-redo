/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
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
import com.artigile.warehouse.domain.details.DetailBatchImport;
import com.artigile.warehouse.utils.dto.details.DetailBatchImportTO;
import com.artigile.warehouse.utils.transofmers.DetailBatchImportTransformer;

/**
 * @author Aliaksandr.Chyrtsik, 06.11.11
 */
public class DetailBatchImportTransformationRules {

    public DetailBatchImportTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getDetailBatchImportToDetailBatchImportTORule());
    }

    private EntityTransformRule getDetailBatchImportToDetailBatchImportTORule() {
        //Rule for transformation from DetailBatchImport entity to DetailBatchImportTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(DetailBatchImport.class);
        rule.setTargetClass(DetailBatchImportTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return DetailBatchImportTransformer.transform((DetailBatchImport) entity);
            }
        });
        return rule;
    }
}
