/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.orders.items;

import com.artigile.warehouse.domain.orders.OrderItemProcessingResult;
import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;
import com.artigile.warehouse.utils.dto.orders.OrderItemTO;

import java.awt.*;

/**
 * @author Shyrik, 18.04.2010
 */

/*
 * Styles factory for order items coloring.
 */
public class OrderItemStyleFactory implements StyleFactory {
    private Style problemItemStyle = new Style(new Color(255, 200, 200));

    @Override
    public Style getStyle(Object rowData) {
        OrderItemTO orderItem = (OrderItemTO) rowData;
        if (OrderItemProcessingResult.PROBLEM.equals(orderItem.getProcessingResult())){
            return problemItemStyle;
        }
        else{
            return null;
        }
    }
}
