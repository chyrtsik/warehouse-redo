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
 * @author Shyrik, 01.12.2008
 */

/**
 * Contact represents information about update's contact person.
 */
@Entity
public class Contact {
    /**
     * Primary key.
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * Full name (First, Middle, Last names, etc.).
     */
    private String fullName;

    /**
     * Job title of this contact.
     */
    private String appointment;

    /**
     * Phone number (or numbers in one string with user defined separator).
     */
    private String phone;

    /**
     * E-mail address.
     */
    private String email;

    /**
     * Icq identifier.
     */
    private String icqId;

    /**
     * Skype identifier.
     */
    private String skypeId;

    /**
     * Notice about contact.
     */
    private String notice;

    /**
     * Contact's update (organization).
     */
    @ManyToOne(optional = false)
    private Contractor contractor;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public Contact() {
    }

    public Contact(String fullName, String appointment, String phone, String email, String icqId, String skypeId, String notice) {
        this.fullName = fullName;
        this.appointment = appointment;
        this.phone = phone;
        this.email = email;
        this.icqId = icqId;
        this.skypeId = skypeId;
        this.notice = notice;
    }

    //======================= Getters and setters =====================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcqId() {
        return icqId;
    }

    public void setIcqId(String icqId) {
        this.icqId = icqId;
    }

    public String getSkypeId() {
        return skypeId;
    }

    public void setSkypeId(String skypeId) {
        this.skypeId = skypeId;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
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
