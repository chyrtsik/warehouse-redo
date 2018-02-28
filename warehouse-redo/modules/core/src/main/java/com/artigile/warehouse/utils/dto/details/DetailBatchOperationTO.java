/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.details;

import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTOForReport;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Aliaksandr.Chyrtsik, 02.10.11
 */
public class DetailBatchOperationTO extends EqualsByIdImpl {
    //Information about operation.
    private long id;
    private Long batchNo;
    private Date dateTime;
    private String performedUser;

    //Information about goods that was shipped of received..
    private DetailBatchTO detailBatch;
    private StoragePlaceTOForReport storagePlace;
    private Date receiptDate;
    private BigDecimal buyPrice;
    private CurrencyTO buyCurrency;
    private Date shelfLifeDateOfChangedWarehouseBatch;

    //Document assigned to this operation.
    private Long documentNumber;
    private String documentName;
    private Date documentDate;
    private String documentContractorName;

    //Changes in stock counts and costs (buying costs).
    private long initialCount;
    private long postedCount;
    private long chargedOffCount;
    private long finalCount;
    private BigDecimal initialCost;
    private BigDecimal postedCost;
    private BigDecimal chargedOffCost;
    private BigDecimal finalCost;

//    //Sell price (for some clients to get revenue statistics).
//    private BigDecimal price;
//    private BigDecimal total;
//    private CurrencyTO currency;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getPerformedUser() {
        return performedUser;
    }

    public void setPerformedUser(String performedUser) {
        this.performedUser = performedUser;
    }

    public DetailBatchTO getDetailBatch() {
        return detailBatch;
    }

    public void setDetailBatch(DetailBatchTO detailBatch) {
        this.detailBatch = detailBatch;
    }

    public StoragePlaceTOForReport getStoragePlace() {
        return storagePlace;
    }

    public void setStoragePlace(StoragePlaceTOForReport storagePlace) {
        this.storagePlace = storagePlace;
    }

    public Long getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(Long documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentContractorName() {
        return documentContractorName;
    }

    public void setDocumentContractorName(String documentContractorName) {
        this.documentContractorName = documentContractorName;
    }

    public long getInitialCount() {
        return initialCount;
    }

    public void setInitialCount(long initialCount) {
        this.initialCount = initialCount;
    }

    public long getPostedCount() {
        return postedCount;
    }

    public void setPostedCount(long postedCount) {
        this.postedCount = postedCount;
    }

    public long getChargedOffCount() {
        return chargedOffCount;
    }

    public void setChargedOffCount(long chargedOffCount) {
        this.chargedOffCount = chargedOffCount;
    }

    public long getFinalCount() {
        return finalCount;
    }

    public void setFinalCount(long finalCount) {
        this.finalCount = finalCount;
    }

    public BigDecimal getInitialCost() {
        return initialCost;
    }

    public void setInitialCost(BigDecimal initialCost) {
        this.initialCost = initialCost;
    }

    public BigDecimal getPostedCost() {
        return postedCost;
    }

    public void setPostedCost(BigDecimal postedCost) {
        this.postedCost = postedCost;
    }

    public BigDecimal getChargedOffCost() {
        return chargedOffCost;
    }

    public void setChargedOffCost(BigDecimal chargedOffCost) {
        this.chargedOffCost = chargedOffCost;
    }

    public BigDecimal getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(BigDecimal finalCost) {
        this.finalCost = finalCost;
    }

//    public BigDecimal getItemPrice() {
//        return price;
//    }
//
//    public void setPrice(BigDecimal price) {
//        this.price = price;
//    }
//
//    public BigDecimal getTotal() {
//        return total;
//    }
//
//    public void setTotal(BigDecimal total) {
//        this.total = total;
//    }
//
//    public CurrencyTO getCurrency() {
//        return currency;
//    }
//
//    public void setCurrency(CurrencyTO currency) {
//        this.currency = currency;
//    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public CurrencyTO getBuyCurrency() {
        return buyCurrency;
    }

    public void setBuyCurrency(CurrencyTO buyCurrency) {
        this.buyCurrency = buyCurrency;
    }

    public Date getShelfLifeDateOfChangedWarehouseBatch() {
        return shelfLifeDateOfChangedWarehouseBatch;
    }

    public void setShelfLifeDateOfChangedWarehouseBatch(Date shelfLifeDateOfChangedWarehouseBatch) {
        this.shelfLifeDateOfChangedWarehouseBatch = shelfLifeDateOfChangedWarehouseBatch;
    }
}
