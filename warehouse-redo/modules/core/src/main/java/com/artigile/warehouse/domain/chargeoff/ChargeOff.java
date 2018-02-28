/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.chargeoff;

import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.inventorization.Inventorization;
import com.artigile.warehouse.domain.movement.Movement;
import com.artigile.warehouse.domain.orders.Order;
import com.artigile.warehouse.domain.warehouse.Warehouse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Shyrik, 09.10.2009
 */

/**
 * Represents action, when ware removed from warehouse (for example, during inventorization or
 * shipping to the customer).
 */
@Entity
public class ChargeOff {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Unique number of charge-off document.
     */
    @Column(nullable = false, unique = true)
    private long number;

    /**
     * Warehouse, from which wares have been charged off.
     */
    @ManyToOne(optional = false)
    private Warehouse warehouse;

    /**
     * User, who has performed operation.
     */
    @ManyToOne(optional = false)
    private User performer;

    /**
     * Date and time, when charge-off has been performed.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date performDate;

    /**
     * Reference to ware movement if this charge off was performed to deliver wares to another warehouse.
     */
    @ManyToOne(optional = true)
    private Movement movement;

    /**
     * Reference to order if this charge off was performed to deliver ordered goods to customer.
     */
    @ManyToOne(optional = true)
    private Order order;

    /**
     * Reference to intentorization if this charge off was performed as a result of verification of items in stock.
     */
    @OneToOne(mappedBy = "chargeOff")
    private Inventorization inventorization;

    /**
     * Reason, by what wares have been charged off.
     */
    @Enumerated(value = EnumType.STRING)
    private ChargeOffReason reason;

    /**
     * Additional textual information about charge-off.
     */
    private String notice;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * Wares, has been affected by charge off operation.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chargeOff")
    private List<ChargeOffItem> items = new ArrayList<ChargeOffItem>();

    //==================================== Constructors ======================================
    public ChargeOff(){
    }

    //========================================== Getters and setters ============================================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public User getPerformer() {
        return performer;
    }

    public void setPerformer(User performer) {
        this.performer = performer;
    }

    public Date getPerformDate() {
        return performDate;
    }

    public void setPerformDate(Date performDate) {
        this.performDate = performDate;
    }

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setInventorization(Inventorization inventorization) {
        this.inventorization = inventorization;
    }

    public Inventorization getInventorization() {
        return inventorization;
    }

    public ChargeOffReason getReason() {
        return reason;
    }

    public void setReason(ChargeOffReason reason) {
        this.reason = reason;
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

    public List<ChargeOffItem> getItems() {
        return items;
    }

    public void setItems(List<ChargeOffItem> items) {
        this.items = items;
        for (ChargeOffItem item : items){
            item.setChargeOff(this);
        }
    }
}
