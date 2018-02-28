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

/**
 * @author Shyrik, 05.02.2009
 */

/**
 * Filter for loading storage places.
 */
public class StoragePlaceFilter {
    /**
     * Is true, only storage, that are available for postings of details are loaded.
     */
    private boolean availableForPosting;

    /**
     * Warehouses, each storage places will be examined.
     */
    private Long warehouseId;


    private StoragePlaceFilter(){
    }

    public static StoragePlaceFilter createByWarehouseFilter(long warehouseId){
        StoragePlaceFilter filter = new StoragePlaceFilter();
        filter.warehouseId = warehouseId;
        return filter;
    }

    public static StoragePlaceFilter createAvailableForPostingsFilter(long warehouseId){
        StoragePlaceFilter filter = new StoragePlaceFilter();
        filter.availableForPosting = true;
        filter.warehouseId = warehouseId;
        return filter;
    }

    //======================= Getters and setters ============================================
    public boolean isAvailableForPosting() {
        return availableForPosting;
    }

    public Long getWarehouseId(){
        return warehouseId;
    }
}
