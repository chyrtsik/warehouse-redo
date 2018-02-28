/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.license;

import java.util.Date;

/**
 * @author Aliaksandr.Chyrtsik, 26.11.11
 */
public class LicensePeriod {
    private Date validFromDate;
    private Date validTillDate;

    public LicensePeriod(Date validFromDate, Date validTillDate) {
        this.validFromDate = validFromDate;
        this.validTillDate = validTillDate;
    }

    public Date getValidFromDate() {
        return validFromDate;
    }

    public Date getValidTillDate() {
        return validTillDate;
    }
}
