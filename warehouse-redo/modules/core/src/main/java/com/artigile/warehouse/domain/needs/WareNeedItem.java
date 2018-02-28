/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.needs;

import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.contractors.Contractor;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.finance.Currency;
import com.artigile.warehouse.domain.purchase.PurchaseItem;
import com.artigile.warehouse.utils.ModelFieldsLengths;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Shyrik, 25.02.2009
 */

/**
 * Ware need item entity.
 */
@Entity
public class WareNeedItem {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Current state of the item.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private WareNeedItemState state;

    /**
     * Ware need list, to which this item belongs to.
     */
    @ManyToOne
    private WareNeed wareNeed;

    /**
     * Major number of the item.
     */
    @Column(nullable = false)
    private Long number;

    /**
     * Minor number of the item. Used for composite numbers, such as 1.1, 1.2, ... 1.999
     */
    private Long subNumber;

    /**
     * Date and time, when this need item was created.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDateTime;

    /**
     * User, who created this need item.
     */
    @ManyToOne(optional = false)
    private User createdUser;

    /**
     * Detail batch (price list items), representing full information about this are need.
     */
    @ManyToOne
    private DetailBatch detailBatch;

    /**
     * Name of the item, if need item is simple text (not detail from the price list).
     */
    private String name;

    /**
     * Misc field of item, if need item is simple text (not detail from the price list).
     */
    private String misc;

    /**
     * Count of the ware needed.
     */
    private Long amount;

    /**
     * Is set, represents minimal year of the ware to be bought.
     */
    private Long minYear;

    /**
     * The maximum price, allowable for the ware needed.
     */
    @Column(precision = ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION,
            scale = ModelFieldsLengths.MAX_LENGTH_DOUBLE_SCALE)
    private BigDecimal maxPrice;

    /**
     * Currency of the price of the need item.
     */
    @ManyToOne
    private Currency currency;

    /**
     * Notice for the need item.
     */
    private String notice;

    /**
     * If set, means update, for whom this ware is needed.
     */
    @ManyToOne
    private Contractor customer;

    /**
     * Purchase item, in which this ware need was placed.
     */
    @OneToOne(mappedBy = "wareNeedItem")
    private PurchaseItem purchaseItem;

    @Column(name = "auto_created", nullable = false, columnDefinition = "bit", length = 1)
    private boolean autoCreated;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //====================================== Constructors ================================================
    public WareNeedItem(){
    }

    public WareNeedItem(WareNeed wareNeed){
        this.wareNeed = wareNeed;
    }

    /**
     * Creates new ware need item using given ware need item as a template.
     * @param template
     */
    public WareNeedItem(WareNeedItem template) {
        this.state = template.getState();
        this.wareNeed = template.getWareNeed();
        this.number = template.getNumber();
        this.createDateTime = template.getCreateDateTime();
        this.createdUser = template.getCreatedUser();
        this.detailBatch = template.getDetailBatch();
        this.name = template.getName();
        this.misc = template.getMisc();
        this.minYear = template.getMinYear();
        this.maxPrice = template.getMaxPrice();
        this.currency = template.getCurrency();
        this.notice = template.getNotice();
        this.customer = template.getCustomer();
    }

    //=================================== Getters and setters ============================================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WareNeedItemState getState() {
        return state;
    }

    public void setState(WareNeedItemState state) {
        this.state = state;
    }

    public WareNeed getWareNeed() {
        return wareNeed;
    }

    public void setWareNeed(WareNeed wareNeed) {
        this.wareNeed = wareNeed;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getSubNumber() {
        return subNumber;
    }

    public void setSubNumber(Long subNumber) {
        this.subNumber = subNumber;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public DetailBatch getDetailBatch() {
        return detailBatch;
    }

    public void setDetailBatch(DetailBatch detailBatch) {
        this.detailBatch = detailBatch;
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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getMinYear() {
        return minYear;
    }

    public void setMinYear(Long minYear) {
        this.minYear = minYear;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Contractor getCustomer() {
        return customer;
    }

    public void setCustomer(Contractor customer) {
        this.customer = customer;
    }

    public PurchaseItem getPurchaseItem() {
        return purchaseItem;
    }

    public void setPurchaseItem(PurchaseItem purchaseItem) {
        this.purchaseItem = purchaseItem;
    }

    public boolean getAutoCreated() {
        return autoCreated;
    }

    public void setAutoCreated(boolean autoCreated) {
        this.autoCreated = autoCreated;
    }

    public long getVersion() {
        return version;
    }
}