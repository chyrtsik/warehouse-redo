/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto;

import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

/**
 * @author Ihar, Dec 11, 2008
 */

public class ContactTO extends EqualsByIdImpl {
    private long id;

    private String fullName;

    private String appointment;

    private String phone;

    private String email;

    private String icqId;

    private String skypeId;

    private String notice;

    public ContactTO() {
    }

    public ContactTO(long id, String fullName, String appointment, String phone, String email, String icqId, String skypeId, String notice) {
        this.id = id;
        this.fullName = fullName;
        this.appointment = appointment;
        this.phone = phone;
        this.email = email;
        this.icqId = icqId;
        this.skypeId = skypeId;
        this.notice = notice;
    }

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
}
