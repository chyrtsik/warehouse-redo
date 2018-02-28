/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.test.imports.excel;

import com.artigile.warehouse.adapter.spi.impl.DomainColumn;
import com.artigile.warehouse.adapter.spi.impl.configuration.ColumnRelationship;
import com.artigile.warehouse.adapter.spi.impl.configuration.ExcelDataAdapterConfigurationData;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test of excel configuration parsing.
 *
 * @author Aliaksandr.Chyrtsik, 17.08.11
 */
public class ExcelImportConfigurationParsingTest {
    private static final Map<String, DomainColumn> domainColumns;

    static{
        domainColumns = new HashMap<String, DomainColumn>();
        domainColumns.put("NAME", new DomainColumn("NAME", "name", true, true, " "));
        domainColumns.put("DESCRIPTION", new DomainColumn("DESCRIPTION", "description", false, true, " "));
        domainColumns.put("YEAR", new DomainColumn("YEAR", "year", false));
        domainColumns.put("QUANTITY", new DomainColumn("QUANTITY", "quantity", true));
        domainColumns.put("WHOLESALE_PRICE", new DomainColumn("WHOLESALE_PRICE", "wholesale.price", false));
        domainColumns.put("RETAIL_PRICE", new DomainColumn("RETAIL_PRICE", "retail.price", false));
        domainColumns.put("PACK", new DomainColumn("PACK", "pack", false));
        //=========== News fields (custom fields) allowed from version 3 ===================
        domainColumns.put("CUSTOM_FIELD_1", new DomainColumn("CUSTOM_FIELD_1", "custom.field.1", false));
        domainColumns.put("CUSTOM_FIELD_2", new DomainColumn("CUSTOM_FIELD_2", "custom.field.2", true));
        domainColumns.put("CUSTOM_FIELD_3", new DomainColumn("CUSTOM_FIELD_3", "custom.field.3", false, true, " "));
    }

    @Test
    public void testConfigurationParsingVer1(){
        //Test that ensures that old configuration is parsed with the current parser.
        final String config = "{StoredFile:2};0=NOT_DEFINED;1=NOT_DEFINED;2=NAME;3=DESCRIPTION;4=YEAR;5=DESCRIPTION;6=RETAIL_PRICE;7=NOT_DEFINED;8=WHOLESALE_PRICE;9=QUANTITY;";
        ExcelDataAdapterConfigurationData parsedConfig = ExcelDataAdapterConfigurationData.parse(config);

        Assert.assertEquals("{StoredFile:2}", parsedConfig.getFileStreamId());
        Assert.assertEquals(1, parsedConfig.getSheetsColumnsConfig().size());

        String sheetName1 = "";
        List<ColumnRelationship> sheetColumns = parsedConfig.getSheetsColumnsConfig().get(sheetName1);
        Assert.assertEquals(10, sheetColumns.size());

        Assert.assertEquals(0, sheetColumns.get(0).getColumnIndex());
        Assert.assertEquals(DomainColumn.NOT_DEFINED, sheetColumns.get(0).getDomainColumn());

        Assert.assertEquals(1, sheetColumns.get(1).getColumnIndex());
        Assert.assertEquals(DomainColumn.NOT_DEFINED, sheetColumns.get(1).getDomainColumn());

        Assert.assertEquals(2, sheetColumns.get(2).getColumnIndex());
        Assert.assertEquals(domainColumns.get("NAME"), sheetColumns.get(2).getDomainColumn());

        Assert.assertEquals(3, sheetColumns.get(3).getColumnIndex());
        Assert.assertEquals(domainColumns.get("DESCRIPTION"), sheetColumns.get(3).getDomainColumn());

        Assert.assertEquals(4, sheetColumns.get(4).getColumnIndex());
        Assert.assertEquals(domainColumns.get("YEAR"), sheetColumns.get(4).getDomainColumn());

        Assert.assertEquals(5, sheetColumns.get(5).getColumnIndex());
        Assert.assertEquals(domainColumns.get("DESCRIPTION"), sheetColumns.get(5).getDomainColumn());

        Assert.assertEquals(6, sheetColumns.get(6).getColumnIndex());
        Assert.assertEquals(domainColumns.get("RETAIL_PRICE"), sheetColumns.get(6).getDomainColumn());

        Assert.assertEquals(7, sheetColumns.get(7).getColumnIndex());
        Assert.assertEquals(DomainColumn.NOT_DEFINED, sheetColumns.get(7).getDomainColumn());

        Assert.assertEquals(8, sheetColumns.get(8).getColumnIndex());
        Assert.assertEquals(domainColumns.get("WHOLESALE_PRICE"), sheetColumns.get(8).getDomainColumn());

        Assert.assertEquals(9, sheetColumns.get(9).getColumnIndex());
        Assert.assertEquals(domainColumns.get("QUANTITY"), sheetColumns.get(9).getDomainColumn());
    }

