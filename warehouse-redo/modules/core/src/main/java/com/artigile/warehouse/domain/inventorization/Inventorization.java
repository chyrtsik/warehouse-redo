/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.inventorization;

import com.artigile.warehouse.dao.generic.lock.LockOwner;
import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.chargeoff.ChargeOff;
import com.artigile.warehouse.domain.postings.Posting;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.domain.warehouse.Warehouse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Shyrik, 29.09.2009
 */

/**
 * Inventorization is action of acceptance batch of detail to be counted at some places of the warehouse.
 */
@Entity
@LockOwner
public class Inventorization {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Number of the document 'Inventorization'.
     */
    @Column(nullable = false)
    private long number;

    /**
     * Type of the inventorization.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private InventorizationType type;

    /**
     * Current state of the inventorization.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private InventorizationState state;

    /**
     * Result of the inventorization.
     */
    @Enumerated(value = EnumType.STRING)
    private InventorizationResult result;

    /**
     * Date and time, when inventorization has been created.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    /**
     * User, who created the inventorization.
     */
    @ManyToOne(optional = false)
    private User createUser;

    /**
     * Date and time, when inventorization has been closed.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date closeDate;

    /**
     * User, who has closed inventorization.
     */
    @ManyToOne
    private User closeUser;

    /**
     * Warehouse, which is to be checked during the inventorization.
     */
    @ManyToOne(optional = false)
    private Warehouse warehouse;

    /**
     * Used defined notice about the inventorization.
     */
    private String notice;

    /**
     * Posting, that was created after this inventorization (if posting was needed).
     */
    @OneToOne
    private Posting posting;

    /**
     * Charge off, that was created after completion of this inventorization (if posting was needed).
     */
    @OneToOne
    private ChargeOff chargeOff;

    /**
     * Items in the posting.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inventorization")
    private List<InventorizationItem> items = new ArrayList<InventorizationItem>();

    /**
     * Storage places for check (if inventorization was created for checking of given storage places).
     */
    @ManyToMany
    @JoinTable(inverseJoinColumns=@JoinColumn(name="storagePlace_id"))
    private List<StoragePlace> storagePlacesToCheck = new ArrayList<StoragePlace>();

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //=================================== Operations =================================
    /**
     * Refreshes current state of inventorization according to state of all it's items.
     * The most slowly processing ites of inventorisation will influence on the state of inventorization.
     * But poblems should be seen as soon, as possible.
     */
    public void refreshProcessingState() {
        boolean hasNotProcessedItems = false;
        boolean hasInProcessItems = false;
        boolean hasProcessedItems = false;
        boolean hasProblemItems = false;
        for (InventorizationItem item : getItems()){
            if (item.getState().equals(InventorizationItemState.NOT_PROCESSED)){
                hasNotProcessedItems = true;
            }
            else if (item.getState().equals(InventorizationItemState.IN_PROCESS)){
                hasInProcessItems = true;
            }
            else if (item.getState().equals(InventorizationItemState.PROCESSED)){
                hasProcessedItems = true;
            }

            if (item.isProblem()){
                hasProblemItems = true;
            }
        }

        if (hasNotProcessedItems || hasInProcessItems){
            setState(InventorizationState.IN_PROCESS);
            setResult(hasProblemItems ? InventorizationResult.PROBLEM : null);
        }
        else if (hasProcessedItems){
            setState(InventorizationState.PROCESSED);
            setResult(hasProblemItems ? InventorizationResult.PROBLEM : InventorizationResult.SUCCESS);
        }
    }


    //=============================== Setters and getters ============================
    public Inventorization() {
    }

    public Inventorization(long id) {
        this.id = id;
    }

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

    public InventorizationType getType() {
        return type;
    }

    public void setType(InventorizationType type) {
        this.type = type;
    }

    public InventorizationState getState() {
        return state;
    }

    public void setState(InventorizationState state) {
        this.state = state;
    }

    public InventorizationResult getResult() {
        return result;
    }

    public void setResult(InventorizationResult result) {
        this.result = result;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public User getCloseUser() {
        return closeUser;
    }

    public void setCloseUser(User closeUser) {
        this.closeUser = closeUser;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public List<StoragePlace> getStoragePlacesToCheck() {
        return storagePlacesToCheck;
    }

    public void setStoragePlacesToCheck(List<StoragePlace> storagePlacesToCheck) {
        this.storagePlacesToCheck = storagePlacesToCheck;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public List<InventorizationItem> getItems() {
        return items;
    }

    public void setItems(List<InventorizationItem> items) {
        this.items = items;
    }

    public Posting getPosting() {
        return posting;
    }

    public void setPosting(Posting posting) {
        this.posting = posting;
    }

    public ChargeOff getChargeOff() {
        return chargeOff;
    }

    public void setChargeOff(ChargeOff chargeOff) {
        this.chargeOff = chargeOff;
    }

    public long getVersion() {
        return version;
    }
}
