/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.format.predefined;

import com.artigile.warehouse.gui.core.report.format.ColumnFormatFactory;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.date.DateUtils;

import java.util.Date;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class TimeColumnFormatFactory implements ColumnFormatFactory {

    /**
     * Singleton instance
     */
    private static TimeColumnFormatFactory instance;
    
    
    private TimeColumnFormatFactory() { /* Silence is gold */ }
    
    
    public static TimeColumnFormatFactory getInstance() {
        if (instance == null) {
            instance = new TimeColumnFormatFactory();
        }
        return instance;
    }
    
    @Override
    public String getFormattedValue(Object value) {
        return DateUtils.format(StringUtils.getTimeFormat(), (Date) value);
    }
}
