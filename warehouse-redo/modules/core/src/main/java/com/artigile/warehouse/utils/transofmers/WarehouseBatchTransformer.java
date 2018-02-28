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

import com.artigile.warehouse.domain.warehouse.WarehouseBatch;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 30.12.2008
 */
public final class WarehouseBatchTransformer {
    private WarehouseBatchTransformer() {
    }

    public static WarehouseBatchTO transform(WarehouseBatch warehouseBatch) {
        if (warehouseBatch == null) {
            return null;
        }
        WarehouseBatchTO warehouseBatchTO = new WarehouseBatchTO();
        update(warehouseBatchTO, warehouseBatch);
        return warehouseBatchTO;
    }

    /**
     * @param warehouseBatchTO in,out -- DTO to be updated from entity data.
     * @param warehouseBatch out -- entity with fresh data.
     */
    public static void update(WarehouseBatchTO warehouseBatchTO, WarehouseBatch warehouseBatch) {
        warehouseBatchTO.setId(warehouseBatch.getId());
        warehouseBatchTO.setDetailBatch(DetailBatchTransformer.batchTO(warehouseBatch.getDetailBatch()));
        warehouseBatchTO.setStoragePlace(StoragePlaceTransformer.transformForReport(warehouseBatch.getStoragePlace()));
        warehouseBatchTO.setCount(warehouseBatch.getAmount());
        warehouseBatchTO.setReservedCount(warehouseBatch.getReservedCount());
        warehouseBatchTO.setNeedRecalculate(warehouseBatch.getNeedRecalculate());
        warehouseBatchTO.setNotice(warehouseBatch.getNotice());
        warehouseBatchTO.setPostingItem(PostingsTransformer.transformItem(warehouseBatch.getPostingItem()));
        warehouseBatchTO.setShelfLifeDate(warehouseBatch.getShelfLifeDate());
    }

    public static List<WarehouseBatchTO> transformList(List<WarehouseBatch> warehouseBatches) {
        List<WarehouseBatchTO> list = new ArrayList<WarehouseBatchTO>();
        for (WarehouseBatch warehouseBatch : warehouseBatches) {
            list.add(transform(warehouseBatch));
        }
        return list;
    }

    public static WarehouseBatch transform(WarehouseBatchTO warehouseBatchTO) {
        if (warehouseBatchTO == null) {
            return null;
        }

        WarehouseBatch warehouseBatch = SpringServiceContext.getInstance().getWarehouseBatchService().getWarehouseBatchById(warehouseBatchTO.getId());
        if (warehouseBatch == null){
            warehouseBatch = new WarehouseBatch();
        }

        return warehouseBatch;
    }

    public static List<WarehouseBatch> transformListDesc(List<WarehouseBatchTO> warehouseBatchTOs) {
        List<WarehouseBatch> list = new ArrayList<WarehouseBatch>();
        for (WarehouseBatchTO warehouseBatchTO : warehouseBatchTOs) {
            list.add(transform(warehouseBatchTO));
        }
        return list;
    }
}
