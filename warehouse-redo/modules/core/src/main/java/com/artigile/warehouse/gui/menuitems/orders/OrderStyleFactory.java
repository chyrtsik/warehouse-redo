/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.orders;

import com.artigile.warehouse.domain.orders.OrderState;
import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;
import com.artigile.warehouse.utils.dto.orders.OrderTOForReport;

import java.awt.*;

/**
 * @author Shyrik, 18.04.2010
 */

/**
 * Factory of styles for decorating orders in list.
 */
public class OrderStyleFactory implements StyleFactory {
    private Style problemOrderStyle = new Style(new Color(255, 200, 200));

    @Override
    public Style getStyle(Object rowData) {
        OrderTOForReport order = (OrderTOForReport) rowData;
        if (order.getState() == OrderState.COLLECTION_PROBLEM){
           return problemOrderStyle; 
        }
        else{
            return null;
        }
    }
}
