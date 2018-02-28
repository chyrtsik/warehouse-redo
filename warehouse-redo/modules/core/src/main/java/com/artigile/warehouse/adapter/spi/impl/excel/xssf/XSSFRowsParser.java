/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.excel.xssf;

import com.artigile.warehouse.adapter.spi.impl.excel.CellValue;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelReaderListener;
import com.artigile.warehouse.utils.Utils;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Internal class for loading of rows of an excel 2007 spreadsheet.
 *
 * @author Aliaksandr.Chyrtsik, 16.08.11
 */
public class XSSFRowsParser implements XSSFUtils.XmlIterationListener{
    private ExcelReaderListener listener;

    private int maxResults;

    private SharedStringsTable sharedStringsTable;

    private int rowsProcessed = 0;
    private int rowIndex = -1;
    private int colIndex = -1;
    private boolean nextIsString = false;

    public XSSFRowsParser(ExcelReaderListener listener, int maxResults, SharedStringsTable sharedStringsTable) {
        this.listener = listener;
        this.maxResults = maxResults;
        this.sharedStringsTable = sharedStringsTable;
    }

    @Override
    public boolean processEvent(XMLStreamReader xmlReader, int event) throws XMLStreamException {
        switch (event) {
            case XMLStreamReader.START_ELEMENT: {
                String localName = xmlReader.getLocalName();
                if (localName.equals("row")) {
                    String row = xmlReader.getAttributeValue(null, "r");
                    rowIndex = Integer.parseInt(row)-1;
                    listener.onBeginRow(rowIndex);
                } else if (localName.equals("c")) {
                    String cell = xmlReader.getAttributeValue(null, "r");
                    String t = xmlReader.getAttributeValue(null, "t");
                    nextIsString = t != null && t.equals("s");
                    char[] chars = cell.toCharArray();
                    colIndex = 0;
                    int j = 0;
                    for (; j < chars.length && isLatin(chars[j]); ++j) {
                        colIndex = colIndex * 26 + chars[j] - 'A';
                    }
                } else if (localName.equals("v")) {
                    String value = xmlReader.getElementText();
                    if (nextIsString) {
                        Integer index = Utils.getInteger(value);
                        value = new XSSFRichTextString(sharedStringsTable.getEntryAt(index)).toString();
                    }
                    CellValue cellValue = new CellValue(rowIndex, colIndex, value);
                    listener.onNextCellValue(cellValue);
                }
                break;
            }
            case XMLStreamReader.END_ELEMENT: {
                String localName = xmlReader.getLocalName();
                if (localName.equals("sheetData")) {
                    //End of sheet data have been reached.
                    return false;
                } else if (localName.equals("c")) {
                    colIndex = -1;
                } else if (localName.equals("row")) {
                    listener.onEndRow();
                    rowIndex = -1;
                    if (++rowsProcessed >= maxResults) {
                        //Limit maximum number of rows to parse.
                        return false;
                    }
                }
                break;
            }
        }
        return true;
    }

    private static boolean isLatin(char ch) {
        return 'A' <= ch && ch <= 'Z';
    }
}
