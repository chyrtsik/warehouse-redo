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
 * Reader for the one excel worksheet.
 *
 * @author Aliaksandr.Chyrtsik, 15.08.11
 */
public interface ExcelSheetReader {

    /**
     * @return top row index of excel worksheet.
     */
    public int getTopRow();

    /**
     * @return bottom row index of excel worksheet.
     */
    public int getBottomRow();

    /**
     * @return left column index of excel worksheet.
     */
    public int getLeftColumn();

    /**
     * @return right column index of excel worksheet.
     */
    public int getRightColumn();

    /**
     * @return total count of rows in excel worksheet.
     */
    public int getRowCount();

    /**
     * @return total count of columns in excel worksheet.
     */
    public int getColumnCount();

    /**
     * @return user defined name of the worksheet.
     */
    public String getSheetName();

    /**
     * Parses excel worksheet using specified listener.
     * @param listener listens for excel worksheet parsing events.
     * @param maxResults maximum rows to be parsed.
     */
    public void parse(ExcelReaderListener listener, int maxResults);
}
