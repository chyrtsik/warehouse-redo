/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.movement;

import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.warehouse.Warehouse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Shyrik, 21.11.2009
 */

/**
 * Movement document: describes a set of ware movement from one location to another.
 */

@Entity
public class Movement {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Unique number of movement document.
     */
    @Column(nullable = false, unique = true)
    private long number;

    /**
     * Current state of the movement.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MovementState state;

    /**
     * Result of the movement.
     */
    @Enumerated(value = EnumType.STRING)
    private MovementResult result;

    /**
     * User, created movement document.
     */
    @ManyToOne(optional = false)
    private User createUser;

    /**
     * Date, when movement document was created.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    /**
     * Date, when movement has begun.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginDate;

    /**
     * Date, when movement has been completed.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    /**
     * Source warehouse from which products are being moved.
     */
    @JoinColumn
    @ManyToOne
    private Warehouse fromWarehouse;

    /**
     * Destination warehouse to which products are being moved.
     */
    @JoinColumn
    @ManyToOne
    private Warehouse toWarehouse;

    /**
     * Additional text about wares movement.
     */
    private String notice;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * Items of the movement.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movement")
    private List<MovementItem> items = new ArrayList<MovementItem>();

    //=============================== Getters and setters ===============================

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

    public MovementState getState() {
        return state;
    }

    public void setState(MovementState state) {
        this.state = state;
    }

    public MovementResult getResult() {
        return result;
    }

    public void setResult(MovementResult result) {
        this.result = result;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Warehouse getFromWarehouse() {
        return fromWarehouse;
    }

    public void setFromWarehouse(Warehouse fromWarehouse) {
        this.fromWarehouse = fromWarehouse;
    }

    public Warehouse getToWarehouse() {
        return toWarehouse;
    }

    public void setToWarehouse(Warehouse toWarehouse) {
        this.toWarehouse = toWarehouse;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public List<MovementItem> getItems() {
        return items;
    }

    public void setItems(List<MovementItem> items) {
        this.items = items;
    }

    public long getVersion() {
        return version;
    }
}
