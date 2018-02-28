/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.purchase;

import com.artigile.warehouse.domain.directory.MeasureUnit;
import com.artigile.warehouse.domain.needs.WareNeedItem;
import com.artigile.warehouse.utils.ModelFieldsLengths;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Shyrik, 01.03.2009
 */

/**
 * Entity, of a purchase item (ware being purchased during the purchase).
 */
@Entity
public class PurchaseItem {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Unique number of the item in the scope of purchase.
     */
    @Column(nullable = false)
    private Long number;

    /**
     * Purchase, to which this item belongs to.
     */
    @ManyToOne(optional = false)
    private Purchase purchase;

    /**
     * Price of the ware in the purchase item.
     */
    @Column(precision = ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION,
            scale = ModelFieldsLengths.MAX_LENGTH_DOUBLE_SCALE)
    private BigDecimal price;

    /**
     * Quantity of wares to be purchased.
     */
    private Long amount;

    /**
     * Measure unit of a purchase item.
     */
    @ManyToOne(optional = false)
    private MeasureUnit countMeas;

    /**
     * Need in ware, for which satisfaction this purchase item have been created.
     */
    @OneToOne
    private WareNeedItem wareNeedItem;

    /**
     * Name of the purchase item, if it is a text, bun not the ware need.
     */
    private String name;

    /**
     * Misc field of the purchase item, if it is a text, bun not the ware need.
     */
    private String misc;

    /**
     * Additional user defined comment for the purchase item.
     */
    private String notice;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //============================ Getters and setters ===========================================
    public boolean isText() {
        return wareNeedItem == null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
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

    public MeasureUnit getCountMeas() {
        return countMeas;
    }

    public void setCountMeas(MeasureUnit countMeas) {
        this.countMeas = countMeas;
    }

    public WareNeedItem getWareNeedItem() {
        return wareNeedItem;
    }

    public void setWareNeedItem(WareNeedItem wareNeedItem) {
        this.wareNeedItem = wareNeedItem;
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

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public long getVersion() {
        return version;
    }
}
