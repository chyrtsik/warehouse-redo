/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.license;

/**
 * Result of licence checking.
 *
 * @author Aliaksandr.Chyrtsik, 17.07.11
 */
public interface LicenseCheckResult {

    public boolean isValid();

    public Integer getDaysToExpire();

    public String getDescription();
}
