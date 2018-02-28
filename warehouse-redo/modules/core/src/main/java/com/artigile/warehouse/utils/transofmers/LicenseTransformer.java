/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.admin.license.License;
import com.artigile.warehouse.utils.dto.license.LicenseTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aliaksandr.Chyrtsik, 04.08.11
 */
public final class LicenseTransformer {
    private LicenseTransformer(){
    }

    public static List<LicenseTO> transformList(List<License> licenses) {
        List<LicenseTO> licenseTOs = new ArrayList<LicenseTO>();
        for (License license : licenses){
            licenseTOs.add(transform(license));
        }
        return licenseTOs;
    }

    public static LicenseTO transform(License license) {
        LicenseTO licenseTO = new LicenseTO();
        licenseTO.setId(license.getId());
        licenseTO.setAppliedByUser(license.getAppliedByUser().getDisplayName());
        licenseTO.setDateApplied(license.getDateApplied());
        if (license.getParsedLicenseData() != null){
            licenseTO.setValidLicenseData(true);
            licenseTO.setLicenseType(license.getParsedLicenseData().getLicenseType());
            licenseTO.setValidFromDate(license.getParsedLicenseData().getValidFromDate());
            licenseTO.setValidTillDate(license.getParsedLicenseData().getValidTillDate());
            licenseTO.setIssuedFor(license.getParsedLicenseData().getIssuedFor());
            licenseTO.setDescription(license.getParsedLicenseData().getDescription());
        }
        return licenseTO;
    }
}
