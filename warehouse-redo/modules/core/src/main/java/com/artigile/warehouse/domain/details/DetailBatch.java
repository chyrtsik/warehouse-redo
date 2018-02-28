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

import com.artigile.warehouse.domain.directory.Manufacturer;
import com.artigile.warehouse.domain.directory.MeasureUnit;
import com.artigile.warehouse.domain.finance.Currency;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Batch of the details. One entity really equals to one element of the price list.
 * @author Shyrik, 26.12.2008
 */
@Entity
public class DetailBatch {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Unique identification number of goods in global directory.
     */
    @Column(unique = true, updatable = false, length = ModelFieldsLengths.UID_LENGTH)
    private String uidGoods;

    /**
     * Model, what detail batch is referenced to.
     */
    @ManyToOne(optional = false)
    private DetailModel model;

    /**
     * Number of this detail batch withing batches list for detail model.
     */
    private Integer sortNum;

    /**
     * Name of the detail batch.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Miscellaneous information about detail batch.
     */
    private String misc;

    /**
     * Type field of the detail batch.
     */
    private String type;

    /**
     * Acceptance type of the detail in the batch.
     */
    private String acceptance;

    /**
     * Year of detail producing.
     */
    private Integer year;

    /**
     * Bar code of product.
     */
    @Column(unique = true, length = ModelFieldsLengths.MAX_BAR_CODE_LENGTH)
    private String barCode;

    /**
     * Nomenclature article of detail 
     */
    @Column(unique = true)
    private String nomenclatureArticle;

    /**
     * Manufacturer of the batch.
     */
    @ManyToOne
    private Manufacturer manufacturer;

    /**
     * Buy price of the batch.
     */
    @Column(precision = ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION,
            scale = ModelFieldsLengths.MAX_LENGTH_DOUBLE_SCALE)
    private BigDecimal buyPrice;

    /**
     * Cell price of the detail.
     */
    @Column(precision = ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION,
            scale = ModelFieldsLengths.MAX_LENGTH_DOUBLE_SCALE)
    private BigDecimal sellPrice;

    /**
     * Currency of the price.
     */
    @ManyToOne(optional = false)
    private Currency currency;

    /**
     * Cell price of the detail in the second currency.
     */
    @Column(precision = ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION,
            scale = ModelFieldsLengths.MAX_LENGTH_DOUBLE_SCALE)
    private BigDecimal sellPrice2;

    /**
     * Second currency of the price.
     */
    @ManyToOne
    private Currency currency2;

    /**
     * Measure unit of the details in the batch.
     */
    @ManyToOne(optional = false)
    private MeasureUnit countMeas;

    /**
     * Notice for the batch.
     */
    private String notice;

    @OneToMany(mappedBy = "detail")
    private List<DetailSerialNumber> detailSerialNumbers;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * Count of the detail in the batch.
     */
    @Formula("(select IFNULL(sum(wb.amount), 0) from WarehouseBatch wb where wb.detailBatch_id=id)")
    private long count;

    /**
     * Count or reserved details (that details are not for sale).
     */
    @Formula("(select IFNULL(sum(wb.reservedCount), 0) from WarehouseBatch wb where wb.detailBatch_id=id)")
    private long reservedCount;

    @Formula("(select distinct 1 from WarehouseBatch wb where wb.needRecalculate=1 and wb.detailBatch_id=id)")
    private Boolean needRecalculate;

    //=========================== Operations =============================================

    /**
     * Use this method to manually change count of wares in the detail batch to keep
     * model im memory in consistent state.  For example, already after changing warehouse
     * batches count.
     * @param countDiff
     */
    public void changeCount(long countDiff) {
        this.count += countDiff;
    }

    /**
     * Use this method to manually change count of reserved wares in the detail batch to keep
     * model im memory in consistent state.  For example, already after changing warehouse
     * batches count.
     * @param reservedCountDiff
     */
    public void changeReservedCount(long reservedCountDiff) {
        this.reservedCount += reservedCountDiff;
    }

    //======================= Getters and setters ========================================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUidGoods() {
        return uidGoods;
    }

    public void setUidGoods(String uidGoods) {
        this.uidGoods = uidGoods;
    }

    public DetailModel getModel() {
        return model;
    }

    public void setModel(DetailModel model) {
        this.model = model;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMisc() {
        return misc;
    }

    public void setMisc(String misc) {
        this.misc = misc;
    }

    public String getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(String acceptance) {
        this.acceptance = acceptance;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getNomenclatureArticle() {
        return nomenclatureArticle;
    }

    public void setNomenclatureArticle(String nomenclatureArticle) {
        this.nomenclatureArticle = nomenclatureArticle;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getSellPrice2() {
        return sellPrice2;
    }

    public void setSellPrice2(BigDecimal sellPrice2) {
        this.sellPrice2 = sellPrice2;
    }

    public Currency getCurrency2() {
        return currency2;
    }

    public void setCurrency2(Currency currency2) {
        this.currency2 = currency2;
    }

    public long getCount() {
        return count;
    }

    public long getReservedCount() {
        return reservedCount;
    }

    public MeasureUnit getCountMeas() {
        return countMeas;
    }

    public void setCountMeas(MeasureUnit countMeas) {
        this.countMeas = countMeas;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getNeedRecalculate() {
        return needRecalculate != null ? needRecalculate : false;
    }

    public List<DetailSerialNumber> getDetailSerialNumbers() {
        return detailSerialNumbers;
    }

    public void setDetailSerialNumbers(List<DetailSerialNumber> detailSerialNumbers) {
        this.detailSerialNumbers = detailSerialNumbers;
    }

    public long getVersion() {
        return version;
    }
}
