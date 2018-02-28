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

import java.util.List;

/**
 * @author Shyrik, 03.02.2009
 */

/**
 * Filter for loading warehouses list.
 */
public class WarehouseFilter {
    /**
     * Is true, only warehouses, that are available for postings of details are loaded.
     */
    private boolean onlyAvailableForPosting;

    /**
     * User, for whom availabible warehouses should be loaded.
     */
    private long complectingUserId;

    /**
     * Warehouses, that should NOT be loaded.
     */
    private Long excludedWarehouseIds[];

    public WarehouseFilter() {
    }

    /**
     * Created filter, that is used for loaging only warehouses, that are available for posting.
     * @return
     */
    public static WarehouseFilter createOnlyAvailableForPostingsFilter(){
        WarehouseFilter filter = new WarehouseFilter();
        filter.onlyAvailableForPosting = true;
        return filter;
    }

    /**
     * Created filter, that helps to load list of warehouses excluding the given one.
     * @param warehouseId warehouse to be excluded from result warehouses list.
     * @return
     */
    public static WarehouseFilter createExcludeWarehouseFilter(long warehouseId){
        WarehouseFilter filter = new WarehouseFilter();
        filter.setExcludedWarehouseIds(new Long[]{warehouseId});
        return filter;
    }

    //======================= Getters and setters ============================================
    public boolean isOnlyAvailableForPosting() {
        return onlyAvailableForPosting;
    }

    public long getComplectingUserId() {
        return complectingUserId;
    }

    public void setComplectingUserId(long complectingUserId) {
        this.complectingUserId = complectingUserId;
    }

    public Long[] getExcludedWarehouseIds() {
        return excludedWarehouseIds;
    }

    public void setExcludedWarehouseIds(Long excludedWarehouseIds[]) {
        this.excludedWarehouseIds = excludedWarehouseIds;
    }

    public void setExcludedWarehouseIds(List<Long> excludedWarehouseIds) {
        if (excludedWarehouseIds != null && excludedWarehouseIds.size() > 0){
            this.excludedWarehouseIds = new Long[excludedWarehouseIds.size()];
            for (int index = 0; index < excludedWarehouseIds.size(); index++){
                this.excludedWarehouseIds[index] = excludedWarehouseIds.get(index);
            }
        }
        else{
            this.excludedWarehouseIds = null;
        }
    }
}
