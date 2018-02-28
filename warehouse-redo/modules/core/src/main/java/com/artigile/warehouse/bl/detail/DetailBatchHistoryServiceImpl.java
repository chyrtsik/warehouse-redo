/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.detail;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchChangeDocument;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchChangeListener;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchCountChangeEvent;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchService;
import com.artigile.warehouse.dao.DetailBatchHistoryDAO;
import com.artigile.warehouse.dao.UserDAO;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.details.DetailBatchOperation;
import com.artigile.warehouse.domain.details.DetailBatchOperationType;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.domain.warehouse.WarehouseBatch;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.dto.details.DetailBatchOperationTO;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.artigile.warehouse.utils.transofmers.DetailBatchOperationTransformer;
import com.artigile.warehouse.utils.transofmers.DetailBatchTransformer;
import com.artigile.warehouse.utils.transofmers.StoragePlaceTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Service for working with detail batch history records.
 *
 * @author Aliaksandr.Chyrtsik, 02.10.11
 */
@Transactional(rollbackFor = BusinessException.class)
public class DetailBatchHistoryServiceImpl implements DetailBatchHistoryService, WarehouseBatchChangeListener{

    private DetailBatchHistoryDAO detailBatchHistoryDAO;
    private UserDAO userDAO;
    private WarehouseBatchService warehouseBatchService;

    public DetailBatchHistoryServiceImpl(){
    }

    //=========================== Listening on changes in warehouse batch and history writing ====================

    public void initialize(){
        warehouseBatchService.addWarehouseBatchChangeListener(this);
    }

    @Override
    public void onWarehouseBatchCountChanged(WarehouseBatchCountChangeEvent event) throws BusinessException {
        //Perform logging of warehouse batch change.
        WarehouseBatch finalWarehouseBatch = event.getFinalWarehouseBatch();

        DetailBatchOperation operation = new DetailBatchOperation();
        operation.setOperationDateTime(Calendar.getInstance().getTime());
        operation.setPerformedUser(userDAO.get(WareHouse.getUserSession().getUser().getId())); //TODO: eliminate this reference to presentation tier.
        operation.setDetailBatch(finalWarehouseBatch.getDetailBatch());
        operation.setStoragePlace(finalWarehouseBatch.getStoragePlace());
        operation.setInitialCount(event.getInitialCount());
        operation.setNewCount(finalWarehouseBatch.getAmount());
        operation.setChangeOfCount(event.getCountDelta());

        if (warehouseBatchService.isTrackPostingItem()) {
            //Keep track on posting item of changes warehouse batch to distinguish changes in future.
            operation.setPostingItemOfChangedWarehouseBatch(event.getFinalWarehouseBatch().getPostingItem());
        }

        if (warehouseBatchService.isTrackShelfLife()) {
            operation.setShelfLifeDateOfChangedWarehouseBatch(event.getFinalWarehouseBatch().getShelfLifeDate());
        }

        WarehouseBatchChangeDocument document = event.getDocument();
        if (document.isInWarehouseMovement()){
            operation.setOperationType(DetailBatchOperationType.MOVEMENT);
        }
        else if (document.isManualCountCorrection()){
            operation.setOperationType(DetailBatchOperationType.MANUAL_CORRECTION);
        }
        else if (document.isPosting()){
            operation.setOperationType(DetailBatchOperationType.POSTING);
            operation.setPostingItem(document.getPostingItem());
        }
        else if (document.isChargeOff()){
            operation.setOperationType(DetailBatchOperationType.CHARGE_OFF);
            operation.setChargeOffItem(document.getChargeOffItem());
            document.getChargeOffItem().setChargeOffOperation(operation);
        }
        else{
            LoggingFacade.logWarning(this, "Unsupported warehouse batch change document.");
        }

        detailBatchHistoryDAO.save(operation);
    }

