/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.details;

import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.chargeoff.ChargeOffItem;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteItem;
import com.artigile.warehouse.domain.finance.Currency;
import com.artigile.warehouse.domain.postings.PostingItem;
import com.artigile.warehouse.domain.warehouse.StoragePlace;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * History record about single operation with detail batch.
 * Holds full information about changing of detail batch count at all warehouse storage places.
 *
 * @author Aliaksandr.Chyrtsik, 02.10.11
 */
@Entity
public class DetailBatchOperation {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Date and time of operation.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationDateTime;

    /**
     * User that performed this operation.
     */
    @ManyToOne(optional = false)
    private User performedUser;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private DetailBatchOperationType operationType;

    /**
     * Detail batch have been changed.
     */
    @ManyToOne(optional = false)
    private DetailBatch detailBatch;

    /**
     * Warehouse storage place of this detail batch (at the time of operation).
     */
    @ManyToOne(optional = false)
    private StoragePlace storagePlace;

    /**
     * Posting item that was source of changed warehouse batch.
     * This helps us to track which warehouse batch was changed. Specified only if tracking of posting items for
     * warehouse batches is enabled.
     */
    @ManyToOne
    private PostingItem postingItemOfChangedWarehouseBatch;

    private Date shelfLifeDateOfChangedWarehouseBatch;

    /**
     * Posting item for this operation (when count has been changes due to the posting).
     */
    @ManyToOne
    private PostingItem postingItem;

    /**
     * Charge off item for this operation (when count has been changes due to the charge off).
     */
    @JoinColumn
    @OneToOne
    private ChargeOffItem chargeOffItem;

    /**
     * Initial count.
     */
    @Column(nullable = false)
    private long initialCount;

    /**
     * New count count.
     */
    @Column(nullable = false)
    private long newCount;

    /**
     * Expected change of count.
     */
    @Column(nullable = false)
    private long changeOfCount;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //=============================== Calculated getters ===================================

    @Transient
    private DetailBatchDocumentDesc documentDesc;

    /**
     * @return number of document caused operation to be performed.
     */
    public Long getDocumentNumber(){
        return getDocumentDesc().getNumber(this);
    }

    /**
     * @return full name of a document that caused this operation to be logged.
     */
    public String getDocumentName() {
        return getDocumentDesc().getName(this);
    }

    /**
     * @return date of document.
     */
    public Date getDocumentDate(){
        return getDocumentDesc().getDate(this);
    }

    /**
     * @return contractor related to this operation if any.
     */
    public String getDocumentContractorName() {
        return getDocumentDesc().getContractorName(this);
    }

    private DetailBatchDocumentDesc getDocumentDesc() {
        if (documentDesc == null){
            synchronized (this){
                if (documentDesc == null){
                    documentDesc = DetailBatchDocumentDesc.getInstance(this);
                }
            }
        }
        return documentDesc;
    }

    /**
     * Calculate price of item. For posting it will be buy price, for orders it will be sell price.
     * This value allows to calculate costs, revenue and profit in time.
     */
    public BigDecimal getItemPrice(){
        if (DetailBatchOperationType.POSTING.equals(operationType)){
            return postingItem.getPrice();
        }
        else if (DetailBatchOperationType.CHARGE_OFF.equals(operationType)){
            DeliveryNoteItem deliveryNoteItem = chargeOffItem.getDeliveryNoteItem();
            return deliveryNoteItem != null ? deliveryNoteItem.getPrice() : null;
        }
        else{
            return null;
        }
    }

    /**
     * Calculate cost of one item changed during this operation. This value is based on buying price and
     * is used to calculate cost changes of goods in stock disregarding sales.
     */
    public BigDecimal getItemCost() {
        if (postingItemOfChangedWarehouseBatch != null){
            return postingItemOfChangedWarehouseBatch.getPrice();
        }
        else{
            return detailBatch.getBuyPrice();
        }
    }

    public Currency getCurrency(){
        if (DetailBatchOperationType.POSTING.equals(operationType)){
            return postingItem.getPosting().getCurrency();
        }
        else if (DetailBatchOperationType.CHARGE_OFF.equals(operationType)){
            DeliveryNoteItem deliveryNoteItem = chargeOffItem.getDeliveryNoteItem();
            return deliveryNoteItem != null ? deliveryNoteItem.getDeliveryNote().getCurrency() : null;
        }
        else{
            return null;
        }
    }

    //============================== Getters and setters ===================================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getOperationDateTime() {
        return operationDateTime;
    }

    public void setOperationDateTime(Date operationDateTime) {
        this.operationDateTime = operationDateTime;
    }

    public User getPerformedUser() {
        return performedUser;
    }

    public void setPerformedUser(User performedUser) {
        this.performedUser = performedUser;
    }

    public DetailBatchOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(DetailBatchOperationType operationType) {
        this.operationType = operationType;
    }

    public DetailBatch getDetailBatch() {
        return detailBatch;
    }

    public void setDetailBatch(DetailBatch detailBatch) {
        this.detailBatch = detailBatch;
    }

    public StoragePlace getStoragePlace() {
        return storagePlace;
    }

    public void setStoragePlace(StoragePlace storagePlace) {
        this.storagePlace = storagePlace;
    }

    public PostingItem getPostingItemOfChangedWarehouseBatch() {
        return postingItemOfChangedWarehouseBatch;
    }

    public void setPostingItemOfChangedWarehouseBatch(PostingItem postingItemOfChangedWarehouseBatch) {
        this.postingItemOfChangedWarehouseBatch = postingItemOfChangedWarehouseBatch;
    }

    public Date getShelfLifeDateOfChangedWarehouseBatch() {
        return shelfLifeDateOfChangedWarehouseBatch;
    }

    public void setShelfLifeDateOfChangedWarehouseBatch(Date shelfLifeDateOfChangedWarehouseBatch) {
        this.shelfLifeDateOfChangedWarehouseBatch = shelfLifeDateOfChangedWarehouseBatch;
    }

    public PostingItem getPostingItem() {
        return postingItem;
    }

    public void setPostingItem(PostingItem postingItem) {
        this.postingItem = postingItem;
    }

    public ChargeOffItem getChargeOffItem() {
        return chargeOffItem;
    }

    public void setChargeOffItem(ChargeOffItem chargeOffItem) {
        this.chargeOffItem = chargeOffItem;
    }

    public long getInitialCount() {
        return initialCount;
    }

    public void setInitialCount(long initialCount) {
        this.initialCount = initialCount;
    }

    public long getNewCount() {
        return newCount;
    }

    public void setNewCount(long newCount) {
        this.newCount = newCount;
    }

    public long getChangeOfCount() {
        return changeOfCount;
    }

    public void setChangeOfCount(long changeOfCount) {
        this.changeOfCount = changeOfCount;
    }

    public long getVersion() {
        return version;
    }
}
