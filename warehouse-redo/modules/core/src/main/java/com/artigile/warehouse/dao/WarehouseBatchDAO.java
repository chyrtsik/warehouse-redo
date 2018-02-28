/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchFilter;
import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.details.DetailModel;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.domain.warehouse.WarehouseBatch;

import java.util.List;

/**
 * @author Borisok V.V., 29.12.2008
 */
public interface WarehouseBatchDAO extends EntityDAO<WarehouseBatch> {

    List<WarehouseBatch> getSimilarBatches(DetailBatch detailBatch, StoragePlace storagePlace);

    List<WarehouseBatch> getWarehouseBatchesForDetailBatch(DetailBatch detailBatch);

    List<WarehouseBatch> getWarehouseBatchesForDetailModel(DetailModel detailBatch);

    List<WarehouseBatch> getWarehouseBatchesByStoragePlace(StoragePlace storagePlace);

    List<WarehouseBatch> getWarehouseBatches(DetailBatch detailBatch, StoragePlace storagePlace, String notice);

    List<WarehouseBatch> getWarehouseBatchesByPostingItem(long postingItemId);

    List<WarehouseBatch> getByFilterSortedByPostingDate(WarehouseBatchFilter filter);

    List<WarehouseBatch> getByFilter(WarehouseBatchFilter filter);
}