    @Test
    public void testConfigurationParsingVer2(){
        //Test that ensures that old configuration is parsed with the current parser.
        String configurationString = "Version=2;File_Stream_123;Sheet0=1;0=NOT_DEFINED,1=NAME,2=DESCRIPTION,3=YEAR,4=QUANTITY,5=WHOLESALE_PRICE,6=RETAIL_PRICE,7=PACK,;Sheet1=2;;Sheet2=3;0=NAME,1=DESCRIPTION,2=QUANTITY,;";
        ExcelDataAdapterConfigurationData parsedConfiguration = ExcelDataAdapterConfigurationData.parse(configurationString);

        //Testing that configuration is restored properly.
        Assert.assertEquals("File_Stream_123", parsedConfiguration.getFileStreamId());
        Assert.assertEquals(3, parsedConfiguration.getSheetsColumnsConfig().size());

        //sheet 1
        Assert.assertEquals(8, parsedConfiguration.getSheetsColumnsConfig().get("1").size());
        Assert.assertEquals(DomainColumn.NOT_DEFINED, parsedConfiguration.getSheetsColumnsConfig().get("1").get(0).getDomainColumn());
        Assert.assertEquals(0, parsedConfiguration.getSheetsColumnsConfig().get("1").get(0).getColumnIndex());
        Assert.assertEquals(domainColumns.get("NAME"), parsedConfiguration.getSheetsColumnsConfig().get("1").get(1).getDomainColumn());
        Assert.assertEquals(1, parsedConfiguration.getSheetsColumnsConfig().get("1").get(1).getColumnIndex());
        Assert.assertEquals(domainColumns.get("DESCRIPTION"), parsedConfiguration.getSheetsColumnsConfig().get("1").get(2).getDomainColumn());
        Assert.assertEquals(2, parsedConfiguration.getSheetsColumnsConfig().get("1").get(2).getColumnIndex());
        Assert.assertEquals(domainColumns.get("YEAR"), parsedConfiguration.getSheetsColumnsConfig().get("1").get(3).getDomainColumn());
        Assert.assertEquals(3, parsedConfiguration.getSheetsColumnsConfig().get("1").get(3).getColumnIndex());
        Assert.assertEquals(domainColumns.get("QUANTITY"), parsedConfiguration.getSheetsColumnsConfig().get("1").get(4).getDomainColumn());
        Assert.assertEquals(4, parsedConfiguration.getSheetsColumnsConfig().get("1").get(4).getColumnIndex());
        Assert.assertEquals(domainColumns.get("WHOLESALE_PRICE"), parsedConfiguration.getSheetsColumnsConfig().get("1").get(5).getDomainColumn());
        Assert.assertEquals(5, parsedConfiguration.getSheetsColumnsConfig().get("1").get(5).getColumnIndex());
        Assert.assertEquals(domainColumns.get("RETAIL_PRICE"), parsedConfiguration.getSheetsColumnsConfig().get("1").get(6).getDomainColumn());
        Assert.assertEquals(6, parsedConfiguration.getSheetsColumnsConfig().get("1").get(6).getColumnIndex());
        Assert.assertEquals(domainColumns.get("PACK"), parsedConfiguration.getSheetsColumnsConfig().get("1").get(7).getDomainColumn());
        Assert.assertEquals(7, parsedConfiguration.getSheetsColumnsConfig().get("1").get(7).getColumnIndex());

        //sheet 2 -- empty
        Assert.assertEquals(0, parsedConfiguration.getSheetsColumnsConfig().get("2").size());

        //sheet 3
        Assert.assertEquals(3, parsedConfiguration.getSheetsColumnsConfig().get("3").size());
        Assert.assertEquals(domainColumns.get("NAME"), parsedConfiguration.getSheetsColumnsConfig().get("3").get(0).getDomainColumn());
        Assert.assertEquals(0, parsedConfiguration.getSheetsColumnsConfig().get("3").get(0).getColumnIndex());
        Assert.assertEquals(domainColumns.get("DESCRIPTION"), parsedConfiguration.getSheetsColumnsConfig().get("3").get(1).getDomainColumn());
        Assert.assertEquals(1, parsedConfiguration.getSheetsColumnsConfig().get("3").get(1).getColumnIndex());
        Assert.assertEquals(domainColumns.get("QUANTITY"), parsedConfiguration.getSheetsColumnsConfig().get("3").get(2).getDomainColumn());
        Assert.assertEquals(2, parsedConfiguration.getSheetsColumnsConfig().get("3").get(2).getColumnIndex());
    }

