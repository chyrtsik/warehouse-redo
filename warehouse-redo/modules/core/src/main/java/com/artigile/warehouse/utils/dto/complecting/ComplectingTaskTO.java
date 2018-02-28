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

import com.artigile.warehouse.domain.complecting.ComplectingTaskState;
import com.artigile.warehouse.utils.custom.types.CompositeNumber;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.movement.MovementItemTO;
import com.artigile.warehouse.utils.dto.orders.OrderSubItemTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.Date;

/**
 * @author Shyrik, 28.04.2009
 */
public class ComplectingTaskTO extends EqualsByIdImpl {
    private long id;

    private OrderSubItemTO orderSubItem;

    private MovementItemTO movementItem;

    private ComplectingTaskState state;

    private long neededCount;

    private Long foundCount;

    private boolean printed;

    private boolean stickerPrinted;

    private UserTO worker;

    private Date workBegin;

    private Date workEnd;

    //============================ Manipulators ====================================================
    public void copyFrom(ComplectingTaskTO src){
        this.id = src.id;
        this.orderSubItem = src.orderSubItem;
        this.movementItem = src.movementItem;
        this.state = src.state;
        this.neededCount = src.neededCount;
        this.foundCount = src.foundCount;
        this.printed = src.printed;
        this.worker = src.worker;
        this.workBegin = src.workBegin;
        this.workEnd = src.workEnd;
    }

    //========================= Special getters and setters ========================================
    public String getTargetLocation(){
        return getFieldsCalculationStrategy().getTargetLocation(this); 
    }

    public Long getParcelNo(){
        return getFieldsCalculationStrategy().getParcelNo(this);
    }

    public CompositeNumber getItemNo(){
        return getFieldsCalculationStrategy().getItemNo(this);
    }

    public String getItemName(){
        return getFieldsCalculationStrategy().getItemName(this);
    }

    public String getItemMisc(){
        return getFieldsCalculationStrategy().getItemMisc(this);
    }

    public String getItemType(){
        return getFieldsCalculationStrategy().getItemType(this);
    }

    public String getItemMeas(){
        return getFieldsCalculationStrategy().getItemMeas(this);
    }

    public Long getRemainder(){
        return getFieldsCalculationStrategy().getRemainder(this);
    }

    public Integer getYear(){
        return getFieldsCalculationStrategy().getYear(this);
    }

    public WarehouseTOForReport getWarehouse(){
        return getFieldsCalculationStrategy().getWarehouse(this);
    }

    public String getStoragePlace(){
        return getFieldsCalculationStrategy().getStoragePlace(this);
    }

    public Long getFillRate(){
        return getFieldsCalculationStrategy().getFillRate(this);
    }

    public String getNotice(){
        return getFieldsCalculationStrategy().getNotice(this);                
    }

    public String getWarehouseNotice(){
        return getFieldsCalculationStrategy().getWarehouseNotice(this);
    }

    public String getItemArticle(){
        return getFieldsCalculationStrategy().getArticle(this);
    }

    public String getItemBarCode(){
        return getFieldsCalculationStrategy().getBarCode(this);
    }
    public String getProcessingResult(){
        if (foundCount == null){
            return null;
        }
        else if (foundCount == neededCount){
            return I18nSupport.message("complectingTask.processing.result.name.complected");
        }
        else if (foundCount == 0){
            return I18nSupport.message("complectingTask.processing.result.name.notFound");
        }
        else if (foundCount < neededCount){
            return I18nSupport.message("complectingTask.processing.result.name.notEnough");
        }
        else if (foundCount > neededCount){
            return I18nSupport.message("complectingTask.processing.result.name.tooMuch");
        }
        throw new RuntimeException("ComplectingTaskTO.getProcessingResult: unexpected complecting task state");
    }

    public boolean isProblem() {
        return getState() == ComplectingTaskState.PROCESSED && getFoundCount() != getNeededCount();
    }

    public boolean isInProcess() {
        return getState() == ComplectingTaskState.PROCESSING;
    }

    public boolean isCompleted() {
        return getFoundCount() != null;
    }

    public boolean isReadyForShipping() {
        return getState().equals(ComplectingTaskState.PROCESSED) && !isProblem();
    }

    private boolean isMovementComplectingTask() {
        return movementItem != null;
    }

    private boolean isOrderComplectingTask() {
        return orderSubItem != null;
    }

    private ComplectingTaskCalculationStrategy getFieldsCalculationStrategy() {
        if (isOrderComplectingTask()){
            return ComplectingTaskForOrderCalculationStrategy.getInstance();             
        }
        else if (isMovementComplectingTask()){
            return ComplectingTaskForMovementCalculationStrategy.getInstance();
        }
        else{
            throw new RuntimeException("Uncorrect complecting task type: not order, and not movement.");
        }
    }

    //======================== Getters and setters =====================================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrderSubItemTO getOrderSubItem() {
        return orderSubItem;
    }

    public void setOrderSubItem(OrderSubItemTO orderSubItem) {
        this.orderSubItem = orderSubItem;
    }

    public MovementItemTO getMovementItem() {
        return movementItem;
    }

    public void setMovementItem(MovementItemTO movementItem) {
        this.movementItem = movementItem;
    }

    public ComplectingTaskState getState() {
        return state;
    }

    public void setState(ComplectingTaskState state) {
        this.state = state;
    }

    public long getNeededCount(){
        return neededCount;
    }

    public void setNeededCount(long neededCount) {
        this.neededCount = neededCount;
    }

    public Long getFoundCount(){
        return this.foundCount;
    }

    public void setFoundCount(Long foundCount) {
        this.foundCount = foundCount;
    }

    public boolean getPrinted() {
        return printed;
    }

    public void setPrinted(boolean printed) {
        this.printed = printed;
    }

    public boolean getStickerPrinted() {
        return stickerPrinted;
    }

    public void setStickerPrinted(boolean stickerRrinted) {
        this.stickerPrinted = stickerRrinted;
    }

    public UserTO getWorker() {
        return worker;
    }

    public void setWorker(UserTO worker) {
        this.worker = worker;
    }

    public Date getWorkBegin() {
        return workBegin;
    }

    public void setWorkBegin(Date workBegin) {
        this.workBegin = workBegin;
    }

    public Date getWorkEnd() {
        return workEnd;
    }

    public void setWorkEnd(Date workEnd) {
        this.workEnd = workEnd;
    }
}
