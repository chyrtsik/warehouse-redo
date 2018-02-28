/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.postings.PostingItem;

import java.util.Date;

/**
 * @author Shyrik, 02.02.2009
 */
public interface PostingItemDAO extends EntityDAO<PostingItem> {
    long getNextAvailableNumber(long postingId);

    PostingItem findSamePostingItem(long postingId, long detailBatchId, Date shelfLifeDate);

    PostingItem findSameUnclassifiedPostingItem(long postingId, String barCode);
}
