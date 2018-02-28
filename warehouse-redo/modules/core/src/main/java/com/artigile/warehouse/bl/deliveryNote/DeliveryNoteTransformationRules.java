/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.deliveryNote;

import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNote;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteItem;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteItemTO;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteTOForReport;
import com.artigile.warehouse.utils.transofmers.DeliveryNoteTransformer;

/**
 * @author Shyrik, 31.03.2010
 */

/**
 * Rules of transformation for DeliveryNote-related classes.
 */
public class DeliveryNoteTransformationRules {
    public DeliveryNoteTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getDeliveryNoteToDeliveryNoteTOForReportRule());
        notifier.registerTransformRule(getDeliveryNoteItemToDeliveryNoteItemTORule());

    }

    private EntityTransformRule getDeliveryNoteToDeliveryNoteTOForReportRule() {
        //Rule for transformation from DeliveryNote entity to DeliveryNoteTOForReport DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(DeliveryNote.class);
        rule.setTargetClass(DeliveryNoteTOForReport.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return DeliveryNoteTransformer.transformForReport((DeliveryNote)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getDeliveryNoteItemToDeliveryNoteItemTORule() {
        //Rule for transformation from DeliveryNoteItem entity to DeliveryNoteItemTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(DeliveryNoteItem.class);
        rule.setTargetClass(DeliveryNoteItemTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return DeliveryNoteTransformer.transformItem((DeliveryNoteItem)entity);
            }
        });
        return rule;
    }
}
