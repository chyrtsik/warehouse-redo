/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.warehouseBatch;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.common.verifications.Verifications;
import com.artigile.warehouse.bl.detail.DetailBatchService;
import com.artigile.warehouse.bl.postings.PostingService;
import com.artigile.warehouse.bl.warehouse.StoragePlaceService;
import com.artigile.warehouse.dao.WarehouseBatchDAO;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.postings.PostingItem;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.domain.warehouse.WarehouseBatch;
import com.artigile.warehouse.utils.MiscUtils;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;
import com.artigile.warehouse.utils.properties.Properties;
import com.artigile.warehouse.utils.transofmers.WarehouseBatchTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Borisok V.V., 30.12.2008
 */

@Transactional(rollbackFor = BusinessException.class)
public class WarehouseBatchService {
    private WarehouseBatchDAO warehouseBatchDAO;

    private StoragePlaceService storagePlaceService;

    private PostingService postingService;

    private DetailBatchService detailBatchService;

    public WarehouseBatchService() {
    }

    //========================== Settings ======================================

    /**
     * If true then warehouse batches are checked for posting item to be unique for the same detail batch. Warehouse
     * batches with the same posting item can be merged into one batch.
     **/
    private Boolean trackPostingItem;
    private static final String PROPERTY_WAREHOUSE_BATCH_TRACK_POSTING_ITEM = "warehouse.batch.track.posting.item";

    private Boolean trackShelfLife;
    private static final String PROPERTY_WAREHOUSE_BATCH_TRACK_SHELF_LIFE = "warehouse.batch.track.shelf.life";

    public boolean isTrackPostingItem() {
        if (trackPostingItem == null){
            trackPostingItem = Properties.getPropertyAsBoolean(PROPERTY_WAREHOUSE_BATCH_TRACK_POSTING_ITEM);
            if (trackPostingItem == null){
                trackPostingItem = false;
            }
        }
        return trackPostingItem;
    }

    public void setTrackPostingItem(boolean trackPostingItem) {
        Properties.setProperty(PROPERTY_WAREHOUSE_BATCH_TRACK_POSTING_ITEM, String.valueOf(trackPostingItem));
        this.trackPostingItem = trackPostingItem;
    }

    public Boolean isTrackShelfLife() {
        if (trackShelfLife == null) {
            trackShelfLife = Properties.getPropertyAsBoolean(PROPERTY_WAREHOUSE_BATCH_TRACK_SHELF_LIFE);
            if (trackShelfLife == null) {
                trackShelfLife = false;
            }
        }
        return trackShelfLife;
    }

    public void setTrackShelfLife(Boolean trackShelfLife) {
        Properties.setProperty(PROPERTY_WAREHOUSE_BATCH_TRACK_SHELF_LIFE, String.valueOf(trackShelfLife));
        this.trackShelfLife = trackShelfLife;
    }

    //======================== Listeners support ===============================
    private List<WarehouseBatchChangeListener> listeners = new LinkedList<WarehouseBatchChangeListener>();

