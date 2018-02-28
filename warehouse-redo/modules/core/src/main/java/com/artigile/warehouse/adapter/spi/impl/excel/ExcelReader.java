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

/**
 * Interface of excel reader (general abstraction for working with excel files).
 *
 * @author Valery Barysok, 6/20/11
 */
public interface ExcelReader {

    /**
     * @return total count of sheets in excel document.
     */
    public int getSheetCount();

    /**
     * Use to get reader for the given excel worksheet.
     * @param sheetIndex zero-based index of the sheet.
     * @return reader for specified worksheet.
     */
    public ExcelSheetReader getSheetReader(int sheetIndex);
}
