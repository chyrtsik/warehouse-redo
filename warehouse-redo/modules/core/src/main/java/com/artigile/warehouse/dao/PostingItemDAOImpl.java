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

import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.postings.PostingItem;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

/**
 * @author Shyrik, 02.02.2009
 */
public class PostingItemDAOImpl extends GenericEntityDAO<PostingItem> implements PostingItemDAO {
    private PostingDAO postingsDAO;

    @Override
    public long getNextAvailableNumber(long postingId) {
        List result = getSession()
            .createCriteria(PostingItem.class)
            .setProjection(Projections.max("number"))
            .add(Restrictions.eq("posting", postingsDAO.get(postingId)))
            .list();
        return (result.size() > 0 && result.get(0) != null) ? (Long)result.get(0) + 1 : 1;
    }

    @Override
    public PostingItem findSamePostingItem(long postingId, long detailBatchId, Date shelfLifeDate) {
        List result = getSession()
            .createCriteria(PostingItem.class)
            .add(Restrictions.eq("posting.id", postingId))
            .add(Restrictions.eq("detailBatch.id", detailBatchId))
            .add(shelfLifeDate == null ? Restrictions.isNull("shelfLifeDate") : Restrictions.eq("shelfLifeDate", shelfLifeDate))
            .list();

        return result.size() > 0 ? (PostingItem)result.get(0) : null;
    }

    @Override
    public PostingItem findSameUnclassifiedPostingItem(long postingId, String barCode) {
        List result = getSession()
            .createCriteria(PostingItem.class)
            .createAlias("unclassifiedCatalogItem", "uci")
            .add(Restrictions.eq("posting.id", postingId))
            .add(Restrictions.eq("uci.barCode", barCode))
            .list();

        return result.size() > 0 ? (PostingItem)result.get(0) : null;
    }

    //========================= Spring setters ==============================
    public void setPostingsDAO(PostingDAO postingsDAO) {
        this.postingsDAO = postingsDAO;
    }
}
