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

import com.artigile.warehouse.adapter.spi.impl.excel.CellValue;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelReaderListener;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.Utils;
import org.apache.poi.hssf.eventusermodel.AbortableHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFUserException;
import org.apache.poi.hssf.record.*;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Internal class for loading of rows of a excel 97-2003 spreadsheet.
 *
 * @author Aliaksandr.Chyrtsik, 15.08.11
 */
public class HSSFExcelRowsParser extends AbortableHSSFListener {
    private SSTRecord sstrec;
    private ExcelReaderListener listener;
    private int maxRows;
    private List<CellValue> row;
    private int lastRow = -1;
    private int curRow;
    private boolean curRowInit = true;
    private int processed = 0;

    public HSSFExcelRowsParser(SSTRecord sharedStringsRecord, ExcelReaderListener listener, int colCnt, int maxRows) {
        this.sstrec = sharedStringsRecord;
        this.listener = listener;
        this.maxRows = maxRows;
        row = new ArrayList<CellValue>(colCnt);
    }

    public short[] getSids() {
        //List of all sids used by this parser.
        return new short[] {RowRecord.sid, NumberRecord.sid, LabelRecord.sid, LabelSSTRecord.sid};
    }

    @Override
    public short abortableProcessRecord(Record record) throws HSSFUserException {
        switch (record.getSid()) {
            case RowRecord.sid: {
                RowRecord rowrec = (RowRecord) record;
                if (curRowInit) {
                    if (lastRow != -1) {
                        processRow(curRow);
                    }
                    curRow = rowrec.getRowNumber();
                    curRowInit = false;
                }
                lastRow = rowrec.getRowNumber();
            }
            break;
            default: {
                curRowInit = true;
                CellValue cellValue = getCellValue(record);
                if (cellValue != null) {
                    int rowIndex = cellValue.getRowIndex();
                    if (curRow != rowIndex) {
                        processRow(curRow);
                        curRow = rowIndex;
                    }
                    row.add(cellValue);
                }
            }
            break;
        }

        if (processed >= maxRows) {
            return -1;
        }

        return 0;
    }

    public void processLastRow() {
        if (processed < maxRows) {
            processRow(lastRow);
        }
    }

    private void processRow(int rowIndex) {
        listener.onBeginRow(rowIndex);
        for (CellValue cellValue : row) {
            listener.onNextCellValue(cellValue);
        }
        listener.onEndRow();
        row.clear();
        ++processed;
    }

    private CellValue getCellValue(Record record) {
        if (record instanceof CellValueRecordInterface) {
            CellValueRecordInterface cellValue = (CellValueRecordInterface) record;
            String value = "<Invalid value>";
            switch (record.getSid()) {
                case NumberRecord.sid: {
                    NumberRecord numrec = (NumberRecord) record;
                    value = NumberToTextConverter.toText(numrec.getValue());
                    if (value != null) {
                        Long longNum = Utils.getLong(value);
                        if (longNum != null) {
                            value = longNum.toString();
                        }

                        Double doubleNum = Utils.getDouble(value);
                        if (doubleNum != null) {
                            value = StringUtils.formatNumber(doubleNum);
                        }
                    }
                }
                break;
                case LabelRecord.sid: {
                    LabelRecord lrec = (LabelRecord) record;
                    value = lrec.getValue();
                }
                break;
                case LabelSSTRecord.sid: {
                    LabelSSTRecord lrec = (LabelSSTRecord) record;
                    org.apache.poi.hssf.record.common.UnicodeString str = sstrec.getString(lrec.getSSTIndex());
                    value = str.getString();
                }
                break;
            }
            return new CellValue(cellValue.getRow(), cellValue.getColumn(), value);
        }

        return null;
    }
}
