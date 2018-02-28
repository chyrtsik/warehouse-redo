/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.date;

import com.artigile.warehouse.utils.logging.LoggingFacade;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Calendar utility methods.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public abstract class DateUtils {

    /**
     * @return Current datetime
     */
    public static Date now() {
        return Calendar.getInstance().getTime();
    }

    public static long getDateDifferenceLength(Date dateStart, Date dateEnd, int field) {
        Calendar calendar = Calendar.getInstance();
        long timeStart = calendar.getTimeInMillis();
        calendar.add(field, 1);
        long singlePeriodLength = calendar.getTimeInMillis() - timeStart;

        calendar.setTime(dateStart);
        long dateStartInMilliseconds = calendar.getTimeInMillis();
        calendar.setTime(dateEnd);
        long dateEndInMilliseconds = calendar.getTimeInMillis();
        return (dateEndInMilliseconds - dateStartInMilliseconds) / singlePeriodLength;
    }

    /**
     * Moves datetime on the given value by the given calendar field.
     *
     * @param datetime Initial datetime (for moving)
     * @param calendarField One of the calendar fields
     * @param moveValue Moving value (may be positive (move forward) and negative (move back))
     * @return Datetime after moving.
     */
    public static Date moveDatetime(Date datetime, int calendarField, int moveValue) {
        Assert.notNull(datetime);
        Calendar calendar = getCalendarInstance(datetime);
        setCalendarParameter(calendar, calendarField, getCalendarParameter(calendar, calendarField) + moveValue);
        return calendar.getTime();
    }

    /**
     * @param check Datetime for checking
     * @param from Start datetime of the checking interval
     * @param to End datetime of the checking interval
     * @param includingBoundaries Include interval boundaries in check
     * @return True - if datetime for checking between start and end dates, false - if not between.
     */
    public static boolean isBetweenDates(Date check, Date from, Date to, boolean includingBoundaries) {
        Assert.notNull(check);
        Assert.notNull(from);
        Assert.notNull(to);
        if (from.before(to) || (from.equals(to) && includingBoundaries)) {
            if ((check.after(from) && check.before(to))
                    || (check.equals(from) && includingBoundaries)
                    || (check.equals(to) && includingBoundaries)) {
                return true;
            }
        }
        return false;
    }

    public static String format(DateFormat formatter, Date date) {
        Assert.notNull(formatter);
        if (date != null) {
            return formatter.format(date);
        }
        return null;
    }

    public static Date parse(DateFormat formatter, String datetime) {
        Assert.notNull(formatter);
        if (datetime != null) {
            try {
                return formatter.parse(datetime);
            } catch (ParseException e) {
                LoggingFacade.logError(e);
            }
        }
        return null;
    }

    /**
     * Set value of the calendar field.
     *
     * @param calendar Calendar instance
     * @param calendarField One of the fields of the calendar
     * @param fieldValue New value of the calendar field
     */
    public static void setCalendarParameter(Calendar calendar, int calendarField, int fieldValue) {
        Assert.notNull(calendar);
        try {
            calendar.set(calendarField, fieldValue);
        } catch (ArrayIndexOutOfBoundsException e) {
            LoggingFacade.logError(e);
        }
    }

    /**
     * @param calendar Calendar instance
     * @param calendarField One of the fields of the calendar
     * @return Value of the calendar field
     */
    public static int getCalendarParameter(Calendar calendar, int calendarField) {
        Assert.notNull(calendar);
        try {
            return calendar.get(calendarField);
        } catch (ArrayIndexOutOfBoundsException e) {
            LoggingFacade.logError(e);
            return -1;
        }
    }

   /**
     * @param datetime Some datetime
     * @return Calendar instance for the given datetime
     */
    public static Calendar getCalendarInstance(Date datetime) {
        Assert.notNull(datetime);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(datetime);
        return calendar;
    }

    /**
     * Calculate the first day of month specified.
     * @param date date to calculate.
     * @return the 1st day of given month.
     */
    public static Date getBeginningOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    public static Date getDayBegin(Date date) {
        Calendar cal = getCalendarInstance(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date addDays(Date date, int daysToAdd) {
        Calendar cal = getCalendarInstance(date);
        cal.add(Calendar.DAY_OF_YEAR, daysToAdd);
        return cal.getTime();
    }
}
