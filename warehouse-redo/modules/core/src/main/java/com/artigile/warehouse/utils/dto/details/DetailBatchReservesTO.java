package com.artigile.warehouse.utils.dto.details;

import java.util.Date;

/**
 * @author Valery Barysok, 2013-05-19
 */
public class DetailBatchReservesTO {

    private DetailBatchDocumentType documentType;

    private long number;

    private Date createDate;

    private String contractorName;

    private long itemNumber;

    private long amount;

    private String userName;

    private String warehouseName;

    private String storagePlaceSign;

    private Long batchNo;

    public DetailBatchDocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DetailBatchDocumentType documentType) {
        this.documentType = documentType;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public long getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(long itemNumber) {
        this.itemNumber = itemNumber;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getStoragePlaceSign() {
        return storagePlaceSign;
    }

    public void setStoragePlaceSign(String storagePlaceSign) {
        this.storagePlaceSign = storagePlaceSign;
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }
}
