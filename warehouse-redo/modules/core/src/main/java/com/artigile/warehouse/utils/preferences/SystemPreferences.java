/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.preferences;

import com.artigile.warehouse.utils.StringUtils;

/**
 * All available custom options of this application.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public enum SystemPreferences {

    /**
     * Number format options.
     * To apply all these options, enough to apply only 'NUMBER_FORMAT_PATTERN' in the <code>SystemPreferencesUtils</code>
     *
     */
    NUMBER_FORMAT_PATTERN,
    NUMBER_FORMAT_DECIMAL_SEPARATOR,
    NUMBER_FORMAT_GROUP_SEPARATOR,

    /**
     * Barcode scanner settings.
     */
    BARCODE_SCANNER_ENABLED,
    BARCODE_SCANNER_PORT,
    BARCODE_SCANNER_BAUD_RATE,
    BARCODE_SCANNER_DATA_BITS,
    BARCODE_SCANNER_STOP_BITS,
    BARCODE_SCANNER_PARITY;


    /**
     * @return Option's name, that used for saving in the database
     */
    public String getOptionName() {
        return StringUtils.buildString(this.getClass().getSimpleName(), ".", this.name());
    }
}
