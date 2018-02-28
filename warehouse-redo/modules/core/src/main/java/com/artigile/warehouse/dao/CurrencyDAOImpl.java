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
import com.artigile.warehouse.domain.finance.Currency;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Shyrik, 09.12.2008
 */
public class CurrencyDAOImpl extends GenericEntityDAO<Currency> implements CurrencyDAO {

    @Override
    public Currency getCurrencyBySign(String sign) {
        List currencies = getSession()
            .createCriteria(Currency.class)
            .add(Restrictions.eq("sign", sign))
            .list();
        return (currencies.size() == 0) ? null : (Currency)currencies.get(0);
    }

    @Override
    public Currency getCurrencyByName(String name) {
        List currencies = getSession()
            .createCriteria(Currency.class)
            .add(Restrictions.eq("name", name))
            .list();
        return (currencies.size() == 0) ? null : (Currency)currencies.get(0);
    }

    @Override
    public void setNewDefaultCurrency(Currency newDefaultCurrency) {
        //Update database to prevent existence of more than one defaut currencies.
        List<Currency> currencies = getAll();
        for (Currency currency : currencies){
            if ( currency.getId() != newDefaultCurrency.getId() && currency.isDefaultCurrency() ){
                currency.setDefaultCurrency(false);
            }
        }
    }

    @Override
    public Currency getDefaultCurrency() {
        List result = getSession()
            .createCriteria(Currency.class)
            .add(Restrictions.eq("defaultCurrency", true))
            .list();
        return result.size() > 0 ? (Currency)result.get(0) : null;
    }

    @Override
    public Currency getCurrencyByUid(String uid) {
        return (Currency) getSession().createCriteria(Currency.class)
                .add(Restrictions.eq("uidCurrency", uid))
                .uniqueResult();
    }

    @Override
    public List<String> getUidsByIds(List<Long> ids) {
        org.hibernate.Criteria criteria = getSession().createCriteria(Currency.class);
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("uidCurrency"));
        criteria.setProjection(projectionList);

        List<String> results = criteria.add(Restrictions.in("id", ids)).list();
        return results;
    }
}
