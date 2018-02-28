/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.test.utils;

import com.artigile.warehouse.utils.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for text utilities.
 * @author Aliaksandr.Chyrtsik, 01.10.11
 */
public class StringUtilsTest {

    @Test
    public void testEAN13BarCodeGeneration() {
        //Test of EAN13 bar codes generation (this is a bar code with check sum at the end).
        Assert.assertEquals("1000000000009", StringUtils.generateBarCode("1000000", 0, 5, true));
        Assert.assertEquals("1000000000016", StringUtils.generateBarCode("1000000", 1, 5, true));
        Assert.assertEquals("1000000000023", StringUtils.generateBarCode("1000000", 2, 5, true));
        Assert.assertEquals("1000000000030", StringUtils.generateBarCode("1000000", 3, 5, true));
        Assert.assertEquals("1000000000047", StringUtils.generateBarCode("1000000", 4, 5, true));
        Assert.assertEquals("1000000000054", StringUtils.generateBarCode("1000000", 5, 5, true));
        Assert.assertEquals("1000000000061", StringUtils.generateBarCode("1000000", 6, 5, true));
        Assert.assertEquals("1000000000078", StringUtils.generateBarCode("1000000", 7, 5, true));
        Assert.assertEquals("1000000000085", StringUtils.generateBarCode("1000000", 8, 5, true));
        Assert.assertEquals("1000000000092", StringUtils.generateBarCode("1000000", 9, 5, true));
        Assert.assertEquals("1000000000108", StringUtils.generateBarCode("1000000", 10, 5, true));
        Assert.assertEquals("3838957873913", StringUtils.generateBarCode("3838957", 87391, 5, true));
        Assert.assertEquals("4607156834617", StringUtils.generateBarCode("4607156", 83461, 5, true));
        Assert.assertEquals("3474374500010", StringUtils.generateBarCode("34743745", 1, 4, true));
        Assert.assertEquals("3474374500041", StringUtils.generateBarCode("34743745", 4, 4, true));
        Assert.assertEquals("3474374500027", StringUtils.generateBarCode("34743745", 2, 4, true));
        Assert.assertEquals("4607042439186", StringUtils.generateBarCode("46070424", 3918, 4, true));
        Assert.assertEquals("4813653002477", StringUtils.generateBarCode("4813653", 247, 5, true));
        Assert.assertEquals("4813653002309", StringUtils.generateBarCode("4813653", 230, 5, true));
        Assert.assertEquals("4810833013230", StringUtils.generateBarCode("4810833", 1323, 5, true));
        Assert.assertEquals("8697422826060", StringUtils.generateBarCode("86974228", 2606, 4, true));
        Assert.assertEquals("3800030290108", StringUtils.generateBarCode("3800030", 29010, 5, true));
        Assert.assertEquals("4605246004339", StringUtils.generateBarCode("4605246", 433, 5, true));
        Assert.assertEquals("4601674008642", StringUtils.generateBarCode("4601674", 864, 5, true));
        Assert.assertEquals("4690326037390", StringUtils.generateBarCode("4690326", 3739, 5, true));
        Assert.assertEquals("4006067799938", StringUtils.generateBarCode("4006067", 79993, 5, true));
        Assert.assertEquals("4605246007101", StringUtils.generateBarCode("4605246", 710, 5, true));
        Assert.assertEquals("4605246003523", StringUtils.generateBarCode("4605246", 352, 5, true));
        Assert.assertEquals("4008167165354", StringUtils.generateBarCode("4008167", 16535, 5, true));
        Assert.assertEquals("9002517123808", StringUtils.generateBarCode("9002517", 12380, 5, true));
        Assert.assertEquals("8000070020580", StringUtils.generateBarCode("800007", 2058, 6, true));
        Assert.assertEquals("4041485221890", StringUtils.generateBarCode("40414852", 2189, 4, true));
        Assert.assertEquals("4813653000930", StringUtils.generateBarCode("4813653", 93, 5, true));
        Assert.assertEquals("5907636708018", StringUtils.generateBarCode("59076367", 801, 4, true));
        Assert.assertEquals("4022583109303", StringUtils.generateBarCode("40225831", 930, 4, true));
        Assert.assertEquals("4606998021773", StringUtils.generateBarCode("4606998", 2177, 5, true));
        Assert.assertEquals("4030969801314", StringUtils.generateBarCode("40309698", 131, 4, true));
        Assert.assertEquals("4605565043231", StringUtils.generateBarCode("4605565", 4323, 5, true));
        Assert.assertEquals("3154148517313", StringUtils.generateBarCode("31541485", 1731, 4, true));
    }
}
