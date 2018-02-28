/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.needs.WareNeedItem;

import java.util.List;

/**
 * @author Shyrik, 25.02.2009
 */
public interface WareNeedItemDAO extends EntityDAO<WareNeedItem> {
    Long getNextAvailableNumber(long wareNeedId);

    Long getNextAvailableSubNumber(long wareNeedId, long number);

    List<WareNeedItem> findSameWareNeedItems(WareNeedItem needItem);
}
