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

import com.artigile.warehouse.bl.common.listeners.*;
import com.artigile.warehouse.domain.details.DetailField;
import com.artigile.warehouse.domain.details.DetailType;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailTypeTO;
import com.artigile.warehouse.utils.dto.details.DetailTypeTOForReport;
import com.artigile.warehouse.utils.transofmers.DetailTypesTransformer;

/**
 * @author Shyrik, 31.03.2010
 */

/**
 * Rules of transformation for DetailType-related classes.
 */
public class DetailTypeTransformationRules {
    private DetailTypeService detailTypeService;

    public DetailTypeTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getDetailTypeToDetailTypeTOForReportRule());
        notifier.registerTransformRule(getDetailTypeToDetailTypeTORule());
        notifier.registerTransformRule(getDetailFieldToDetailFieldTORule());
        notifier.registerTransformRule(getDetailFieldToDetailTypeTOForReportRule());
    }

    private EntityTransformRule getDetailTypeToDetailTypeTOForReportRule() {
        //Rule for transformation from DetailType entity to DetailTypeForReport DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(DetailType.class);
        rule.setTargetClass(DetailTypeTOForReport.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return DetailTypesTransformer.transformDetailTypeForReport((DetailType) entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getDetailTypeToDetailTypeTORule() {
        //Rule for transformation from DetailType entity to DetailTypeTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(DetailType.class);
        rule.setTargetClass(DetailTypeTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return DetailTypesTransformer.transformDetailType((DetailType) entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getDetailFieldToDetailFieldTORule() {
        //Rule for transformation from DetailField entity to DetailFieldTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(DetailField.class);
        rule.setTargetClass(DetailFieldTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return DetailTypesTransformer.transformDetailTypeField((DetailField) entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getDetailFieldToDetailTypeTOForReportRule() {
        //Rule for transformation from DetailField entity to DetailTypeTO DTO.
        OperationReductionEntityTransformRule rule = new OperationReductionEntityTransformRule();
        rule.setFromClass(DetailField.class);
        rule.setFromOperations(EntityOperation.ALL);
        rule.setTargetClass(DetailTypeTOForReport.class);
        rule.setTargetOperation(EntityOperation.CHANGE);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                DetailField detailField = (DetailField)entity;
                DetailType detailType = detailTypeService.getDetailTypeByFieldId(detailField.getId());
                return DetailTypesTransformer.transformDetailTypeForReport(detailType);
            }
        });
        return rule;
    }

    //========================== Spting setters ============================

    public void setDetailTypeService(DetailTypeService detailTypeService) {
        this.detailTypeService = detailTypeService;
    }
}