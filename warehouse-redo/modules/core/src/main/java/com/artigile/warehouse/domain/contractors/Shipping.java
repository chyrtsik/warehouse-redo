/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.contractors;

import javax.persistence.*;

/**
 * @author Valery Barysok, 9/13/11
 */

@Entity
public class Shipping {

    @Id
    @GeneratedValue
    private long id;

    private String address;

    private String courier;

    private String phone;

    private String note;

    @ManyToOne(optional = false)
    private Contractor contractor;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public Shipping() {
    }

    public Shipping(String address, String courier, String phone, String note) {
        this.address = address;
        this.courier = courier;
        this.phone = phone;
        this.note = note;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public long getVersion() {
        return version;
    }
}
