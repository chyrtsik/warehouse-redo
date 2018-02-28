/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.warehouse;

import com.artigile.warehouse.dao.StoragePlaceDAO;
import com.artigile.warehouse.dao.WarehouseBatchDAO;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.domain.warehouse.WarehouseBatch;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTO;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTOForReport;
import com.artigile.warehouse.utils.transofmers.StoragePlaceTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Borisok V.V., 22.12.2008
 */

@Transactional
public class StoragePlaceService {
    private StoragePlaceDAO storagePlaceDAO;

    private WarehouseBatchDAO warehouseBatchDAO;

    public StoragePlaceService() {
    }

    public void save(StoragePlaceTO storagePlaceTO) {
        StoragePlace storagePlace = StoragePlaceTransformer.transform(storagePlaceTO);
        storagePlaceDAO.save(storagePlace);
        storagePlaceTO.setId(storagePlace.getId());
    }

    public StoragePlaceTO get(long id) {
        return StoragePlaceTransformer.transform(storagePlaceDAO.get(id));
    }

    public List<StoragePlaceTO> getAll() {
        return StoragePlaceTransformer.transformList(storagePlaceDAO.getAll());
    }

    public StoragePlace getStoragePlaceById(long storagePlaceId) {
        return storagePlaceDAO.get(storagePlaceId);
    }

    public List<StoragePlaceTOForReport> getStoragePlacesByFilter(StoragePlaceFilter filter) {
        return StoragePlaceTransformer.transformListForReport(storagePlaceDAO.getListByFilter(filter));
    }


    public void updateFillStoragePercent(StoragePlaceTOForReport storagePlace) {
        StoragePlace storagePlaceDB = storagePlaceDAO.get(storagePlace.getId());
        updateFillStoragePercent(storagePlaceDB);
    }

    public void updateFillStoragePercent(StoragePlace storagePlaceDB) {
        List<WarehouseBatch> detailsList = warehouseBatchDAO.getWarehouseBatchesByStoragePlace(storagePlaceDB);
        if (detailsList.size() == 0) {
            storagePlaceDB.setFillingDegree((long)0);
        }
        storagePlaceDAO.save(storagePlaceDB);
    }

    //======================== Spring setters ===================================
    public void setStoragePlaceDAO(StoragePlaceDAO storagePlaceDAO) {
        this.storagePlaceDAO = storagePlaceDAO;
    }

    public void setWarehouseBatchDAO(WarehouseBatchDAO warehouseBatchDAO) {
        this.warehouseBatchDAO = warehouseBatchDAO;
    }
}
