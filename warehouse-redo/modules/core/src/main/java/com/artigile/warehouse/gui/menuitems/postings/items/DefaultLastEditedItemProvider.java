/*
 * Copyright (c) 2007-2012 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.postings.items;

/**
 * @author Aliaksandr.Chyrtsik, 18.11.12
 */
public class DefaultLastEditedItemProvider implements LastEditedItemProvider {
    private Long lastEditedItemId;

    @Override
    public Long getLastEditedItemId() {
        return lastEditedItemId;
    }

    @Override
    public void setLastEditedItemId(Long itemId) {
        lastEditedItemId = itemId;
    }
}
