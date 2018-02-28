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

import com.artigile.warehouse.domain.movement.MovementItem;
import com.artigile.warehouse.domain.postings.PostingItem;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.movement.MovementItemTO;
import com.artigile.warehouse.utils.dto.movement.MovementTOForReport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 22.11.2009
 */
public final class MovementItemTransformer {
    private MovementItemTransformer(){
    }


    public static List<MovementItemTO> transformList(List<MovementItem> items, MovementTOForReport movementTO) {
        List<MovementItemTO> itemsTO = new ArrayList<MovementItemTO>();
        for (MovementItem item : items){
            itemsTO.add(transform(item, movementTO));
        }
        return itemsTO;
    }

    private static MovementItemTO transform(MovementItem item, MovementTOForReport movementTO) {
        if (item == null){
            return null;
        }
        MovementItemTO itemTO = new MovementItemTO();
        itemTO.setMovement(movementTO);
        update(itemTO, item);
        return itemTO;
    }

    public static MovementItem transform(MovementItemTO itemTO) {
        if (itemTO == null){
            return null;
        }
        MovementItem item = SpringServiceContext.getInstance().getMovementService().getMovementItemById(itemTO.getId());
        if (item == null){
            item = new MovementItem();
        }
        return item;
    }

    public static MovementItemTO transform(MovementItem item) {
        if (item == null){
            return null;
        }
        return transform(item, MovementTransformer.transformForReport(item.getMovement()));
    }

    /**
     * @param itemTO (in, out) -- DTO to be synchronized with entity.
     * @param item (in) -- entity with fresh data.
     */
    public static void update(MovementItemTO itemTO, MovementItem item) {
        itemTO.setId(item.getId());
        itemTO.setNumber(item.getNumber());
        itemTO.setState(item.getState());
        itemTO.setProcessingResult(item.getProcessingResult());
        itemTO.setWarehouseBatch(WarehouseBatchTransformer.transform(item.getWarehouseBatch()));
        itemTO.setDetailBatch(DetailBatchTransformer.batchTO(item.getDetailBatch()));
        itemTO.setShelfLifeDate(item.getShelfLifeDate());
        itemTO.setWarehouseNotice(item.getWarehouseNotice());
        itemTO.setCount(item.getAmount());
        itemTO.setCountMeas(MeasureUnitTransformer.transform(item.getCountMeas()));
        itemTO.setFromStoragePlace(StoragePlaceTransformer.transformForReport(item.getFromStoragePlace()));

        //Adding information to display buying price of movement item.
        BigDecimal itemCost = item.getItemCost();
        itemTO.setPrice(itemCost);
        itemTO.setTotalPrice((itemCost != null && item.getAmount() != null) ? itemCost.multiply(BigDecimal.valueOf(item.getAmount())) : null);

        PostingItem initialPostingItem = item.getInitialPostingItem();
        if (initialPostingItem != null){
            //Information about product batch.
            itemTO.setBatchNo(initialPostingItem.getId());
            itemTO.setReceiptDate(initialPostingItem.getPosting().getCreateDate());
        }

        //Additional information about movement item from warehouse documents.
        itemTO.setShippedUser(UserTransformer.transformUser(item.getShippedUser()));
        itemTO.setShippedDate(item.getShippedDate());
        itemTO.setPostedUser(UserTransformer.transformUser(item.getPostedUser()));
        itemTO.setPostedDate(item.getPostedDate());
    }

    /**
     * @param item (in, out) -- entity to be updated.
     * @param itemTO (in) -- DTO with data to be written to the entity.
     */
    public static void update(MovementItem item, MovementItemTO itemTO) {
        item.setNumber(itemTO.getNumber());
        item.setState(itemTO.getState());
        item.setProcessingResult(itemTO.getProcessingResult());
        item.setWarehouseBatch(WarehouseBatchTransformer.transform(itemTO.getWarehouseBatch()));
        item.setDetailBatch(DetailBatchTransformer.batch(itemTO.getDetailBatch()));
        item.setShelfLifeDate(itemTO.getShelfLifeDate());
        item.setWarehouseNotice(itemTO.getWarehouseNotice());
        item.setAmount(itemTO.getCount());
        item.setCountMeas(MeasureUnitTransformer.transform(itemTO.getCountMeas()));
        item.setFromStoragePlace(StoragePlaceTransformer.transform(itemTO.getFromStoragePlace()));
    }
}
