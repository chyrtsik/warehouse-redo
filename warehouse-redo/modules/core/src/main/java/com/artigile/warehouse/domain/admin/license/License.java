/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.admin.license;

import com.artigile.warehouse.domain.admin.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Holds information about one license of this application.
 * @author Aliaksandr.Chyrtsik, 03.08.11
 */
@Entity
public class License {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Date and time when license has been applied (added to the list of licenses).
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, insertable = false)
    private Date dateApplied;

    /**
     * Use who have applied this license.
     */
    @ManyToOne(optional = false)
    private User appliedByUser;

    /**
     * Licence data in internal format.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String licenseData;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * Parsed license data (not stored in database, for usage in application only).
     */
    @Transient
    transient private ParsedLicense parsedLicenseData;

    public License() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }

    public User getAppliedByUser() {
        return appliedByUser;
    }

    public void setAppliedByUser(User appliedByUser) {
        this.appliedByUser = appliedByUser;
    }

    public String getLicenseData() {
        return licenseData;
    }

    public void setLicenseData(String licenseData) {
        this.licenseData = licenseData;
    }

    public long getVersion() {
        return version;
    }

    public ParsedLicense getParsedLicenseData() {
        return parsedLicenseData;
    }

    public void setParsedLicenseData(ParsedLicense parsedLicenseData) {
        this.parsedLicenseData = parsedLicenseData;
    }
}
