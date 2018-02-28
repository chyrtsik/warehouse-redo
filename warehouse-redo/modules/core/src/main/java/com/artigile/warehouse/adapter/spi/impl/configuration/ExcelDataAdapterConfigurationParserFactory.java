/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.configuration;

/**
 * Factory for getting excel configuration parsers.
 * SINGLETON.
 *
 * @author Aliaksandr.Chyrtsik, 18.08.11
 */
public class ExcelDataAdapterConfigurationParserFactory {

    private static ExcelDataAdapterConfigurationParserFactory instance = new ExcelDataAdapterConfigurationParserFactory();

    private ExcelDataAdapterConfigurationParserFactory() {
    }

    public static ExcelDataAdapterConfigurationParserFactory getInstance() {
        return instance;
    }

    public ExcelDataAdapterConfigurationParser createParser(String configurationString) {
        if (configurationString.contains("<excelDataAdapterConfigurationData version=\"3\">")) {
            return new ExcelDataAdapterConfigurationParserVer3(configurationString);
        }
        else if (configurationString.startsWith("Version=2;")) {
            return new ExcelDataAdapterConfigurationParserVer2(configurationString);
        }
        else {
            return new ExcelDataAdapterConfigurationParserVer1(configurationString);
        }
    }
}
