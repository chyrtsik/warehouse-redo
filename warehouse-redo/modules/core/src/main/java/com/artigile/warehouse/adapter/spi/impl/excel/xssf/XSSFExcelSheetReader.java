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

import com.artigile.warehouse.adapter.spi.impl.excel.Dimension;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelReaderListener;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelSheetReader;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.eventusermodel.XSSFReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

/**
 * Reader for working with content of a single Excel 2007 worksheet.
 *
 * @author Aliaksandr.Chyrtsik, 15.08.11
 */
public class XSSFExcelSheetReader implements ExcelSheetReader {

    private String sheetId;

    private String sheetName;

    private Dimension dimension;

    private XMLInputFactory factory;

    private XSSFReader xssfReader;

    public XSSFExcelSheetReader(String sheetName, String sheetId, XMLInputFactory factory, XSSFReader xssfReader) {
        this.sheetId = sheetId;
        this.sheetName = sheetName;
        this.factory = factory;
        this.xssfReader = xssfReader;
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
        return getBottomRow() - getTopRow() + 1;
    }

    @Override
    public int getColumnCount() {
        return getRightColumn() - getLeftColumn() + 1;
    }

    @Override
    public String getSheetName() {
        return sheetName;
    }

    @Override
    public void parse(ExcelReaderListener listener, int maxResults) {
        try {
            XSSFRowsParser parser = new XSSFRowsParser(listener, maxResults, xssfReader.getSharedStringsTable());
            XMLStreamReader reader = factory.createXMLStreamReader(xssfReader.getSheet(sheetId));
            XSSFUtils.iterateXMLDocument(reader, parser);
        } catch (XMLStreamException e) {
            LoggingFacade.logError(this, e);
            throw new RuntimeException(e);
        } catch (InvalidFormatException e) {
            LoggingFacade.logError(this, e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            LoggingFacade.logError(this, e);
            throw new RuntimeException(e);
        }
    }

    //================================ Helpers ==================================
    private Dimension readSheetDimension() {
        XMLStreamReader xmlStreamReader;
        try {
            xmlStreamReader = factory.createXMLStreamReader(xssfReader.getSheet(sheetId));
        } catch (XMLStreamException e) {
            LoggingFacade.logError(this, e);
            throw new RuntimeException(e);
        } catch (InvalidFormatException e) {
            LoggingFacade.logError(this, e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            LoggingFacade.logError(this, e);
            throw new RuntimeException(e);
        }
        final Dimension dimension = new Dimension();
        XSSFUtils.iterateXMLDocument(xmlStreamReader, new XSSFUtils.XmlIterationListener(){
            @Override
            public boolean processEvent(XMLStreamReader xmlReader, int event) {
                if (event == XMLStreamReader.START_ELEMENT && xmlReader.getLocalName().equals("dimension")) {
                    Dimension tempDimension = XSSFUtils.parseDimension(xmlReader.getAttributeValue(null, "ref"));
                    dimension.setLeft(tempDimension.getLeft());
                    dimension.setRight(tempDimension.getRight());
                    dimension.setTop(tempDimension.getTop());
                    dimension.setBottom(tempDimension.getBottom());
                    return false;
                }
                return true;
            }
        });
        return dimension;
    }
}
