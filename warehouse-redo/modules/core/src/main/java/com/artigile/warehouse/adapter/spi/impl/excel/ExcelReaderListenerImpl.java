/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.excel;

import com.artigile.warehouse.adapter.spi.DataImportProgressListener;
import com.artigile.warehouse.adapter.spi.DataSaver;
import com.artigile.warehouse.adapter.spi.impl.DomainColumn;
import com.artigile.warehouse.adapter.spi.impl.configuration.ColumnRelationship;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Valery Barysok, 6/27/11
 */

public class ExcelReaderListenerImpl implements ExcelReaderListener {

    private DataImportProgressListener importProgressListener;

    private DataSaver dataSaver;

    private Map<Integer, DomainColumn> columnToDomain;

    private int processed = 0;

    private Map<String, String> currentRowData;

    private Map<String, String> currentRowSourceData;


    /* Constructors
    ------------------------------------------------------------------------------------------------------------------*/
    public ExcelReaderListenerImpl(List<ColumnRelationship> columnsConfiguration, DataSaver dataSaver,
                                   DataImportProgressListener importProgressListener) {
        this.importProgressListener = importProgressListener;
        this.dataSaver = dataSaver;
        this.columnToDomain = getColumnToDomain(columnsConfiguration);
        this.currentRowData = new HashMap<String, String>();
        this.currentRowSourceData = new LinkedHashMap<String, String> ();
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onBeginRow(int rowIndex) {
        //Starting filling of a new row.
        currentRowData.clear();
        currentRowSourceData.clear();
    }

    @Override
    public void onEndRow() {
        //Saving imported row.
        dataSaver.setSourceDataRow(currentRowSourceData);
        dataSaver.saveDataRow(currentRowData);

        //Updating import progress indicator.
        ++processed;
        if (processed % 10 == 0) {
            importProgressListener.onImportedRowCountChanged(processed);
        }
    }

    @Override
    public void onNextCellValue(CellValue cellValue) {
        //Saving next cell value of the current row.
        DomainColumn domainColumn = columnToDomain.get(cellValue.getColumnIndex());
        String value = cellValue.getValue();

        if (domainColumn == null || !StringUtils.containsSymbols(value)) {
            return;
        }

        // Put source data into the special map, where ...
        // key - X:Y (X - column index, Y - friendly column name)
        // value - column value
        String columnName = domainColumn.equals(DomainColumn.NOT_DEFINED)
                ? StringUtils.buildString(cellValue.getColumnIndex(), ":", I18nSupport.message("data.import.source.data.column.unknown"))
                : StringUtils.buildString(cellValue.getColumnIndex(), ":", domainColumn.getName());
        currentRowSourceData.put(columnName, value);

        if (!domainColumn.equals(DomainColumn.NOT_DEFINED)){
            //This column will be imported.
            if (domainColumn.isMultiple()) {
                //For this domain column we append values from one or more source columns.
                String oldValue = currentRowData.get(domainColumn.getId());
                if (oldValue != null) {
                    currentRowData.put(domainColumn.getId(), oldValue + domainColumn.getMultipleDelimiter() + value);
                } else {
                    currentRowData.put(domainColumn.getId(), value);
                }
            }
            else {
                currentRowData.put(domainColumn.getId(), value);
            }
        }
    }

    private Map<Integer, DomainColumn> getColumnToDomain(List<ColumnRelationship> configs) {
        Map<Integer, DomainColumn> columnToDomain = new HashMap<Integer, DomainColumn>();
        for (ColumnRelationship columnConfig : configs) {
            columnToDomain.put(columnConfig.getColumnIndex(), columnConfig.getDomainColumn());
        }
        return columnToDomain;
    }
}
