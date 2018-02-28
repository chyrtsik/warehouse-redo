/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.postings;

import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.directory.MeasureUnit;
import com.artigile.warehouse.domain.finance.Currency;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.utils.ModelFieldsLengths;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Shyrik, 02.02.2009
 */

/**
 * One item of a posting. Represents detail (or other good, that has been posted to the warehouse).
 */
@Entity
public class PostingItem {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Posting, to which this items belongs to.
     */
    @ManyToOne(optional = false)
    private Posting posting;

    /**
     * Number of the item in the posting.
     */
    private long number;

    /**
     * Price of the item (in the target posting currency).
     */
    @Column(precision = ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION,
            scale = ModelFieldsLengths.MAX_LENGTH_DOUBLE_SCALE)
    private BigDecimal price;

    /**
     * Count of the goods in this posting item.
     */
    private Long amount;

    /**
     * Detail batch item of this posting item. This item is optional when posting is being edited by
     * it should be set when posting is completed.
     */
    @ManyToOne
    private DetailBatch detailBatch;

    /**
     * Item that pending classifying before posting can be processed. This item can be loaded during bar code scanning
     * or importing of items list from external file.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    private UnclassifiedCatalogItem unclassifiedCatalogItem;

    /**
     * Currency of buying this item.
     */
    @ManyToOne
    private Currency originalCurrency;

    /**
     * Original price of this item.
     */
    @Column(precision = ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION,
            scale = ModelFieldsLengths.MAX_LENGTH_DOUBLE_SCALE)
    private BigDecimal originalPrice;

    /**
     * Changed sale price of the item
     */
    @Column(precision = ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION,
            scale = ModelFieldsLengths.MAX_LENGTH_DOUBLE_SCALE)
    private BigDecimal salePrice;

    /**
     * Storage place, to which this item has been posted.
     */
    @ManyToOne
    private StoragePlace storagePlace;

    /**
     * Notice about posting item.
     */
    private String notice;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "shelf_life_date")
    private Date shelfLifeDate;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Posting getPosting() {
        return posting;
    }

    public void setPosting(Posting posting) {
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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public DetailBatch getDetailBatch() {
        return detailBatch;
    }

    public void setDetailBatch(DetailBatch detailBatch) {
        this.detailBatch = detailBatch;
    }

    public UnclassifiedCatalogItem getUnclassifiedCatalogItem() {
        return unclassifiedCatalogItem;
    }

    public void setUnclassifiedCatalogItem(UnclassifiedCatalogItem unclassifiedCatalogItem) {
        this.unclassifiedCatalogItem = unclassifiedCatalogItem;
    }

    public Currency getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(Currency originalCurrency) {
        this.originalCurrency = originalCurrency;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public StoragePlace getStoragePlace() {
        return storagePlace;
    }

    public void setStoragePlace(StoragePlace storagePlace) {
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

    public long getVersion() {
        return version;
    }
}
