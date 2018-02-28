/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.inventorization.task;

import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;
import com.artigile.warehouse.utils.dto.inventorization.task.InventorizationTaskTO;

import java.awt.*;

/**
 * @author Borisok V.V., 03.10.2009
 */

/**
 * Style factory for decorating "Task printed" column.
 */
public class TaskPrintedStyleFactory implements StyleFactory {
    private Style printedStyle = new Style(Color.YELLOW);

    @Override
    public Style getStyle(Object rowData) {
        InventorizationTaskTO task = (InventorizationTaskTO) rowData;
        return task.getPrinted() ? printedStyle : null;
    }
}
