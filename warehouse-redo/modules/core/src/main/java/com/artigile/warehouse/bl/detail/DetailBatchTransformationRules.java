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
import com.artigile.warehouse.dao.DetailBatchDAO;
import com.artigile.warehouse.dao.WarehouseBatchDAO;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.details.DetailModel;
import com.artigile.warehouse.domain.warehouse.WarehouseBatch;
import com.artigile.warehouse.gui.menuitems.details.batches.PriceUpdater;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;
import com.artigile.warehouse.utils.transofmers.DetailBatchTransformer;
import com.artigile.warehouse.utils.transofmers.WarehouseBatchTransformer;

import java.util.List;

/**
 * @author Shyrik, 31.03.2010
 */

/**
 * Rules of transformation for DetailBatch-related classes.
 */
public class DetailBatchTransformationRules {
    private DetailBatchDAO detailBatchDAO;
    private WarehouseBatchDAO warehouseBatchDAO;

    public DetailBatchTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getDetailBatchToDetailBatchTORule());
        notifier.registerTransformRule(getWarehouseBatchToDetailBatchTORule());
        notifier.registerTransformRule(getDetailModelToDetailBatchTORule());
        notifier.registerTransformRule(getDetailModelToWarehouseBatchTORule());
    }

    private EntityTransformRule getDetailBatchToDetailBatchTORule() {
        //Rule for transformation from DetailBatch entity to DetailBatchTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(DetailBatch.class);
        rule.setTargetClass(DetailBatchTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return PriceUpdater.updatePrices(DetailBatchTransformer.batchTO((DetailBatch)entity));
            }
        });
        return rule;
    }

    private EntityTransformRule getWarehouseBatchToDetailBatchTORule() {
        //Rule for transformation from WarehouseBatch entity to DetailBatchTO DTO.
        OperationReductionEntityTransformRule rule = new OperationReductionEntityTransformRule();
        rule.setFromClass(WarehouseBatch.class);
        rule.setFromOperations(EntityOperation.ALL);
        rule.setTargetClass(DetailBatchTO.class);
        rule.setTargetOperation(EntityOperation.CHANGE);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                WarehouseBatch warehouseBatch = (WarehouseBatch)entity;
                DetailBatch detailBatch = warehouseBatch.getDetailBatch();
                return detailBatch == null ? null : DetailBatchTransformer.batchTO(detailBatch);
            }
        });
        return rule;
    }

    private EntityTransformRule getDetailModelToDetailBatchTORule() {
        //Rule for transformation from DetailModel entity to DetailBatchTO DTO.
        OperationReductionEntityTransformRule rule = new OperationReductionEntityTransformRule();
        rule.setFromClass(DetailModel.class);
        rule.setFromOperations(EntityOperation.CHANGE);
        rule.setTargetClass(DetailBatchTO.class);
        rule.setTargetOperation(EntityOperation.CHANGE);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                DetailModel model = (DetailModel)entity;
                List<DetailBatch> batches = detailBatchDAO.getBatchesByDetailModel(model);
                return DetailBatchTransformer.list(batches);
            }
        });
        return rule;
    }

    private EntityTransformRule getDetailModelToWarehouseBatchTORule() {
        //Rule for transformation from DetailModel entity to WarehouseBatchTO DTO.
        OperationReductionEntityTransformRule rule = new OperationReductionEntityTransformRule();
        rule.setFromClass(DetailModel.class);
        rule.setFromOperations(EntityOperation.CHANGE);
        rule.setTargetClass(WarehouseBatchTO.class);
        rule.setTargetOperation(EntityOperation.CHANGE);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                DetailModel model = (DetailModel)entity;
                List<WarehouseBatch> warehouseBatches = warehouseBatchDAO.getWarehouseBatchesForDetailModel(model);
                return WarehouseBatchTransformer.transformList(warehouseBatches);
            }
        });
        return rule;
    }

    //================================ Spring setters ===============================

    public void setDetailBatchDAO(DetailBatchDAO detailBatchDAO) {
        this.detailBatchDAO = detailBatchDAO;
    }

    public void setWarehouseBatchDAO(WarehouseBatchDAO warehouseBatchDAO) {
        this.warehouseBatchDAO = warehouseBatchDAO;
    }
}

