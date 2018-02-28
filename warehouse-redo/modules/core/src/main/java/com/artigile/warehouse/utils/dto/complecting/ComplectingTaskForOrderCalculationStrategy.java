/*
 * Copyright (c) 2007-2011 Artigile.
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
 * Calculated fields of complecting task, that is created for processing of order item.
 * <strong>SINGLETON</strong>.
 */
class ComplectingTaskForOrderCalculationStrategy implements ComplectingTaskCalculationStrategy {
    private ComplectingTaskForOrderCalculationStrategy(){
    }

    private static ComplectingTaskForOrderCalculationStrategy instance = new ComplectingTaskForOrderCalculationStrategy();

    public static ComplectingTaskCalculationStrategy getInstance() {
        return instance;
    }

    @Override
    public String getTargetLocation(ComplectingTaskTO complectingTask) {
        return complectingTask.getOrderSubItem().getOrderItem().getOrder().getContractor().getName();
    }

    @Override
    public Long getParcelNo(ComplectingTaskTO complectingTask) {
        return complectingTask.getOrderSubItem().getOrderItem().getOrder().getNumber();
    }

    @Override
    public CompositeNumber getItemNo(ComplectingTaskTO complectingTask) {
        if (complectingTask.getOrderSubItem().getOrderItem().getSubItems().size() > 1){
            //noinspection UnresolvedPropertyKey
            return new CompositeNumber(
                I18nSupport.message("complectingTask.template.orderItemNumberWithSubitem"),
                new Long[]{complectingTask.getOrderSubItem().getOrderItem().getNumber(), complectingTask.getOrderSubItem().getNumber()}
            );
        }
        else{
            //noinspection UnresolvedPropertyKey
            return new CompositeNumber(
                I18nSupport.message("complectingTask.template.orderItemNumber"),
                new Long[]{complectingTask.getOrderSubItem().getOrderItem().getNumber()}
            );
        }
    }

    @Override
    public String getItemName(ComplectingTaskTO complectingTask) {
        return complectingTask.getOrderSubItem().getOrderItem().getName();
    }

    @Override
    public String getItemMisc(ComplectingTaskTO complectingTask) {
        if (complectingTask.getOrderSubItem().getOrderItem().isDetailItem()){
            //Misc field is available only for details, but not for text items.
            return complectingTask.getOrderSubItem().getOrderItem().getDetailBatch().getMisc();
        }
        else{
            return "";
        }
    }

    @Override
    public String getItemType(ComplectingTaskTO complectingTask) {
        if (complectingTask.getOrderSubItem().getOrderItem().isDetailItem()){
            //Type field is available only for details, but not for text items.
            return complectingTask.getOrderSubItem().getOrderItem().getDetailBatch().getType();
        }
        else{
            return "";
        }
    }

    @Override
    public String getItemMeas(ComplectingTaskTO complectingTask) {
        return complectingTask.getOrderSubItem().getOrderItem().getMeasureSign();
    }

    @Override
    public Long getRemainder(ComplectingTaskTO complectingTask) {
        WarehouseBatchTO warehouseBatch = complectingTask.getOrderSubItem().getWarehouseBatch(); 
        return warehouseBatch == null ? null : warehouseBatch.getCount();
    }

    @Override
    public Integer getYear(ComplectingTaskTO complectingTask) {
        return complectingTask.getOrderSubItem().getOrderItem().getDetailBatch().getYear();
    }

    @Override
    public WarehouseTOForReport getWarehouse(ComplectingTaskTO complectingTask) {
        return complectingTask.getOrderSubItem().getStoragePlace().getWarehouse();
    }

    @Override
    public String getStoragePlace(ComplectingTaskTO complectingTask) {
        return complectingTask.getOrderSubItem().getStoragePlace().getSign();
    }

    @Override
    public Long getFillRate(ComplectingTaskTO complectingTask) {
        return complectingTask.getOrderSubItem().getStoragePlace().getFillingDegree();
    }

    @Override
    public String getNotice(ComplectingTaskTO complectingTask) {
        return complectingTask.getOrderSubItem().getNotice();
    }

    @Override
    public String getArticle(ComplectingTaskTO complectingTask) {
        return complectingTask.getOrderSubItem().getOrderItem().getDetailBatch().getNomenclatureArticle();
    }

    @Override
    public String getBarCode(ComplectingTaskTO complectingTask) {
        return complectingTask.getOrderSubItem().getOrderItem().getDetailBatch().getBarCode();
    }

    @Override
    public String getWarehouseNotice(ComplectingTaskTO complectingTask) {
        return complectingTask.getOrderSubItem().getWarehouseNotice();
    }
}
