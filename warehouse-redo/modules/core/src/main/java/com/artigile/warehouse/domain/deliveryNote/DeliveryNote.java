/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.deliveryNote;

import com.artigile.warehouse.domain.chargeoff.ChargeOff;
import com.artigile.warehouse.domain.contractors.Contractor;
import com.artigile.warehouse.domain.finance.Currency;
import com.artigile.warehouse.domain.postings.Posting;
import com.artigile.warehouse.domain.warehouse.Warehouse;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Shyrik, 01.11.2009
 */

/**
 * Delivery note -- is a document that describes ware moving between warehouses of between warehouse and
 * customers of supplier.
 */
@Entity
public class DeliveryNote {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Current state of the delivery note document.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private DeliveryNoteState state;

    /**
     * Number of the delivery note document.
     */
    @Column(nullable = false, unique = true)
    private long number;

    /**
     * Charge off, according to which this delivery note was created.
     * Used, if delivery note comes from one of the enterprise's warehouses.
     */
    @ManyToOne(optional = false)
    private ChargeOff chargeOff;

    /**
     * If this delivery note is used when wares are shipped to the one of the enterprise's warehouses,
     * this field represents destination warehouse of the delivery note.
     */
    @ManyToOne
    private Warehouse destinationWarehouse;

    /**
     * This fields is used, when delivery note is shows wares shipped to the contractor (customer).
     */
    @ManyToOne
    private Contractor contractor;

    /**
     * If delivery note has peen processed (or is being processed) at the destination warehouse,
     * this field holds posting, created for delivery note at the destination warehouse.
     */
    @OneToOne(mappedBy = "deliveryNote")
    private Posting posting;

    /**
     * Currency, in which price of delivery note is calculated.
     */
    @ManyToOne(optional = false)
    private Currency currency;

    /**
     * VAT rate (value added tax) used in order. In percents.
     */
    private BigDecimal vatRate;

    /**
     * Custom notice text about delivery note.
     */
    private String notice;

    /**
     * Total price of delivery note.
     */
    @Formula(value = "(select sum(IFNULL(di.price, 0)*IFNULL(di.amount, 0)) from DeliveryNoteItem di where di.deliveryNote_id=id)")
    private BigDecimal totalPrice;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * Items of a delivery note.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deliveryNote")
    private List<DeliveryNoteItem> items = new ArrayList<DeliveryNoteItem>();

    @Column(length = 32)
    private String blankNumber;

    @Column(name = "brand")
    private String brand;

    @Column(name = "state_number")
    private String stateNumber;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "owner")
    private String owner;

    @Column(name = "trailer")
    private String trailer;

    //===================================== Calculated getters ==========================
    @Transient
    private DeliveryNoteDocumentDesc documentDesc;

    private DeliveryNoteDocumentDesc getDocumentDesc() {
        if (documentDesc == null){
            synchronized (this){
                if (documentDesc == null){
                    documentDesc = DeliveryNoteDocumentDesc.getInstance(this);
                }
            }
        }
        return documentDesc;
    }

    public Date getDocumentDate() {
        return getDocumentDesc().getDate(this);
    }

    public String getDocumentName() {
        return getDocumentDesc().getName(this);
    }

    public long getDocumentNumber() {
        return getDocumentDesc().getNumber(this);
    }

    //====================================== Getters and setters ===========================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DeliveryNoteState getState() {
        return state;
    }

    public void setState(DeliveryNoteState state) {
        this.state = state;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public ChargeOff getChargeOff() {
        return chargeOff;
    }

    public void setChargeOff(ChargeOff chargeOff) {
        this.chargeOff = chargeOff;
    }

    public Warehouse getDestinationWarehouse() {
        return destinationWarehouse;
    }

    public void setDestinationWarehouse(Warehouse destinationWarehouse) {
        this.destinationWarehouse = destinationWarehouse;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public Posting getPosting() {
        return posting;
    }

    public void setPosting(Posting posting) {
        this.posting = posting;
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

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public List<DeliveryNoteItem> getItems() {
        return items;
    }

    public void setItems(List<DeliveryNoteItem> items) {
        this.items = items;
        for (DeliveryNoteItem item : items){
            item.setDeliveryNote(this);
        }
    }

    public String getBlankNumber() {
        return blankNumber;
    }

    public void setBlankNumber(String blankNumber) {
        this.blankNumber = blankNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStateNumber() {
        return stateNumber;
    }

    public void setStateNumber(String stateNumber) {
        this.stateNumber = stateNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public long getVersion() {
        return version;
    }
}
