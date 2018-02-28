/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils;

/**
 * @author Valery Barysok, 13.01.2010
 */
public class EnumConst {
    /**
     * TINYBLOB, TINYTEXT L + 1 bytes, where L < 2^8
     */
    public static final int TINY_MAX_LENGTH = 255;

    /**
     * BLOB, TEXT L + 2 bytes, where L < 2^16
     */
    public static final int TEXT_MAX_LENGTH = 65535;

    /**
     * MEDIUMBLOB, MEDIUMTEXT L + 3 bytes, where L < 2^24
     */
    public static final int MEDIUM_MAX_LENGTH = 16777215;

    /**
     * LONGBLOB, LONGTEXT L + 4 bytes, where L < 2^32
     */
    public static final long LONG_MAX_LENGTH = 4294967295L;
}
