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

import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.postings.Posting;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Shyrik, 02.02.2009
 */
public class PostingDAOImpl extends GenericEntityDAO<Posting> implements PostingDAO {
    @Override
    public long getNextAvailablePostingNumber() {
        List result = getSession()
            .createCriteria(Posting.class)
            .setProjection(Projections.max("number"))
            .list();
        return (result.size() > 0 && result.get(0) != null) ? (Long)result.get(0) + 1 : 1;
    }

    @Override
    public Posting getPostingByNumber(long number) {
        List postings = getSession()
            .createCriteria(Posting.class)
            .add(Restrictions.eq("number", number))
            .list();

        if (postings.size() > 0 ){
            return (Posting)postings.get(0);
        }
        return null;
    }
}
