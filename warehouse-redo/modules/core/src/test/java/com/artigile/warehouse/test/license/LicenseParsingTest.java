/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.test.license;

import com.artigile.warehouse.utils.license.LicenseParsingUtils;
import com.artigile.warehouse.utils.license.LicensePeriod;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Aliaksandr.Chyrtsik, 26.11.11
 */
public class LicenseParsingTest {
    @Test
    public void testLicenseExpirationDateCalculation(){
        List<LicensePeriod> licensePeriods = new ArrayList<LicensePeriod>();

        Calendar currentTime = Calendar.getInstance();
        int currentYear = currentTime.get(Calendar.YEAR);
        int currentMonth = currentTime.get(Calendar.MONTH);
        int currentDay = currentTime.get(Calendar.DAY_OF_MONTH);
        Calendar currentDate = new GregorianCalendar(currentYear, currentMonth, currentDay);

        //1. Expired license.
        Calendar validFrom1 = (Calendar)currentDate.clone();
        validFrom1.add(Calendar.DAY_OF_MONTH, -60);

        Calendar validTill1 = (Calendar)currentDate.clone();
        validTill1.add(Calendar.DAY_OF_MONTH, -10);

        licensePeriods.add(new LicensePeriod(validFrom1.getTime(), validTill1.getTime()));
        Assert.assertEquals(validTill1.getTime(), LicenseParsingUtils.calculateExpirationDate(licensePeriods));

        //2. Add one valid license.
        Calendar validFrom2 = (Calendar)currentDate.clone();
        validFrom2.add(Calendar.DAY_OF_MONTH, -5);

        Calendar validTill2 = (Calendar)currentDate.clone();
        validTill2.add(Calendar.DAY_OF_MONTH, 10);

        licensePeriods.add(new LicensePeriod(validFrom2.getTime(), validTill2.getTime()));
        Assert.assertEquals(validTill2.getTime(), LicenseParsingUtils.calculateExpirationDate(licensePeriods));

        //3. Add one more valid license (with gap between current license and this new one).
        Calendar validFrom3 = (Calendar)currentDate.clone();
        validFrom3.add(Calendar.DAY_OF_MONTH, 20);

        Calendar validTill3 = (Calendar)currentDate.clone();
        validTill3.add(Calendar.DAY_OF_MONTH, 50);

        licensePeriods.add(new LicensePeriod(validFrom3.getTime(), validTill3.getTime()));
        Assert.assertEquals(validTill2.getTime(), LicenseParsingUtils.calculateExpirationDate(licensePeriods));

        //4. Add license to fill the gap (to make each license activated immediately ofter expiration of the previous one).
        Calendar validFrom4 = (Calendar)currentDate.clone();
        validFrom4.add(Calendar.DAY_OF_MONTH, 10);

        Calendar validTill4 = (Calendar)currentDate.clone();
        validTill4.add(Calendar.DAY_OF_MONTH, 20);

        licensePeriods.add(new LicensePeriod(validFrom4.getTime(), validTill4.getTime()));
        Assert.assertEquals(validTill3.getTime(), LicenseParsingUtils.calculateExpirationDate(licensePeriods));

        //5. Add one more license with another gap.
        Calendar validFrom5 = (Calendar)currentDate.clone();
        validFrom5.add(Calendar.DAY_OF_MONTH, 100);

        Calendar validTill5 = (Calendar)currentDate.clone();
        validTill5.add(Calendar.DAY_OF_MONTH, 200);

        licensePeriods.add(new LicensePeriod(validFrom5.getTime(), validTill5.getTime()));
        Assert.assertEquals(validTill3.getTime(), LicenseParsingUtils.calculateExpirationDate(licensePeriods));

        //6. Add long license to intersect the last license and previous one (gap is filled by this intersection of times).
        Calendar validFrom6 = (Calendar)currentDate.clone();
        validFrom6.add(Calendar.DAY_OF_MONTH, 5);

        Calendar validTill6 = (Calendar)currentDate.clone();
        validTill6.add(Calendar.DAY_OF_MONTH, 150);

        licensePeriods.add(new LicensePeriod(validFrom6.getTime(), validTill6.getTime()));
        Assert.assertEquals(validTill5.getTime(), LicenseParsingUtils.calculateExpirationDate(licensePeriods));

        //7. Add license without expiration date.
        licensePeriods.add(new LicensePeriod(null, null));
        Assert.assertEquals(null, LicenseParsingUtils.calculateExpirationDate(licensePeriods));
    }
}
