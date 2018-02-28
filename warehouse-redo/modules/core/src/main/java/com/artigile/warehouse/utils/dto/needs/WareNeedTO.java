/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.needs;

import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 28.02.2009
 */
public class WareNeedTO extends EqualsByIdImpl {
    private long id;

    private List<WareNeedItemTO> items = new ArrayList<WareNeedItemTO>();

    //===================== Operations ================================
    public void addNewItem(WareNeedItemTO newWareNeedItem) {
        newWareNeedItem.setWareNeed(this);
        items.add(newWareNeedItem);
        SpringServiceContext.getInstance().getWareNeedsService().addItemToWareNeed(newWareNeedItem);
    }

    public void deleteItem(WareNeedItemTO wareNeedItem) {
        items.remove(wareNeedItem);
        SpringServiceContext.getInstance().getWareNeedsService().deleteItemFromWareNeed(wareNeedItem);
    }

    //============================== Getters and setters ==============================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<WareNeedItemTO> getItems() {
        return items;
    }

    public void setItems(List<WareNeedItemTO> items) {
        this.items = items;
    }
}
