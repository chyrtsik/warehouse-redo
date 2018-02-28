/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.orders;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 07.01.2009
 */

/**
 * Extended representation of the orders TO. Includes detailed order content.
 * Supports listeners mechanism for detecting changed in the order.
 */
public class OrderTO extends OrderTOForReport {
    private List<OrderItemTO> items = new ArrayList<OrderItemTO>();

    public int getItemsCount(){
        return items.size();
    }

    //================ Getters and setters ============================
    public List<OrderItemTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemTO> items) {
        this.items = items;
    }
}
