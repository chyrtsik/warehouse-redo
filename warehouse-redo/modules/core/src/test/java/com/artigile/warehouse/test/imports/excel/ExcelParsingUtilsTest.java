/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.test.imports.excel;

import com.artigile.warehouse.adapter.spi.impl.excel.Dimension;
import com.artigile.warehouse.adapter.spi.impl.excel.xssf.XSSFUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for important excel parsing utilities.
 *
 * @author Aliaksandr.Chyrtsik, 26.08.11
 */
public class ExcelParsingUtilsTest {
    @Test
    public void testExcel2007DimensionParsing(){
        //Simple dimension.
        {
            Dimension dimension = XSSFUtils.parseDimension("A1:D10");
            Assert.assertEquals(0, dimension.getTop());
            Assert.assertEquals(9, dimension.getBottom());
            Assert.assertEquals(0, dimension.getLeft());
            Assert.assertEquals(3, dimension.getRight());
        }

        //Dimension with offset.
        {
            Dimension dimension = XSSFUtils.parseDimension("B2:D10");
            Assert.assertEquals(1, dimension.getTop());
            Assert.assertEquals(9, dimension.getBottom());
            Assert.assertEquals(1, dimension.getLeft());
            Assert.assertEquals(3, dimension.getRight());
        }

        //Large dimension.
        {
            Dimension dimension = XSSFUtils.parseDimension("A1:AC443");
            Assert.assertEquals(0, dimension.getTop());
            Assert.assertEquals(442, dimension.getBottom());
            Assert.assertEquals(0, dimension.getLeft());
            Assert.assertEquals(28, dimension.getRight());
        }

        //Large dimension with offset.
        {
            Dimension dimension = XSSFUtils.parseDimension("AA10:AC443");
            Assert.assertEquals(9, dimension.getTop());
            Assert.assertEquals(442, dimension.getBottom());
            Assert.assertEquals(26, dimension.getLeft());
            Assert.assertEquals(28, dimension.getRight());
        }
    }
}
