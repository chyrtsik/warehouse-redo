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

import com.artigile.warehouse.adapter.spi.impl.excel.Dimension;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelReaderListener;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelSheetReader;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.DimensionsRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * Reader for working with content of a single Excel 97-2003 worksheet.
 *
 * @author Aliaksandr.Chyrtsik, 15.08.11
 */
public class HSSFExcelSheetReader implements ExcelSheetReader {

    private String sheetName;

    private Dimension dimension;

    private SSTRecord sharedStringsRecord;

    private POIFSFileSystem docFileSystem;

    public HSSFExcelSheetReader(BoundSheetRecord sheet, SSTRecord sharedStringsRecord, POIFSFileSystem docFileSystem){
        this.sheetName = sheet.getSheetname();
        this.sharedStringsRecord = sharedStringsRecord;
        this.docFileSystem = docFileSystem;
        this.dimension = readSheetDimension();
    }

    @Override
    public int getTopRow() {
        return dimension.getTop();
    }

    @Override
    public int getBottomRow() {
        return dimension.getBottom();
    }

    @Override
    public int getLeftColumn() {
        return dimension.getLeft();
    }

    @Override
    public int getRightColumn() {
        return dimension.getRight();
    }

    @Override
    public int getRowCount() {
        return getBottomRow() - getTopRow();
    }

    @Override
    public int getColumnCount() {
        return getRightColumn() - getLeftColumn();
    }

    @Override
    public String getSheetName() {
        return sheetName;
    }

    @Override
    public void parse(ExcelReaderListener listener, int maxResults) {
        HSSFExcelRowsParser parser = new HSSFExcelRowsParser(sharedStringsRecord, listener, getColumnCount(), maxResults);
        HSSFUtils.processRequestForSheetRecordsWithMultipleSid(docFileSystem, sheetName, parser.getSids(), parser);
        parser.processLastRow();
    }

    //================================ Helpers ==================================
    private Dimension readSheetDimension() {
        final Dimension dimension = new Dimension();
        HSSFUtils.processRequestForSheetRecordsWithSid(docFileSystem, sheetName, DimensionsRecord.sid, new HSSFListener() {
            @Override
            public void processRecord(Record record) {
                DimensionsRecord dimensionRecord = (DimensionsRecord) record;
                dimension.setTop(dimensionRecord.getFirstRow());
                dimension.setBottom(dimensionRecord.getLastRow());
                dimension.setLeft(dimensionRecord.getFirstCol());
                dimension.setRight(dimensionRecord.getLastCol());
            }
        });
        return dimension;
    }
}
