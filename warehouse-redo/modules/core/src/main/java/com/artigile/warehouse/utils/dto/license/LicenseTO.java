/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.license;

import com.artigile.warehouse.domain.admin.license.LicenseType;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

import java.util.Date;

/**
 * Information about licence data shown to the user.
 * All information is read only.
 *
 * @author Aliaksandr.Chyrtsik, 04.08.11
 */
public class LicenseTO extends EqualsByIdImpl {
    private long id;
    private Date validFromDate;
    private Date validTillDate;
    private String issuedFor;
    private String description;
    private String appliedByUser;
    private Date dateApplied;
    private LicenseType licenseType;
    private boolean validLicenseData;

    public LicenseTO() {
    }

    public void setValidLicenseData(boolean validLicenseData) {
        this.validLicenseData = validLicenseData;
    }

    public boolean isValidLicenseData() {
        return validLicenseData;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public Date getValidFromDate() {
        return validFromDate;
    }

    public void setValidFromDate(Date validFromDate) {
        this.validFromDate = validFromDate;
    }

    public Date getValidTillDate() {
        return validTillDate;
    }

    public void setValidTillDate(Date validTillDate) {
        this.validTillDate = validTillDate;
    }

    public String getIssuedFor() {
        return issuedFor;
    }

    public void setIssuedFor(String issuedFor) {
        this.issuedFor = issuedFor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAppliedByUser() {
        return appliedByUser;
    }

    public void setAppliedByUser(String appliedByUser) {
        this.appliedByUser = appliedByUser;
    }

    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }
}
