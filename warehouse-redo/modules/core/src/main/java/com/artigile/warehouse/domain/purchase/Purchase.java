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

import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.contractors.Contractor;
import com.artigile.warehouse.domain.contractors.LoadPlace;
import com.artigile.warehouse.domain.finance.Currency;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Shyrik, 01.03.2009
 */

/**
 * Purchase entity (list of wares to be purchased from the constactor).
 */
@Entity
public class Purchase {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Number of the purchase document.
     */
    @Column(nullable = false, unique = true)
    private Long number;

    /**
     * Current state of the purchase document.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PurchaseState state;

    /**
     * Date of creating of purchase document.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date createDate;

    /**
     * User, who created purchase.
     */
    @ManyToOne(optional = false)
    private User createdUser;

    /**
     * Contractor (supplier), from whom purchased wares will come.
     */
    @ManyToOne(optional = false)
    private Contractor contractor;

    /**
     * Load place of the purchase.
     */
    @ManyToOne(optional = false)
    private LoadPlace loadPlace;

    /**
     * Currency of the purchase.
     */
    @ManyToOne(optional = false)
    private Currency currency;

    /**
     * Additional user defined information about purchase.
     */
    private String notice;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * Summary price of all wares in purchase.
     */
    @Formula("(select IFNULL(sum(pi.price*pi.amount), 0) from PurchaseItem pi where pi.purchase_id=id)")
    private BigDecimal totalPrice = BigDecimal.valueOf(0.);

    /**
     * Items of the purchase (wares to be purchased).
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchase")
    private List<PurchaseItem> items = new ArrayList<PurchaseItem>();

    //================================ Getters and setters ============================================
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

    public PurchaseState getState() {
        return state;
    }

    public void setState(PurchaseState state) {
        this.state = state;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public LoadPlace getLoadPlace() {
        return loadPlace;
    }

    public void setLoadPlace(LoadPlace loadPlace) {
        this.loadPlace = loadPlace;
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

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public long getVersion() {
        return version;
    }

    public List<PurchaseItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItem> items) {
        this.items = items;
    }
}
