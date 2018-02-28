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

import com.artigile.warehouse.adapter.spi.impl.excel.ExcelReader;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelSheetReader;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reader for working with Excel 2007 format (*.xlsx).
 *
 * @author Valery Barysok, 6/20/11
 */

public class XSSFExcelReader implements ExcelReader {

    private List<ExcelSheetReader> sheetReaders;

    public XSSFExcelReader(OPCPackage pkg) {
        try {
            this.sheetReaders = readSheetsFromPackage(pkg);
        } catch (Exception e) {
            LoggingFacade.logError(this, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getSheetCount() {
        return sheetReaders.size();
    }

    @Override
    public ExcelSheetReader getSheetReader(int sheetIndex) {
        return sheetReaders.get(sheetIndex);
    }

    private List<ExcelSheetReader> readSheetsFromPackage(OPCPackage pkg) throws IOException, OpenXML4JException, XMLStreamException {
        XSSFReader reader = new XSSFReader(pkg);
        XMLInputFactory factory = XMLInputFactory.newFactory();

        List<ExcelSheetReader> readers = new ArrayList<ExcelSheetReader>();
        List<SheetInfo> sheetsIdAndName = parseSheetsIdAndName(factory.createXMLStreamReader(reader.getWorkbookData()));

        for (SheetInfo sheet : sheetsIdAndName){
            readers.add(new XSSFExcelSheetReader(sheet.getSheetName(), sheet.getSheetId(), factory, reader));
        }

        return readers;
    }

    private List<SheetInfo> parseSheetsIdAndName(XMLStreamReader workbookReader) {
        final List<SheetInfo> sheetsIdAndName = new ArrayList<SheetInfo>();
        XSSFUtils.iterateXMLDocument(workbookReader, new XSSFUtils.XmlIterationListener() {
            @Override
            public boolean processEvent(XMLStreamReader xmlReader, int event) {
                if (event == XMLStreamReader.START_ELEMENT && xmlReader.getLocalName().equals("sheet")) {
                    //Reads sheet xml element.
                    String sheetId = xmlReader.getAttributeValue(null, "id");
                    String sheetName = xmlReader.getAttributeValue(null, "name");
                    sheetsIdAndName.add(new SheetInfo(sheetId, sheetName));
                }
                return true;
            }
        });
        return sheetsIdAndName;
    }

    private class SheetInfo {
        private String sheetId;
        private String sheetName;

        public SheetInfo(String sheetId, String sheetName){
            this.sheetId = sheetId;
            this.sheetName = sheetName;
        }

        public String getSheetId() {
            return sheetId;
        }

        public String getSheetName() {
            return sheetName;
        }
    }
}
