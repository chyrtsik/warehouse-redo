/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.postings;

import com.artigile.warehouse.bl.common.listeners.*;
import com.artigile.warehouse.domain.postings.Posting;
import com.artigile.warehouse.domain.postings.PostingItem;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;
import com.artigile.warehouse.utils.dto.postings.PostingTO;
import com.artigile.warehouse.utils.dto.postings.PostingTOForReport;
import com.artigile.warehouse.utils.transofmers.PostingsTransformer;

/**
 * @author Shyrik, 01.04.2010
 */

/**
 * Rules of transformation for -related classes.
 */
public class PostingTransformationRules {
    public PostingTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getPostingToPostingTORule());
        notifier.registerTransformRule(getPostingToPostingTOForReportRule());
        notifier.registerTransformRule(getPostingItemToPostingItemTORule());
        notifier.registerTransformRule(getPostingItemToPostingTOForReportRule());
    }

    private EntityTransformRule getPostingToPostingTORule() {
        //Rule for transformation from Posting entity to PostingTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(Posting.class);
        rule.setTargetClass(PostingTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return PostingsTransformer.transform((Posting)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getPostingToPostingTOForReportRule() {
        //Rule for transformation from Posting entity to PostingTOForReport DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(Posting.class);
        rule.setTargetClass(PostingTOForReport.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return PostingsTransformer.transformPostingForReport((Posting)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getPostingItemToPostingItemTORule() {
        //Rule for transformation from PostingItem entity to PostingItemTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(PostingItem.class);
        rule.setTargetClass(PostingItemTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return PostingsTransformer.transformItem((PostingItem)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getPostingItemToPostingTOForReportRule() {
        //Rule for transformation from PostingItem entity to PostingTOForReport DTO.
        OperationReductionEntityTransformRule rule = new OperationReductionEntityTransformRule();
        rule.setFromClass(PostingItem.class);
        rule.setFromOperations(EntityOperation.ALL);
        rule.setTargetClass(PostingTOForReport.class);
        rule.setTargetOperation(EntityOperation.CHANGE);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                PostingItem item = (PostingItem)entity;
                return PostingsTransformer.transformPostingForReport(item.getPosting());
            }
        });
        return rule;
    }
}
