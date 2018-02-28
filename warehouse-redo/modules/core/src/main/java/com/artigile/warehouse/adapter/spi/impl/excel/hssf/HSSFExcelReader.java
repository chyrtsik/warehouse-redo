/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.excel.hssf;

import com.artigile.warehouse.adapter.spi.impl.excel.ExcelReader;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelSheetReader;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Reader for working with Excel 97 - 2003 format (*.xls).
 *
 * @author Valery Barysok, 7/16/11
 */
public class HSSFExcelReader implements ExcelReader {

    private List<ExcelSheetReader> sheetRecords;

    public HSSFExcelReader(InputStream inputStream) {
        try {
            this.sheetRecords = readSheetsFromStream(new POIFSFileSystem(inputStream));
        } catch (IOException e) {
            LoggingFacade.logError(this, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getSheetCount() {
        return sheetRecords.size();
    }

    @Override
    public ExcelSheetReader getSheetReader(int sheetIndex) {
        return sheetRecords.get(sheetIndex);
    }

    //============================= Helpers =================================
    private List<ExcelSheetReader> readSheetsFromStream(final POIFSFileSystem docFileSystem) throws IOException {
        class SheetsListener implements HSSFListener {
            private SSTRecord sharedStringsRecord;
            private List<BoundSheetRecord> sheetRecords = new ArrayList<BoundSheetRecord>();

            @Override
            public void processRecord(Record record) {
                if (record.getSid() == SSTRecord.sid){
                    this.sharedStringsRecord = (SSTRecord)record;
                }
                else if (record.getSid() == BoundSheetRecord.sid){
                    this.sheetRecords.add((BoundSheetRecord) record);
                }
            }

            public SSTRecord getSharedStringsRecord() {
                return sharedStringsRecord;
            }

            public List<BoundSheetRecord> getSheetRecords() {
                return this.sheetRecords;
            }
        }

        SheetsListener listener = new SheetsListener();
        HSSFUtils.processRequestForRecordsWithMultipleSid(docFileSystem, new short[]{SSTRecord.sid, BoundSheetRecord.sid}, listener);

        List<ExcelSheetReader> sheetReaders = new ArrayList<ExcelSheetReader>();
        for (BoundSheetRecord sheet : listener.getSheetRecords()){
            sheetReaders.add(new HSSFExcelSheetReader(sheet, listener.getSharedStringsRecord(), docFileSystem));
        }
        return sheetReaders;
    }
}
