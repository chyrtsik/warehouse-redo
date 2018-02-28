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
 * Factory of styles for decorating inventorization tasks.
 */
public class InventorizationTaskStyleFactory implements StyleFactory {
    private Style notYetInProcessTaskStyle = new Style(new Color(200, 200, 200));
    private Style processingTaskStyle = new Style(new Color(200, 200, 255));
    private Style succeededTaskStyle = new Style(new Color(200, 255, 200));
    private Style problemTaskStyle = new Style(new Color(255, 200, 200));

    @Override
    public Style getStyle(Object rowData) {
        InventorizationTaskTO task = (InventorizationTaskTO) rowData;
        if (task.isSucceeded()) {
            return succeededTaskStyle;
        }
        else if (task.isProblem()) {
            return problemTaskStyle;
        }
        else if (task.isNotProcessed()) {
           return notYetInProcessTaskStyle;
        }
        else if (task.isInProcess()) {
            return processingTaskStyle;
        }
        else{
            return null;
        }
    }
}
