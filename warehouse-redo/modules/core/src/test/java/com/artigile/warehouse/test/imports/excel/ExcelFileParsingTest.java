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

import com.artigile.warehouse.adapter.spi.impl.ExcelTableModel;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelReader;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelReaderFactory;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelSheetReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Tests of excel files parsing.
 *
 * @author Aliaksandr.Chyrtsik, 16.08.11
 */
public class ExcelFileParsingTest {

    private ExcelReader readExcelFileFromResource(String resourceName) {
        //Loads excel file content from the resources.
        File resourceFile;
        try {
            resourceFile = new File(getClass().getResource("/imports/excel/" + resourceName).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return ExcelReaderFactory.create(resourceFile.getAbsolutePath());
    }

    @Test
    public void testPrice1_Xls(){
        makeAssertsForPrice1("price1.xls", false);
    }

    @Test
    public void testPrice1_Xlsx(){
        makeAssertsForPrice1("price1.xlsx", true);
    }

    private void makeAssertsForPrice1(String priceListResource, boolean excel2007) {
        ExcelReader reader = readExcelFileFromResource(priceListResource);

        Assert.assertEquals(4, reader.getSheetCount());

        Assert.assertEquals(30, reader.getSheetReader(0).getRowCount());
        Assert.assertEquals(11, reader.getSheetReader(0).getColumnCount());

        Assert.assertEquals(1, reader.getSheetReader(1).getRowCount());
        Assert.assertEquals(1, reader.getSheetReader(1).getColumnCount());

        if (excel2007){
            Assert.assertEquals(1, reader.getSheetReader(2).getRowCount());
            Assert.assertEquals(1, reader.getSheetReader(2).getColumnCount());
        }
        else{
            Assert.assertEquals(0, reader.getSheetReader(2).getRowCount());
            Assert.assertEquals(0, reader.getSheetReader(2).getColumnCount());
        }

        Assert.assertEquals(3, reader.getSheetReader(3).getRowCount());
        Assert.assertEquals(3, reader.getSheetReader(3).getColumnCount());

        //Sheet 1 data
        ExcelSheetReader sheetReader = reader.getSheetReader(0);
        ExcelTableModel tableModel = ExcelTableModel.create(sheetReader, Integer.MAX_VALUE);
        Assert.assertEquals(sheetReader.getColumnCount(), tableModel.getColumnCount());
        Assert.assertEquals(sheetReader.getRowCount(), tableModel.getRowCount());
        Assert.assertEquals("test value 0", tableModel.getValueAt(0, 0));
        Assert.assertEquals("test value 1", tableModel.getValueAt(17, 1));
        Assert.assertEquals("test value 2", tableModel.getValueAt(22, 4));
        Assert.assertEquals("test value 3", tableModel.getValueAt(24, 0));
        Assert.assertEquals("test value 4", tableModel.getValueAt(29, 6));

        //Sheet 2 data
        ExcelSheetReader sheetReader2 = reader.getSheetReader(1);
        ExcelTableModel tableModel2 = ExcelTableModel.create(sheetReader2, Integer.MAX_VALUE);
        Assert.assertEquals(sheetReader2.getColumnCount(), tableModel2.getColumnCount());
        Assert.assertEquals(sheetReader2.getRowCount(), tableModel2.getRowCount());
        Assert.assertEquals("1", tableModel2.getValueAt(0, 0));

        //Sheet 4 data
        ExcelSheetReader sheetReader4 = reader.getSheetReader(3);
        ExcelTableModel tableModel4 = ExcelTableModel.create(sheetReader4, Integer.MAX_VALUE);
        Assert.assertEquals(sheetReader4.getColumnCount(), tableModel4.getColumnCount());
        Assert.assertEquals(sheetReader4.getRowCount(), tableModel4.getRowCount());
        Assert.assertEquals("1", tableModel4.getValueAt(0, 0));
        Assert.assertEquals("string 2", tableModel4.getValueAt(1, 1));
        Assert.assertEquals("3", tableModel4.getValueAt(2, 2));
        Assert.assertEquals(null, tableModel4.getValueAt(0, 1));
    }
}
