/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.postings;

import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.contractors.Contractor;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNote;
import com.artigile.warehouse.domain.finance.Currency;
import com.artigile.warehouse.domain.inventorization.Inventorization;
import com.artigile.warehouse.domain.purchase.Purchase;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.domain.warehouse.Warehouse;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Shyrik, 02.02.2009
 */

/**
 * Posting is action (and document) of aceptance batch of detail to be placed at some places of the warehouses.
 * Posting is the only way to enter new goods into system database.
 */
@Entity
public class Posting {

    @Id
    @GeneratedValue
    private long id;

    /**
     * State of the posting.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PostingState state;

    /**
     * Used, that created the posting document.
     */
    @ManyToOne(optional = false)
    private User createdUser;

    /**
     * Unique number of the order (specific for the organization).
     */
    @Column(nullable = false, unique = true)
    private long number;

    /**
     * Date of creating the posting.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date createDate;

    /**
     * Contractor of the posting (from whom details are received).
     */
    @ManyToOne
    private Contractor contractor;

    /**
     * Currency, in what order price is calculated and stored.
     */
    @ManyToOne
    private Currency currency;

    /**
     * Currency, in what prices of posting items are by default.
     */
    @ManyToOne
    private Currency defaultCurrency;

    /**
     * Default warehouse to place posting items.
     */
    @ManyToOne
    private Warehouse defaultWarehouse;


    /**
     * Default storage place for posting items.
     */
    @ManyToOne
    private StoragePlace defaultStoragePlace;

    /**
     * Notice for the posting.
     */
    private String notice;

    /**
     * Purchase, from which this posting has been created.
     */
    @OneToOne
    private Purchase purchase;

    /**
     * Delivery note, from which this posting has been created.
     */
    @OneToOne
    private DeliveryNote deliveryNote;

    /**
     * Reference to inventorization when this posting was created as a result of inventorization.
     */
    @OneToOne(mappedBy = "posting")
    private Inventorization inventorization;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * Total price of the posting.
     */
    @Formula("(select IFNULL(sum(pi.price*pi.amount), 0) from PostingItem pi where pi.posting_id=id)")
    private BigDecimal totalPrice = BigDecimal.valueOf(0.);

    /**
     * Items in the posting.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "posting")
    private List<PostingItem> items = new ArrayList<PostingItem>();

    //================================= Getters and setters ==================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PostingState getState() {
        return state;
    }

    public void setState(PostingState state) {
        this.state = state;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
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

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(Currency defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public Warehouse getDefaultWarehouse() {
        return defaultWarehouse;
    }

    public void setDefaultWarehouse(Warehouse defaultWarehouse) {
        this.defaultWarehouse = defaultWarehouse;
    }

    public StoragePlace getDefaultStoragePlace() {
        return defaultStoragePlace;
    }

    public void setDefaultStoragePlace(StoragePlace defaultStoragePlace) {
        this.defaultStoragePlace = defaultStoragePlace;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public DeliveryNote getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public Inventorization getInventorization() {
        return inventorization;
    }

    public void setInventorization(Inventorization inventorization) {
        this.inventorization = inventorization;
    }

    public long getVersion() {
        return version;
    }

    public List<PostingItem> getItems() {
        return items;
    }

    public void setItems(List<PostingItem> items) {
        this.items = items;
        for (PostingItem item : items){
            item.setPosting(this);
        }
    }
}
