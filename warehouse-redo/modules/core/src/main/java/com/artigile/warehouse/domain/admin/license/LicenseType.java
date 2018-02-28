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

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * All supported types of license.
 *
 * @author Aliaksandr.Chyrtsik, 04.08.11
 */
public enum LicenseType {
    /**
     * The main license required for application to be used by the end user.
     * No other licenses are valid until user hasn't this license.
     */
    APPLICATION_USAGE_LICENSE(I18nSupport.message("license.type.applicationUsageLicense")),;

    /**
     * Display name of license type.
     */
    private String name;

    private LicenseType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
