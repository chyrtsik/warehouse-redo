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

/**
 * Full representation of the warehouse (for warehouse editor and so on).
 */
public class WarehouseTO extends WarehouseTOForReport {
    private List<StoragePlaceTO> storagePlaces = new ArrayList<StoragePlaceTO>();

    public WarehouseTO() {
    }

    public WarehouseTO(long id) {
        super(id);
    }

    //================================ Operations ============================================
    /**
     * Searches storage place with a given sign.
     *
     * @param sign
     * @return
     */
    public StoragePlaceTO findStoragePlaceBySign(String sign) {
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
     * Deep copy of another warehouse data.
     * @param src - source of the data.
     */
    public void copyFrom(WarehouseTO src) {
        super.copyFrom(src);

        List<StoragePlaceTO> rootPlaces = new ArrayList<StoragePlaceTO>();
        for (StoragePlaceTO rootPlace : src.getStoragePlaces()){
            StoragePlaceTO copyPlace = new StoragePlaceTO();
            copyPlace.copyFrom(rootPlace, this);
            rootPlaces.add(copyPlace);
        }
        setStoragePlaces(rootPlaces);
    }

    //=============================== Gettters and setters ==================================
    public List<StoragePlaceTO> getStoragePlaces() {
        return storagePlaces;
    }

    public void setStoragePlaces(List<StoragePlaceTO> storagePlaces) {
        this.storagePlaces = storagePlaces;
    }
}
