/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.complecting;

import com.artigile.warehouse.utils.custom.types.CompositeNumber;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 10.12.2009
 */

/**
 * Strategy for calculation of complecting task's fields, when complecting task is created to
 * process movement item.
 * <strong>SINGLETON</strong>.
 */
class ComplectingTaskForMovementCalculationStrategy implements ComplectingTaskCalculationStrategy {
    private ComplectingTaskForMovementCalculationStrategy(){
    }

    private static ComplectingTaskForMovementCalculationStrategy instance = new ComplectingTaskForMovementCalculationStrategy();

    public static ComplectingTaskForMovementCalculationStrategy getInstance() {
        return instance;
    }

    @Override
    public String getTargetLocation(ComplectingTaskTO complectingTask) {
        return complectingTask.getMovementItem().getMovement().getToWarehouse().getName();
    }

    @Override
    public Long getParcelNo(ComplectingTaskTO complectingTask) {
        return complectingTask.getMovementItem().getMovement().getNumber();
    }

    @Override
    public CompositeNumber getItemNo(ComplectingTaskTO complectingTask) {
        return new CompositeNumber(
            I18nSupport.message("complectingTask.template.movementItemNumber"),
            new Long[]{complectingTask.getMovementItem().getNumber()}
        );
    }

    @Override
    public String getItemName(ComplectingTaskTO complectingTask) {
        return complectingTask.getMovementItem().getItemName();
    }

    @Override
    public String getItemMisc(ComplectingTaskTO complectingTask) {
        return complectingTask.getMovementItem().getItemMisc();
    }

    @Override
    public String getItemType(ComplectingTaskTO complectingTask) {
        return complectingTask.getMovementItem().getItemType();
    }

    @Override
    public String getItemMeas(ComplectingTaskTO complectingTask) {
        return complectingTask.getMovementItem().getCountMeas().getSign();
    }

    @Override
    public Long getRemainder(ComplectingTaskTO complectingTask) {
        WarehouseBatchTO warehouseBatch = complectingTask.getMovementItem().getWarehouseBatch();
        return warehouseBatch != null ? warehouseBatch.getCount() : null;
    }

    @Override
    public Integer getYear(ComplectingTaskTO complectingTask) {
        return complectingTask.getMovementItem().getDetailBatch().getYear();
    }

    @Override
    public WarehouseTOForReport getWarehouse(ComplectingTaskTO complectingTask) {
        return complectingTask.getMovementItem().getMovement().getFromWarehouse();
    }

    @Override
    public String getStoragePlace(ComplectingTaskTO complectingTask) {
        return complectingTask.getMovementItem().getFromStoragePlace().getSign();
    }

    @Override
    public Long getFillRate(ComplectingTaskTO complectingTask) {
        return complectingTask.getMovementItem().getFromStoragePlace().getFillingDegree();
    }

    @Override
    public String getNotice(ComplectingTaskTO complectingTask) {
        return complectingTask.getMovementItem().getItemNotice();
    }

    @Override
    public String getArticle(ComplectingTaskTO complectingTask) {
        return complectingTask.getMovementItem().getDetailBatch().getNomenclatureArticle();
    }

    @Override
    public String getBarCode(ComplectingTaskTO complectingTask) {
        return complectingTask.getMovementItem().getDetailBatch().getBarCode();
    }

    @Override
    public String getWarehouseNotice(ComplectingTaskTO complectingTask) {
        return complectingTask.getMovementItem().getWarehouseNotice();
    }
}