    @Test
    public void testConfigurationParsingVer3(){
        //Writes and reads new configuration in version = 3 format.

        //1. Generate new columns configuration.
        ExcelDataAdapterConfigurationData configuration = new ExcelDataAdapterConfigurationData();
        configuration.setFileStreamId("File_Stream_123");

        //sheet 1
        String sheetName1 = "1";
        configuration.getSheetsColumnsConfig().put(sheetName1, new ArrayList<ColumnRelationship>());
        List<ColumnRelationship> relationships = configuration.getSheetsColumnsConfig().get(sheetName1);
        relationships.add(new ColumnRelationship(DomainColumn.NOT_DEFINED, 0));
        relationships.add(new ColumnRelationship(domainColumns.get("NAME"), 1));
        relationships.add(new ColumnRelationship(domainColumns.get("DESCRIPTION"), 2));
        relationships.add(new ColumnRelationship(domainColumns.get("YEAR"), 3));
        relationships.add(new ColumnRelationship(domainColumns.get("QUANTITY"), 4));
        relationships.add(new ColumnRelationship(domainColumns.get("WHOLESALE_PRICE"), 5));
        relationships.add(new ColumnRelationship(domainColumns.get("RETAIL_PRICE"), 6));
        relationships.add(new ColumnRelationship(domainColumns.get("PACK"), 7));
        relationships.add(new ColumnRelationship(domainColumns.get("CUSTOM_FIELD_1"), 8));
        relationships.add(new ColumnRelationship(domainColumns.get("CUSTOM_FIELD_2"), 9));
        relationships.add(new ColumnRelationship(domainColumns.get("CUSTOM_FIELD_3"), 10));
        relationships.add(new ColumnRelationship(domainColumns.get("CUSTOM_FIELD_3"), 11));

        //sheet 2 -- empty
        String sheetName2 = "2";
        configuration.getSheetsColumnsConfig().put(sheetName2, new ArrayList<ColumnRelationship>());

        //sheet 3
        String sheetName3 = "3";
        configuration.getSheetsColumnsConfig().put(sheetName3, new ArrayList<ColumnRelationship>());
        List<ColumnRelationship> relationships3 = configuration.getSheetsColumnsConfig().get(sheetName3);
        relationships3.add(new ColumnRelationship(domainColumns.get("NAME"), 0));
        relationships3.add(new ColumnRelationship(domainColumns.get("DESCRIPTION"), 1));
        relationships3.add(new ColumnRelationship(domainColumns.get("QUANTITY"), 2));

        //2. Saving column configuration to string.
        String configurationString = configuration.toString();

        //3. Restoring configuration from string.
        ExcelDataAdapterConfigurationData parsedConfiguration = ExcelDataAdapterConfigurationData.parse(configurationString);

        //4. Testing that configuration is restored properly.
        Assert.assertEquals("File_Stream_123", parsedConfiguration.getFileStreamId());
        Assert.assertEquals(3, parsedConfiguration.getSheetsColumnsConfig().size());

        //sheet 1
        Assert.assertEquals(12, parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).size());
        Assert.assertEquals(DomainColumn.NOT_DEFINED, parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(0).getDomainColumn());
        Assert.assertEquals(0, parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(0).getColumnIndex());
        Assert.assertEquals(domainColumns.get("NAME"), parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(1).getDomainColumn());
        Assert.assertEquals(1, parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(1).getColumnIndex());
        Assert.assertEquals(domainColumns.get("DESCRIPTION"), parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(2).getDomainColumn());
        Assert.assertEquals(2, parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(2).getColumnIndex());
        Assert.assertEquals(domainColumns.get("YEAR"), parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(3).getDomainColumn());
        Assert.assertEquals(3, parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(3).getColumnIndex());
        Assert.assertEquals(domainColumns.get("QUANTITY"), parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(4).getDomainColumn());
        Assert.assertEquals(4, parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(4).getColumnIndex());
        Assert.assertEquals(domainColumns.get("WHOLESALE_PRICE"), parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(5).getDomainColumn());
        Assert.assertEquals(5, parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(5).getColumnIndex());
        Assert.assertEquals(domainColumns.get("RETAIL_PRICE"), parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(6).getDomainColumn());
        Assert.assertEquals(6, parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(6).getColumnIndex());
        Assert.assertEquals(domainColumns.get("PACK"), parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(7).getDomainColumn());
        Assert.assertEquals(7, parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(7).getColumnIndex());

