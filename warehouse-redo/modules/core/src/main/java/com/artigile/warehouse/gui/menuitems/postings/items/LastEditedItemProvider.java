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
 * Implementations of this interface provide information about last edited posting item.
 * This information is used by UI to highlight recent changes to user.
 * If this approach will be reused then move this interface into Report Core packages.
 *
 * @author Aliaksandr.Chyrtsik, 18.11.12
 */
public interface LastEditedItemProvider {
    /**
     * @return identifier of the last edited (created of updated) item of null is there are no such item.
     */
    Long getLastEditedItemId();

    void setLastEditedItemId(Long id);
}