    //========================== Loading of history =============================================================
    @Override
    public List<DetailBatchOperationTO> getDetailBatchHistoryForReport(Date periodStart, Date periodEnd, DetailBatchHistoryFilter filter) {
        //1. Load list of all operations with given filter.
        List<DetailBatchOperationTO> operations = DetailBatchOperationTransformer.transformList(detailBatchHistoryDAO.loadStockChangeHistory(periodStart, periodEnd, filter));

        //2. Perform auto summing of counts of different batches of the same detail (to make data usable for users).
        //first -- detail batch id, second - current count of details in stock.
        Map<Long, Long> detailsCount = new HashMap<Long, Long>();
        //first -- detail batch id, second - current cost of details in stock.
        Map<Long, BigDecimal> detailsCost = new HashMap<Long, BigDecimal>();
        //first -- detail batch id, second -- set identified of batches (posting items) that are included into count calculation.
        Map<Long, Set<Long>> batchesIncludedIntoCount = new HashMap<Long, Set<Long>>();

        //First -- detail batch id. Second -- entity of that detail batch.
        Map<Long, DetailBatch> detailsCache = new HashMap<Long, DetailBatch>();
        //First -- detail batch id, second -- storage places where this detail batch is located.
        Map<Long, Map<Long, StoragePlace>> storagePlacesWithoutChanges = new HashMap<Long, Map<Long, StoragePlace>>();

        if (periodStart != null){
            //2.1. Calculation initial values of counts and costs.
            List<DetailBatchOperation> stockReport = detailBatchHistoryDAO.loadStockReport(periodStart, filter);
            for (DetailBatchOperation stockReportItem : stockReport){
                long detailId = stockReportItem.getDetailBatch().getId();
                Long batchNo = stockReportItem.getPostingItemOfChangedWarehouseBatch() == null ? null : stockReportItem.getPostingItemOfChangedWarehouseBatch().getId();

                if (filter.isAddSummaryForItemsWithoutOperations()){
                    //Initialize structures for providing summary for items without changes.
                    detailsCache.put(detailId, stockReportItem.getDetailBatch());
                    Map<Long, StoragePlace> detailPlaces = storagePlacesWithoutChanges.get(detailId);
                    if (detailPlaces == null){
                        detailPlaces = new HashMap<Long, StoragePlace>();
                        storagePlacesWithoutChanges.put(detailId, detailPlaces);
                    }
                    detailPlaces.put(stockReportItem.getStoragePlace().getId(), stockReportItem.getStoragePlace());
                }

                Long lastDetailCount = detailsCount.get(detailId);
                if (lastDetailCount == null){
                    //This is the first time we found this detail in results -- initialize maps.
                    detailsCount.put(detailId, stockReportItem.getNewCount());
                    detailsCost.put(detailId, multiplyNotNullValues(stockReportItem.getItemPrice(), BigDecimal.valueOf(stockReportItem.getNewCount())));
                    Set<Long> batchedInCount = new HashSet<Long>();
                    batchedInCount.add(batchNo);
                    batchesIncludedIntoCount.put(detailId, batchedInCount);
                }
                else {
                    BigDecimal lastDetailCost = detailsCost.get(detailId);
                    Set<Long> batchesInCount = batchesIncludedIntoCount.get(detailId);
                    if (!batchesInCount.contains(batchNo)){
                        //Summarize count with other batches of the same detail.
                        batchesInCount.add(batchNo);
                        detailsCount.put(detailId, stockReportItem.getNewCount() + lastDetailCount);
                        detailsCost.put(detailId, addNotNullValues(lastDetailCost, multiplyNotNullValues(stockReportItem.getItemPrice(), BigDecimal.valueOf(stockReportItem.getNewCount()))));
                    }
                    else{
                        //Override initial count of operation with information from previously summarized batches and calculate new final cost and count.
                        detailsCount.put(detailId, lastDetailCount + stockReportItem.getChangeOfCount());
                        detailsCost.put(detailId, addNotNullValues(lastDetailCost, multiplyNotNullValues(stockReportItem.getItemPrice(), BigDecimal.valueOf(stockReportItem.getChangeOfCount()))));
                    }
                }
            }
        }

        //2.2. Summing counts costs for history operations.
        for (DetailBatchOperationTO operation : operations){
            long detailId = operation.getDetailBatch().getId();
            Long batchNo = operation.getBatchNo();

            if (filter.isAddSummaryForItemsWithoutOperations()){
                //Exclude all storage places having changes.
                Map<Long, StoragePlace> detailPlaces = storagePlacesWithoutChanges.get(detailId);
                if (detailPlaces != null){
                    detailPlaces.remove(operation.getStoragePlace().getId());
                    if (detailPlaces.isEmpty()){
                        storagePlacesWithoutChanges.remove(detailId);
                    }
                }
            }

            Long lastDetailCount = detailsCount.get(detailId);
            if (lastDetailCount == null){
                //This is the first time we found this detail in results -- initialize maps.
                detailsCount.put(detailId, operation.getFinalCount());
                detailsCost.put(detailId, operation.getFinalCost());
                Set<Long> batchedInCount = new HashSet<Long>();
                batchedInCount.add(batchNo);
                batchesIncludedIntoCount.put(detailId, batchedInCount);
            }
            else {
                BigDecimal lastDetailCost = detailsCost.get(detailId);
                Set<Long> batchesInCount = batchesIncludedIntoCount.get(detailId);
                if (!batchesInCount.contains(batchNo)){
                    //Summarize count with other batches of the same detail.
                    batchesInCount.add(batchNo);
                    operation.setInitialCount(operation.getInitialCount() + lastDetailCount);
                    operation.setInitialCost(addNotNullValues(operation.getInitialCost(), lastDetailCost));
                    operation.setFinalCount(operation.getFinalCount() + lastDetailCount);
                    operation.setFinalCost(addNotNullValues(operation.getFinalCost(), lastDetailCost));
                }
                else{
                    //Override initial count of operation with information from previously summarized batches.
                    operation.setInitialCount(lastDetailCount);
                    operation.setInitialCost(lastDetailCost);
                    operation.setFinalCount(lastDetailCount + operation.getPostedCount() - operation.getChargedOffCount());
                    if (operation.getPostedCost() != null){
                        lastDetailCost = addNotNullValues(lastDetailCost, operation.getPostedCost());
                    }
                    if (operation.getChargedOffCost() != null){
                        lastDetailCost = subtractNotNullValues(lastDetailCost, operation.getChargedOffCost());
                    }
                    operation.setFinalCost(lastDetailCost);
                }

                detailsCount.put(detailId, operation.getFinalCount());
                detailsCost.put(detailId, operation.getFinalCost());
            }
        }

        if (filter.isAddSummaryForItemsWithoutOperations()){
            //2.3. Adding records for items without changes to display at least reminders for such items.
            for (Long detailBatchId : storagePlacesWithoutChanges.keySet()){
                DetailBatch detailBatch = detailsCache.get(detailBatchId);
                for (StoragePlace storagePlace : storagePlacesWithoutChanges.get(detailBatchId).values()){
                    DetailBatchOperationTO summary = new DetailBatchOperationTO();
                    summary.setDetailBatch(DetailBatchTransformer.batchTO(detailBatch));
                    summary.setStoragePlace(StoragePlaceTransformer.transform(storagePlace));

                    long count = detailsCount.get(detailBatchId);
                    summary.setInitialCount(count);
                    summary.setFinalCount(count);

                    BigDecimal cost = detailsCost.get(detailBatchId);
                    summary.setInitialCost(cost);
                    summary.setFinalCost(cost);

                    operations.add(summary);
                }
            }
        }

        return operations;
    }

    private BigDecimal addNotNullValues(BigDecimal value1, BigDecimal value2){
        if (value1 == null && value2 == null){
            return null;
        }
        else if (value1 == null){
            return value2;
        }
        else if (value2 == null){
            return value1;
        }
        else{
            return value1.add(value2);
        }
    }

    private BigDecimal multiplyNotNullValues(BigDecimal value1, BigDecimal value2){
        if (value1 == null || value2 == null){
            return null;
        }
        else{
            return value1.multiply(value2);
        }
    }

    private BigDecimal subtractNotNullValues(BigDecimal value1, BigDecimal value2) {
        if (value1 == null && value2 == null){
            return null;
        }
        else if (value1 == null){
            return BigDecimal.ZERO.subtract(value2);
        }
        else if (value2 == null){
            return value1;
        }
        else{
            return value1.subtract(value2);
        }
    }

    //=========================== Spring setters ================================================================
    public void setDetailBatchHistoryDAO(DetailBatchHistoryDAO detailBatchHistoryDAO) {
        this.detailBatchHistoryDAO = detailBatchHistoryDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setWarehouseBatchService(WarehouseBatchService warehouseBatchService) {
        this.warehouseBatchService = warehouseBatchService;
    }
}
