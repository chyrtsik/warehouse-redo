/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.details.DetailBatchOperation;
import com.artigile.warehouse.domain.postings.PostingItem;
import com.artigile.warehouse.utils.dto.details.DetailBatchOperationTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aliaksandr.Chyrtsik, 02.10.11
 */
public class DetailBatchOperationTransformer {
    public static List<DetailBatchOperationTO> transformList(List<DetailBatchOperation> detailBatchOperations) {
        List<DetailBatchOperationTO> list = new ArrayList<DetailBatchOperationTO>(detailBatchOperations.size());
        for (DetailBatchOperation operation : detailBatchOperations){
            list.add(transform(operation));
        }
        return list;
    }

    private static DetailBatchOperationTO transform(DetailBatchOperation operation) {
        DetailBatchOperationTO operationTO = new DetailBatchOperationTO();

        //Information about operation.
        operationTO.setId(operation.getId());
        operationTO.setDateTime(operation.getOperationDateTime());
        operationTO.setPerformedUser(operation.getPerformedUser().getDisplayName());

        //Information about goods changed.
        operationTO.setDetailBatch(DetailBatchTransformer.batchTO(operation.getDetailBatch()));
        operationTO.setStoragePlace(StoragePlaceTransformer.transformForReport(operation.getStoragePlace()));
        PostingItem postingItem = operation.getPostingItemOfChangedWarehouseBatch();
        if (postingItem != null){
            operationTO.setBatchNo(postingItem.getId());
            operationTO.setReceiptDate(postingItem.getPosting().getCreateDate());
            operationTO.setBuyPrice(postingItem.getOriginalPrice());
            operationTO.setBuyCurrency(CurrencyTransformer.transformCurrency(postingItem.getOriginalCurrency()));
        }
        operationTO.setShelfLifeDateOfChangedWarehouseBatch(operation.getShelfLifeDateOfChangedWarehouseBatch());

        //Document according to which this operation was performed.
        operationTO.setDocumentNumber(operation.getDocumentNumber());
        operationTO.setDocumentName(operation.getDocumentName());
        operationTO.setDocumentDate(operation.getDocumentDate());
        operationTO.setDocumentContractorName(operation.getDocumentContractorName());

        //Changed counts and costs (buying costs in the most of cases).
        operationTO.setInitialCount(operation.getInitialCount());
        operationTO.setPostedCount(operation.getChangeOfCount() > 0 ? operation.getChangeOfCount() : 0);
        operationTO.setChargedOffCount(operation.getChangeOfCount() < 0 ? -operation.getChangeOfCount() : 0);
        operationTO.setFinalCount(operationTO.getInitialCount() + operation.getChangeOfCount());

        BigDecimal itemCost = operation.getItemCost();
        operationTO.setInitialCost(itemCost != null ? itemCost.multiply(BigDecimal.valueOf(operation.getInitialCount())) : null);
        operationTO.setPostedCost(itemCost != null && operation.getChangeOfCount() > 0 ? itemCost.multiply(BigDecimal.valueOf(operation.getChangeOfCount())) : null);
        operationTO.setChargedOffCost(itemCost != null && operation.getChangeOfCount() < 0 ? itemCost.multiply(BigDecimal.valueOf(-operation.getChangeOfCount())) : null);
        operationTO.setFinalCost(itemCost != null ? itemCost.multiply(BigDecimal.valueOf(operation.getNewCount())) : null);

//        //Sell price (specially for some clients to calculate revenue per product).
//        operationTO.setPrice(operation.getItemPrice());
//        operationTO.setCurrency(CurrencyTransformer.transformCurrency(operation.getCurrency()));
//        operationTO.setTotal(calculateOperationTotal(operation.getItemPrice(), operation.getChangeOfCount()));

        return operationTO;
    }

    private static BigDecimal calculateOperationTotal(BigDecimal price, long count){
        return price == null ? null : price.multiply(BigDecimal.valueOf(Math.abs(count)));
    }
}
