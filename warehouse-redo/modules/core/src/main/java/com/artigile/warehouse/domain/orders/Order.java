/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.orders;

import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.contractors.Contractor;
import com.artigile.warehouse.domain.contractors.LoadPlace;
import com.artigile.warehouse.domain.finance.Currency;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Shyrik, 06.01.2009
 */

/**
 * Filter for loading only non deleted order items.
 */
@FilterDef(
    name = "nonDeletedOrderItems",
    defaultCondition = "deleted <> 1"
)

/**
 * Entity, that holds order's data.
 */
@Entity
@Table(name = "OrderList")
public class Order {

    @Id
    @GeneratedValue
    private long id;

    /**
     * State of the order.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderState state;

    /**
     * Unique number of the order (specific for the organization).
     */
    @Column(nullable = false)
    private long number;

    /**
     * True if the order has been deleted from the order list. 
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean deleted;

    /**
     * Date of creating the order.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date createDate;

    /**
     * User, that created the oerder.
     */
    @ManyToOne(optional = false)
    private User createdUser;

    /**
     * Contractor of the order.
     */
    @ManyToOne(optional = false)
    private Contractor contractor;

    /**
     * Load place of the order (possibly delivery address).
     */
    @ManyToOne(optional = false)
    private LoadPlace loadPlace;

    /**
     * Date, when order has been send to the update.
     */
    @Temporal(TemporalType.DATE)
    private Date loadDate;

    /**
     * Currency, in what order price is calculated.
     */
    @ManyToOne(optional = false)
    private Currency currency;

    /**
     * Items in the order.
     */
    @Filter(name = "nonDeletedOrderItems")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    List<OrderItem> items = new ArrayList<OrderItem>();

    /**
     * User defined notice for the order.
     */
    private String notice;

    /**
     * Type of reserving of items in the order.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderReservingType reservingType;

    /**
     * Information about processing of the order. Due to such separation from the order's
     * data processing information may be updated without a risk or concurrent order data updating
     * (for example, order price updating).
     */
    @NotNull
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderProcessingInfo processingInfo = new OrderProcessingInfo();

    /**
     * Total price of the order.
     */
    @Formula("(select IFNULL(sum(oi.price*oi.amount), 0) from OrderItem oi where oi.order_id=id and oi.deleted<>1)")
    private BigDecimal totalPrice = BigDecimal.valueOf(0.);

    /**
     * VAT rate (value added tax). In percents.
     */
    private BigDecimal vatRate;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //======================================= Calculated gettes ==============================================

    /**
     * Checks, if order has items in given state.
     * @param itemState
     * @return
     */
    public boolean hasItemsInState(OrderItemState itemState) {
        for (OrderItem item : getItems()){
            if (item.getState().equals(itemState)){
                return true;
            }
        }
        return false;
    }
    
    public boolean isEditableState() {
        switch (state) {
            case CONSTRUCTION:
            case READY_FOR_COLLECTION:
            case COLLECTION:
            case COLLECTION_PROBLEM:
            case COLLECTED:
            case READY_FOR_SHIPPING:      
                return true;
            case SHIPPED:
            case SOLD:
                return false;
        }

        return false;
    } 
    
    //======================================= Getters and setters ============================================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
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

    public Date getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(Date loadDate) {
        this.loadDate = loadDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public OrderReservingType getReservingType() {
        return reservingType;
    }

    public void setReservingType(OrderReservingType reservingType) {
        this.reservingType = reservingType;
    }

    public OrderProcessingInfo getProcessingInfo() {
        return processingInfo;
    }

    public void setProcessingInfo(OrderProcessingInfo processingInfo) {
        this.processingInfo = processingInfo;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public long getVersion() {
        return version;
    }
}
