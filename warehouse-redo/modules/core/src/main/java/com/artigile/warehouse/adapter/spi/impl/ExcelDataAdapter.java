/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl;

import com.artigile.warehouse.adapter.spi.DataAdapter;
import com.artigile.warehouse.adapter.spi.DataAdapterInfo;
import com.artigile.warehouse.adapter.spi.DataImportContext;
import com.artigile.warehouse.adapter.spi.DataImportProgressListener;
import com.artigile.warehouse.adapter.spi.impl.configuration.ExcelDataAdapterConfigurationData;
import com.artigile.warehouse.adapter.spi.impl.excel.*;
import com.artigile.warehouse.utils.StreamUtils;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Valery Barysok, 6/17/11
 */

public class ExcelDataAdapter implements DataAdapter {
    private static DataAdapterInfo dataAdapterInfo =
            new DataAdapterInfoImpl(
                "8f825720-6a93-426d-9244-e0c2281b734e",
                I18nSupport.message("excel.import.data.adapter.name"),
                I18nSupport.message("excel.import.data.adapter.description")
            );

    public static DataAdapterInfo getDataAdapterInfo() {
        return dataAdapterInfo;
    }

    @Override
    public void importData(DataImportContext context) {
        //Extract data stream name from configuration next.
        String configString = context.getConfiguration().getConfigurationString();
        ExcelDataAdapterConfigurationData parsedConfig = ExcelDataAdapterConfigurationData.parse(configString);

        InputStream excelInputStream = context.getConfiguration().getDataInputStream(parsedConfig.getFileStreamId());
        try{
            //Initialize excel parser from the proper stream.
            //To prevent parser from consuming a lot of memory we use temporary file -- excel
            // parser will not load all data in memory then.
            File tempFile = StreamUtils.createTemporaryFileFromStream(excelInputStream);
            ExcelReader reader = ExcelReaderFactory.create(tempFile.getPath());

            //Initialize progress listener.
            LinkedHashMap<String, Integer> configuredSheets = parsedConfig.getConfiguredSheets();
            context.getImportProgressListener().onTotalRowCountChanged(calculateTotalRowsCount(configuredSheets, reader));

            //Parse each excel worksheet (during parsing we provide single progress tracking for all sheets).
            int totalRowsImported = 0;
            for (Map.Entry<String, Integer> sheet : configuredSheets.entrySet()) {
                ExcelSheetReader sheetReader = reader.getSheetReader(sheet.getValue());
                CumulativeProgressListenerWrapper processListenerWrapper = new CumulativeProgressListenerWrapper(
                        totalRowsImported, context.getImportProgressListener());

                ExcelReaderListener listener = new ExcelReaderListenerImpl(
                        parsedConfig.getSheetsColumnsConfig().get(sheet.getKey()),
                        context.getDataSaver(),
                        processListenerWrapper
                );
                sheetReader.parse(listener, Integer.MAX_VALUE);

                totalRowsImported = processListenerWrapper.getTotalRowsImported();
            }
        } catch (IOException e) {
            LoggingFacade.logError(this, e);
            throw new RuntimeException(e);
        } finally {
            try {
                excelInputStream.close();
            } catch (IOException e) {
                LoggingFacade.logError(this, "Cannot close stream with id=" + parsedConfig.getFileStreamId(), e);
            }
        }
    }

    private int calculateTotalRowsCount(LinkedHashMap<String, Integer> sheets, ExcelReader reader) {
        int totalRows = 0;
        for (Map.Entry<String, Integer> sheet : sheets.entrySet()) {
            totalRows += reader.getSheetReader(sheet.getValue()).getRowCount();
        }
        return totalRows;
    }

    /**
     * This progress listener summarizes total count of row imported.
     * This allow us to show total row of the whole excel file instead os total rows withing current worksheet.
     */
    private class CumulativeProgressListenerWrapper implements DataImportProgressListener {

        /**
         * Original listener wrapped by this component.
         */
        private DataImportProgressListener wrappedProgressListener;

        /**
         * Previously imported count of rows.
         */
        private int offset;

        /**
         * Count of rows imported with current instance of listener.
         */
        private int rowsImported;

        public CumulativeProgressListenerWrapper(int offset, DataImportProgressListener wrappedProgressListener) {
            this.wrappedProgressListener = wrappedProgressListener;
            this.offset = offset;
            this.rowsImported = 0;
        }

        @Override
        public void onTotalRowCountChanged(int newTotalRowCount) {
            wrappedProgressListener.onTotalRowCountChanged(newTotalRowCount);
        }

        @Override
        public void onImportedRowCountChanged(int newImportedRowCount) {
            rowsImported = newImportedRowCount;
            wrappedProgressListener.onImportedRowCountChanged(offset + rowsImported);
        }

        public int getTotalRowsImported(){
            return offset + rowsImported;
        }
    }
}
