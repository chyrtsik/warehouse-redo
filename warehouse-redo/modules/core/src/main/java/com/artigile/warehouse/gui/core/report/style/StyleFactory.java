/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.style;

/**
 * @author Shyrik, 20.05.2009
 */

/**
 * Interface for obtaining style for table rows.
 */
public interface StyleFactory {
    /**
     * Should return style for row, that shows given object.
     * @param rowData - row data (object, that is shown in the currency row of the table).
     * @return
     */
    Style getStyle(Object rowData);
}
