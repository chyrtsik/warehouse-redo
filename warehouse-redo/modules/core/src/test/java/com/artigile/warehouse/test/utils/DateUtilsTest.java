/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.test.utils;

import com.artigile.warehouse.utils.date.DateUtils;
import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class DateUtilsTest extends TestCase {

    private static SimpleDateFormat standardDatetimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");


    public void testMoveDatetime() {
        Date testDatetime = DateUtils.now();

        Date nextDayDatetime = DateUtils.moveDatetime(testDatetime, Calendar.DATE, 1);
        assertNotNull(nextDayDatetime);
        assertTrue(nextDayDatetime.after(testDatetime));

        Date nextMonthDatetime = DateUtils.moveDatetime(testDatetime, Calendar.MONTH, 1);
        assertNotNull(nextMonthDatetime);
        assertTrue(nextMonthDatetime.after(testDatetime));

        Date previousYearDatetime = DateUtils.moveDatetime(testDatetime, Calendar.YEAR, -1);
        assertNotNull(previousYearDatetime);
        assertTrue(previousYearDatetime.before(testDatetime));
    }

    public void testIsBetweenDates() {
        Date check = DateUtils.parse(standardDatetimeFormat, "10.11.2011 14:12:32.332");
        Date from = DateUtils.parse(standardDatetimeFormat, "02.11.2011 14:17:44.332");
        Date to = DateUtils.parse(standardDatetimeFormat, "26.11.2011 14:12:32.332");

        assertTrue(DateUtils.isBetweenDates(check, from, to, false));
        assertTrue(DateUtils.isBetweenDates(check, from, to, true));
        check = DateUtils.parse(standardDatetimeFormat, "27.11.2011 14:12:32.332");
        assertFalse(DateUtils.isBetweenDates(check, from, to, false));

        check = DateUtils.parse(standardDatetimeFormat, "10.11.2011 14:12:32.332");
        to =  DateUtils.parse(standardDatetimeFormat, "01.11.2011 14:12:32.332");
        assertFalse(DateUtils.isBetweenDates(check, from, to, false));

        to = check;
        assertFalse(DateUtils.isBetweenDates(check, from, to, false));
        assertTrue(DateUtils.isBetweenDates(check, from, to, true));

        to = DateUtils.parse(standardDatetimeFormat, "26.11.2011 14:12:32.332");
        from = check;
        assertFalse(DateUtils.isBetweenDates(check, from, to, false));
        assertTrue(DateUtils.isBetweenDates(check, from, to, true));

        to = from;
        assertFalse(DateUtils.isBetweenDates(check, from, to, false));
        assertTrue(DateUtils.isBetweenDates(check, from, to, true));
    }
}
