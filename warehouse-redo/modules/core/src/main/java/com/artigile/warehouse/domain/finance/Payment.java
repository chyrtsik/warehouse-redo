/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.finance;

import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.contractors.Contractor;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNote;
import com.artigile.warehouse.utils.ModelFieldsLengths;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Shyrik, 13.03.2010
 */

/**
 * Information about single payment.
 */
@Entity
public class Payment {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Date and time of the payment.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;

    /**
     * User, who accepted the payment (took money from contractor).
     */
    @ManyToOne(optional = false)
    private User acceptedUser;

    /**
     * Contractor, who has made this payment.
     */
    @ManyToOne(optional = false)
    private Contractor contractor;

    /**
     * Total sum of the payment.
     */
    @Column(precision = ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION,
            scale = ModelFieldsLengths.MAX_LENGTH_DOUBLE_SCALE,
            nullable = false)
    private BigDecimal paymentSum;

    /**
     * Currency, in what payment has been made.
     */
    @ManyToOne(optional = false)
    private Currency paymentCurrency;

    /**
     * Additional textual information about payment.
     */
    private String notice;

    /**
     * If payment has a resulting account operation (for example withdraw from contractor's balance),
     * then this field holds this operation's details.
     */
    @ManyToOne
    private AccountOperation accountOperation;

    /**
     * List of delivery notes, that ware payed for with this payment.
     */
    @OneToMany
    @JoinColumn(name = "payment_id")
    private List<DeliveryNote> deliveryNotes = new ArrayList<DeliveryNote>();

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public User getAcceptedUser() {
        return acceptedUser;
    }

    public void setAcceptedUser(User acceptedUser) {
        this.acceptedUser = acceptedUser;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public BigDecimal getPaymentSum() {
        return paymentSum;
    }

    public void setPaymentSum(BigDecimal paymentSum) {
        this.paymentSum = paymentSum;
    }

    public Currency getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(Currency paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public AccountOperation getAccountOperation() {
        return accountOperation;
    }

    public void setAccountOperation(AccountOperation accountOperation) {
        this.accountOperation = accountOperation;
    }

    public List<DeliveryNote> getDeliveryNotes() {
        return deliveryNotes;
    }

    public void setDeliveryNotes(List<DeliveryNote> deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }

    public long getVersion() {
        return version;
    }
}
