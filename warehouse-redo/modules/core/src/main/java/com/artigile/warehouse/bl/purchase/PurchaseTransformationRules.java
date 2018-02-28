/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.purchase;

import com.artigile.warehouse.bl.common.listeners.*;
import com.artigile.warehouse.domain.purchase.Purchase;
import com.artigile.warehouse.domain.purchase.PurchaseItem;
import com.artigile.warehouse.utils.dto.purchase.PurchaseItemTO;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTO;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTOForReport;
import com.artigile.warehouse.utils.transofmers.PurchaseTransformer;

/**
 * @author Shyrik, 01.04.2010
 */

/**
 * Rules of transformation for Purchase-related classes.
 */
public class PurchaseTransformationRules {
    public PurchaseTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getPurchaseToPurchaseTORule());
        notifier.registerTransformRule(getPurchaseToPurchaseTOForReportRule());
        notifier.registerTransformRule(getPurchaseItemToPurchaseItemTORule());
        notifier.registerTransformRule(getPurchaseItemToPurchaseTOForReportRule());
    }

    private EntityTransformRule getPurchaseToPurchaseTORule() {
        //Rule for transformation from Purchase entity to PurchaseTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(Purchase.class);
        rule.setTargetClass(PurchaseTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return PurchaseTransformer.transformPurchaseFull((Purchase)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getPurchaseToPurchaseTOForReportRule() {
        //Rule for transformation from Purchase entity to PurchaseTOForReport DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(Purchase.class);
        rule.setTargetClass(PurchaseTOForReport.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return PurchaseTransformer.transform((Purchase)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getPurchaseItemToPurchaseItemTORule() {
        //Rule for transformation from PurchaseItem entity to PurchaseItemTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(PurchaseItem.class);
        rule.setTargetClass(PurchaseItemTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return PurchaseTransformer.transformItem((PurchaseItem)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getPurchaseItemToPurchaseTOForReportRule() {
        //Rule for transformation from PurchaseItem entity to PurchaseTOForReport DTO.
        OperationReductionEntityTransformRule rule = new OperationReductionEntityTransformRule();
        rule.setFromClass(PurchaseItem.class);
        rule.setFromOperations(EntityOperation.ALL);
        rule.setTargetClass(Purchase.class);
        rule.setTargetOperation(EntityOperation.CHANGE);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                PurchaseItem purchaseItem = (PurchaseItem) entity;
                return PurchaseTransformer.transform(purchaseItem.getPurchase());
            }
        });
        return rule;
    }
}