    public void addWarehouseBatchChangeListener(WarehouseBatchChangeListener listener){
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    public void removeWarehouseBatchChangeListener(WarehouseBatchChangeListener listener){
        listeners.remove(listener);
    }

    private void fireWarehouseBatchCountChanged(WarehouseBatch finalWarehouseBatch, long initialCount, long countDelta,
                                                WarehouseBatchChangeDocument document) throws BusinessException {
        WarehouseBatchCountChangeEvent event =
                new WarehouseBatchCountChangeEvent(finalWarehouseBatch, initialCount, countDelta, document);

        for (WarehouseBatchChangeListener listener : listeners){
            listener.onWarehouseBatchCountChanged(event);
        }
    }

    //============================== Warehouse batch reading ==========================================

    public WarehouseBatch getWarehouseBatchById(long warehouseBatchId) {
        return warehouseBatchDAO.get(warehouseBatchId);
    }

    public List<WarehouseBatchTO> getWarehouseBatchesForDetailBatch(long detailBatchId) {
        DetailBatch detailBatch = detailBatchService.getDetailBatchById(detailBatchId);
        return WarehouseBatchTransformer.transformList(warehouseBatchDAO.getWarehouseBatchesForDetailBatch(detailBatch));
    }

    public WarehouseBatchTO getWarehouseBatchForPostingItem(long postingItemId) {
        List<WarehouseBatch> warehouseBatches;

        if (isTrackPostingItem()){
            //Looking for warehouse batch by direct link as these links are tracked.
            warehouseBatches = warehouseBatchDAO.getWarehouseBatchesByPostingItem(postingItemId);
        }
        else{
            //Looking for warehouse batch by parameters.
            PostingItem postingItem = postingService.getPostingItemById(postingItemId);
            warehouseBatches = warehouseBatchDAO.getWarehouseBatches(postingItem.getDetailBatch(), postingItem.getStoragePlace(), postingItem.getNotice());
        }

        //Only single item is allowed.
        return warehouseBatches.size() == 1 ? WarehouseBatchTransformer.transform(warehouseBatches.get(0)) : null;
    }

    public List<WarehouseBatchTO> getWarehouseBatchesByFilter(WarehouseBatchFilter filter) {
        List<WarehouseBatch> warehouseBatches;
        if (isTrackPostingItem()){
            warehouseBatches = warehouseBatchDAO.getByFilterSortedByPostingDate(filter);
        }
        else{
            warehouseBatches = warehouseBatchDAO.getByFilter(filter);
        }
        return WarehouseBatchTransformer.transformList(warehouseBatches);
    }

    //=============================== Warehouse batch updating =============================================

    public void save(WarehouseBatchTO warehouseBatchTO) {
        WarehouseBatch warehouseBatch = WarehouseBatchTransformer.transform(warehouseBatchTO);
        warehouseBatch.setNeedRecalculate(warehouseBatchTO.getNeedRecalculate());
        warehouseBatch.setNotice(warehouseBatchTO.getNotice());
        warehouseBatch.setShelfLifeDate(warehouseBatchTO.getShelfLifeDate());
        save(warehouseBatch);
        WarehouseBatchTransformer.update(warehouseBatchTO, warehouseBatch);
    }

    //TODO: Add lock of warehouse batch here.
    private void save(WarehouseBatch warehouseBatchToSave) {
        //Saving changes to the warehouse batch it self.
        warehouseBatchDAO.save(warehouseBatchToSave);
    }

    /**
     * Perform manual ware count correction (with all proper logging).
     * @param warehouseBatchId warehouse batch to change count.
     * @param countDiff change of count.
     * @throws BusinessException when cannot change count.
     */
    public void performWareCountCorrection(long warehouseBatchId, long countDiff) throws BusinessException {
        WarehouseBatch warehouseBatch = warehouseBatchDAO.get(warehouseBatchId);
        WarehouseBatchChangeDocument document = WarehouseBatchChangeDocument.createManualCountCorrectionDocument(warehouseBatch);
        if (countDiff > 0){
            //Add wares to warehouse batch.
            performWareIncome(warehouseBatch.getDetailBatch(), countDiff, warehouseBatch.getStoragePlace(), warehouseBatch.getNotice(), document);
        }
        else if (countDiff < 0){
            //Remove some wares from warehouse batch.
            performWareChargeOff(warehouseBatch.getId(), -countDiff, document);
        }
    }

    /**
     * Moves wares from one warehouse batch to another warehouse places.
     * @param warehouseBatchId warehouse batch, which items are to be moved.
     * @param targetStoragePlaceIds array with codes of target storage places.
     * @param amountsToMove array with quantities of ware for target storage places.
     * @throws BusinessException when moving cannot be performed.
     */
    public void moveWarehouseBatches(long warehouseBatchId, List<Long>targetStoragePlaceIds, List<Long>amountsToMove) throws BusinessException{
        WarehouseBatch warehouseBatch = warehouseBatchDAO.get(warehouseBatchId);
        StoragePlace fromPlace = warehouseBatch.getStoragePlace();

        //Perform ware moving (charge off + posting for each item).
        WarehouseBatchChangeDocument document = WarehouseBatchChangeDocument.createInWarehouseMovementDocument(warehouseBatch);
        for (int i = 0; i < targetStoragePlaceIds.size(); i++) {
            StoragePlace targetStoragePlace = storagePlaceService.getStoragePlaceById(targetStoragePlaceIds.get(i));
            long amountToMove = amountsToMove.get(i);
            performWareChargeOff(warehouseBatch.getId(), amountToMove, document);
            performWareIncome(warehouseBatch.getDetailBatch(), amountToMove, targetStoragePlace, warehouseBatch.getNotice(), document);
        }

        //Force recalculation of storage place fill percent.
        storagePlaceService.updateFillStoragePercent(fromPlace);
    }

    /**
     * This method is called to process income into warehouse. Similar wares are put into the
     * similar warehouse batches.
     * @param detailBatch detail batch that was added to warehouse.
     * @param count count of wares, being added.
     * @param storagePlace storage place of new ware.
     * @param notice same notice as warehouseBatch notice field.
     * @param document document that caused change of warehouse batch count.
     * @return warehouse batch created for this operation.
     * @throws BusinessException if charge off cannot be performed.
     */
    //TODO: Add lock of warehouse batch here.
    public WarehouseBatch performWareIncome(DetailBatch detailBatch, long count, StoragePlace storagePlace,
                                            String notice, WarehouseBatchChangeDocument document) throws BusinessException {
        WarehouseBatch resultWarehouseBatch = null;
        long initialCount = 0;

        //Searching for same warehouse batches.
        List<WarehouseBatch> sameBatches = warehouseBatchDAO.getSimilarBatches(detailBatch, storagePlace);
        for (WarehouseBatch sameBatch : sameBatches){
            if (isSameWarehouseBatch(sameBatch, notice, document)) {
                //We can use existing batch to store new wares.
                initialCount = sameBatch.getAmount();
                sameBatch.setAmount(sameBatch.getAmount() + count);
                sameBatch.getDetailBatch().changeCount(count);
                warehouseBatchDAO.save(sameBatch);
                resultWarehouseBatch = sameBatch;
                break;
            }
        }

        if (resultWarehouseBatch == null){
            //Saving new ware as new warehouse batch.
            initialCount = 0;
            WarehouseBatch newBatch = new WarehouseBatch();
            newBatch.setDetailBatch(detailBatch);
            newBatch.setAmount(count);
            newBatch.setStoragePlace(storagePlace);
            newBatch.setNotice(notice);
            newBatch.getDetailBatch().changeCount(count);
            if (isTrackPostingItem()){
                if (document.isPosting()) {
                    //Track posting item of created warehouse batch to be able to get price and receipt date.
                    newBatch.setPostingItem(document.getPostingItem());
                }
                else if (document.getChangedWarehouseBatch() != null) {
                    //Copy posting item from changed warehouse batch (to keep track on posting item).
                    newBatch.setPostingItem(document.getChangedWarehouseBatch().getPostingItem());
                }
            }
            if (isTrackShelfLife()) {
                if (document.isPosting()) {
                    newBatch.setShelfLifeDate(document.getPostingItem().getShelfLifeDate());
                }
                else if (document.getChangedWarehouseBatch() != null) {
                    newBatch.setShelfLifeDate(document.getChangedWarehouseBatch().getShelfLifeDate());
                }
            }
            warehouseBatchDAO.save(newBatch);
            resultWarehouseBatch = newBatch;
        }

        //Notify about changing of warehouse batch count.
        fireWarehouseBatchCountChanged(resultWarehouseBatch, initialCount, count, document);

        return resultWarehouseBatch;
    }

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    private boolean isSameWarehouseBatch(WarehouseBatch sameBatch, String notice, WarehouseBatchChangeDocument document) {
        boolean res = true;
        PostingItem postingItem = sameBatch.getPostingItem();
        PostingItem documentPostingItem = document.getPostingItem();
        WarehouseBatch changedWarehouseBatch = document.getChangedWarehouseBatch();
        if (isTrackShelfLife()) {
            if (document.isPosting()) {
                res = postingItem != null && documentPostingItem != null &&
                        equals(postingItem.getShelfLifeDate(), documentPostingItem.getShelfLifeDate());
            }
            else {
                res = (document.isManualCountCorrection() || document.isInWarehouseMovement())
                        && changedWarehouseBatch != null
                        && equals(postingItem.getShelfLifeDate(), changedWarehouseBatch.getShelfLifeDate());
            }
        }

        if (res) {
            if (isTrackPostingItem()) {
                if (document.isPosting()) {
                    res = postingItem != null &&
                            documentPostingItem != null &&
                            postingItem.getId() == documentPostingItem.getId();
                }
                else {
                    res = (document.isManualCountCorrection() || document.isInWarehouseMovement())
                            && postingItem != null
                            && changedWarehouseBatch != null
                            && changedWarehouseBatch.getPostingItem() != null
                            && postingItem.getId() == changedWarehouseBatch.getPostingItem().getId();
                }
            }
            else {
                res = postingItem == null && MiscUtils.stringsEquals(sameBatch.getNotice(), notice);
            }
        }
        return res;
    }

    /**
     * This method is called, when count of ware at the warehouse should be decreased.
     * @param warehouseBatchId warehouse batch to be charged off.
     * @param count count of ware to be removed from warehouse batch after charge off operation.
     * @param document document that caused change of warehouse batch count.
     * @throws BusinessException if given amount of ware cannot be charged off.
     */
    //TODO: Add lock of warehouse batch here.
    public void performWareChargeOff(long warehouseBatchId, long count,
                                     WarehouseBatchChangeDocument document) throws BusinessException {
        //Charging off.
        WarehouseBatch batch = warehouseBatchDAO.get(warehouseBatchId);
        long initialCount = batch.getAmount();
        batch.setAmount(batch.getAmount() - count);
        batch.getDetailBatch().changeCount(-count);

        if ( batch.getAmount() == 0 ){
            warehouseBatchDAO.remove(batch);
        }
        else{
            warehouseBatchDAO.save(batch);
        }

        //Notify about changing of warehouse batch count.
        fireWarehouseBatchCountChanged(batch, initialCount, -count, document);
    }

    /**
     * Unreserves given count of ware and performed it's change off.
     * @param warehouseBatchId warehouse batch to be charged off.
     * @param count count of ware to be removed from warehouse batch.
     * @throws BusinessException if given amount of ware cannot be charged off.
     */
    //TODO: Add lock of warehouse batch here.
    public void performReservedWareChargeOff(long warehouseBatchId, long count,
                                             WarehouseBatchChangeDocument document) throws BusinessException {
        unreserve(warehouseBatchId, count);
        performWareChargeOff(warehouseBatchId, count, document);
    }

    /**
     * Reserves given count of specified warehouse batch.
     * @param warehouseBatchId - identifier of warehouse batch, from which wares needs to be reserved.
     * @param count - amount of wares needed.
     * @throws BusinessException - thrown, if given amount of wares cannot be reserved.
     */
    //TODO: Add lock of warehouse batch here.
    public void reserve(long warehouseBatchId, long count) throws BusinessException {
        WarehouseBatch warehouseBatch = warehouseBatchDAO.get(warehouseBatchId);

        //1. Check, if some amount of wares from warehouse batch can be reserved.
        Verifications.ensureVerificationPasses(warehouseBatch, new BeforeReserveVerification(count));
        
        //2. Reserves wares.
        warehouseBatch.setReservedCount(warehouseBatch.getReservedCount() + count);
        warehouseBatch.getDetailBatch().changeReservedCount(count);
        warehouseBatchDAO.save(warehouseBatch);
    }

    /**
     * Unreserves given count of specified warehouse batch.
     * @param warehouseBatchId - identifier of warehouse batch, from which wares needs to be unreserved.
     * @param count - amount of wares not needed.
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException - thrown, if given amount of wares cannot be unreserved.
     */
    //TODO: Add lock of warehouse batch here.
    public void unreserve(long warehouseBatchId, long count) throws BusinessException {
        WarehouseBatch warehouseBatch = warehouseBatchDAO.get(warehouseBatchId);

        //1. Check, if some amount of wares from warehouse batch can be unreserved.
        Verifications.ensureVerificationPasses(warehouseBatch, new BeforeUnreserveVerification(count));
        
        //2. Reserves wares.
        warehouseBatch.setReservedCount(warehouseBatch.getReservedCount() - count);
        warehouseBatch.getDetailBatch().changeReservedCount(-count);
        warehouseBatchDAO.save(warehouseBatch);
    }

    //=============================== Spring setters =====================================
    public void setWarehouseBatchDAO(WarehouseBatchDAO warehouseBatchDAO) {
        this.warehouseBatchDAO = warehouseBatchDAO;
    }

    public void setStoragePlaceService(StoragePlaceService storagePlaceService) {
        this.storagePlaceService = storagePlaceService;
    }

    public void setPostingService(PostingService postingService) {
        this.postingService = postingService;
    }

    public void setDetailBatchService(DetailBatchService detailBatchService) {
        this.detailBatchService = detailBatchService;
    }
}
