/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.excel;

import com.artigile.warehouse.adapter.spi.impl.excel.hssf.HSSFExcelReader;
import com.artigile.warehouse.adapter.spi.impl.excel.xssf.XSSFExcelReader;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * @author Valery Barysok, 6/20/11
 */

public class ExcelReaderFactory {

    private ExcelReaderFactory() {
    }

    public static ExcelReader create(String path) {
        InputStream inp;
        try {
            inp = new FileInputStream(path);

            // If clearly doesn't do mark/reset, wrap up
            if (! inp.markSupported()) {
                inp = new PushbackInputStream(inp, 8);
            }

            if(POIFSFileSystem.hasPOIFSHeader(inp)) {
                return new HSSFExcelReader(inp);
            }
            if (POIXMLDocument.hasOOXMLHeader(inp)) {
                inp.close();
                return new XSSFExcelReader(OPCPackage.open(path));
            }
        } catch (InvalidFormatException e) {
            LoggingFacade.logWarning(e);
        } catch (OpenXML4JException e) {
            LoggingFacade.logWarning(e);
        } catch (IOException e) {
            LoggingFacade.logWarning(e);
        }
        throw new IllegalArgumentException("Your file was neither an OLE2, nor an OOXML");
    }
}