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

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.orders.OrderItemTO;

import java.math.BigDecimal;

/**
 * @author: Vadim.Zverugo
 */

/**
 * Saver for price of order item.
 */
public class OrderItemPriceSaver extends OrderItemCellEditingStrategy {
    @Override
    protected void doSave(OrderItemTO orderItem, Object newValue) {
        String value = (String) newValue;
        if (value.isEmpty()){
            orderItem.setPrice(null);
        }
        else{
            if (!StringUtils.isNumber(value)){
                return;
            }
            BigDecimal price = StringUtils.parseStringToBigDecimal(value);
            orderItem.setPrice(price);
        }
        try {
            SpringServiceContext.getInstance().getOrdersService().saveOrderItem(orderItem);
        }
        catch (BusinessException e) {
            MessageDialogs.showWarning(e.getMessage());
        }
    }
}
