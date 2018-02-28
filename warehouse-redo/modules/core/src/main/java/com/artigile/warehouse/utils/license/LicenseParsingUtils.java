/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.license;

import com.artigile.warehouse.utils.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utility class for license data parsing.
 * Data formats of license are independent from other application data formats (for example, date time formats).
 *
 * @author Aliaksandr.Chyrtsik, 06.08.11
 */
public final class LicenseParsingUtils {
    private LicenseParsingUtils(){
    }

    /**
     * Date time formatter.
     */
    static private DateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public static Date parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isEmpty()){
            return null;
        }
        try {
            return dateTimeFormat.parse(dateTimeString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateTimeToString(Date dateTime){
        if (dateTime == null){
            return "";
        }
        return dateTimeFormat.format(dateTime);
    }

    public static String valuesListToString(List<String> values){
        return StringUtils.listToDelimitedString(values, ",");
    }

    public static List<String> parseValuesList(String valuesString){
        return StringUtils.delimitedStringToList(valuesString, ",");
    }

    public static String booleanToString(Boolean booleanValue){
        if (booleanValue == null){
            return "";
        }
        else{
            return booleanValue ? "Yes" : "No";
        }
    }

    public static Boolean parseBoolean(String booleanValueString){
        if (booleanValueString == null || booleanValueString.isEmpty()){
            return null;
        }
        else if (booleanValueString.equals("Yes")){
            return true;
        }
        else if (booleanValueString.equals("No")){
            return false;
        }
        else{
            return null;
        }
    }

    /**
     * Calculate expiration for given periods of licenses.
     * @param licensePeriods license periods (from and till date each license is valid).
     * @return license expiration date or null if expiration date cannot be calculated.
     */
    public static Date calculateExpirationDate(List<LicensePeriod> licensePeriods){
        List<LicensePeriod> sortedPeriods = new ArrayList<LicensePeriod>(licensePeriods);
        Collections.sort(sortedPeriods, new Comparator<LicensePeriod>() {
            @Override
            public int compare(LicensePeriod first, LicensePeriod second) {
                int fromDateCompareResult = compareDates(first.getValidFromDate(), second.getValidFromDate());
                return fromDateCompareResult == 0 ? compareDates(first.getValidTillDate(), second.getValidTillDate()) : fromDateCompareResult;
            }

            private int compareDates(Date first, Date second) {
                if (first == null && second == null){
                    return 0;
                }
                else if (first == null && second != null){
                    return -1;
                }
                else if (first != null && second == null){
                    return 1;
                }
                else{
                    return first.compareTo(second);
                }
            }
        });

        Date currentDate = Calendar.getInstance().getTime();
        Date expirationDate = null;
        for (LicensePeriod licensePeriod : sortedPeriods){
            if (licensePeriod.getValidTillDate() == null){
                //Unlimited period on license (license without expiration).
                return null;
            }
            else if (expirationDate == null){
                expirationDate = licensePeriod.getValidTillDate();
            }
            else{
                if (expirationDate.compareTo(licensePeriod.getValidTillDate()) < 0 &&
                    (expirationDate.compareTo(licensePeriod.getValidFromDate()) >= 0 || currentDate.compareTo(licensePeriod.getValidFromDate()) >= 0))
                {
                    expirationDate = licensePeriod.getValidTillDate();
                }
            }
        }
        return expirationDate;
    }
}
