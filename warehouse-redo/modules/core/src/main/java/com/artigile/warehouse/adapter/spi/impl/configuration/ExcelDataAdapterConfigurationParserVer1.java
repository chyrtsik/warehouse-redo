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
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Parser for the version 1 of Excel configuration (only 1 sheet is allowed).
 *
 * @author Aliaksandr.Chyrtsik, 18.08.11
 */
public class ExcelDataAdapterConfigurationParserVer1 implements ExcelDataAdapterConfigurationParser {

    //Predefined list of domain columns, used in this old import versions.
    static final List<DomainColumn> IMPORT_DOMAIN_COLUMNS;
    static{
        IMPORT_DOMAIN_COLUMNS = new ArrayList<DomainColumn>();
        IMPORT_DOMAIN_COLUMNS.add(new DomainColumn("NAME", I18nSupport.message("price.import.relationship.column.name"), true, true, " "));
        IMPORT_DOMAIN_COLUMNS.add(new DomainColumn("DESCRIPTION", I18nSupport.message("price.import.relationship.column.description"), false, true, " "));
        IMPORT_DOMAIN_COLUMNS.add(new DomainColumn("YEAR", I18nSupport.message("price.import.relationship.column.year"), false));
        IMPORT_DOMAIN_COLUMNS.add(new DomainColumn("QUANTITY", I18nSupport.message("price.import.relationship.column.quantity"), true));
        IMPORT_DOMAIN_COLUMNS.add(new DomainColumn("WHOLESALE_PRICE", I18nSupport.message("price.import.relationship.column.wholesale.price"), false));
        IMPORT_DOMAIN_COLUMNS.add(new DomainColumn("RETAIL_PRICE", I18nSupport.message("price.import.relationship.column.retail.price"), false));
        IMPORT_DOMAIN_COLUMNS.add(new DomainColumn("PACK", I18nSupport.message("price.import.relationship.column.pack"), false));
    }

    private String configString;

    public ExcelDataAdapterConfigurationParserVer1(String configString) {
        this.configString = configString;
    }

    @Override
    public String getFileStreamId() throws AdapterConfigurationFormatException {
        return configString.substring(0, configString.indexOf(';'));
    }

    @Override
    public LinkedHashMap<String, List<ColumnRelationship>> getSheetsColumnsConfig() throws AdapterConfigurationFormatException {
        //Version 1 configuration string holds only configuration for the first sheet of excel document.
        List<ColumnRelationship> sheet1Columns = new ArrayList<ColumnRelationship>();
        String [] configs = configString.split(";");
        for (int i = 1; i < configs.length; ++i) {
            String s = configs[i];
            int pos = s.indexOf('=');
            Integer index = Integer.valueOf(s.substring(0, pos));

            DomainColumn domainColumn = ExcelDataAdapterConfigurationParserUtils.getDomainColumnById(IMPORT_DOMAIN_COLUMNS, s.substring(pos + 1));
            if (domainColumn != null){
                sheet1Columns.add(new ColumnRelationship(domainColumn, index));
            }
            else{
                //Domain column is not supported now. No actions are required, just skip this column.
            }
        }

        LinkedHashMap<String, List<ColumnRelationship>> sheetsColumnsConfig = new LinkedHashMap<String, List<ColumnRelationship>>();
        // sheet without name means first sheet
        sheetsColumnsConfig.put("", sheet1Columns);
        return sheetsColumnsConfig;
    }
}
