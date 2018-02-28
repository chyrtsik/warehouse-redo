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

/**
 * All supported license fields.
 *
 * @author Aliaksandr.Chyrtsik, 04.08.11
 */
public enum LicenseField {
    /**
     * Type of a license (general license for whole application, license for some functions etc.).
     */
    LICENSE_TYPE,

    /**
     * Date and time when license become valid for usage.
     */
    LICENSE_VALID_FROM_DATE,

    /**
     * Date and time when license will expire.
     */
    LICENSE_VALID_TILL_DATE,

    /**
     * Identifier of client in Artigile contractors list.
     */
    LICENSE_OWNER_CLIENT_ID,

    /**
     * Name of client in Artigile contractors list.
     */
    LICENSE_OWNER_CLIENT_NAME,

    /*
     * Textual description of features included into license.
     */
    LICENSE_DESCRIPTION,

    /**
     * List of features enabled by this license. List of permissions limits features available to
     * user according to his license.
     */
    LICENSE_INCLUDED_PERMISSIONS,

    /**
     * If this flag is set to true then ALL permissions current and feature permissions are included into this license.
     *
     */
    LICENSE_ALL_PERMISSIONS_FLAG,

    /**
     * List of computer identifiers. Is set then application should work only on these computers.
     */
    LICENSE_HARDWARE_IDS,
}
