/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.inventorization.InventorizationItem;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.inventorization.InventorizationItemTO;
import com.artigile.warehouse.utils.dto.inventorization.InventorizationTO;
import com.artigile.warehouse.utils.dto.inventorization.task.InventorizationTaskTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 15.10.2009
 */
public final class InventorizationItemTransformer {
    private InventorizationItemTransformer() {
    }

    public static InventorizationItemTO transform(InventorizationItem item) {
        InventorizationItemTO itemTO = new InventorizationItemTO();
        itemTO.setInventorization(InventorizationTransformer.transformForReport(item.getInventorization()));
        itemTO.setInventorizationTask(InventorizationTaskTransformer.transform(item.getInventorizationTask(), itemTO));
        update(itemTO, item);
        return itemTO;
    }

    public static InventorizationItemTO transform(InventorizationItem item, InventorizationTaskTO taskTO) {
        InventorizationItemTO itemTO = new InventorizationItemTO();
        itemTO.setInventorization(InventorizationTransformer.transformForReport(item.getInventorization()));
        itemTO.setInventorizationTask(taskTO);
        update(itemTO, item);
        return itemTO;
    }

    private static InventorizationItemTO transform(InventorizationItem item, InventorizationTO inventorizationTO) {
        InventorizationItemTO itemTO = new InventorizationItemTO();
        itemTO.setInventorization(inventorizationTO);
        itemTO.setInventorizationTask(InventorizationTaskTransformer.transform(item.getInventorizationTask(), itemTO));
        update(itemTO, item);
        return itemTO;  
    }

    public static InventorizationItem transform(InventorizationItemTO itemTO) {
        if (itemTO == null){
            return null;
        }
        InventorizationItem item = SpringServiceContext.getInstance().getInventorizationService().getInventorizationItemById(itemTO.getId());
        if (item == null){
            item = new InventorizationItem();
        }
        return item;
    }

    /**
     * Synchronizes DTO with entity.
     * @param itemTO (in, out) -- DTO to be updated.
     * @param item (in) -- entity with fresh data.
     */
    public static void update(InventorizationItemTO itemTO, InventorizationItem item) {
        itemTO.setId(item.getId());
        itemTO.setNumber(item.getNumber());
        itemTO.setDetailBatch(DetailBatchTransformer.batchTO(item.getDetailBatch()));
        itemTO.setStoragePlace(StoragePlaceTransformer.transform(item.getStoragePlace()));
        itemTO.setWarehouseBatchNotice(item.getWarehouseBatch() == null ? null : item.getWarehouseBatch().getNotice());
        itemTO.setCountMeas(MeasureUnitTransformer.transform(item.getCountMeas()));
        itemTO.setNeededCount(item.getNeededCount());
        itemTO.setProcessingResult(item.getProcessingResult());
        itemTO.setState(item.getState());
        itemTO.setNeededCount(item.getNeededCount());
    }

    /**
     * Synchronizes entity with DTO.
     * @param item (in, out) -- entity to be updated.
     * @param itemTO (in) -- DTO with fresh data.
     */
    public static void update(InventorizationItem item, InventorizationItemTO itemTO) {
        item.setNumber(itemTO.getNumber());
        item.setDetailBatch(DetailBatchTransformer.batch(itemTO.getDetailBatch()));
        item.setStoragePlace(StoragePlaceTransformer.transform(itemTO.getStoragePlace()));
        item.setCountMeas(MeasureUnitTransformer.transform(itemTO.getCountMeas()));
        item.setNeededCount(itemTO.getNeededCount());
        item.setProcessingResult(itemTO.getProcessingResult());
        item.setState(itemTO.getState());
        item.setNeededCount(itemTO.getNeededCount());
    }

    public static List<InventorizationItemTO> transformList(List<InventorizationItem> items, InventorizationTO inventorizationTO) {
        List<InventorizationItemTO> itemTOs = new ArrayList<InventorizationItemTO>();
        for (InventorizationItem item : items){
            itemTOs.add(transform(item, inventorizationTO));
        }
        return itemTOs;
    }
}
