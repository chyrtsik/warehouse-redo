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

import com.artigile.warehouse.utils.formatter.FormatUtils;
import junit.framework.TestCase;

import java.text.DecimalFormat;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class FormatUtilsTest extends TestCase {
    
    public void testPatternValidation() {
        String pattern = "#.####";
        assertTrue(FormatUtils.isValidPattern(pattern));
        pattern = ",###";
        assertTrue(FormatUtils.isValidPattern(pattern));
        pattern = "#.###;;";
        assertFalse(FormatUtils.isValidPattern(pattern));
    }

    public void testNumberFormatting() {
        int i = 123456789;
        assertEquals("123 456 789", FormatUtils.format(i, "###,###", null, ' '));
        assertEquals("1 23 45 67 89", FormatUtils.format(i, "##,##", null, ' '));
        assertEquals("123_456_789", FormatUtils.format(i, ",###", null, '_'));

        double d = 2323.00023;
        assertEquals("2 323,00023", FormatUtils.format(d, "###,###.#####", ',', ' '));
        assertEquals("23 23", FormatUtils.format(d, "##,##.###", ',', ' '));
        assertEquals("2 323,0002", FormatUtils.format(d, "###,###.####", ',', ' '));
    }
    
    public void testNumberParsing() {
        DecimalFormat format = FormatUtils.getDecimalFormatInstance(",###.###", ',', ' ');
        String number = "123 123,334";
        assertEquals(123123.334, FormatUtils.parse(number, format));
        number = "123123,334";
        assertEquals(123123.334, FormatUtils.parse(number, format));
        number = "123     123,334";
        assertEquals(123123.334, FormatUtils.parse(number, format));
        number = "aa123 123,334";
        assertEquals(123123.334, FormatUtils.parse(number, format));
        number = "n -123 123.334";
        assertEquals(-123123.334, FormatUtils.parse(number, format));
    }
}
