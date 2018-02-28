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
 * @author Valery Barysok, 6/20/11
 */

public interface ExcelReaderListener {

    /**
     * Called when new row is started.
     * @param rowIndex index of the new row.
     */
    public void onBeginRow(int rowIndex);

    /**
     * Called when new cell in the current row is found.
     * @param cellValue value of the cell.
     */
    public void onNextCellValue(CellValue cellValue);

    /**
     * Called when current row is ended.
     */
    public void onEndRow();

}
