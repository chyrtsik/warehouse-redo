/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl;

import com.artigile.warehouse.adapter.spi.impl.excel.CellValue;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelReaderListener;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelSheetReader;

import javax.swing.table.DefaultTableModel;

/**
 * @author Valery Barysok, 6/20/11
 */

public class ExcelTableModel extends DefaultTableModel {

    private int colCount;

    public static ExcelTableModel create(ExcelSheetReader reader, final int maxResult) {
        final ExcelTableModel tableModel = new ExcelTableModel();
        final int leftColumn = reader.getLeftColumn();
        tableModel.colCount = reader.getColumnCount();
        reader.parse(new ExcelReaderListener() {

            private Object[] row;

            @Override
            public void onBeginRow(int rowIndex) {
                row = new Object[tableModel.colCount];
            }

            public void onEndRow() {
                tableModel.addRow(row);
                row = null;
            }

            @Override
            public void onNextCellValue(CellValue cellValue) {
                int ind = cellValue.getColumnIndex() - leftColumn;
                String value = cellValue.getValue();
                // TODO:
//                if (value != null) {
//                    Long longNum = Utils.getLong(value);
//                    if (longNum != null) {
//                        row[ind] = longNum;
//                        return;
//                    }
//
//                    Double doubleNum = Utils.getDouble(value);
//                    if (doubleNum != null) {
//                        row[ind] = StringUtils.formatNumber(doubleNum);
//                        return;
//                    }
//                }

                row[ind] = value;
            }
        }, maxResult);

        return tableModel;
    }

    @Override
    public int getColumnCount() {
        return colCount;
    }
}
