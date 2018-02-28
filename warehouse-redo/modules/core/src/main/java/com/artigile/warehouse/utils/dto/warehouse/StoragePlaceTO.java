/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.warehouse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 22.12.2008
 */
public class StoragePlaceTO extends StoragePlaceTOForReport {
    private StoragePlaceTO parentStoragePlace;
    private List<StoragePlaceTO> storagePlaces = new ArrayList<StoragePlaceTO>();

    public StoragePlaceTO() {
    }

    public StoragePlaceTO(long id) {
        super(id);
    }

    public StoragePlaceTO(long id, String sign, Long fillingDegree, String notice, boolean availableForPosting,
                          WarehouseTO warehouse, StoragePlaceTO parentStoragePlace) {
        super(id, sign, fillingDegree, notice, availableForPosting, warehouse);
        this.parentStoragePlace = parentStoragePlace;
    }

    public StoragePlaceTO(WarehouseTO warehouse) {
        setWarehouse(warehouse);
    }

    public StoragePlaceTO(StoragePlaceTO parentStoragePlace) {
        this.parentStoragePlace = parentStoragePlace;
    }

    //=================================== Operations =============================================
    /**
     * Searches storage place with a given sign.
     *
     * @param sign
     * @return
     */
    public StoragePlaceTO findStoragePlaceBySign(String sign) {
        if (sign.toLowerCase().equals(getSign().toLowerCase())) {
            return this;
        }
        StoragePlaceTO result;
        for (StoragePlaceTO child : storagePlaces) {
            result = child.findStoragePlaceBySign(sign);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Deep copy of the storage place data.
     *
     * @param src - source of the data.
     */
    public void copyFrom(StoragePlaceTO src, WarehouseTOForReport warehouseTO) {
        setId(src.getId());
        setSign(src.getSign());
        setFillingDegree(src.getFillingDegree());
        setNotice(src.getNotice());
        setAvailableForPosting(src.isAvailableForPosting());
        setWarehouse(warehouseTO);

        List<StoragePlaceTO> childPlaces = new ArrayList<StoragePlaceTO>();
        for (StoragePlaceTO childPlace : src.getStoragePlaces()) {
            StoragePlaceTO copyPlace = new StoragePlaceTO(this);
            copyPlace.copyFrom(childPlace, warehouseTO);
            childPlaces.add(copyPlace);
        }
        setStoragePlaces(childPlaces);
    }

    //=========================== Calculated getters ==============================================
    @Override
    public String toString() {
        return getSign();
    }

    //=============================== Getters and setters ========================================
    public StoragePlaceTO getParentStoragePlace() {
        return parentStoragePlace;
    }

    public void setParentStoragePlace(StoragePlaceTO parentStoragePlaceID) {
        this.parentStoragePlace = parentStoragePlaceID;
    }

    public List<StoragePlaceTO> getStoragePlaces() {
        return storagePlaces;
    }

    public void setStoragePlaces(List<StoragePlaceTO> storagePlaces) {
        this.storagePlaces = storagePlaces;
    }
}
