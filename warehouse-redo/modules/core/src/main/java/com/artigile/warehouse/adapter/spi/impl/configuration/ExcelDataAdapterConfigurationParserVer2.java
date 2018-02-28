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

import com.artigile.warehouse.adapter.spi.impl.DomainColumn;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.artigile.warehouse.adapter.spi.impl.configuration.ExcelDataAdapterConfigurationParserVer1.IMPORT_DOMAIN_COLUMNS;

/**
 * Parser for the version 2 of Excel configuration (configuration for more that one sheet is supported).
 *
 * @author Aliaksandr.Chyrtsik, 18.08.11
 */
public class ExcelDataAdapterConfigurationParserVer2 implements ExcelDataAdapterConfigurationParser {

    private String configString;

    public ExcelDataAdapterConfigurationParserVer2(String configurationString) {
        this.configString = configurationString;
    }

    @Override
    public String getFileStreamId() throws AdapterConfigurationFormatException {
        if (!configString.startsWith("Version=2;")){
            throw new AdapterConfigurationFormatException("Version record \"Version=2;\" is expected.");
        }
        String configFromFileStream = configString.substring("Version=2;".length());
        return configFromFileStream.substring(0, configFromFileStream.indexOf(';'));
    }

    private String[] splitConfigString(String configString) {
        char[] chs = configString.toCharArray();

        int cnt = 0;
        char lc = 0;
        for (char c : chs) {
            if (cnt < 2 && c == ';' || cnt >= 2 && lc != '\\' && c == ';') {
                ++cnt;
            }
            lc = c;
        }

        String[] result = new String[cnt];
        lc = 0;
        for (int i = 0, cur = 0, sp = 0; i < chs.length; ++i) {
            char c = chs[i];
            if (cur < 2 && c == ';' || cnt >= 2 && lc != '\\' && c == ';') {
                result[cur++] = configString.substring(sp, i);
                sp = i + 1;
            }
            lc = c;
        }

        return result;
    }

    @Override
    public LinkedHashMap<String, List<ColumnRelationship>> getSheetsColumnsConfig() throws AdapterConfigurationFormatException {
        LinkedHashMap<String, List<ColumnRelationship>> sheetsColumnsConfig = new LinkedHashMap<String, List<ColumnRelationship>>();

        String[] sheetConfigs = splitConfigString(configString);

        for (int sheet = 2; sheet < sheetConfigs.length; sheet += 2) {
            List<ColumnRelationship> sheetColumns = new ArrayList<ColumnRelationship>();
            String sc = sheetConfigs[sheet + 1];
            String[] columnConfigs = sc.isEmpty() ? new String[0] : sc.split(",");
            String[] sheetName = sheetConfigs[sheet].split("=");

            String sheerRecord = "Sheet" + String.valueOf((sheet - 2)/2);
            if (!sheetName[0].equals(sheerRecord)){
                throw new AdapterConfigurationFormatException("Sheet record \"" + sheerRecord + "\" is expected.");
            }

            for (String columnConfig : columnConfigs) {
                int pos = columnConfig.indexOf('=');
                Integer columnIndex = Integer.valueOf(columnConfig.substring(0, pos));
                DomainColumn domainColumn = ExcelDataAdapterConfigurationParserUtils.getDomainColumnById(IMPORT_DOMAIN_COLUMNS, columnConfig.substring(pos + 1));
                if (domainColumn != null){
                    sheetColumns.add(new ColumnRelationship(domainColumn, columnIndex));
                }
                else{
                    //Domain column is not supported now. No actions are required, just skip this column.
                }
            }
            sheetsColumnsConfig.put(sheetName[1], sheetColumns);
        }

        return sheetsColumnsConfig;
    }
}
