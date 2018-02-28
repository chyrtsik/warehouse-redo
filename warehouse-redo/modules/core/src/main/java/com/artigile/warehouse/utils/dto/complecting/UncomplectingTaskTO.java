/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.complecting;

import com.artigile.warehouse.domain.complecting.UncomplectingTaskState;
import com.artigile.warehouse.domain.complecting.UncomplectingTaskType;
import com.artigile.warehouse.utils.custom.types.CompositeNumber;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.orders.OrderSubItemTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.Date;

/**
 * @author Shyrik, 13.06.2009
 */
public class UncomplectingTaskTO extends EqualsByIdImpl {
    private long id;

    private UncomplectingTaskType type;

    private OrderSubItemTO orderSubItem;

    private UncomplectingTaskState state;

    private long countToProcess;

    private UserTO worker;

    private Date createDate;

    private Date doneDate;

    //========================= Special getters and setters ========================================
    public String getAction(){
        if (type == UncomplectingTaskType.DECREASE_AMOUNT_OF_WARES){
            return I18nSupport.message("uncomplectingTask.action.decreaseAmountOfWares", countToProcess);
        }
        else if (type == UncomplectingTaskType.REMOVE_ORDER_ITEM){
            return I18nSupport.message("uncomplectingTask.action.removeOrderItem");
        }
        else{
            throw new RuntimeException("UncomplectingTaskTO.getAction: unsupported type of task.");
        }
    }

    public long getParcelNo(){
        return orderSubItem.getOrderItem().getOrder().getNumber();
    }

    public CompositeNumber getItemNo(){
        if (orderSubItem.getOrderItem().getSubItems().size() > 1){
            return new CompositeNumber("{0} [ {1} ]", new Long[]{orderSubItem.getOrderItem().getNumber(), orderSubItem.getNumber()});
        }
        else{
            return new CompositeNumber("{0}", new Long[]{orderSubItem.getOrderItem().getNumber()});
        }
    }

    public String getItemName(){
        return orderSubItem.getOrderItem().getName();
    }

    public String getItemMisc(){
        if (orderSubItem.getOrderItem().isDetailItem()){
            //Misc field is available only for details, but not for text items.
            return orderSubItem.getOrderItem().getDetailBatch().getMisc();
        }
        else{
            return "";
        }
    }

    public String getItemType(){
        if (orderSubItem.getOrderItem().isDetailItem()){
            //Type field is available only for details, but not for text items.
            return orderSubItem.getOrderItem().getDetailBatch().getType();
        }
        else{
            return "";
        }
    }

    public String getItemMeas(){
        return orderSubItem.getOrderItem().getMeasureSign();
    }

    public WarehouseTOForReport getWarehouse(){
        return orderSubItem.getStoragePlace().getWarehouse();
    }

    public String getStoragePlace(){
        return orderSubItem.getStoragePlace().getSign();
    }

    //======================== Getters and setters =====================================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UncomplectingTaskType getType() {
        return type;
    }

    public void setType(UncomplectingTaskType type) {
        this.type = type;
    }

    public OrderSubItemTO getOrderSubItem() {
        return orderSubItem;
    }

    public void setOrderSubItem(OrderSubItemTO orderSubItem) {
        this.orderSubItem = orderSubItem;
    }

    public UncomplectingTaskState getState() {
        return state;
    }

    public void setState(UncomplectingTaskState state) {
        this.state = state;
    }

    public long getCountToProcess() {
        return countToProcess;
    }

    public void setCountToProcess(long countToProcess) {
        this.countToProcess = countToProcess;
    }

    public UserTO getWorker() {
        return worker;
    }

    public void setWorker(UserTO worker) {
        this.worker = worker;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(Date doneDate) {
        this.doneDate = doneDate;
    }
}
