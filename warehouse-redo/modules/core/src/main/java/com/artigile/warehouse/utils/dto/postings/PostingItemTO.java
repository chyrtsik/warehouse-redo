/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.postings;

import com.artigile.warehouse.utils.Copiable;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Shyrik, 06.02.2009
 */
public class PostingItemTO extends EqualsByIdImpl implements Copiable{
    private long id;

    private PostingTOForReport posting;

    private long number;

    private BigDecimal price;

    private Long count;

    private DetailBatchTO detailBatch;

    private UnclassifiedCatalogItemTO unclassifiedCatalogItem;

    private CurrencyTO originalCurrency;

    private BigDecimal originalPrice;

    private BigDecimal salePrice;

    private StoragePlaceTOForReport storagePlace;

    private String notice;

    private Date shelfLifeDate;

    public PostingItemTO(){
    }

    /**
     * Use this constructor to create detail posting item.
     * @param posting
     * @param detailBatch
     */
    public PostingItemTO(PostingTOForReport posting, DetailBatchTO detailBatch){
        this.posting = posting;
        this.detailBatch = detailBatch;
    }

    /**
     * Use this constructor to create text posting item.
     * @param posting posting.
     *
     */
    public PostingItemTO(PostingTOForReport posting){
        this.posting = posting;
    }

    /**
     * Use this factory method to create unclassified posting item.
     * @param posting posting of new item.
     * @param barCode bar code of new item.
     * @return created posting item.
     */
    public static PostingItemTO createUnclassifiedBarCodeItem(PostingTOForReport posting, String barCode){
        PostingItemTO postingItem = new PostingItemTO(posting);
        postingItem.unclassifiedCatalogItem = new UnclassifiedCatalogItemTO();
        postingItem.unclassifiedCatalogItem.setBarCode(barCode);
        return postingItem;
    }

    //============================== Calculated getters ==============================================

    /**
     * Name may differ from original detail batch name. This is why use this getter for obtaining name
     * of the posting item.
     * @return name of posting item to display.
     */
    public String getName(){
        if (isUnclassifiedItem()){
            return unclassifiedCatalogItem.getDisplayName();
        }
        else{
            return detailBatch.getName();
        }
    }

    /**
     * @return true if item is not classified.
     */
    public boolean isUnclassifiedItem(){
        return detailBatch == null;
    }

    public boolean isNew() {
        return id == 0;
    }

    public WarehouseTOForReport getWarehouse(){
        return storagePlace == null ? null : storagePlace.getWarehouse();
    }

    public BigDecimal getTotalPrice(){
        if (getPrice() == null || getCount() == null){
            return null;
        }
        return getPrice().multiply(new BigDecimal(getCount()));
    }

    public boolean isSameItem(PostingItemTO itemToCompare) {
        if (isUnclassifiedItem() && itemToCompare.isUnclassifiedItem()){
            return unclassifiedCatalogItem.isSameItem(itemToCompare.getUnclassifiedCatalogItem());
        }
        else{
            return !isUnclassifiedItem() && !itemToCompare.isUnclassifiedItem() && getDetailBatch().getId() == itemToCompare.getDetailBatch().getId();
        }
    }

    //================================= Getters and setters ==========================================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PostingTOForReport getPosting() {
        return posting;
    }

    public void setPosting(PostingTOForReport posting) {
        this.posting = posting;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public DetailBatchTO getDetailBatch() {
        return detailBatch;
    }

    public void setDetailBatch(DetailBatchTO detailBatch) {
        this.detailBatch = detailBatch;
    }

    public UnclassifiedCatalogItemTO getUnclassifiedCatalogItem() {
        return unclassifiedCatalogItem;
    }

    public void setUnclassifiedCatalogItem(UnclassifiedCatalogItemTO unclassifiedCatalogItem) {
        this.unclassifiedCatalogItem = unclassifiedCatalogItem;
    }

    public CurrencyTO getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(CurrencyTO originalCurrency) {
        this.originalCurrency = originalCurrency;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
        if (originalPrice != null && originalCurrency != null){
            //Refreshing price of the item by converting original price into appropriate currency.
            long toCurrencyId = getPosting().getCurrency().getId();
            long fromCurrencyId = getOriginalCurrency().getId();
            setPrice(SpringServiceContext.getInstance().getExchangeService().convert(toCurrencyId, fromCurrencyId, originalPrice));
        }
        else{
            setPrice(null);
        }
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public StoragePlaceTOForReport getStoragePlace() {
        return storagePlace;
    }

    public void setStoragePlace(StoragePlaceTOForReport storagePlace) {
        this.storagePlace = storagePlace;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Date getShelfLifeDate() {
        return shelfLifeDate;
    }

    public void setShelfLifeDate(Date shelfLifeDate) {
        this.shelfLifeDate = shelfLifeDate;
    }

    @Override
    public void copyFrom(Object source) {
        PostingItemTO sourceItem = (PostingItemTO)source;

        assert(getId() == sourceItem.getId());
        assert(getPosting().getId() == sourceItem.getPosting().getId());

        setId(sourceItem.getId());
        setNumber(sourceItem.getNumber());
        setOriginalPrice(sourceItem.getOriginalPrice());
        setOriginalCurrency(sourceItem.getOriginalCurrency());
        setCount(sourceItem.getCount());
        setPrice(sourceItem.getPrice());
        setDetailBatch(sourceItem.getDetailBatch());
        setUnclassifiedCatalogItem(sourceItem.getUnclassifiedCatalogItem());
        setNotice(sourceItem.getNotice());
        setStoragePlace(sourceItem.getStoragePlace());
    }
}