        Assert.assertEquals(domainColumns.get("CUSTOM_FIELD_1"), parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(8).getDomainColumn());
        Assert.assertEquals(8, parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(8).getColumnIndex());
        Assert.assertEquals(domainColumns.get("CUSTOM_FIELD_2"), parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(9).getDomainColumn());
        Assert.assertEquals(9, parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(9).getColumnIndex());
        Assert.assertEquals(domainColumns.get("CUSTOM_FIELD_3"), parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(10).getDomainColumn());
        Assert.assertEquals(10, parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(10).getColumnIndex());
        Assert.assertEquals(domainColumns.get("CUSTOM_FIELD_3"), parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(11).getDomainColumn());
        Assert.assertEquals(11, parsedConfiguration.getSheetsColumnsConfig().get(sheetName1).get(11).getColumnIndex());

        //sheet 2 -- empty
        Assert.assertEquals(0, parsedConfiguration.getSheetsColumnsConfig().get(sheetName2).size());

        //sheet 3
        Assert.assertEquals(3, parsedConfiguration.getSheetsColumnsConfig().get(sheetName3).size());
        Assert.assertEquals(domainColumns.get("NAME"), parsedConfiguration.getSheetsColumnsConfig().get(sheetName3).get(0).getDomainColumn());
        Assert.assertEquals(0, parsedConfiguration.getSheetsColumnsConfig().get(sheetName3).get(0).getColumnIndex());
        Assert.assertEquals(domainColumns.get("DESCRIPTION"), parsedConfiguration.getSheetsColumnsConfig().get(sheetName3).get(1).getDomainColumn());
        Assert.assertEquals(1, parsedConfiguration.getSheetsColumnsConfig().get(sheetName3).get(1).getColumnIndex());
        Assert.assertEquals(domainColumns.get("QUANTITY"), parsedConfiguration.getSheetsColumnsConfig().get(sheetName3).get(2).getDomainColumn());
        Assert.assertEquals(2, parsedConfiguration.getSheetsColumnsConfig().get(sheetName3).get(2).getColumnIndex());
    }

}
