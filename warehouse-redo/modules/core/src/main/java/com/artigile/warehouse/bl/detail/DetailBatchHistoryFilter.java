/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.detail;

/**
 * Filter to load detail batch operaions.
 * @author Aliaksandr.Chyrtsik, 08.12.11
 */
public class DetailBatchHistoryFilter {
    /**
     * Detail batch id (when only stats for concrete details batch is needed).
     */
    private Long detailBatchId;

    /**
     * Storage place id to load only stats for this particular storage place.
     */
    private Long storagePlaceId;

    /**
     * Warehouse id to load stats only for this warehouse.
     */
    private Long warehouseId;

    /**
     * If true then add summary data with count and price when item has not operations withing period building
     * the report. This allows to show records without any changes during the period (was 10, became 10 and so on).
     */
    private boolean addSummaryForItemsWithoutOperations;

    public Long getDetailBatchId() {
        return detailBatchId;
    }

    public void setDetailBatchId(Long detailBatchId) {
        this.detailBatchId = detailBatchId;
    }

    public Long getStoragePlaceId() {
        return storagePlaceId;
    }

    public void setStoragePlaceId(Long storagePlaceId) {
        this.storagePlaceId = storagePlaceId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public boolean isAddSummaryForItemsWithoutOperations() {
        return addSummaryForItemsWithoutOperations;
    }

    public void setAddSummaryForItemsWithoutOperations(boolean addSummaryForItemsWithoutOperations) {
        this.addSummaryForItemsWithoutOperations = addSummaryForItemsWithoutOperations;
    }
}
