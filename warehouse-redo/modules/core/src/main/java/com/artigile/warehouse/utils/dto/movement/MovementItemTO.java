/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.movement;

import com.artigile.warehouse.domain.movement.MovementItemProcessingResult;
import com.artigile.warehouse.domain.movement.MovementItemState;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Shyrik, 22.11.2009
 */
public class MovementItemTO extends EqualsByIdImpl {
    private long id;
    private MovementTOForReport movement;
    private long number;
    private MovementItemState state;
    private MovementItemProcessingResult processingResult;
    private WarehouseBatchTO warehouseBatch;
    private DetailBatchTO detailBatch;
    private Date shelfLifeDate;
    private String warehouseNotice;
    private Long count;
    private MeasureUnitTO countMeas;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private CurrencyTO currency;
    private StoragePlaceTOForReport fromStoragePlace;

    private Long batchNo;
    private Date receiptDate;

    private UserTO shippedUser;
    private Date shippedDate;
    private UserTO postedUser;
    private Date postedDate;

    //=========================== Operations ===================================
    public void init(MovementTOForReport movement, WarehouseBatchTO warehouseBatch) {
        setMovement(movement);
        setWarehouseBatch(warehouseBatch);
        setShelfLifeDate(warehouseBatch.getShelfLifeDate());
    }

    public boolean isSame(MovementItemTO second) {
        return getWarehouseBatch().getId() == second.getWarehouseBatch().getId(); 
    }

    //======================== Calculated getters ==============================
    public String getItemType(){
        return getDetailBatch().getType();
    }

    public String getItemName(){
        return getDetailBatch().getName();
    }

    public String getItemMisc(){
        return getDetailBatch().getMisc();
    }

    public String getItemNotice(){
        return getDetailBatch().getNotice();
    }

    public Long getAvailableCount(){
        Long availableAtWarehouse = getWarehouseBatch() == null ? null : getWarehouseBatch().getAvailableCount();
        if ( availableAtWarehouse == null ){
            return null;
        }
        if ( getCount() == null ){
            return availableAtWarehouse;
        }
        return availableAtWarehouse + getCount();
    }

    //======================== Getters and setters =============================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MovementTOForReport getMovement() {
        return movement;
    }

    public void setMovement(MovementTOForReport movement) {
        this.movement = movement;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public MovementItemState getState() {
        return state;
    }

    public void setState(MovementItemState state) {
        this.state = state;
    }

    public MovementItemProcessingResult getProcessingResult() {
        return processingResult;
    }

    public void setProcessingResult(MovementItemProcessingResult processingResult) {
        this.processingResult = processingResult;
    }

    public WarehouseBatchTO getWarehouseBatch() {
        return warehouseBatch;
    }

    public void setWarehouseBatch(WarehouseBatchTO warehouseBatch) {
        this.warehouseBatch = warehouseBatch;
    }

    public DetailBatchTO getDetailBatch() {
        return warehouseBatch == null ? detailBatch : warehouseBatch.getDetailBatch();
    }

    public void setDetailBatch(DetailBatchTO detailBatch) {
        this.detailBatch = detailBatch;
    }

    public Date getShelfLifeDate() {
        return shelfLifeDate;
    }

    public void setShelfLifeDate(Date shelfLifeDate) {
        this.shelfLifeDate = shelfLifeDate;
    }

    public String getWarehouseNotice() {
        return warehouseBatch == null ? warehouseNotice : warehouseBatch.getNotice();
    }

    public void setWarehouseNotice(String warehouseNotice) {
        this.warehouseNotice = warehouseNotice;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public MeasureUnitTO getCountMeas() {
        return warehouseBatch == null ? countMeas : warehouseBatch.getDetailBatch().getCountMeas();
    }

    public void setCountMeas(MeasureUnitTO countMeas) {
        this.countMeas = countMeas;
    }

    public StoragePlaceTOForReport getFromStoragePlace() {
        return warehouseBatch == null ? fromStoragePlace : warehouseBatch.getStoragePlace();
    }

    public void setFromStoragePlace(StoragePlaceTOForReport fromStoragePlace) {
        this.fromStoragePlace = fromStoragePlace;
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public UserTO getShippedUser() {
        return shippedUser;
    }

    public void setShippedUser(UserTO shippedUser) {
        this.shippedUser = shippedUser;
    }

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    public UserTO getPostedUser() {
        return postedUser;
    }

    public void setPostedUser(UserTO postedUser) {
        this.postedUser = postedUser;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }
}
